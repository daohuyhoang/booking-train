package com.group2.case_study.config;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WebSocketSessionManager {
    private static final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    public static void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    public static void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }

    public static void sendToAll(String message) {
        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(new TextMessage(message));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}