\c postgres;
DROP DATABASE IF EXISTS horacertarepository;
CREATE DATABASE horacertarepository;

\c horacertarepository;

DO $$ 
BEGIN 
  IF NOT EXISTS (SELECT FROM pg_user WHERE usename = 'horacertamaster') THEN
    CREATE USER horacertamaster WITH PASSWORD '12345';
    ALTER USER horacertamaster CREATEDB;
    GRANT ALL PRIVILEGES ON DATABASE horacertarepository TO horacertamaster;
  END IF;
END $$;

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    area_of_expertise VARCHAR(255),
    hours_worked_daily int,
    hours_worked_weekly int,
    created_at timestamp NOT NULL
);

CREATE TABLE IF NOT EXISTS daily_work_info (
    id SERIAL PRIMARY KEY,
    userId int NOT NULL,
    started_at timestamp NOT NULL,
    finished_at timestamp NOT NULL,
    lunch_started_at timestamp NOT NULL,
    lunch_finished_at timestamp NOT NULL,
    is_day_off boolean NOT NULL,
    is_vacation boolean NOT NULL,
    created_at timestamp NOT NULL,
    FOREIGN KEY (userId) REFERENCES users(id)
);
