package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.ReturningDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Returning;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentReturnState;
import com.nashtech.rookies.java05.AssetManagement.mappers.ReturningMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.ReturningRepository;
import com.nashtech.rookies.java05.AssetManagement.services.ReturningService;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Builder
@Service
public class ReturningServiceImpl implements ReturningService {
    private static final int pageSize = 15;

    @Autowired
    private ReturningRepository returningRepository;
    @Autowired
    ReturningMapper returningMapper;
    @Override
    public APIResponse<List<ReturningDto>> getReturningByPredicates(List<String> stateFilterList, String returnedDate, String keyword, int page, String orderBy, int locationId) {
        String[] parts = orderBy.split("_");
        String columnName = parts[0];
        String order = parts[1];
        Pageable pageable;
        if ("DESC".equals(order)) {
            pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, columnName);
        } else {
            pageable = PageRequest.of(page, pageSize, Sort.Direction.ASC, columnName);
        }

        List<AssignmentReturnState> stateList = new ArrayList<>();
        AssignmentReturnState[] returnStates = AssignmentReturnState.values();
        if (stateFilterList.isEmpty()) { // if states filter = null, then find all
            for (AssignmentReturnState state : returnStates) {
                stateList.add(state);
            }
        } else {
            for (AssignmentReturnState state : returnStates) {
                if (stateFilterList.contains(state.getName())) {
                    stateList.add(state);
                }
            }
        }
        Page<Returning> result;
        result = returningRepository.findByPredicates
                (stateList, returnedDate, keyword.toLowerCase(), locationId, pageable);
        //return null;
        return new APIResponse<>(result.getTotalPages(),
                returningMapper.toDtoList(result.toList()));

    }
}
