CREATE TABLE user_mfa (
                          id UNIQUEIDENTIFIER NOT NULL,

                          user_id UNIQUEIDENTIFIER NOT NULL,

                          secret VARCHAR(500) NOT NULL,

                          status VARCHAR(20) NOT NULL
                              CONSTRAINT df_user_mfa_status DEFAULT 'NOT_CONFIGURED',

                          created_at DATETIME2 NOT NULL,

                          updated_at DATETIME2 NOT NULL,

                          CONSTRAINT pk_user_mfa
                              PRIMARY KEY (id),

                          CONSTRAINT uk_user_mfa_user
                              UNIQUE (user_id),

                          CONSTRAINT fk_user_mfa_user
                              FOREIGN KEY (user_id)
                                  REFERENCES users (id)
);