package com.nashtech.rookies.java05.AssetManagement.services;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssignmentDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentDetailDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentResponseDto;

import java.util.List;

public interface AssignmentService {
    AssignmentResponseDto createAssignment(AssignmentDto dto);
    void updateAssignment(AssignmentDto dto, Integer id);
    void deleteAssignment(Integer id);
    APIResponse<List<AssignmentResponseDto>> getAssignmentByPredicates
            (List<String> stateFilterList, String assignDate, String keyword, int page, String orderBy);
    AssignmentDetailDto getAssignment(int id);
    APIResponse<List<AssignmentResponseDto>> getAssignmentsByUser
            (String id, int page, String orderBy);

    AssignmentDetailDto  changeStateAssignment(int assignmentId , AssignmentDto req);
}
