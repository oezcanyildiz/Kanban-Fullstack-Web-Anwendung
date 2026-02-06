package com.yildiz.teamsync;

import org.springframework.boot.SpringApplication;

public class TestTeamsyncApplication {

	public static void main(String[] args) {
		SpringApplication.from(TeamsyncApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
