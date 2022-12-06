package com.nashtech.rookies.java05.AssetManagement.controllers;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.CategoryRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.CategoryResponseDto;
import com.nashtech.rookies.java05.AssetManagement.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping(value = "")
    @ResponseBody
    public CategoryResponseDto insert(@RequestBody CategoryRequestDto dto){
        return categoryService.insert(dto);
    }

    @GetMapping("/name")
    public List<String> getAllCategoriesName(){
        List<String> result = categoryService.getAllCategoriesName();
        return result;
    }

    @GetMapping("")
    public List<CategoryResponseDto> getAll(){
        return categoryService.getAll();
    }
}
