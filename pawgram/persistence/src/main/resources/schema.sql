CREATE TABLE IF NOT EXISTS users (
id SERIAL PRIMARY KEY,
name varchar(50),
surname varchar(50),
mail varchar(50),
password char(60)
);
