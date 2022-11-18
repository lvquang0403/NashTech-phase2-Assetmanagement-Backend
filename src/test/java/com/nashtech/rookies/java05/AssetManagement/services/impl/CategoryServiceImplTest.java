package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import com.nashtech.rookies.java05.AssetManagement.repository.CategoryRepository;
import com.nashtech.rookies.java05.AssetManagement.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class CategoryServiceImplTest {

    private CategoryRepository categoryRepository;
    private CategoryServiceImpl categoryServiceImpl;
    private Category category;

    @BeforeEach
    void beforeEach(){
        category = mock(Category.class);
        categoryRepository = mock(CategoryRepository.class);
        categoryServiceImpl = CategoryServiceImpl.builder().categoryRepository(categoryRepository).build();
    }

    @Test
    void getAllCategoriesName_WhenCategoriesIsNotNull() {
        List<Category> listCategories = new ArrayList<>();
        List<String> expected = new ArrayList<>();
        category.setName("Expected");
        expected.add(category.getName());
        listCategories.add(category);
        when(categoryRepository.findAll()).thenReturn(listCategories);
        List<String> result = categoryServiceImpl.getAllCategoriesName();
        assertThat(expected,is(result));
    }

    @Test
    void getAllCategoriesName_WhenCategoriesIsNull(){
        List<String> expected = new ArrayList<>();
        //When categories is null it should return an empty list of String
        when(categoryRepository.findAll()).thenReturn(null);
        List<String> result = categoryServiceImpl.getAllCategoriesName();
        assertThat(expected,is(result));
    }
}










