package com.daw.cinemadaw.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class CartSessionService {

    public static final String CART_SESSION_KEY = "cartByScreening";

    public HashMap<Long, ArrayList<Long>> getOrCreateCart(HttpSession session) {

        Object value = session.getAttribute(CART_SESSION_KEY);

        if (value instanceof HashMap<?, ?> hashMapValue) {
            HashMap<Long, ArrayList<Long>> normalized = normalizeCartMap(hashMapValue);
            session.setAttribute(CART_SESSION_KEY, normalized);
            return normalized;
        }

        if (value instanceof Map<?, ?> mapValue) {
            HashMap<Long, ArrayList<Long>> normalized = normalizeCartMap(mapValue);
            session.setAttribute(CART_SESSION_KEY, normalized);
            return normalized;
        }

        HashMap<Long, ArrayList<Long>> cart = new HashMap<>();
        session.setAttribute(CART_SESSION_KEY, cart);
        return cart;
    }

    public int addSeats(HttpSession session, Long screeningId, List<Long> seatIds) {

        if (screeningId == null || seatIds == null || seatIds.isEmpty()) {
            return 0;
        }

        HashMap<Long, ArrayList<Long>> cart = getOrCreateCart(session);
        ArrayList<Long> currentSeatIds = cart.computeIfAbsent(screeningId, key -> new ArrayList<>());

        Set<Long> uniqueRequestedIds = new LinkedHashSet<>();
        for (Long seatId : seatIds) {
            if (seatId != null) {
                uniqueRequestedIds.add(seatId);
            }
        }

        int addedCount = 0;
        for (Long seatId : uniqueRequestedIds) {
            if (!currentSeatIds.contains(seatId)) {
                currentSeatIds.add(seatId);
                addedCount++;
            }
        }

        cart.put(screeningId, currentSeatIds);
        session.setAttribute(CART_SESSION_KEY, cart);
        return addedCount;
    }

    public void removeSeat(HttpSession session, Long screeningId, Long seatId) {

        if (screeningId == null || seatId == null) {
            return;
        }

        HashMap<Long, ArrayList<Long>> cart = getOrCreateCart(session);
        ArrayList<Long> seatIds = cart.get(screeningId);

        if (seatIds == null) {
            return;
        }

        seatIds.removeIf(id -> seatId.equals(id));

        if (seatIds.isEmpty()) {
            cart.remove(screeningId);
        } else {
            cart.put(screeningId, seatIds);
        }

        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void removeScreening(HttpSession session, Long screeningId) {

        if (screeningId == null) {
            return;
        }

        HashMap<Long, ArrayList<Long>> cart = getOrCreateCart(session);
        cart.remove(screeningId);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void clearCart(HttpSession session) {

        HashMap<Long, ArrayList<Long>> cart = getOrCreateCart(session);
        cart.clear();
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public int countAllSeats(HttpSession session) {

        HashMap<Long, ArrayList<Long>> cart = getOrCreateCart(session);
        int count = 0;

        for (ArrayList<Long> seatIds : cart.values()) {
            if (seatIds != null) {
                count += seatIds.size();
            }
        }

        return count;
    }

    private HashMap<Long, ArrayList<Long>> normalizeCartMap(Map<?, ?> sourceMap) {

        HashMap<Long, ArrayList<Long>> normalized = new HashMap<>();

        for (Map.Entry<?, ?> entry : sourceMap.entrySet()) {
            Long screeningId = toLong(entry.getKey());
            if (screeningId == null) {
                continue;
            }

            ArrayList<Long> seatIds = toSeatIdList(entry.getValue());
            if (!seatIds.isEmpty()) {
                normalized.put(screeningId, seatIds);
            }
        }

        return normalized;
    }

    private Long toLong(Object value) {

        if (value instanceof Long longValue) {
            return longValue;
        }

        if (value instanceof Number numberValue) {
            return numberValue.longValue();
        }

        return null;
    }

    private ArrayList<Long> toSeatIdList(Object value) {

        ArrayList<Long> normalizedSeatIds = new ArrayList<>();

        if (!(value instanceof List<?> sourceList)) {
            return normalizedSeatIds;
        }

        for (Object item : sourceList) {
            Long seatId = toLong(item);
            if (seatId != null && !normalizedSeatIds.contains(seatId)) {
                normalizedSeatIds.add(seatId);
            }
        }

        return normalizedSeatIds;
    }
}
