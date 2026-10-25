package com.aeroflex.flightservice.aircraft.entity;

import com.aeroflex.common.entity.BaseEntity;
import com.aeroflex.flightservice.aircraft.enums.AircraftStatus;
import com.aeroflex.flightservice.airline.entity.Airline;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "aircrafts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_aircraft_registration",
                        columnNames = "registration_number"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aircraft extends BaseEntity {

    @Column(
            name = "registration_number",
            nullable = false,
            length = 20
    )
    private String registrationNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "aircraft_type_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_aircraft_aircraft_type")
    )
    private AircraftType aircraftType;

    @Column(length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private AircraftStatus status = AircraftStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airline;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;
}
