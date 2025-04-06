CREATE TABLE transaction (
                             id UUID PRIMARY KEY,
                             timestamp TIMESTAMP NOT NULL,
                             amount DECIMAL(15, 2) NOT NULL,
                             type VARCHAR(50) NOT NULL,
                             source_account_id UUID NOT NULL,
                             target_account_id UUID,
                             FOREIGN KEY (source_account_id) REFERENCES account(id) ON DELETE CASCADE,
                             FOREIGN KEY (target_account_id) REFERENCES account(id) ON DELETE CASCADE
);
