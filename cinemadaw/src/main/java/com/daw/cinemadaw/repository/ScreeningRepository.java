package com.daw.cinemadaw.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.daw.cinemadaw.domain.cinema.Screening;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {

	List<Screening> findByMovieId(Long movieId);

	List<Screening> findByMovieIdAndDateTimeGreaterThanEqualOrderByDateTimeAsc(Long movieId, LocalDateTime dateTime);

	@Transactional
	long deleteByMovieId(Long movieId);

}
