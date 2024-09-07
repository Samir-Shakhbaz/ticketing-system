package com.sash.ticketing_system.controllers;

import com.sash.ticketing_system.models.Ticket;
import com.sash.ticketing_system.models.User;
import com.sash.ticketing_system.services.TicketService;
import com.sash.ticketing_system.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/tickets")
public class TicketController {

   @Autowired
   TicketService ticketService;

   @Autowired
   UserService userService;

   //CONSTRUCTOR INJECTION private final UserRepository userRepository;
    //
    //    @Autowired
    //    public UserService(UserRepository userRepository) {
    //        this.userRepository = userRepository;
    //    }

    @GetMapping
    public String viewTickets(Model model) {
        List<Ticket> tickets = ticketService.getAllTickets();
        model.addAttribute("tickets", tickets);
        return "tickets";
    }

    @GetMapping("/create")
    public String showCreateTicketForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "create-ticket";
    }

    @PostMapping("/create")
    public String createTicket(@ModelAttribute Ticket ticket, Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("User must be logged in to create a ticket");
        }

        String username = principal.getName();
        User user = (User) userService.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("Invalid user");
        }

        ticket.setUser(user); // Set the user on the ticket
        ticketService.createTicket(ticket); // Save the ticket
        return "redirect:/tickets"; // Redirect after successful ticket creation
    }


    @GetMapping("/delete/{id}")
    public String deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return "redirect:/tickets";
    }
}