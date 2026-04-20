package com.daw.cinemadaw;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.daw.cinemadaw.domain.cinema.Comanda;
import com.daw.cinemadaw.repository.ComandaRepository;
import com.daw.cinemadaw.repository.SeatRepository;
import com.daw.cinemadaw.repository.TicketRepository;
import com.daw.cinemadaw.service.NewsService;
import com.daw.cinemadaw.service.TicketService;

@SpringBootTest
class CinemadawApplicationTests {

	@Autowired
	private NewsService newsService;

	@Autowired
	private TicketService ticketService;

	@Autowired
	private ComandaRepository comandaRepository;

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private SeatRepository seatRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void newsServiceLoadsNewsFromClasspathResource() {
		var news = newsService.getNews();

		assertFalse(news.isEmpty());
		assertTrue(news.stream().anyMatch(item -> item.getHeadline().contains("S'inaugura nou cinema")));
	}

	@Test
	@Transactional
	void ticketServiceCreateOrderPersistsComandaTicketsAndSeatState() {

		HashMap<Long, List<Long>> cart = new HashMap<>();
		cart.put(1L, List.of(1L, 2L));

		Comanda created = ticketService.createOrder(cart, "client.test", "client.test@example.com");

		assertNotNull(created.getId());
		assertEquals(2, created.getTickets().size());
		assertEquals(17.0, created.getTotalAmount(), 0.0001);
		assertTrue(comandaRepository.findById(created.getId()).isPresent());
		assertEquals(2, ticketRepository.findByComandaId(created.getId()).size());
		assertFalse(seatRepository.findById(1L).orElseThrow().isState());
		assertFalse(seatRepository.findById(2L).orElseThrow().isState());
	}

	@Test
	@Transactional
	void ticketServiceCreateOrderRejectsAlreadyReservedSeat() {

		HashMap<Long, List<Long>> firstCart = new HashMap<>();
		firstCart.put(1L, List.of(3L));

		ticketService.createOrder(firstCart, "first.client", "first.client@example.com");

		HashMap<Long, List<Long>> secondCart = new HashMap<>();
		secondCart.put(1L, List.of(3L));

		IllegalStateException exception = assertThrows(IllegalStateException.class,
				() -> ticketService.createOrder(secondCart, "second.client", "second.client@example.com"));
		assertFalse(exception.getMessage() == null || exception.getMessage().isBlank());
	}

}
