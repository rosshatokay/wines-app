CREATE TABLE IF NOT EXISTS wines (
    id SERIAL PRIMARY KEY,
    fixed_acidity DOUBLE PRECISION,
    volatile_acidity DOUBLE PRECISION,
    citric_acid DOUBLE PRECISION,
    residual_sugar DOUBLE PRECISION,
    chlorides DOUBLE PRECISION,
    free_sulfur_dioxide DOUBLE PRECISION,
    total_sulfur_dioxide DOUBLE PRECISION,
    density DOUBLE PRECISION,
    ph DOUBLE PRECISION,
    sulphates DOUBLE PRECISION,
    alcohol DOUBLE PRECISION,
    color VARCHAR(10),
    quality VARCHAR(50),  -- Updated to VARCHAR since it's "neutral" instead of INT
    date_added DATE
);
