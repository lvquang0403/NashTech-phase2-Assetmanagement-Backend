package com.nashtech.rookies.java05.AssetManagement;

import com.nashtech.rookies.java05.AssetManagement.utils.UserUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AssetManagementApplication {

	@Bean
	UserUtils userUtils(){
		return new UserUtils();
	}

	public static void main(String[] args) {
		SpringApplication.run(AssetManagementApplication.class, args);
	}

}
