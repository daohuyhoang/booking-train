package com.group2.case_study.services.impl;

import com.group2.case_study.models.Seat;
import com.group2.case_study.repositories.ISeatRepository;
import com.group2.case_study.services.ISeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeatService implements ISeatService {
    @Autowired
    private ISeatRepository seatRepository;

    @Override
    public List<List<Seat>> getSeatsGroupedByRows(Integer flightId) {
        List<Seat> seats = seatRepository.findSeatsByFlightId(flightId);
        List<List<Seat>> seatRows = new ArrayList<>();
        for (int i = 0; i < seats.size(); i += 6) {
            seatRows.add(seats.subList(i, Math.min(i + 6, seats.size())));
        }
        return seatRows;
    }

    @Override
    public Map<Integer, List<List<Seat>>> getSeatsGroupedByCoach(Integer flightId) {
        List<Seat> seats = seatRepository.findSeatsByFlightId(flightId);
        
        // Group seats by coachNumber
        Map<Integer, List<Seat>> seatsByCoach = seats.stream()
                .collect(Collectors.groupingBy(Seat::getCoachNumber, TreeMap::new, Collectors.toList()));
        
        // Group seats in each coach by rows (6 seats per row)
        Map<Integer, List<List<Seat>>> result = new LinkedHashMap<>();
        for (Map.Entry<Integer, List<Seat>> entry : seatsByCoach.entrySet()) {
            List<Seat> coachSeats = entry.getValue();
            List<List<Seat>> seatRows = new ArrayList<>();
            for (int i = 0; i < coachSeats.size(); i += 6) {
                seatRows.add(coachSeats.subList(i, Math.min(i + 6, coachSeats.size())));
            }
            result.put(entry.getKey(), seatRows);
        }
        
        return result;
    }

    @Override
    @Transactional
    public void updateSeatStatus(List<Integer> seatIds, String status, LocalDateTime holdExpiration) {
        for (Integer seatId : seatIds) {
            seatRepository.updateSeatStatus(seatId, status, holdExpiration);
        }
    }

    @Override
    public void updateSeatStatusConfig(List<Integer> seatIds, String status) {
        for (Integer seatId : seatIds) {
            seatRepository.updateSeatStatusConfig(seatId, status);
        }
    }

    @Override
    public long countAvailableSeatsByFlightId(Integer flightId) {
        return seatRepository.countAvailableSeatsByFlightId(flightId);
    }

    @Override
    public Seat findById(Integer seatId) {
        return seatRepository.findById(seatId).orElse(null);
    }

    @Override
    public List<Seat> findAllSeat(int flightId, int id) {
        return seatRepository.findAllSeat(flightId, id);
    }
}