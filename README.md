# java-filmorate

![scheme](/scheme.png)


--Examples of SQL requests:

--READ
```SQL
--("/films/{id}")
SELECT *
FROM "films"
WHERE "film_id" = id;

--("/films/popular")
SELECT *
FROM "films"
ORDER BY "likes_count" DESC
LIMIT(10);

--("/users")
SELECT *
FROM "users";


--("/users/{id}")
SELECT *
FROM "users"
WHERE "user_id" = id;

--("/users/{id}/friends")
SELECT *
FROM "friends" fr
INNER JOIN "users" u ON fr."friend_id"=u."user_id"
WHERE fr."user_id" = id;

--("/users/{id}/friends/common/{otherId}")
SELECT *
FROM "users" us
INNER JOIN (SELECT fr."user_id"
FROM "friends" fr
INNER JOIN "users" u1 ON fr."friend_id"=u1."user_id"
INNER JOIN "users" u2 ON fr."friend_id"=u2."user_id"
WHERE u1."user_id" = id
AND u2."user_id" = otherId) friends ON us."user_id"=friends."user_id";
```
