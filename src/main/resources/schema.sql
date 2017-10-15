CREATE EXTENSION IF NOT EXISTS CITEXT;

DROP TABLE IF EXISTS forums;
CREATE TABLE IF NOT EXISTS forums (
  posts   BIGINT,
  slug    TEXT   NOT NULL,
  threads INTEGER,
  title   TEXT   NOT NULL,
  "user"  CITEXT NOT NULL UNIQUE
);

DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users (
  about    TEXT          NOT NULL,
  email    CITEXT UNIQUE NOT NULL,
  fullname TEXT          NOT NULL,
  nickname CITEXT UNIQUE NOT NULL PRIMARY KEY
);

DROP TABLE IF EXISTS threads;
CREATE TABLE IF NOT EXISTS threads (
  author  CITEXT NOT NULL,
  created TIMESTAMP DEFAULT current_timestamp,
  forum   TEXT   NOT NULL,
  id      SERIAL PRIMARY KEY,
  message TEXT   NOT NULL,
  slug    CITEXT UNIQUE,
  title   TEXT   NOT NULL,
  votes   INTEGER
);

DROP TABLE IF EXISTS posts;
CREATE TABLE IF NOT EXISTS posts (
  author   CITEXT  NOT NULL,
  created  TIMESTAMP DEFAULT current_timestamp,
  forum    TEXT    NOT NULL,
  id       SERIAL PRIMARY KEY,
  isEdited BOOLEAN NOT NULL DEFAULT FALSE,
  message  TEXT    NOT NULL,
  parent   BIGINT  NOT NULL,
  thread   INTEGER NOT NULL
);
