package com.group2.case_study.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeatWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, String> seats = new HashMap<>();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        WebSocketSessionManager.addSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        WebSocketSessionManager.removeSession(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        System.out.println("Received message from client: " + payload);

        try {
            if (payload == null || payload.trim().isEmpty()) {
                throw new IOException("Received empty or invalid payload");
            }

            String action = extractAction(payload);
            System.out.println("Action extracted: " + action);

            if ("lockSeats".equals(action)) {
                List<String> seatIds = extractSeatIds(payload);
                System.out.println("Seat IDs extracted: " + seatIds);

                for (String seatId : seatIds) {
                    seats.put(seatId, "BOOKED");
                }

                String responseMessage = payload;
                System.out.println("Sending update seats message: " + responseMessage);

                WebSocketSessionManager.sendToAll(responseMessage);
            } else {
                System.err.println("Unhandled action: " + action);
            }
        } catch (IOException e) {
            System.err.println("Error processing WebSocket message: " + e.getMessage());
        }
    }


    private String extractAction(String payload) throws IOException {

        JsonNode rootNode = OBJECT_MAPPER.readTree(payload);

        JsonNode actionNode = rootNode.path("action");
        if (actionNode.isMissingNode()) {

            System.err.println("Field 'action' is missing in the payload.");
            return null;
        }

        return actionNode.asText();
    }

    private List<String> extractSeatIds(String payload) throws IOException {

        JsonNode rootNode = OBJECT_MAPPER.readTree(payload);
        List<String> seatIds = new ArrayList<>();

        JsonNode seatIdsNode = rootNode.path("seatIds");
        if (seatIdsNode.isArray()) {
            for (JsonNode node : seatIdsNode) {
                seatIds.add(node.asText());
            }
        } else {
            System.err.println("Field 'seatIds' is missing or not an array.");
        }

        return seatIds;
    }
}