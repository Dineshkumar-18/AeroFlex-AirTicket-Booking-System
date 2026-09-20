package com.aeroflex.identityservice.user.entity;

import com.aeroflex.common.constants.ApprovalStatus;
import com.aeroflex.common.entity.BaseEntity;
import com.aeroflex.identityservice.auth.enums.OwnerProfileStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "flight_owner_profiles",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_flight_owner_user",
                        columnNames = "user_id"
                ),
                @UniqueConstraint(
                        name = "uk_flight_owner_registration",
                        columnNames = "company_registration_number"
                ),
                @UniqueConstraint(
                        name = "uk_flight_owner_license",
                        columnNames = "operating_license_number"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightOwnerProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_flight_owner_user")
    )
    private User user;

    @Column(name = "company_name", nullable = false, length = 150)
    private String companyName;

    @Column(name = "company_registration_number", nullable = false, length = 100)
    private String companyRegistrationNumber;

    @Column(name = "company_phone_number", length = 20)
    private String companyPhoneNumber;

    @Column(name = "company_email", length = 150)
    private String companyEmail;

    @Column(name = "operating_license_number", nullable = false, length = 100)
    private String operatingLicenseNumber;

    @Column(name = "joined_date", nullable = false)
    private LocalDate joinedDate;

    @Column(name = "total_flights_managed", nullable = false)
    private Integer totalFlightsManaged;

    @Column(name = "support_contact", length = 100)
    private String supportContact;

    @Column(name = "profile_picture_url", length = 500)
    private String profilePictureUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false, length = 20)
    @Builder.Default
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "owner_profile_status", nullable = false, length = 30)
    @Builder.Default
    private OwnerProfileStatus ownerProfileStatus = OwnerProfileStatus.NOT_COMPLETED;

    @Column(
            name = "admin_remarks",
            length = 1000
    )
    private String adminRemarks;

    private LocalDateTime submittedAt;

    private LocalDateTime approvedAt;
}
