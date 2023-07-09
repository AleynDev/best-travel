package com.aleyn.best_travel.domain.entities.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity(name = "customer")
public class CustomerEntity implements Serializable {

    @Id
    @Column(name = "dni", length = 20)
    private String dni;

    @Column(name = "full_name", length = 50)
    private String fullName;

    @Column(name = "credit_card", length = 20)
    private String creditCard;

    @Column(name = "phone_number", length = 12)
    private String phoneNumber;

    @Column(name = "total_flights")
    private Integer totalFlights;

    @Column(name = "total_lodgings")
    private Integer totalLodgings;

    @Column(name = "total_tours")
    private Integer totalTours;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true,
            mappedBy = "customer"
    )
    private Set<TicketEntity> tickets;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true,
            mappedBy = "customer"
    )
    private Set<ReservationEntity> reservations;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            mappedBy = "customer"
    )
    private Set<TourEntity> tours;

}
