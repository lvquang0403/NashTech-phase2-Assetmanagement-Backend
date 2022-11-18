package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import com.nashtech.rookies.java05.AssetManagement.repository.CategoryRepository;
import com.nashtech.rookies.java05.AssetManagement.services.CategoryService;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Builder
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
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
