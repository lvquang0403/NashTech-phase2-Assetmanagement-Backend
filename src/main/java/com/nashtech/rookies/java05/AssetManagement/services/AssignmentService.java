package com.nashtech.rookies.java05.AssetManagement.services;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssignmentDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.request.ChangeStateAssignmentDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentDetailDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentListResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentResponseDto;

import java.util.List;

public interface AssignmentService {
    AssignmentResponseDto create(AssignmentDto dto);
    void update(AssignmentDto dto, Integer id);
    void delete(Integer id);
    APIResponse<List<AssignmentListResponseDto>> getAssignmentByPredicates
            (List<String> stateFilterList, String assignDate, String keyword, int page, String orderBy);
    AssignmentDetailDto getAssignment(int id);
    APIResponse<List<AssignmentListResponseDto>> getAssignmentsByUser
            (String id, int page, String orderBy);

    AssignmentDetailDto  changeStateAssignment(int assignmentId , ChangeStateAssignmentDto req);
}
