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