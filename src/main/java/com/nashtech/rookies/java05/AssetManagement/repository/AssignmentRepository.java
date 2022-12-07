package com.nashtech.rookies.java05.AssetManagement.repository;

import com.nashtech.rookies.java05.AssetManagement.entities.Assignment;

import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    @Query(value = "SELECT a FROM Assignment a join a.asset" +
            " WHERE (a.state IN :states)" +
            " AND (to_char(a.assignedDate, 'yyyy-mm-dd')= :assignDate Or :assignDate is null) " +
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
            " WHERE a.state IS NOT 'DECLINED'" +
            "  AND ( not exists(SELECT r FROM a.returning r) OR " +
            "exists(SELECT r FROM a.returning r WHERE r.state = 'WAITING_FOR_RETURNING'))" +
            "  AND (a.assignedTo.id = :id)" +
            " AND  (a.assignedDate <= :curDate)"
    )
    Page<Assignment> findByUserId(
            @Param("id") String id,
            @Param("curDate") Date curDate,
            Pageable p
    );

}
