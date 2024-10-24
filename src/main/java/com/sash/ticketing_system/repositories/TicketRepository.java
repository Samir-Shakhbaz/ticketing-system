package com.sash.ticketing_system.repositories;

import com.sash.ticketing_system.models.Ticket;
import com.sash.ticketing_system.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByUser(User user);

    void deleteByUserId(Long userId);

    Optional<Ticket> findById(Long id);
}
