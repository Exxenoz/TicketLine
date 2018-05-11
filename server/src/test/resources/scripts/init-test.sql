CREATE TABLE IF NOT EXISTS Users (
  id       LONG PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(32) NOT NULL UNIQUE,
  password VARCHAR(60) NOT NULL,
  enabled  BOOLEAN          DEFAULT TRUE,
  strikes  INTEGER          DEFAULT 0
);

CREATE TABLE IF NOT EXISTS Authorities (
  id        LONG PRIMARY KEY AUTO_INCREMENT,
  username  VARCHAR(32),
  authority VARCHAR(32),

  FOREIGN KEY (username) REFERENCES Users (username)
);

-- user
MERGE INTO Users (username, password)
KEY (username)
VALUES ('test', '$2a$10$iZMUIt.55RRIthu4/ELV0.lRcaE7hZRbimJIUiA9/LXWKyzSvCq5e');
MERGE INTO Authorities (username, authority)
KEY (username, authority)
VALUES ('test', 'USER');