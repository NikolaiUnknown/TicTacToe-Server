CREATE TABLE players(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nickname VARCHAR(30) NOT NULL UNIQUE,
    password TEXT NOT NULL,
    date_of_registration DATE NOT NULL,
    rating INT
);

CREATE TABLE refresh_tokens(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    player_id BIGINT NOT NULL,
    token VARCHAR(36) NOT NULL UNIQUE,
    expiry_date timestamp,
    FOREIGN KEY (player_id) REFERENCES players(id)
);

CREATE TABLE games(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_player_id BIGINT NOT NULL,
    second_player_id BIGINT NOT NULL,
    winner_id BIGINT,
    date_of_start timestamp NOT NULL DEFAULT now(),
    date_of_end timestamp,
    status VARCHAR(12) NOT NULL
);