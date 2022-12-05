package com.nashtech.rookies.java05.AssetManagement.controler.services;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssignmentRequestPostDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssignmentRequestPutDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentResponseInsertDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentDetailDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentListResponseDto;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface AssignmentService {
    AssignmentResponseInsertDto create(AssignmentRequestPostDto dto);
    void update(AssignmentRequestPutDto dto, Integer id);
    void delete(Integer id);
    APIResponse<List<AssignmentListResponseDto>> getAssignmentByPredicates
            (List<String> stateFilterList, String assignDate, String keyword, int page, String orderBy);
    AssignmentDetailDto getAssignment(int id);
    APIResponse<List<AssignmentListResponseDto>> getAssignmentsByUser
            (String id, int page, String orderBy);
}
