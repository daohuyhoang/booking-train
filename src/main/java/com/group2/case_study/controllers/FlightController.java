package com.group2.case_study.controllers;

import com.group2.case_study.models.Airport;
import com.group2.case_study.models.Booking;
import com.group2.case_study.models.Flight;
import com.group2.case_study.services.IAirportService;
import com.group2.case_study.services.IBookingService;
import com.group2.case_study.services.IFlightService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
public class FlightController {

    @Autowired
    private IAirportService airportService;

    @Autowired
    private IFlightService flightService;

    @Autowired
    private IBookingService bookingService;

    @GetMapping
    public String showSearchForm(Model model, Principal principal, HttpSession session) {
        List<Airport> airports = flightService.getDistinctDepartureAirportsWithFutureFlights();
        List<Flight> flights = flightService.findAllFlightsByCurrentDateTime();
        model.addAttribute("airports", airports);
        model.addAttribute("flights", flights);
        if (principal != null) {
            String username = principal.getName();
            model.addAttribute("username", username);
            session.setAttribute("username", username);
        }
        return "flight/home";
    }

    @PostMapping("/flights/search")
    public String searchFlights(@RequestParam (value = "departureAirportId", defaultValue = "") Integer departureAirportId,
                                @RequestParam (value = "arrivalAirportId", defaultValue = "") Integer arrivalAirportId,
                                @RequestParam (value = "departure-date", defaultValue = "") String departureDate,
                                @RequestParam (value = "tripType", defaultValue = "oneway") String tripType,
                                @RequestParam (value = "return-date", defaultValue = "") String returnDate,
                                Model model,
                                RedirectAttributes redirectAttributes
                                ) {

        if (departureAirportId == null) {
            redirectAttributes.addFlashAttribute("messageWarning", "Vui lòng chọn điểm khởi hành!");
            return "redirect:/";
        } else if (arrivalAirportId == null) {
            redirectAttributes.addFlashAttribute("messageWarning", "Vui lòng chọn điểm đến!");
            return "redirect:/";
        } else {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate localDate;
            Airport departureAirport = airportService.findById(departureAirportId);
            Airport arrivalAirport = airportService.findById(arrivalAirportId);
            List<Flight> flights;
            List<Flight> returnFlights = null;
            if(departureDate.isEmpty()){
                flights = flightService.findAllFlightsByCurrentDate(departureAirportId, arrivalAirportId);
            } else {
                try {
                    localDate = LocalDate.parse(departureDate, inputFormatter);
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid date format. Please use dd-MM-yyyy.");
                }
                flights = flightService.findAllFlightsByDateOnly(localDate, arrivalAirportId, departureAirportId);
            }
            if ("roundtrip".equalsIgnoreCase(tripType)) {
                model.addAttribute("tripType", "roundtrip");
                if (!returnDate.isEmpty()) {
                    try {
                        LocalDate returnLocalDate = LocalDate.parse(returnDate, inputFormatter);
                        
                        returnFlights = flightService.findAllFlightsByDateOnly(returnLocalDate, departureAirportId, arrivalAirportId);
                    } catch (DateTimeParseException e) {
                        throw new IllegalArgumentException("Invalid date format. Please use dd-MM-yyyy.");
                    }
                } else {
                    returnFlights = flightService.findAllFlightsByCurrentDate(arrivalAirportId, departureAirportId);
                }
            } else {
                model.addAttribute("tripType", "oneway");
            }
            model.addAttribute("flights", flights);
            model.addAttribute("departureAirport", departureAirport);
            model.addAttribute("arrivalAirport", arrivalAirport);
            model.addAttribute("departureDate", departureDate);
            model.addAttribute("returnFlights", returnFlights);
            model.addAttribute("returnDate", returnDate);
            return "flight/list";
        }

    }

    @GetMapping("/booked")
    public String booked(@RequestParam("booked") String booked, Model model) {
        Booking booking = bookingService.findByCode(booked);
        model.addAttribute("booking", booking);
        model.addAttribute("booked", booked);
        return "flight/Tickets-booked";
    }

    @GetMapping("/introduction")
    public String introduction() {
        return "flight/introduction";
    }
}
