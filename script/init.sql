CREATE TABLE IF NOT EXISTS "user"
(
 "user_id"    INT PRIMARY KEY,
 "name"       VARCHAR(50) NOT NULL,
 "last_name"  VARCHAR(50) NOT NULL,
 "post_id"    INT NOT NULL
);

CREATE TABLE IF NOT EXISTS "post"
(
 "post_id"    SERIAL PRIMARY KEY,
 "name"       VARCHAR(50) NOT NULL,
 "post_code"  VARCHAR(5) NOT NULL
);

ALTER TABLE
	"user"
ADD CONSTRAINT
	user_post
FOREIGN KEY
	(post_id)
REFERENCES
	"post" (post_id)
;

INSERT INTO
	"post" (name, post_code)
VALUES
	('Opoczno-1', 26300)
	, ('Opoczno-2', 26301)
	, ('Białaczów', 26307)
	, ('Poświętne pod Pilicą', 26315)
	, ('Żarnów', 26330)
;

INSERT INTO
	"user" (user_id, name, last_name, post_id)
VALUES
	(1, 'Patryk', 'Janowski', 1)
	, (2, 'Adam', 'Michalak', 3)
	, (3, 'Eliza', 'Faust', 2)
	, (4, 'Stefania', 'Górzyńska', 2)
	, (5, 'Kazimiera', 'Kratka', 4)
;