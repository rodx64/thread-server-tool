package org.rba.sample_server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rba.sample_server.tasks.TaskServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@RequiredArgsConstructor
@ComponentScan
@EnableAutoConfiguration
public class SampleServerApplication implements CommandLineRunner {

	private final TaskServer taskServer;

	public static void main(String[] args) {
		SpringApplication.run(SampleServerApplication.class, args);
	}
	@Override
	public void run(String[] args){
		try {
			taskServer.exec();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
