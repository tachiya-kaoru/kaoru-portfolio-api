CREATE TABLE learning_records (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    category_id BIGINT NOT NULL REFERENCES categories(id),
    learning_year INTEGER NOT NULL,
    learning_month INTEGER NOT NULL,
    item_name VARCHAR(255) NOT NULL,
    learning_time_minutes INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_learning_records_user_year_month_item
        UNIQUE (user_id, learning_year, learning_month, item_name)
);

CREATE INDEX idx_learning_records_user_id ON learning_records(user_id);
CREATE INDEX idx_learning_records_category_id ON learning_records(category_id);