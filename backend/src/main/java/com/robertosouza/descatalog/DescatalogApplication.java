package com.robertosouza.descatalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.robertosouza.descatalog.services.S3Service;

@SpringBootApplication
public class DescatalogApplication implements CommandLineRunner {

	
	@Autowired
	private S3Service s3;
	
	public static void main(String[] args) {
		SpringApplication.run(DescatalogApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		s3.uploadFile("C:\\wallpaperflare.com_wallpaper.jpg");
	}

}
