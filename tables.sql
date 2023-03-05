-- Drop existing tables if they exist
DROP TABLE IF EXISTS kitchen_store_user_purchases CASCADE;
DROP TABLE IF EXISTS kitchen_store_items CASCADE;
DROP TABLE IF EXISTS food_club_events CASCADE;
DROP TABLE IF EXISTS food_club_event_repository CASCADE;
DROP TABLE IF EXISTS kitchen_store_items_beverage CASCADE;
DROP TABLE IF EXISTS food_club_archived_recipe CASCADE;

-- Create table for kitchen_store_user_purchases entity
CREATE TABLE IF NOT EXISTS kitchen_store_user_purchases (
    id SERIAL PRIMARY KEY,
    room_number INTEGER NOT NULL,
    purchase_amount DOUBLE PRECISION NOT NULL,
    brand VARCHAR(255) NOT NULL,
    quantity INTEGER DEFAULT 1,
    date DATE NOT NULL
);

-- Create table for kitchen_store_items entity
CREATE TABLE IF NOT EXISTS kitchen_store_items (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    price DECIMAL(10,2),
    quantity INTEGER,
    food_type VARCHAR(255),
    description TEXT
);

-- Create table for FoodClubEvent entity
CREATE TABLE IF NOT EXISTS food_club_events (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  meeting_date VARCHAR(255) NOT NULL,
  meeting_time VARCHAR(255) NOT NULL,
  participants VARCHAR(255),
  chefs VARCHAR(255),
  recipe TEXT,
  food_picture BYTEA
);

-- Create index for id column in food_club_events table
CREATE INDEX idx_food_club_events_id ON food_club_events(id);

-- Create table for kitchen_store_items_beverage entity
CREATE TABLE IF NOT EXISTS kitchen_store_items_beverage (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    category VARCHAR(255),
    price DECIMAL(10, 2),
    quantity INTEGER,
    brand VARCHAR(255),
    description TEXT,
    percentage NUMERIC(4, 2),
    orange_factor BOOLEAN
);

-- Create table for ArchivedRecipe entity
CREATE TABLE IF NOT EXISTS food_club_archived_recipe (
    id BIGSERIAL PRIMARY KEY,
    recipe TEXT,
    food_picture BYTEA
);

-- Create repository table for FoodClubEventRepository
CREATE TABLE IF NOT EXISTS food_club_event_repository (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  food_club_event_id BIGINT NOT NULL,
  CONSTRAINT fk_food_club_event_repository FOREIGN KEY (food_club_event_id) REFERENCES food_club_events (id) ON DELETE CASCADE
);

-- Create index for id column in food_club_event_repository table
CREATE INDEX idx_food_club_event_repository_id ON food_club_event_repository(id);
