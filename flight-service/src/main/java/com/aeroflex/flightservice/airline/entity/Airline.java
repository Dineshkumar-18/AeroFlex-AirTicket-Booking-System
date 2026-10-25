package com.aeroflex.flightservice.airline.entity;

import com.aeroflex.common.entity.BaseEntity;
import com.aeroflex.flightservice.aircraft.entity.Aircraft;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
        name = "airlines",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_airline_iata_code",
                        columnNames = "iata_code"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Airline extends BaseEntity {

    @Column(name = "airline_name", nullable = false, length = 100)
    private String airlineName;

    @Column(name = "iata_code", nullable = false, length = 2)
    private String iataCode;

    @Column(name = "headquarters", length = 50)
    private String headquarters;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "contact_number", nullable = false)
    private String contactNumber;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "founded_year", nullable = false)
    private Integer foundedYear;

    @Column(name = "website_url", nullable = false)
    private String websiteUrl;

    @Column(name = "airline_logo", nullable = false)
    private String airlineLogo;

    @Column(
            name = "commission_percentage",
            precision = 5,
            scale = 2
    )
    private BigDecimal commissionPercentage;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;
}
