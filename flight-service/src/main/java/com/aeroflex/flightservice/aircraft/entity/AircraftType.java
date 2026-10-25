package com.aeroflex.flightservice.aircraft.entity;

import com.aeroflex.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "aircraft_types",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_aircraft_type_model",
                        columnNames = "model"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AircraftType extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String model;

    @Column(nullable = false, length = 50)
    private String manufacturer;

    @Column(nullable = false)
    private int economyCapacity;

    @Column(nullable = false)
    private int premiumEconomyCapacity;

    @Column(nullable = false)
    private int businessCapacity;

    @Column(nullable = false)
    private int firstClassCapacity;

    @Column(nullable = false, length = 20)
    private String seatingConfig;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @Transient
    public int getTotalCapacity() {
        return economyCapacity
                + premiumEconomyCapacity
                + businessCapacity
                + firstClassCapacity;
    }
}
