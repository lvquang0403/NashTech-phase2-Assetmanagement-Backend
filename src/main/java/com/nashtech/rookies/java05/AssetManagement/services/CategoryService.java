package com.nashtech.rookies.java05.AssetManagement.services;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CategoryService {

    List<String> getAllCategoriesName();
}
