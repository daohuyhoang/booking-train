package com.group2.case_study.controllers;

import com.group2.case_study.config.VNPayService;
import com.group2.case_study.dtos.BookingDto;
import com.group2.case_study.dtos.BookingWrapper;
import com.group2.case_study.models.Booking;
import com.group2.case_study.models.Flight;
import com.group2.case_study.models.User;
import com.group2.case_study.services.IBookingService;
import com.group2.case_study.services.IFlightService;
import com.group2.case_study.services.ISeatService;
import com.group2.case_study.services.IUserService;
import com.group2.case_study.services.impl.MailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/vnp")
public class VNPController {
    @Autowired
    private VNPayService vnpService;

    @Autowired
    private MailService mailService;

    @Autowired
    private IFlightService flightService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IBookingService bookingService;

    @Autowired
    private ISeatService seatService;

    private static final String PAYMENT_IDENTIFIER = "PENDING";

    @PostMapping("")
    public String home(@RequestParam("ticketPrice") double ticketPrice,
                    @RequestParam("flightId") int flightId,
                    @ModelAttribute BookingWrapper bookingWrapper,
                    HttpSession session,
                    Model model) {
        if (session.getAttribute(PAYMENT_IDENTIFIER) != null) {
            return "pay/error";
        }
        List<BookingDto> customers = (bookingWrapper != null) ? bookingWrapper.getCustomers() : null;
        List<BookingDto> customersNew = new ArrayList<>();
        if (customers != null) {
            for (BookingDto customer : customers) {
                if (customer == null || customer.getName() == null) {
                    continue;
                }
                customersNew.add(customer);
            }
        }
        
        session.setAttribute("bookingWrappers", customersNew);
        
        model.addAttribute("ticketPrice", ticketPrice);
        model.addAttribute("flightId", flightId);
        model.addAttribute("customers", customersNew);

        session.setAttribute(PAYMENT_IDENTIFIER, generateRandomCode());

        return "vnp/index";
    }

    @PostMapping("/submitOrder/{flightId}")
    public String submitOrder(@RequestParam("amount") double orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              @PathVariable("flightId") int flightId,
                              HttpServletRequest request,
                              HttpSession session) throws UnsupportedEncodingException {

        int orderTotalI = (int) orderTotal;
        session.setAttribute("flightId", flightId);
        session.setAttribute("orderInfo", orderInfo);

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnpService.createOrder(orderTotalI, orderInfo, baseUrl);
        session.removeAttribute(PAYMENT_IDENTIFIER);

        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/vnpay-payment")
    public String getPaymentResult(HttpServletRequest request,
                                   HttpSession session,
                                   Model model) {
        List<BookingDto> customers = (List<BookingDto>) session.getAttribute("bookingWrappers");
        if (customers == null) {
            model.addAttribute("errorMessage", "Danh sách khách hàng không được cung cấp.");
            return "vnp/orderfail";
        }

        int flightId = (int) session.getAttribute("flightId");
        String userName = (String) session.getAttribute("username");
        int paymentStatus = vnpService.orderReturn(request);
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String vnpAmount = request.getParameter("vnp_Amount");
        long totalPriceLong = Long.parseLong(vnpAmount);
        String responseCode = request.getParameter("vnp_ResponseCode");
        String bankCode = request.getParameter("vnp_BankCode");
        List<Integer> seatIds = (List<Integer>) session.getAttribute("seats");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPriceLong);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);
        model.addAttribute("responseCode", responseCode);
        model.addAttribute("bankCode", bankCode);

        // Xử lý các mã lỗi cụ thể
        String errorMessage = getErrorMessage(responseCode, bankCode);
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            return "vnp/orderfail";
        }

        Flight flight = flightService.findById(flightId);
        User user = userService.findByName(userName);
        String email = user.getEmail();
        List<Booking> bookings = new ArrayList<>();

        for (BookingDto bookingDto : customers) {
            Booking booking = new Booking();
            String codeBooking = generateRandomCode();
            booking.setCodeBooking(codeBooking);
            booking.setStatus("Đã thanh toán");
            booking.setTotalPrice(flight.getPrice());
            booking.setFlight(flight);
            booking.setUser(user);
            BeanUtils.copyProperties(bookingDto, booking);
            bookings.add(booking);
        }

        session.setAttribute("booking", bookings);

        if (transactionId != null && transactionId.length() > 1) {
            for (Booking booking : bookings) {
                bookingService.save(booking);
                mailService.sendMail(email, "Spring-email-with-thymeleaf subject", booking);
            }
            seatService.updateSeatStatusConfig(seatIds, "unavailable");
        }
        flightService.removeUserId(flightId);
        return paymentStatus == 1 ? "vnp/ordersuccess" : "vnp/orderfail";
    }
    
    private String getErrorMessage(String responseCode, String bankCode) {
        if (responseCode == null) return null;
        
        switch (responseCode) {
            case "00":
                return null; // Thành công
            case "01":
                return "Giao dịch chưa hoàn tất";
            case "02":
                return "Giao dịch bị lỗi";
            case "03":
                return "Mã đối tác không tồn tại";
            case "04":
                return "Dữ liệu gửi sang không đúng định dạng";
            case "05":
                return "Tạo mã giao dịch không thành công";
            case "06":
                return "Giao dịch không thành công do: Ngân hàng từ chối giao dịch";
            case "07":
                return "Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường)";
            case "09":
                return "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking";
            case "10":
                return "Xác thực thông tin thẻ/tài khoản không đúng quá 3 lần";
            case "11":
                return "Đã hết hạn chờ thanh toán. Xin vui lòng thực hiện lại giao dịch";
            case "12":
                return "Giao dịch bị huỷ";
            case "24":
                return "Giao dịch không thành công do: Khách hàng hủy giao dịch";
            case "51":
                return "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch";
            case "65":
                return "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày";
            case "75":
                return "Ngân hàng thanh toán đang bảo trì";
            case "79":
                return "Nhập sai mật khẩu thanh toán quá số lần quy định";
            case "99":
                return "Các lỗi khác (lỗi chưa xác định)";
            default:
                if ("VNPAY".equals(bankCode)) {
                    return "Ngân hàng này không được hỗ trợ. Vui lòng chọn ngân hàng khác.";
                }
                return "Giao dịch không thành công. Mã lỗi: " + responseCode;
        }
    }

    private String generateRandomCode() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            char letter = letters.charAt(random.nextInt(letters.length()));
            code.append(letter);
        }
        for (int i = 0; i < 4; i++) {
            char number = numbers.charAt(random.nextInt(numbers.length()));
            code.append(number);
        }
        return code.toString();
    }
}
