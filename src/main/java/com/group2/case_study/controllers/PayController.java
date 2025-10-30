package com.group2.case_study.controllers;

import com.group2.case_study.models.Flight;
import com.group2.case_study.models.Seat;
import com.group2.case_study.services.IFlightService;
import com.group2.case_study.services.ISeatService;
import com.group2.case_study.services.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private IFlightService flightService;

    @Autowired
    private ISeatService seatService;

    @Autowired
    private IUserService userService;

    @PostMapping
    public String showPay(@RequestParam(value = "flightId", required = false) Integer flightId,
                          @RequestParam(value = "seatIds", required = false) List<Integer> seatIds,
                          @RequestParam(value = "outboundFlightId", required = false) Integer outboundFlightId,
                          @RequestParam(value = "outboundSeatIds", required = false) List<Integer> outboundSeatIds,
                          @RequestParam(value = "returnFlightId", required = false) Integer returnFlightId,
                          @RequestParam(value = "returnSeatIds", required = false) List<Integer> returnSeatIds,
                          HttpSession session,
                          Principal principal,
                          Model model) {
        LocalDateTime holdExpiration = LocalDateTime.now().plusMinutes(2);
        String userName = principal.getName();
        int id = userService.findIdByUserName(userName);

        if (outboundFlightId == null && returnFlightId == null && flightId != null && seatIds != null && !seatIds.isEmpty()) {
            seatService.updateSeatStatus(seatIds, "BOOKED" + id, holdExpiration);
            flightService.saveUserId(flightId, id);
            Flight flight = flightService.getFlightById(flightId);
            List<Seat> seats = seatService.findAllSeat(flightId, id);
            session.setAttribute("seats", seatIds);
            model.addAttribute("flight", flight);
            model.addAttribute("seats", seats);
            // compute total based on selected seat prices
            long grandTotal = 0L;
            for (Integer sId : seatIds) {
                Seat s = seatService.findById(sId);
                if (s != null && s.getPrice() != null) {
                    grandTotal += Math.round(s.getPrice());
                }
            }
            model.addAttribute("grandTotal", grandTotal);
            double perPassengerPrice = seatIds.isEmpty() ? 0 : ((double) grandTotal) / seatIds.size();
            model.addAttribute("perPassengerPrice", perPassengerPrice);
            return "pay/show-pay";
        }

        if (outboundFlightId != null && outboundSeatIds != null && !outboundSeatIds.isEmpty()
                && returnFlightId != null && returnSeatIds != null && !returnSeatIds.isEmpty()) {
            seatService.updateSeatStatus(outboundSeatIds, "BOOKED" + id, holdExpiration);
            flightService.saveUserId(outboundFlightId, id);
            seatService.updateSeatStatus(returnSeatIds, "BOOKED" + id, holdExpiration);
            flightService.saveUserId(returnFlightId, id);

            Flight outboundFlight = flightService.getFlightById(outboundFlightId);
            Flight returnFlight = flightService.getFlightById(returnFlightId);
            List<Seat> outboundSeats = seatService.findAllSeat(outboundFlightId, id);
            List<Seat> returnSeats = seatService.findAllSeat(returnFlightId, id);

            List<Integer> allSeatIds = new ArrayList<>();
            allSeatIds.addAll(outboundSeatIds);
            allSeatIds.addAll(returnSeatIds);
            session.setAttribute("seats", allSeatIds);

            model.addAttribute("outboundFlight", outboundFlight);
            model.addAttribute("returnFlight", returnFlight);
            model.addAttribute("outboundSeats", outboundSeats);
            model.addAttribute("returnSeats", returnSeats);
            model.addAttribute("tripType", "roundtrip");

            long outboundTotal = 0L;
            for (Seat s : outboundSeats) {
                if (outboundSeatIds.contains(s.getSeatId()) && s.getPrice() != null) {
                    outboundTotal += Math.round(s.getPrice());
                }
            }
            long returnTotal = 0L;
            for (Seat s : returnSeats) {
                if (returnSeatIds.contains(s.getSeatId()) && s.getPrice() != null) {
                    returnTotal += Math.round(s.getPrice());
                }
            }
            long grandTotal = outboundTotal + returnTotal;
            model.addAttribute("grandTotal", grandTotal);
            
            double perPassengerPrice = (outboundSeatIds.isEmpty() ? 0 : ((double) grandTotal) / outboundSeatIds.size());
            model.addAttribute("perPassengerPrice", perPassengerPrice);

            double outboundPerPassenger = (outboundSeatIds.isEmpty() ? 0 : ((double) outboundTotal) / outboundSeatIds.size());
            double returnPerPassenger = (returnSeatIds.isEmpty() ? 0 : ((double) returnTotal) / returnSeatIds.size());
            model.addAttribute("outboundPerPassengerPrice", outboundPerPassenger);
            model.addAttribute("returnPerPassengerPrice", returnPerPassenger);
            // add per-leg totals for display
            model.addAttribute("outboundTotal", outboundTotal);
            model.addAttribute("returnTotal", returnTotal);
            return "pay/show-pay";
        }

        throw new IllegalArgumentException("Invalid booking request");
    }
}
