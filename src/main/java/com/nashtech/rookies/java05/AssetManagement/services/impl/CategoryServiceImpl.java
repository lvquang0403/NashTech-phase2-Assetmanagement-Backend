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
    private CategoryRepository repository;
    @Autowired
    private CategoryMapper mapper;

    public CategoryResponseInsertDto insert(CategoryRequestDto dto) {
        Optional<List<Category>> optional = Optional.of(repository.findAll());
        dto.validateInsert(optional);
        Category category = mapper.mapCategoryRequestDtoToEntityInsert(dto);
        Category newcategory = repository.save(category);

        return mapper.mapEntityToResponseInsertDto(newcategory);
    }

    public List<CategoryResponseInsertDto> getToInsert() {
        List<CategoryResponseInsertDto> result = new ArrayList<>();
        Optional<List<Category>> optional = Optional.of(repository.findAll());
        if (optional.isEmpty()) {
            return new ArrayList<>();
        }
        optional.get().forEach(p -> {
            CategoryResponseInsertDto map = mapper.mapEntityToResponseInsertDto(p);
            result.add(map);
        });
        return result;
    }


}
