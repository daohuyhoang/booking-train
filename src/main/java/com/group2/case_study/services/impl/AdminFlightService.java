package com.group2.case_study.services.impl;

import com.group2.case_study.models.Flight;
import com.group2.case_study.repositories.IAdminFlightRepository;
import com.group2.case_study.repositories.ISeatRepository;
import com.group2.case_study.services.IAdminFlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminFlightService implements IAdminFlightService {

    @Autowired
    private IAdminFlightRepository adminFlightRepository;

    @Autowired
    private ISeatRepository seatRepository;

    @Override
    public List<Flight> getAllFlights() {
        return adminFlightRepository.findAll();
    }

    @Override
    public Flight getFlightById(Integer flightId) {
        return adminFlightRepository.findById(flightId).orElse(null);
    }

    @Transactional
    @Override
    public Flight saveFlight(Flight flight) {
        // Save flight first to obtain generated id
        Flight saved = adminFlightRepository.save(flight);

        // If there are already seats for this flight, do not recreate them
        List<com.group2.case_study.models.Seat> existing = seatRepository.findSeatsByFlightId(saved.getFlightId());
        if (existing != null && !existing.isEmpty()) {
            return saved;
        }

        // Generate seats: 10 coaches, 10 columns, rows A-D => 400 seats
        java.util.List<com.group2.case_study.models.Seat> seatsToSave = new java.util.ArrayList<>();
        for (int coach = 1; coach <= 10; coach++) {
            String classType;
            double seatPrice;
            if (coach <= 2) {
                classType = "Giường nằm";
                seatPrice = 1200000.0;
            } else {
                classType = "Ghế ngồi";
                seatPrice = 900000.0;
            }

            for (int col = 1; col <= 10; col++) {
                for (char row = 'A'; row <= 'D'; row++) {
                    com.group2.case_study.models.Seat s = new com.group2.case_study.models.Seat();
                    s.setFlight(saved);
                    s.setAvailabilityStatus("AVAILABLE");
                    s.setClassType(classType);
                    s.setCoachNumber(coach);
                    s.setSeatNumber(String.valueOf(row) + col);
                    s.setHoldExpiration(null);
                    s.setPrice(seatPrice);
                    seatsToSave.add(s);
                }
            }
        }

        seatRepository.saveAll(seatsToSave);
        return saved;
    }


    @Transactional
    @Override
    public void deleteFlight(Integer flightId) {
        seatRepository.deleteAllByFlight_FlightId(flightId);
        adminFlightRepository.deleteById(flightId);
    }
}
