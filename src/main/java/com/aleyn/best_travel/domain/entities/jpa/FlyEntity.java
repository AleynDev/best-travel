package com.aleyn.best_travel.domain.entities.jpa;

import com.aleyn.best_travel.util.enums.AirLine;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity(name = "fly")
public class FlyEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "origin_lat")
    private Double originLat;

    @Column(name = "origin_lng")
    private Double originLng;

    @Column(name = "destiny_lat")
    private Double destinyLat;

    @Column(name = "destiny_lng")
    private Double destinyLng;

    @Column(name = "destiny_name", length = 20)
    private String destinyName;

    @Column(name = "origin_name", length = 20)
    private String originName;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "air_line")
    @Enumerated(EnumType.STRING)
    private AirLine airLine;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true,
            mappedBy = "fly"
    )
    private Set<TicketEntity> tickets;

}
