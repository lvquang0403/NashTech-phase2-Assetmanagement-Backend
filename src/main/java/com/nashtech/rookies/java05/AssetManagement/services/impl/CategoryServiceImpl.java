package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.CategoryRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.CategoryResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import com.nashtech.rookies.java05.AssetManagement.exceptions.RepeatDataException;
import com.nashtech.rookies.java05.AssetManagement.mappers.CategoryMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.CategoryRepository;
import com.nashtech.rookies.java05.AssetManagement.services.CategoryService;
import com.nashtech.rookies.java05.AssetManagement.utils.EntityCheckUtils;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Builder
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper mapper;
    @Autowired
    private EntityCheckUtils entityCheckUtils;

    public CategoryResponseDto createCategory(CategoryRequestDto dto) {
        entityCheckUtils.categoryCheckInsert(dto);
        Category categoriesByName = categoryRepository.findCategoriesByName(dto.getName());
        if (categoriesByName != null) {
            throw new RepeatDataException("category already exists");
        }
        Category categoriesById = categoryRepository.findCategoriesById(dto.getId());
        if (categoriesById != null) {
            throw new RepeatDataException("prefix already exists");
        }

        Category category = mapper.toEntity(dto);
        Category newcategory = categoryRepository.save(category);

        return mapper.toDto(newcategory);
    }

    @Override
    public List<String> getCategoryNames() {
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

    @Override
    public List<CategoryResponseDto> getCategories() {
        Optional<List<Category>> optional = Optional.ofNullable(categoryRepository.findAll());
        List<CategoryResponseDto> result = new ArrayList<>();
        if(optional.isEmpty()){
            return new ArrayList<>();
        }
        optional.get().forEach(category -> {
            result.add(mapper.toDto(category));
        });
        return result;
    }

}
