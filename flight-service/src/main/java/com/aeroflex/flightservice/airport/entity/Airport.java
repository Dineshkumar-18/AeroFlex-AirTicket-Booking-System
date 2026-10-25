package com.aeroflex.flightservice.airport.entity;

import com.aeroflex.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "airports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Airport extends BaseEntity {

    @Column(nullable = false, unique = true, length = 3)
    private String code; // IATA code, e.g. "DEL", "JFK"

    @Column(length = 4)
    private String icaoCode; // e.g. "VIDP", "KJFK"

    @Column(nullable = false, length = 150)
    private String name; // e.g. "Indira Gandhi International Airport"

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String country;

    @Column(length = 50)
    private String timezone; // e.g. "Asia/Kolkata", "America/New_York"

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;
}
