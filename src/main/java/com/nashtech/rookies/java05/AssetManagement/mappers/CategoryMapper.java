package com.nashtech.rookies.java05.AssetManagement.mappers;


import com.nashtech.rookies.java05.AssetManagement.dtos.request.CategoryRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.CategoryResponseInsertDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category mapCategoryRequestDtoToEntityInsert(CategoryRequestDto dto){
        return  Category.builder()
                .name(dto.getName())
                .id(dto.getId())
                .build();
    }

    public CategoryResponseInsertDto mapEntityToResponseInsertDto(Category category){
        return  CategoryResponseInsertDto.builder()
                .name(category.getName())
                .id(category.getId())
                .build();
    }
}
