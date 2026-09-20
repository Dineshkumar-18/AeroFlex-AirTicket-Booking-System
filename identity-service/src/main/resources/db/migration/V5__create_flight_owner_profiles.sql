CREATE TABLE flight_owner_profiles (
                                       id UNIQUEIDENTIFIER NOT NULL,

                                       user_id UNIQUEIDENTIFIER NOT NULL,

                                       company_name VARCHAR(150) NOT NULL,

                                       company_registration_number VARCHAR(100) NOT NULL,

                                       company_phone_number VARCHAR(20) NULL,

                                       company_email VARCHAR(150) NULL,

                                       operating_license_number VARCHAR(100) NOT NULL,

                                       joined_date DATE NOT NULL,

                                       total_flights_managed INT NOT NULL
                                           CONSTRAINT df_flight_owner_total_flights
                                           DEFAULT 0,

                                       support_contact VARCHAR(100) NULL,

                                       profile_picture_url VARCHAR(500) NULL,

                                       approval_status VARCHAR(20) NOT NULL
                                           CONSTRAINT df_flight_owner_approval_status
                                           DEFAULT 'PENDING',

                                       owner_profile_status VARCHAR(30) NOT NULL
                                           CONSTRAINT df_flight_owner_profile_status
                                           DEFAULT 'NOT_COMPLETED',

                                       admin_remarks VARCHAR(1000) NULL,

                                       submitted_at DATETIME2 NULL,

                                       approved_at DATETIME2 NULL,

                                       created_at DATETIME2 NOT NULL,

                                       updated_at DATETIME2 NOT NULL,

                                       CONSTRAINT pk_flight_owner_profiles
                                           PRIMARY KEY (id),

                                       CONSTRAINT uk_flight_owner_user
                                           UNIQUE (user_id),

                                       CONSTRAINT uk_flight_owner_registration
                                           UNIQUE (company_registration_number),

                                       CONSTRAINT uk_flight_owner_license
                                           UNIQUE (operating_license_number),

                                       CONSTRAINT fk_flight_owner_user
                                           FOREIGN KEY (user_id)
                                               REFERENCES users (id)
);