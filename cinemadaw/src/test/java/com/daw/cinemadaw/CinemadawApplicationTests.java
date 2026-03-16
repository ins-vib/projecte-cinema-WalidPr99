package com.daw.cinemadaw;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.daw.cinemadaw.service.NewsService;

@SpringBootTest
class CinemadawApplicationTests {

	@Autowired
	private NewsService newsService;

	@Test
	void contextLoads() {
	}

	@Test
	void newsServiceLoadsNewsFromClasspathResource() {
		var news = newsService.getNews();

		assertFalse(news.isEmpty());
		assertTrue(news.stream().anyMatch(item -> item.getHeadline().contains("S'inaugura nou cinema")));
	}

}
