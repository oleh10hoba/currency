CREATE TABLE IF NOT EXISTS currency (
    currency_id UUID PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    symbol VARCHAR(3) NOT NULL,
    is_active BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    activated_at TIMESTAMP WITH TIME ZONE,
    edited_at TIMESTAMP WITH TIME ZONE,
    deleted_at TIMESTAMP WITH TIME ZONE,

    CONSTRAINT name_unique UNIQUE(name),
    CONSTRAINT symbol_unique UNIQUE(symbol)
    );