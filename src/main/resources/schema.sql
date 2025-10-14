CREATE TABLE IF NOT EXISTS account (
    account_number VARCHAR(6) PRIMARY KEY,
    account_holder_name VARCHAR(255) NOT NULL,
    pin VARCHAR(6) NOT NULL,
    balance LONG NOT NULL
);

CREATE TABLE IF NOT EXISTS transaction (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    source_account VARCHAR(6) NOT NULL,
    destination_account VARCHAR(6) DEFAULT NULL,
    amount LONG NOT NULL,
    reference_number VARCHAR(18) DEFAULT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    note VARCHAR (255) DEFAULT NULL
);