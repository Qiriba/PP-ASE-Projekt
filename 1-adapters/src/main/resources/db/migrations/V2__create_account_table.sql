CREATE TABLE account (
                         id UUID PRIMARY KEY,  -- UUID für Account
                         account_number VARCHAR(255) NOT NULL,  -- Kontonummer
                         balance DECIMAL(15, 2) NOT NULL,  -- Guthaben des Kontos
                         locked BOOLEAN NOT NULL DEFAULT FALSE,  -- Status, ob Konto gesperrt ist
                         customer_id UUID NOT NULL,  -- Fremdschlüssel für Customer
                         FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE  -- Beziehung zu Customer
);
