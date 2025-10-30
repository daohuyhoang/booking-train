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

        Map<Integer, List<Seat>> seatsByCoach = seats.stream()
                .collect(Collectors.groupingBy(Seat::getCoachNumber, TreeMap::new, Collectors.toList()));

        Map<Integer, List<List<Seat>>> result = new LinkedHashMap<>();

        for (Map.Entry<Integer, List<Seat>> entry : seatsByCoach.entrySet()) {
            List<Seat> coachSeats = entry.getValue();

            coachSeats.sort(Comparator.comparing(seat -> {
        try {
            return Integer.parseInt(seat.getSeatNumber().replaceAll("\\D+", ""));
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }));


            List<List<Seat>> rows = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                rows.add(new ArrayList<>());
            }

            for (int i = 0; i < coachSeats.size(); i++) {
                int rowIndex = i % 4;
                rows.get(rowIndex).add(coachSeats.get(i));
            }

            result.put(entry.getKey(), rows);
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

    @Override
    public Double findMinPriceByFlightId(Integer flightId) {
        return seatRepository.findMinPriceByFlightId(flightId);
    }
}