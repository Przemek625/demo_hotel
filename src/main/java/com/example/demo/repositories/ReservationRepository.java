package com.example.demo.repositories;

import com.example.demo.domain.Reservation;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
