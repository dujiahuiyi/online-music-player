package com.example.onlinemusic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude
		={org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class MyMusicApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyMusicApplication.class, args);
	}

}
