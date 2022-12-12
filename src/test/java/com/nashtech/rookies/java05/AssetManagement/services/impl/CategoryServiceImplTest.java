package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.CategoryRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.CategoryResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import com.nashtech.rookies.java05.AssetManagement.exceptions.RepeatDataException;
import com.nashtech.rookies.java05.AssetManagement.mappers.CategoryMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.CategoryRepository;
import com.nashtech.rookies.java05.AssetManagement.utils.EntityCheckUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
public class CategoryServiceImplTest {
    CategoryServiceImpl service;
    CategoryRepository repository;
    CategoryRequestDto categoryRequestDto;
    Category    initialCategory;
    Category    expectedCategory;
    List<Category> categoryList;
    ModelMapper modelMapper;

    CategoryMapper categoryMapper;

    EntityCheckUtils entityCheckUtils;
    CategoryResponseDto expectedResponseDto;
    @BeforeEach
    void beforeEach(){
        repository = mock(CategoryRepository.class);
        categoryMapper = mock(CategoryMapper.class);
        modelMapper = mock(ModelMapper.class);
        entityCheckUtils = mock(EntityCheckUtils.class);

        categoryRequestDto = new CategoryRequestDto();
        initialCategory = new Category();
        expectedCategory = new Category();
        initialCategory.setId("ww");
        initialCategory.setName("computer");
        categoryList = new ArrayList<>();
        categoryList.add(initialCategory);

        expectedResponseDto = CategoryResponseDto.builder().build();

        service= CategoryServiceImpl.builder()
                .categoryRepository(repository)
                .mapper(categoryMapper)
                .entityCheckUtils(entityCheckUtils)
                .build();
    }



    @Test
    void create_ShouldThrowRepeatDataException_WhenCategoryAlreadyExists(){
        doNothing().when(entityCheckUtils).categoryCheckInsert(categoryRequestDto);
        categoryRequestDto.setId("Qs");
        categoryRequestDto.setName("computer");
        initialCategory.setName("posusiu");
        initialCategory.setId("ps");
        Mockito.when(repository.findCategoriesByName("computer")).thenReturn(null);
        Mockito.when(repository.findCategoriesById("Qs")).thenReturn(initialCategory);

        RepeatDataException exception = Assertions.assertThrows(RepeatDataException.class,
                ()->service.createCategory(categoryRequestDto));

        Assertions.assertEquals("category already exists",exception.getMessage());
    }
    @Test
    void create_ShouldThrowRepeatDataException_WhenPrefixAlreadyExists(){
        doNothing().when(entityCheckUtils).categoryCheckInsert(categoryRequestDto);
        categoryRequestDto.setId("Qs");
        categoryRequestDto.setName("computer");
        initialCategory.setName("posusiu");
        initialCategory.setId("ps");
        Mockito.when(repository.findCategoriesByName("computer")).thenReturn(initialCategory);
        Mockito.when(repository.findCategoriesById("Qs")).thenReturn(null);

        RepeatDataException exception = Assertions.assertThrows(RepeatDataException.class,
                ()->service.createCategory(categoryRequestDto));

        Assertions.assertEquals("prefix already exists",exception.getMessage());
    }



    @Test
    void create_ShouldCategoryResponseInsertDto_WhenRequestValid(){
        doNothing().when(entityCheckUtils).categoryCheckInsert(categoryRequestDto);
        categoryRequestDto.setId("Qs");
        categoryRequestDto.setName("computer");
        initialCategory.setName("posusiu");
        initialCategory.setId("ps");

        expectedCategory.setId("Qs");
        expectedCategory.setName("computer");
        expectedResponseDto.setId("Qs");

        Mockito.when(repository.findCategoriesByName("computer")).thenReturn(initialCategory);
        Mockito.when(repository.findCategoriesById("Qs")).thenReturn(initialCategory);
        Mockito.when(categoryMapper.toEntity(any(CategoryRequestDto.class))).thenReturn(expectedCategory);
        Mockito.when(repository.save(any(Category.class))).thenReturn(expectedCategory);
        Mockito.when(categoryMapper.toDto(expectedCategory)).thenReturn(expectedResponseDto);

        CategoryResponseDto actual = service.createCategory(categoryRequestDto);
        Assertions.assertEquals(categoryRequestDto.getId(), actual.getId());
    }
}
