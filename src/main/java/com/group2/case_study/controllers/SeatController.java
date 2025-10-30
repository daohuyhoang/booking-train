package com.group2.case_study.controllers;

import com.group2.case_study.models.Flight;
import com.group2.case_study.models.Seat;
import com.group2.case_study.services.IFlightService;
import com.group2.case_study.services.ISeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/flights")
public class SeatController {
    @Autowired
    private ISeatService seatService;

    @Autowired
    private IFlightService flightService;

    @GetMapping("/{flightId}/seats")
    public String getSeatByFlightId(@PathVariable Integer flightId, Model model) {
        Map<Integer, List<List<Seat>>> seatsByCoach = seatService.getSeatsGroupedByCoach(flightId);
        Flight flight = flightService.getFlightById(flightId);
        
        for (Map.Entry<Integer, List<List<Seat>>> entry : seatsByCoach.entrySet()) {
            int totalSeatsInCoach = entry.getValue().stream()
                .mapToInt(List::size)
                .sum();
            System.out.println("Coach " + entry.getKey() + ": " + totalSeatsInCoach + " seats");
        }
        
        if (flight != null) {
            Double minPrice = seatService.findMinPriceByFlightId(flightId);
            model.addAttribute("seatsByCoach", seatsByCoach);
            model.addAttribute("flightId", flightId);
            model.addAttribute("seatPrice", minPrice != null ? minPrice : 0);
        }
        
        return "seat/seat-re";
    }
}