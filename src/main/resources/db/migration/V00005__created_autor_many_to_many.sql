
-- ensure that the tables with these names are removed before creating a new one.
DROP TABLE IF EXISTS author;
DROP TABLE IF EXISTS author_x_user;

CREATE TABLE author (
    author_id INT,
    name VARCHAR(100),
    last_post_id INT,
    PRIMARY KEY (author_id)
);

CREATE TABLE author_x_user (
    author_id INT NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES tg_user(chat_id),
    FOREIGN KEY (author_id) REFERENCES author(author_id),
    UNIQUE(user_id, author_id)
);
