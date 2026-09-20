CREATE TABLE roles
(
    id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,

    name VARCHAR(50) NOT NULL,

    description VARCHAR(255),

    created_at DATETIME2 NOT NULL,

    updated_at DATETIME2 NOT NULL,

    CONSTRAINT uk_roles_name UNIQUE(name)
);

CREATE TABLE users
(
    id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,

    username VARCHAR(50) NOT NULL,

    email VARCHAR(150) NOT NULL,

    password VARCHAR(255) NOT NULL,

    first_name VARCHAR(100) NOT NULL,

    last_name VARCHAR(100) NOT NULL,

    phone_number VARCHAR(20),

    status VARCHAR(30) NOT NULL,

    email_verified BIT NOT NULL,

    mfa_enabled BIT NOT NULL,

    created_at DATETIME2 NOT NULL,

    updated_at DATETIME2 NOT NULL,

    CONSTRAINT uk_users_email UNIQUE(email),

    CONSTRAINT uk_users_username UNIQUE(username)
);

CREATE TABLE user_roles
(
    id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,

    user_id UNIQUEIDENTIFIER NOT NULL,

    role_id UNIQUEIDENTIFIER NOT NULL,

    created_at DATETIME2 NOT NULL,

    updated_at DATETIME2 NOT NULL,

    CONSTRAINT fk_user_roles_user
        FOREIGN KEY(user_id)
            REFERENCES users(id),

    CONSTRAINT fk_user_roles_role
        FOREIGN KEY(role_id)
            REFERENCES roles(id),

    CONSTRAINT uk_user_role
        UNIQUE(user_id, role_id)
);