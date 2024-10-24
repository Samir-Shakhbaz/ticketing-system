package com.sash.ticketing_system.services;

import com.sash.ticketing_system.config.TicketNotFoundException;
import com.sash.ticketing_system.models.Notification;
import com.sash.ticketing_system.models.Ticket;
import com.sash.ticketing_system.models.User;
import com.sash.ticketing_system.repositories.TicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

   @Autowired
   TicketRepository ticketRepository;

   @Autowired
   UserService userService;

   @Autowired
   NotificationService notificationService;

   public void createTicket(Ticket ticket) {
       System.out.println("Saving ticket: " + ticket);

       ticketRepository.save(ticket);

       System.out.println("Ticket saved successfully: " + ticket);
   }

    public List<Ticket> getTicketsByUser(User user) {
        return ticketRepository.findByUser(user);
    }

    @Transactional
    public Ticket updateTicket(Long id, Ticket updatedTicket, Long userId) {
        Ticket updated = ticketRepository.findById(id)
                .map(ticket -> {

                    System.out.println("Updating ticket: subject=" + updatedTicket.getSubject());

                    ticket.setSubject(updatedTicket.getSubject());
                    ticket.setDescription(updatedTicket.getDescription());
                    ticket.setStatus(updatedTicket.getStatus());
                    ticket.setPriority(updatedTicket.getPriority());
                    return ticketRepository.save(ticket);
                })
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with id " + id));


        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage("Your ticket with ID " + id + " has been updated.");
        notificationService.saveNotification(notification);

        return updated;
    }


    @Transactional
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    public List<Ticket> getAllTickets() {return ticketRepository.findAll();
    }

    public void deleteTicketsByUserId(Long userId) {
        ticketRepository.deleteByUserId(userId);
    }

    public Ticket findTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId).orElse(null);  // unwrap Optional, return null if not found
    }

    public void saveTicket(Ticket ticket) { ticketRepository.save(ticket);}
    }
