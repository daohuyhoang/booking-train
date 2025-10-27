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
    public String showPay(@RequestParam("flightId") int flightId,
                          HttpSession session,
                          @RequestParam("seatIds") List<Integer> seatIds,
                          Principal principal,
                          Model model) {
        LocalDateTime holdExpiration = LocalDateTime.now().plusMinutes(2);
        String userName = principal.getName();
        int id = userService.findIdByUserName(userName);
        seatService.updateSeatStatus(seatIds, "BOOKED" + id, holdExpiration);
        flightService.saveUserId(flightId,id);
        Flight flight = flightService.getFlightById(flightId);
        List<Seat> seats = seatService.findAllSeat(flightId,id);
        session.setAttribute("seats", seatIds);
        model.addAttribute("flight", flight);
        model.addAttribute("seats", seats);
        return "pay/show-pay";
    }
}
