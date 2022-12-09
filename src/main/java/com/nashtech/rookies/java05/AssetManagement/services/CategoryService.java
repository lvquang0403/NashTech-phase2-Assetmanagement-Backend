package com.nashtech.rookies.java05.AssetManagement.services;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.CategoryRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.CategoryResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CategoryService {
    CategoryResponseDto createCategory(CategoryRequestDto dto);
    List<String> getCategoryNames();

    List<CategoryResponseDto> getCategories();
}
