CREATE TABLE refresh_tokens (
                                id UNIQUEIDENTIFIER NOT NULL,

                                user_id UNIQUEIDENTIFIER NOT NULL,

                                token_hash VARCHAR(64) NOT NULL,

                                expires_at DATETIME2 NOT NULL,

                                revoked BIT NOT NULL
                                    CONSTRAINT df_refresh_tokens_revoked DEFAULT 0,

                                revoked_at DATETIME2 NULL,

                                replaced_by_token_id UNIQUEIDENTIFIER NULL,

                                device_info VARCHAR(500) NULL,

                                ip_address VARCHAR(45) NULL,

                                created_at DATETIME2 NOT NULL,

                                updated_at DATETIME2 NOT NULL,

                                CONSTRAINT pk_refresh_tokens
                                    PRIMARY KEY (id),

                                CONSTRAINT uq_refresh_tokens_token_hash
                                    UNIQUE (token_hash),

                                CONSTRAINT fk_refresh_token_user
                                    FOREIGN KEY (user_id)
                                        REFERENCES users (id)
);

CREATE INDEX idx_refresh_token_user
    ON refresh_tokens (user_id);