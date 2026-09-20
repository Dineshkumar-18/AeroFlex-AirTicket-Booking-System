INSERT INTO roles
(
    id,
    name,
    description,
    created_at,
    updated_at
)
VALUES
    (
        NEWID(),
        'CUSTOMER',
        'Regular customer',
        SYSDATETIME(),
        SYSDATETIME()
    ),
    (
        NEWID(),
        'FLIGHT_OWNER',
        'Approved flight owner',
        SYSDATETIME(),
        SYSDATETIME()
    ),
    (
        NEWID(),
        'ADMIN',
        'System administrator',
        SYSDATETIME(),
        SYSDATETIME()
    );