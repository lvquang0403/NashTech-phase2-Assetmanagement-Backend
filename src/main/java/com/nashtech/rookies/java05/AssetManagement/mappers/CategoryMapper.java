package com.nashtech.rookies.java05.AssetManagement.mappers;


import com.nashtech.rookies.java05.AssetManagement.dtos.request.CategoryRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.CategoryResponseDto;
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

    public CategoryResponseDto mapEntityToResponseInsertDto(Category category){
        return  CategoryResponseDto.builder()
                .name(category.getName())
                .id(category.getId())
                .build();
    }
}
