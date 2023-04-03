
ALTER TABLE group_x_user DROP CONSTRAINT group_x_user_user_id_fkey;
ALTER TABLE group_x_user ALTER COLUMN user_id TYPE INT USING user_id::integer;
ALTER TABLE tg_user ALTER COLUMN chat_id TYPE INT USING chat_id::integer;
ALTER TABLE group_x_user ADD CONSTRAINT group_x_user_ibfk_1 FOREIGN KEY (user_id) REFERENCES tg_user(chat_id);
ALTER TABLE group_x_user ADD CONSTRAINT group_x_user_upk1 PRIMARY KEY (user_id, group_sub_id);