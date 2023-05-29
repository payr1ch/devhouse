package com.example.devhouse;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.spring.autoconfigure.sql.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.io.IOException;

@SpringBootApplication
public class DevhouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevhouseApplication.class, args);
	}


}
