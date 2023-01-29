package com.github.roveraven.TrainingTelegrambot;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(scripts = {"/sql/clearDbs.sql", "/sql/telegram_users.sql"})
class TrainingTelegrambotApplicationTests {

	//@Test
	void contextLoads() {
	}

}
