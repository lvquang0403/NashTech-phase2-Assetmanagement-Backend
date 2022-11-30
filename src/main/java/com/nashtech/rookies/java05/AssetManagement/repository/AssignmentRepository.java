package com.nashtech.rookies.java05.AssetManagement.repository;

import com.nashtech.rookies.java05.AssetManagement.entities.Asset;
import com.nashtech.rookies.java05.AssetManagement.entities.Assignment;
import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    @Query(value = "SELECT a FROM Assignment a" +
            " WHERE a.state IN :states" +
            " AND to_char(a.assignedDate, 'yyyy-mm-dd')= :assignDate Or :assignDate is null " +
            " AND (LOWER(a.asset.name) LIKE concat('%',:keyword,'%') OR LOWER(a.asset.id) LIKE concat('%',:keyword,'%') " +
            " OR LOWER(a.assignedTo.username) LIKE concat('%',:keyword,'%') " +
            " OR LOWER(a.assignedBy.username) LIKE concat('%',:keyword,'%'))"
    )
    Page<Assignment> findByPredicates(
            @Param("states") List<AssignmentState> states,
            @Param("assignDate") String assignDate,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query(value = "SELECT a FROM Assignment a join a.asset" +
            " WHERE a.assignedTo.id = :id"
    )
    Page<Assignment> findByUserId(
            @Param("id") String id,
            Pageable pageable
    );
}
