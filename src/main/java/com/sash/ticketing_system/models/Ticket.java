package com.sash.ticketing_system.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;
    private String description;
    private String status;

    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.LOW;  // Default priority is LOW

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // user who created the ticket

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;  // user to whom the ticket is assigned

    public enum Priority {
        LOW, MEDIUM, HIGH, URGENT
    }

    // automatically set creation and update timestamps
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

