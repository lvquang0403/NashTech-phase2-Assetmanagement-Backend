package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.CategoryRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.CategoryResponseInsertDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import com.nashtech.rookies.java05.AssetManagement.exceptions.RepeatDataException;
import com.nashtech.rookies.java05.AssetManagement.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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
    Category    initiaCategory;
    Category    expectedCategory;
    List<Category> categoryList;

    @BeforeEach
    void beforeEach(){
        categoryRequestDto = new CategoryRequestDto();
        initiaCategory = new Category();
        expectedCategory = new Category();
        initiaCategory.setId("ww");
        initiaCategory.setName("computer");
        categoryList = new ArrayList<>();
        categoryList.add(initiaCategory);
    }
//    test insert

    @Test
    void insert_ShouldThrowNullPointerException_WhenParamsIsNull(){
        categoryRequestDto.setId("a");
        categoryRequestDto.setName(null);

        Mockito.when(repository.findAll()).thenReturn(categoryList);
        NullPointerException exception = Assertions.assertThrows(NullPointerException.class,
                ()->service.insert(categoryRequestDto));

        Assertions.assertEquals("null name",exception.getMessage());
    }

    @Test
    void insert_ShouldThrowIllegalArgumentException_WhenParamsIsEmptyString(){
        categoryRequestDto.setId("");
        categoryRequestDto.setName("a");

        Mockito.when(repository.findAll()).thenReturn(categoryList);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->service.insert(categoryRequestDto));

        Assertions.assertEquals("empty prefix",exception.getMessage());
    }

    @Test
    void insert_ShouldThrowIllegalArgumentException_WhenNameTooLong(){
        categoryRequestDto.setId("qw");
        categoryRequestDto.setName("aaaaaaaaaaaaaaaaaaaaTaaaaaaaaaaaaaaaaaaaaTaaaaaaaaaaaaaaaaaaaa");

        Mockito.when(repository.findAll()).thenReturn(categoryList);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->service.insert(categoryRequestDto));

        Assertions.assertEquals("name is too long, name up to 50 characters long",exception.getMessage());
    }

    @Test
    void insert_ShouldThrowIllegalArgumentException_WhenPrefixTooLong(){
        categoryRequestDto.setId("qws");
        categoryRequestDto.setName("computer");

        Mockito.when(repository.findAll()).thenReturn(categoryList);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->service.insert(categoryRequestDto));

        Assertions.assertEquals("prefix only 2 characters",exception.getMessage());
    }

    @Test
    void insert_ShouldThrowRepeatDataException_WhenCategoryAlreadyExists(){
        categoryRequestDto.setId("Qs");
        categoryRequestDto.setName("computer");
        Mockito.when(repository.findAll()).thenReturn(categoryList);
        RepeatDataException exception = Assertions.assertThrows(RepeatDataException.class,
                ()->service.insert(categoryRequestDto));

        Assertions.assertEquals("category already exists",exception.getMessage());
    }

    @Test
    void insert_ShouldThrowRepeatDataException_WhenPrefixAlreadyExists(){
        categoryRequestDto.setId("ww");
        categoryRequestDto.setName("mahua");
        Mockito.when(repository.findAll()).thenReturn(categoryList);
        RepeatDataException exception = Assertions.assertThrows(RepeatDataException.class,
                ()->service.insert(categoryRequestDto));

        Assertions.assertEquals("prefix already exists",exception.getMessage());
    }

    @Test
    void insert_ShouldCategoryResponseInsertDto_WhenRequestValid(){
        categoryRequestDto.setId("qc");
        categoryRequestDto.setName("mahua");
        expectedCategory.setId("qc");
        expectedCategory.setName("mahua");

        Mockito.when(repository.findAll()).thenReturn(categoryList);
        Mockito.when(repository.save(any(Category.class))).thenReturn(expectedCategory);

        CategoryResponseInsertDto actual = service.insert(categoryRequestDto);
        Assertions.assertEquals(categoryRequestDto.getId(), actual.getId());
    }


    @Test
    void getToInsert_ShouldListCategoryResponseInsertDto_WhenDatabaseHasData(){
        expectedCategory.setId("qc");
        expectedCategory.setName("mahua");
        categoryList.add(expectedCategory);
        categoryList.add(expectedCategory);

        Mockito.when(repository.findAll()).thenReturn(categoryList);

        List<CategoryResponseInsertDto> actual = service.getToInsert();
        Assertions.assertEquals(3, actual.size());
    }

    @Test
    void insert_ShouldThrowIllegalArgumentException_WhenNameHasSpecialCharacters(){
        categoryRequestDto.setId("Sw");
        categoryRequestDto.setName("mahua~!@");
        Mockito.when(repository.findAll()).thenReturn(categoryList);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->service.insert(categoryRequestDto));

        Assertions.assertEquals("Name cannot contain special characters:! @ # $ % & * ( )  _ + = |  < > ? { } [ ] ~",exception.getMessage());
    }
    @Test
    void insert_ShouldThrowIllegalArgumentException_WhenIdHasSpecialCharacters(){
        categoryRequestDto.setId("S@");
        categoryRequestDto.setName("mahua");
        Mockito.when(repository.findAll()).thenReturn(categoryList);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->service.insert(categoryRequestDto));

        Assertions.assertEquals("ID cannot contain special characters:! @ # $ % & * ( )  _ + = |  < > ? { } [ ] ~",exception.getMessage());
    }
    @Test
    void insert_ShouldThrowIllegalArgumentException_WhenNameVietnameseWithAccents(){
        categoryRequestDto.setId("SH");
        categoryRequestDto.setName("XE số");
        Mockito.when(repository.findAll()).thenReturn(categoryList);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->service.insert(categoryRequestDto));

        Assertions.assertEquals("Name Do not use Vietnamese with accents",exception.getMessage());
    }

    @Test
    void insert_ShouldThrowIllegalArgumentException_WhenIdVietnameseWithAccents(){
        categoryRequestDto.setId("sỐ");
        categoryRequestDto.setName("XE sO");
        Mockito.when(repository.findAll()).thenReturn(categoryList);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->service.insert(categoryRequestDto));

        Assertions.assertEquals("ID Do not use Vietnamese with accents",exception.getMessage());
    }
}
