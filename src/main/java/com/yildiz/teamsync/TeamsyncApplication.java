package com.yildiz.teamsync;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.yildiz.teamsync.repositories.*;

@SpringBootApplication
public class TeamsyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamsyncApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(UserRepository userRepo, TeamRepository teamRepo, BoardRepository boardRepo, BoardColumnRepository colRepo) {
		return (args) -> {
			System.out.println("--- DB DIAGNOSE ---");
			System.out.println("Users: " + userRepo.count());
			System.out.println("Teams: " + teamRepo.count());
			System.out.println("Boards: " + boardRepo.count());
			System.out.println("Columns: " + colRepo.count());
			System.out.println("-------------------");
		};
	}
}
