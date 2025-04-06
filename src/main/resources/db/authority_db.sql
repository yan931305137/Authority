CREATE TABLE sent_otp (
    session_id BINARY(16) NOT NULL PRIMARY KEY,
    otp VARCHAR(10) NOT NULL,
    expire_time BIGINT NOT NULL,
    destination VARCHAR(255) NOT NULL,
    last_sent_at BIGINT NOT NULL,
    attempts INT
);

CREATE TABLE registered_totp (
    username VARCHAR(255) NOT NULL PRIMARY KEY,
    secret VARCHAR(255) NOT NULL
);

CREATE TABLE webauthn_authenticators (
    username VARCHAR(255) NOT NULL,
    authenticator TEXT NOT NULL
);

CREATE INDEX IX_webauthn_authenticators_username ON webauthn_authenticators (username);