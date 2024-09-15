package com.sash.ticketing_system.controllers;

import com.sash.ticketing_system.models.Notification;
import com.sash.ticketing_system.models.Ticket;
import com.sash.ticketing_system.models.User;
import com.sash.ticketing_system.services.NotificationService;
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
import java.util.Optional;

@Controller
@RequestMapping("/tickets")
public class TicketController {

   @Autowired
   TicketService ticketService;

   @Autowired
   UserService userService;

   @Autowired
    NotificationService notificationService;

   //CONSTRUCTOR INJECTION private final UserRepository userRepository;
    //
    //    @Autowired
    //    public UserService(UserRepository userRepository) {
    //        this.userRepository = userRepository;
    //    }

    @GetMapping
    public String viewTickets(Model model) {
        List<Ticket> tickets = ticketService.getAllTickets();
        List<User> users = userService.findAllUsers();
        Ticket ticket = new Ticket();
        model.addAttribute("tickets", tickets);
        model.addAttribute("ticket", ticket);
        model.addAttribute("users", users);
        return "tickets";
    }


    @GetMapping("/create")
    public String showCreateTicketForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "create-ticket";
    }

    @PostMapping("/create")
    public String createTicket(@ModelAttribute Ticket ticket, Principal principal) {
        System.out.println("Creating ticket: " + ticket.getSubject());  // Log ticket details

        if (principal == null) {
            return "redirect:/tickets";
        }

        String username = principal.getName();
        User user = userService.findByUsername(username);
        if (user == null) {
            return "redirect:/tickets";
        }

        System.out.println("User found: " + user.getUsername());

        ticket.setUser(user);
        ticketService.createTicket(ticket);

        System.out.println("Ticket saved with subject: " + ticket.getSubject());

        return "redirect:/tickets";
    }

    @PostMapping("/assign/{ticketId}")
    public String assignTicket(@PathVariable Long ticketId, @RequestParam Long assigneeId) {
        Ticket ticket = ticketService.findTicketById(ticketId);
        if (ticket == null) {
            return "redirect:/tickets";
        }

        User assignee = userService.findById(assigneeId);
        if (assignee == null) {
            return "redirect:/tickets";
        }

        ticket.setAssignee(assignee);
        ticketService.saveTicket(ticket);

        notificationService.createNotification(assignee.getId(), "You have been assigned a new ticket: " + ticket.getSubject());

        return "redirect:/tickets";
    }

//    @PostMapping
//    public String createTicket(@ModelAttribute Ticket ticket) {
//        ticketService.createTicket(ticket);
//        return "redirect:/tickets";
//    }


    @GetMapping("/delete/{id}")
    public String deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return "redirect:/tickets";
    }

    @GetMapping("/notifications/mark-as-read/{id}")
    public String markAsRead(@PathVariable Long id) {
        Notification notification = notificationService.findById(id);
        if (notification != null) {
            notification.setRead(true);
            notificationService.save(notification);
        }
        return "redirect:/notifications";
    }

    @GetMapping("/update/{id}")
    public String showUpdateTicketForm(@PathVariable Long id, Model model) {
        Ticket ticket = ticketService.findTicketById(id);
        model.addAttribute("ticket", ticket);
        return "update-ticket";
    }

    @PostMapping("/update/{id}")
    public String updateTicket(@PathVariable Long id, @ModelAttribute Ticket ticketDetails, Principal principal) {

        Ticket existingTicket = ticketService.findTicketById(id);

        existingTicket.setSubject(existingTicket.getSubject());
        existingTicket.setDescription(existingTicket.getDescription());
        existingTicket.setPriority(ticketDetails.getPriority());
        existingTicket.setStatus(ticketDetails.getStatus());


        String username = principal.getName();
        User user = userService.findByUsername(username);

        ticketService.updateTicket(id, existingTicket, user.getId());

        return "redirect:/tickets";
    }



}