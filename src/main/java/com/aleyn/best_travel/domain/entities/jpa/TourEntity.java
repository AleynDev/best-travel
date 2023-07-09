package com.aleyn.best_travel.domain.entities.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity(name = "tour")
public class TourEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true,
            mappedBy = "tour"
    )
    private Set<ReservationEntity> reservations;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true,
            mappedBy = "tour"
    )
    private Set<TicketEntity> tickets;

    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;


    @PrePersist
    @PreRemove
    public void updateFk() {
        tickets.forEach(ticket -> ticket.setTour(this));
        reservations.forEach(reservation -> reservation.setTour(this));
    }

    public void removeTicket(UUID ticketId) {
        tickets.stream().filter(ticket -> ticketId.equals(ticket.getId()))
                .forEach(ticket -> ticket.setTour(null));
    }

    public void addTicket(TicketEntity ticket) {
        if (Objects.isNull(tickets)) tickets = new HashSet<>();
        tickets.add(ticket);
        tickets.forEach(t -> t.setTour(this));
    }

    public void removeReservation(UUID reservationId) {
        reservations.stream().filter(reservation -> reservationId.equals(reservation.getId()))
                .forEach(reservation -> reservation.setTour(null));
    }

    public void addReservation(ReservationEntity reservation) {
        if (Objects.isNull(tickets)) tickets = new HashSet<>();
        reservations.add(reservation);
        reservations.forEach(r -> r.setTour(this));
    }

}
