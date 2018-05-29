CREATE TABLE IF NOT EXISTS Users (
  id       LONG PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(64) NOT NULL UNIQUE,
  password VARCHAR(60) NOT NULL,
  enabled  BOOLEAN          DEFAULT TRUE,
  strikes  INTEGER          DEFAULT 0
);

CREATE TABLE IF NOT EXISTS Authorities (
  id        LONG PRIMARY KEY AUTO_INCREMENT,
  username  VARCHAR(64),
  authority VARCHAR(32),

  FOREIGN KEY (username) REFERENCES Users (username)
);

-- user
MERGE INTO Users (username, password, enabled, strikes)
KEY (username)
VALUES ('user', '$2a$10$hXJx1IBhxH2fcTa/NR2ZMetAKy.4w3SoWeJm7FiEjK6XjOOtyRQmO', TRUE, 0);
MERGE INTO Authorities (username, authority)
KEY (username, authority)
VALUES ('user', 'USER');

-- admin
MERGE INTO Users (username, password, enabled, strikes)
KEY (username)
VALUES ('admin', '$2a$10$hXJx1IBhxH2fcTa/NR2ZMetAKy.4w3SoWeJm7FiEjK6XjOOtyRQmO', TRUE, 0);
MERGE INTO Authorities (username, authority)
KEY (username, authority)
VALUES ('admin', 'USER');
MERGE INTO Authorities (username, authority)
KEY (username, authority)
VALUES ('admin', 'ADMIN');

-- user
MERGE INTO Users (username, password, enabled, strikes)
KEY (username)
VALUES ('test', '$2a$10$Fsc5FnxUvahJit.krSUmN.fYDLVlCOBF3l5FGdrOeuEVJFLQkD/cC', TRUE, 0);
MERGE INTO Authorities (username, authority)
KEY (username, authority)
VALUES ('test', 'USER');

