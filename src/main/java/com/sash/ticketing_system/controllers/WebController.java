package com.sash.ticketing_system.controllers;

import com.sash.ticketing_system.models.Notification;
import com.sash.ticketing_system.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WebController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

//    @GetMapping("/notifications-page")
//    public String notificationsPage(Model model, @RequestParam(required = false) Long userId) {
//        if (userId != null) {
//            List<Notification> notifications = notificationService.getNotificationsForUser(userId);
//            model.addAttribute("notifications", notifications);
//        } else {
//            model.addAttribute("notifications", List.of()); // Or handle the case where userId is missing
//        }
//        return "notifications";
//    }

    @GetMapping("/notifications-page")
    public String notificationsPage(Model model, @RequestParam(defaultValue = "1") Long userId) {
        List<Notification> notifications = notificationService.getNotificationsForUser(userId);
        model.addAttribute("notifications", notifications);
        return "notifications";
    }


}

