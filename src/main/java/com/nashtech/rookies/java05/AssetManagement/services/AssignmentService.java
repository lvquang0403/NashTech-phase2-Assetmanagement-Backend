package com.nashtech.rookies.java05.AssetManagement.services;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentDetailDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentListResponseDto;

import java.util.List;

public interface AssignmentService {
    APIResponse<List<AssignmentListResponseDto>> getAssignmentByPredicates
            (List<String> stateFilterList, String assignDate, String keyword, int page, String orderBy);

    AssignmentDetailDto getAssignment(int id);

    APIResponse<List<AssignmentListResponseDto>> getAssignmentsByUser
            (String id, int page, String orderBy);
}
