CREATE TYPE "genre" AS ENUM (
  'COMEDEY',
  'DRAMA',
  'CARTOON',
  'THRILLER',
  'DOCUMENTARY',
  'ACTION'
);

CREATE TYPE "mpa" AS ENUM (
  'G',
  'PG',
  'PG13',
  'R',
  'NC'
);

CREATE TABLE "users" (
  "user_id" int4 PRIMARY KEY,
  "email" varchar(50),
  "login" varchar(30),
  "name" varchar(100),
  "birthday" date
);

CREATE TABLE "friends" (
  "id" int4 PRIMARY KEY,
  "user_id" int4,
  "friend_id" int4
);

CREATE TABLE "likes" (
  "id" int4 PRIMARY KEY,
  "user_id" int4,
  "film_id" int4
);

CREATE TABLE "films" (
  "film_id" int4 PRIMARY KEY,
  "name" varchar(50),
  "description" varchar(150),
  "release_date" date,
  "duration" int2,
  "rate" int4,
  "mpa" mpa,
  "likes_count" int4
);

CREATE TABLE "genres" (
  "id" int4 PRIMARY KEY,
  "film_id" int4,
  "genre" genre
);

ALTER TABLE "genres" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("film_id");

ALTER TABLE "friends" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("user_id");

ALTER TABLE "likes" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("user_id");

ALTER TABLE "likes" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("film_id");
