package com.nashtech.rookies.java05.AssetManagement.repository;

import com.nashtech.rookies.java05.AssetManagement.entities.Asset;
import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, String> {
//    Page<Asset> findByCategoryIsInAndStateIsInAndNameContainingIgnoreCase
//            (List<Category> categories, List<AssetState> states, String name, Pageable pageable);

    @Query(value = "SELECT a FROM Asset a join a.category" +
            " WHERE a.category IN :categories" +
            " AND (a.state IN :states OR :states is null)" +
            " AND (LOWER(a.name) LIKE %:name% OR LOWER(a.id) LIKE %:id%)" +
            " AND a.location.id = :locationId")
    Page<Asset> findByKeywordWithFilter(
            @Param("categories") List<Category> categories,
            @Param("states") List<AssetState> states,
            @Param("name") String name,
            @Param("id") String id,
            @Param("locationId") int locationId,
            Pageable pageable

    );
}
