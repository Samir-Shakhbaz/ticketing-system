package com.sash.ticketing_system.controllers;

import com.sash.ticketing_system.models.Notification;
import com.sash.ticketing_system.models.User;
import com.sash.ticketing_system.services.NotificationService;
import com.sash.ticketing_system.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @PostMapping
    public Notification createNotification(@RequestBody Notification notification) {
        return notificationService.saveNotification(notification);
    }

    @GetMapping("/{id}")
    public Notification getNotification(@PathVariable Long id) {
        return notificationService.getNotification(id);
    }

    @PatchMapping("/{id}/read")
    public void markNotificationAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }
    @GetMapping("/user/{userId}")
    public List<Notification> getNotificationsForUser(@PathVariable Long userId) {
        return notificationService.getNotificationsForUser(userId);
    }

    @GetMapping
    public String viewNotifications(Model model, Principal principal) {

        String username = principal.getName();

        User user = userService.findByUsername(username);

        List<Notification> notifications = notificationService.getUnreadNotifications(user);

        model.addAttribute("notifications", notifications);

        return "notifications";
    }

}

