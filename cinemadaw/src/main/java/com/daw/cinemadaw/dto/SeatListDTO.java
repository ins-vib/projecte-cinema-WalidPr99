package com.daw.cinemadaw.dto;

import java.util.ArrayList;
import java.util.List;

public class SeatListDTO {

	private List<Long> seatIds = new ArrayList<>();

	public List<Long> getSeatIds() {
		return seatIds;
	}

	public void setSeatIds(List<Long> seatIds) {
		this.seatIds = seatIds != null ? seatIds : new ArrayList<>();
	}

	public boolean hasSelectedSeats() {
		return seatIds != null && !seatIds.isEmpty();
	}
}
