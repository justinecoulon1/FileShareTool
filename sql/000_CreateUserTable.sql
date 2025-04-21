CREATE TABLE Users(
	user_id int GENERATED ALWAYS AS IDENTITY,
	name varchar(50) UNIQUE NOT NULL,
	email varchar(100) UNIQUE NOT NULL,
	password varchar NOT NULL,

	CONSTRAINT PK_users PRIMARY KEY (user_id)
);