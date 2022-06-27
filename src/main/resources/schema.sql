CREATE TABLE IF NOT EXISTS GENRE_LIST (
    id int2 PRIMARY KEY,
    name varchar(22)
);

CREATE TABLE IF NOT EXISTS MPA(
    id int2 PRIMARY KEY,
    name varchar(5) UNIQUE
);

CREATE TABLE IF NOT EXISTS USERS (
  id int4 PRIMARY KEY AUTO_INCREMENT,
  email varchar(50),
  login varchar(30),
  name varchar(100),
  birthday date
);

CREATE TABLE IF NOT EXISTS FRIENDS (
  id int4 PRIMARY KEY AUTO_INCREMENT,
  user_id int4,
  friend_id int4
);

CREATE TABLE IF NOT EXISTS LIKES (
  id int4 PRIMARY KEY AUTO_INCREMENT,
  user_id int4,
  film_id int4
);

CREATE TABLE IF NOT EXISTS FILMS (
  id int4 PRIMARY KEY AUTO_INCREMENT,
  name varchar(50),
  description varchar(150),
  release_date date,
  duration int2,
  rate int4,
  mpa int2
);

CREATE TABLE IF NOT EXISTS GENRES (
  id int4 PRIMARY KEY AUTO_INCREMENT,
  film_id int4,
  genre int2
);

ALTER TABLE GENRES ADD FOREIGN KEY (film_id) REFERENCES FILMS (id);

ALTER TABLE FRIENDS ADD FOREIGN KEY (user_id) REFERENCES USERS (id);

ALTER TABLE LIKES ADD FOREIGN KEY (user_id) REFERENCES USERS (id);

ALTER TABLE LIKES ADD FOREIGN KEY (film_id) REFERENCES FILMS (id);
