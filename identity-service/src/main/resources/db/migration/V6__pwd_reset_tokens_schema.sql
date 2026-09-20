CREATE TABLE password_reset_tokens
(
    id UNIQUEIDENTIFIER NOT NULL
        CONSTRAINT pk_password_reset_tokens PRIMARY KEY,

    user_id UNIQUEIDENTIFIER NOT NULL,

    token_hash VARCHAR(64) NOT NULL,

    expires_at DATETIME2 NOT NULL,

    used_at DATETIME2 NULL,

    created_at DATETIME2 NOT NULL,

    updated_at DATETIME2 NOT NULL,

    CONSTRAINT uk_password_reset_token_hash
        UNIQUE (token_hash)
);

CREATE INDEX idx_password_reset_user_id
    ON password_reset_tokens(user_id);

CREATE INDEX idx_password_reset_token_hash
    ON password_reset_tokens(token_hash);