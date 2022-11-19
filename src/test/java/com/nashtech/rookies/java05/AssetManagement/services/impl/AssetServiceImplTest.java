package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssetRequestDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Asset;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import com.nashtech.rookies.java05.AssetManagement.repository.AssetRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.CategoryRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.LocationRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.PresentIdRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Date;

@SpringBootTest
public class AssetServiceImplTest {
    @Autowired
    AssetServiceImpl service;
    @MockBean
    AssetRepository repository;
    @MockBean
    CategoryRepository categoryRepository;
    @MockBean
    LocationRepository locationRepository;
    @MockBean
    PresentIdRepository presentIdRepository;

    AssetRequestDto assetRequestDto;
    Asset initialAsset;
    Asset expectedAsset;
    Long oneDay = 1000*60*60*24L;
    java.util.Date now;

    @BeforeEach
    void beforeEach() {
        now = new java.util.Date();
        assetRequestDto = new AssetRequestDto();
        assetRequestDto.setName("fridge max5");
        assetRequestDto.setSpecification("as");
        assetRequestDto.setCategoryId("pc");
        assetRequestDto.setState(AssetState.AVAILABLE);
        assetRequestDto.setLocationId(1);
        assetRequestDto.setInstalledDate(new Date(now.getTime()-oneDay));

        initialAsset = new Asset();
        expectedAsset = new Asset();
    }

    @Test
    void insert_ShouldThrowNullPointerException_WhenParamsIsNull(){
        assetRequestDto.setName(null);

        NullPointerException exception = Assertions.assertThrows(NullPointerException.class,
                ()->service.insert(assetRequestDto));

        Assertions.assertEquals("null name",exception.getMessage());
    }

    @Test
    void insert_ShouldThrowIllegalArgumentException_WhenParamsIsEmptyString(){
        assetRequestDto.setName("");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->service.insert(assetRequestDto));

        Assertions.assertEquals("empty name",exception.getMessage());
    }

    @Test
    void insert_ShouldThrowIllegalArgumentException_WhenNameTooLong(){
        assetRequestDto.setName("aaaaaaaaaaaKaaaaaaaaaaaKaaaaaaaaaaaKaaaaaaaaaaaKaaaaaaaaaaa");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->service.insert(assetRequestDto));


        Assertions.assertEquals("name is too long, name up to 50 characters long",exception.getMessage());
    }

    @Test
    void insert_ShouldThrowIllegalArgumentException_WhenSpecificationTooLong(){
        String str = "";
        for (int i = 0; i < 51; i++) {
            str += "1234567890";
        }
        assetRequestDto.setSpecification(str);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->service.insert(assetRequestDto));


        Assertions.assertEquals("specification is too long, specification up to 500 characters long",exception.getMessage());
    }

    @Test
    void insert_ShouldThrowIllegalArgumentException_WhenInstalledDateNotPastDate(){
        assetRequestDto.setInstalledDate(new Date(now.getTime()+oneDay));

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->service.insert(assetRequestDto));

        Assertions.assertEquals("installed date must be a date in the past",exception.getMessage());
    }

    @Test
    void insert_ShouldThrowIllegalArgumentException_WhenNameHasSpecialCharacters(){
        assetRequestDto.setName("hahah2~");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->service.insert(assetRequestDto));

        Assertions.assertEquals("Name cannot contain special characters:! @ # $ % & * ( )  _ + = |  < > ? { } [ ] ~",exception.getMessage());
    }


}
