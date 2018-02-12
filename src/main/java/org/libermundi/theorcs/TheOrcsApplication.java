package org.libermundi.theorcs;

import org.libermundi.theorcs.repositories.impl.UndeletableRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = UndeletableRepositoryImpl.class)
public class TheOrcsApplication {
	public static void main(String[] args) {
		SpringApplication.run(TheOrcsApplication.class, args);
	}
}
