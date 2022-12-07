package com.nashtech.rookies.java05.AssetManagement.repository;

import com.nashtech.rookies.java05.AssetManagement.entities.Returning;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentReturnState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturningRepository extends JpaRepository<Returning, Integer> {
    List<Returning> findByAssetIdAndState(String assetId, AssignmentReturnState state);

    @Query(value = "SELECT r FROM Returning r left join r.asset left join User u on r.acceptedBy.id = u.id" +
            " WHERE (r.state IN :states)" +
            " AND (to_char(r.returnedDate, 'yyyy-mm-dd')= :returnedDate OR :returnedDate is null) " +
            " AND (r.requestedBy.location.id = 1)" +
            " AND (LOWER(r.asset.name) LIKE concat('%',:keyword,'%') OR LOWER(r.asset.id) LIKE concat('%',:keyword,'%') " +
            " OR LOWER(r.requestedBy.username) LIKE concat('%',:keyword,'%'))"
    )
    Page<Returning> findByPredicates(
            @Param("states") List<AssignmentReturnState> states,
            @Param("returnedDate") String returnedDate,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
