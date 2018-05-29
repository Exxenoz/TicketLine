CREATE TABLE IF NOT EXISTS users (
  id       LONG PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(64) NOT NULL UNIQUE,
  password VARCHAR(60) NOT NULL,
  enabled  BOOLEAN          DEFAULT TRUE,
  strikes  INTEGER          DEFAULT 0,
  password_change_key VARCHAR(8)
);

CREATE TABLE IF NOT EXISTS authorities (
  username  VARCHAR(255) NOT NULL,
  authority VARCHAR(255) NOT NULL,
  PRIMARY KEY (username, authority),
  FOREIGN KEY (username) REFERENCES users (username)
);

-- user
MERGE INTO users (username, password)
KEY (username)
VALUES ('user', '$2a$10$hXJx1IBhxH2fcTa/NR2ZMetAKy.4w3SoWeJm7FiEjK6XjOOtyRQmO');
MERGE INTO authorities (username, authority)
KEY (username, authority)
VALUES ('user', 'USER');

-- admin
MERGE INTO users (username, password)
KEY (username)
VALUES ('admin', '$2a$10$hXJx1IBhxH2fcTa/NR2ZMetAKy.4w3SoWeJm7FiEjK6XjOOtyRQmO');
MERGE INTO authorities (username, authority)
KEY (username, authority)
VALUES ('admin', 'USER');
MERGE INTO authorities (username, authority)
KEY (username, authority)
VALUES ('admin', 'ADMIN');

-- Horst Gruntz
MERGE INTO users (username, password, enabled)
KEY (username)
VALUES ('hgruntz', '$2a$10$hXJx1IBhxH2fcTa/NR2ZMetAKy.4w3SoWeJm7FiEjK6XjOOtyRQmO', FALSE);
MERGE INTO authorities (username, authority)
KEY (username, authority)
VALUES ('hgruntz', 'USER');

-- Ursel Zahnweh
MERGE INTO users (username, password)
KEY (username)
VALUES ('uzahnweh', '$2a$10$hXJx1IBhxH2fcTa/NR2ZMetAKy.4w3SoWeJm7FiEjK6XjOOtyRQmO');
MERGE INTO authorities (username, authority)
KEY (username, authority)
VALUES ('uzahnweh', 'USER');
MERGE INTO authorities (username, authority)
KEY (username, authority)
VALUES ('uzahnweh', 'ADMIN');
