CREATE TABLE customer (
                          id UUID PRIMARY KEY,
                          username VARCHAR(255) NOT NULL,
                          password VARCHAR(255) NOT NULL,
                          pin VARCHAR(255) NOT NULL,
                          locked BOOLEAN NOT NULL DEFAULT FALSE,
                          failed_login_attempts INT NOT NULL DEFAULT 0
);
