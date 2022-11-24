package com.nashtech.rookies.java05.AssetManagement.config;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
@RequiredArgsConstructor
public class AppConfig {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public Gson gson(){ return new Gson(); }

}
