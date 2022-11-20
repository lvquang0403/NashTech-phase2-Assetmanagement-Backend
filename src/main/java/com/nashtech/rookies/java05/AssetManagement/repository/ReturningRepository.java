package com.nashtech.rookies.java05.AssetManagement.repository;

import com.nashtech.rookies.java05.AssetManagement.entities.Returning;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentReturnState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReturningRepository extends JpaRepository<Returning, Integer> {
    List<Returning> findByAssetIdAndState(String assetId, AssignmentReturnState state);
}
