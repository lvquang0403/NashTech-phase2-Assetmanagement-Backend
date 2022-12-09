package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.CategoryRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.CategoryResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import com.nashtech.rookies.java05.AssetManagement.exceptions.RepeatDataException;
import com.nashtech.rookies.java05.AssetManagement.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.*;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class CategoryServiceImplTest {

    @Autowired
    CategoryServiceImpl service;
    @MockBean
    CategoryRepository repository;
    CategoryRequestDto categoryRequestDto;
    Category    initialCategory;
    Category    expectedCategory;
    List<Category> categoryList;
    ModelMapper modelMapper;

    @BeforeEach
    void beforeEach(){
        categoryRequestDto = new CategoryRequestDto();
        initialCategory = new Category();
        expectedCategory = new Category();
        initialCategory.setId("ww");
        initialCategory.setName("computer");
        categoryList = new ArrayList<>();
        categoryList.add(initialCategory);
        modelMapper = mock(ModelMapper.class);
    }
//    test insert

    @Test
    void create_ShouldThrowNullPointerException_WhenParamsIsNull(){
        categoryRequestDto.setId("a");
        categoryRequestDto.setName(null);

        Mockito.when(repository.findAll()).thenReturn(categoryList);
        NullPointerException exception = Assertions.assertThrows(NullPointerException.class,
                ()->service.createCategory(categoryRequestDto));

        Assertions.assertEquals("null name",exception.getMessage());
    }

    @Test
    void create_ShouldThrowIllegalArgumentException_WhenParamsIsEmptyString(){
        categoryRequestDto.setId("");
        categoryRequestDto.setName("a");

        Mockito.when(repository.findAll()).thenReturn(categoryList);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->service.createCategory(categoryRequestDto));

        Assertions.assertEquals("empty prefix",exception.getMessage());
    }

    @Test
    void create_ShouldThrowIllegalArgumentException_WhenNameTooLong(){
        categoryRequestDto.setId("qw");
        categoryRequestDto.setName("aaaaaaaaaaaaaaaaaaaaTaaaaaaaaaaaaaaaaaaaaTaaaaaaaaaaaaaaaaaaaa");

        Mockito.when(repository.findAll()).thenReturn(categoryList);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->service.createCategory(categoryRequestDto));

        Assertions.assertEquals("name is too long, name up to 50 characters long",exception.getMessage());
    }

    @Test
    void create_ShouldThrowIllegalArgumentException_WhenPrefixTooLong(){
        categoryRequestDto.setId("qws");
        categoryRequestDto.setName("computer1111");

        Mockito.when(repository.findAll()).thenReturn(categoryList);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->service.createCategory(categoryRequestDto));

        Assertions.assertEquals("prefix only 2 characters",exception.getMessage());
    }

    @Test
    void create_ShouldThrowRepeatDataException_WhenCategoryAlreadyExists(){
        categoryRequestDto.setId("Qs");
        categoryRequestDto.setName("computer");
        Mockito.when(repository.findAll()).thenReturn(categoryList);
        RepeatDataException exception = Assertions.assertThrows(RepeatDataException.class,
                ()->service.createCategory(categoryRequestDto));

        Assertions.assertEquals("category already exists",exception.getMessage());
    }

    @Test
    void create_ShouldThrowRepeatDataException_WhenPrefixAlreadyExists(){
        categoryRequestDto.setId("ww");
        categoryRequestDto.setName("mahua");
        Mockito.when(repository.findAll()).thenReturn(categoryList);
        RepeatDataException exception = Assertions.assertThrows(RepeatDataException.class,
                ()->service.createCategory(categoryRequestDto));

        Assertions.assertEquals("prefix already exists",exception.getMessage());
    }

    @Test
    void create_ShouldCategoryResponseInsertDto_WhenRequestValid(){
        categoryRequestDto.setId("qc");
        categoryRequestDto.setName("mahua");
        expectedCategory.setId("qc");
        expectedCategory.setName("mahua");

        Mockito.when(repository.findAll()).thenReturn(categoryList);
        Mockito.when(repository.save(any(Category.class))).thenReturn(expectedCategory);

        CategoryResponseDto actual = service.createCategory(categoryRequestDto);
        Assertions.assertEquals(categoryRequestDto.getId(), actual.getId());
    }

    @Test
    void create_ShouldThrowIllegalArgumentException_WhenNameHasSpecialCharacters(){
        categoryRequestDto.setId("Sw");
        categoryRequestDto.setName("mahua~!@");
        Mockito.when(repository.findAll()).thenReturn(categoryList);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->service.createCategory(categoryRequestDto));

        Assertions.assertEquals("Name Cannot contain special characters",exception.getMessage());
    }
    @Test
    void create_ShouldThrowIllegalArgumentException_WhenIdHasSpecialCharacters(){
        categoryRequestDto.setId("S@");
        categoryRequestDto.setName("ma - hua");
        Mockito.when(repository.findAll()).thenReturn(categoryList);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->service.createCategory(categoryRequestDto));

        Assertions.assertEquals("ID Cannot contain special characters",exception.getMessage());
    }
    @Test
    void create_ShouldThrowIllegalArgumentException_WhenNameVietnameseWithAccents(){
        categoryRequestDto.setId("SH");
        categoryRequestDto.setName("XE số");
        Mockito.when(repository.findAll()).thenReturn(categoryList);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->service.createCategory(categoryRequestDto));

        Assertions.assertEquals("Name Do not use Vietnamese with accents",exception.getMessage());
    }

    @Test
    void create_ShouldThrowIllegalArgumentException_WhenIdVietnameseWithAccents(){
        categoryRequestDto.setId("sỐ");
        categoryRequestDto.setName("XE sO");
        Mockito.when(repository.findAll()).thenReturn(categoryList);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->service.createCategory(categoryRequestDto));

        Assertions.assertEquals("ID Do not use Vietnamese with accents",exception.getMessage());
    }
}
