package com.sash.ticketing_system.repositories;

import com.sash.ticketing_system.models.Ticket;
import com.sash.ticketing_system.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByUser(User user);

    void deleteByUserId(Long userId);
}
