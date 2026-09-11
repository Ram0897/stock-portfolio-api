CREATE TABLE stocks (
    id BIGSERIAL PRIMARY KEY,
    stock_name VARCHAR(255) NOT NULL,
    buy_price NUMERIC(19, 4) NOT NULL,
    quantity INTEGER NOT NULL,
    current_price NUMERIC(19, 4) NOT NULL
);
