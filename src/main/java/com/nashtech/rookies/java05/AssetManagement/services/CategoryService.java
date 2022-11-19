package com.nashtech.rookies.java05.AssetManagement.services;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.CategoryRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.CategoryResponseInsertDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CategoryService {
    public CategoryResponseInsertDto insert(CategoryRequestDto dto);
    public List<CategoryResponseInsertDto> getToInsert();
}
