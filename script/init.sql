CREATE TABLE IF NOT EXISTS "user"
(
 "user_id"    SERIAL PRIMARY KEY,
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
	"user" (name, last_name, post_id)
VALUES
	('Patryk', 'Janowski', 1)
	, ('Adam', 'Michalak', 3)
	, ('Eliza', 'Faust', 2)
	, ('Stefania', 'Górzyńska', 2)
	, ('Kazimiera', 'Kratka', 4)
;