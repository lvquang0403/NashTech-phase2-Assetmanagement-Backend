package com.nashtech.rookies.java05.AssetManagement.repository;


import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    List<Category> findCategoriesByNameIsIn(List<String> categoryNames);
    Category findCategoriesByName(String name);
    Category findCategoriesById(String id);
}
