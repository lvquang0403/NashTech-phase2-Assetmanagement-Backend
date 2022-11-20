package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.CategoryRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.CategoryResponseInsertDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import com.nashtech.rookies.java05.AssetManagement.mappers.CategoryMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.CategoryRepository;
import com.nashtech.rookies.java05.AssetManagement.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper mapper;

    public CategoryResponseInsertDto insert(CategoryRequestDto dto) {
        Optional<List<Category>> optional = Optional.of(categoryRepository.findAll());
        dto.validateInsert(optional);
        Category category = mapper.mapCategoryRequestDtoToEntityInsert(dto);
        Category newcategory = categoryRepository.save(category);

        return mapper.mapEntityToResponseInsertDto(newcategory);
    }

    @Override
    public List<String> getAllCategoriesName() {
        List<Category> categories = categoryRepository.findAll();
        List<String> result = new ArrayList<>();
        if(categories == null){
            return new ArrayList<>();
        }else{
            categories.forEach(category -> {
                result.add(category.getName());
            });
        }
        return result;
    }

}
