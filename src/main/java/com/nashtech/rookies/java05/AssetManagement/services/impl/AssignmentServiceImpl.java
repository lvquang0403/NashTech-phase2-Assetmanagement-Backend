package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssignmentRequestPostDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentResponseInsertDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Assignment;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentState;
import com.nashtech.rookies.java05.AssetManagement.exceptions.BadRequestException;
import com.nashtech.rookies.java05.AssetManagement.mappers.AssignmentMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.AssignmentRepository;
import com.nashtech.rookies.java05.AssetManagement.services.AssignmentService;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentDetailDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentListResponseDto;
import com.nashtech.rookies.java05.AssetManagement.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@Builder
public class AssignmentServiceImpl implements AssignmentService {
    private static final int pageSize = 15;

    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private AssignmentMapper assignmentMapper;

    @Override
    public AssignmentResponseInsertDto create(AssignmentRequestPostDto dto) {
        Assignment newAssignment = assignmentMapper.mapAssignmentRequestPostDtoToAssignmentEntity(dto);
        if(!newAssignment.getAsset().getState().equals(AssetState.AVAILABLE)){
            throw new BadRequestException("This asset isn't available to assign");
        }
        if(newAssignment.getAssignedTo().isDisabled()){
            throw new BadRequestException(String.format("User is assigned with id %s is disabled", newAssignment.getAssignedTo().getId()));
        }
        Date dateNow = new Date();
        Timestamp now = new Timestamp(dateNow.getTime());
        newAssignment.setCreatedWhen(now);
        newAssignment.setUpdatedWhen(now);
        newAssignment.setAssignedDate(dto.getAssignedDate());
        newAssignment.setState(AssignmentState.WAITING);
        newAssignment.getAsset().setState(AssetState.ASSIGNED);
        return assignmentMapper.mapEntityToResponseInsertDto(assignmentRepository.save(newAssignment));
    }

    @Override
    public APIResponse<List<AssignmentListResponseDto>> getAssignmentByPredicates
            (List<String> stateFilterList, String assignDate, String keyword, int page, String orderBy) {
        String[] parts = orderBy.split("_");
        String columnName = parts[0];
        String order = parts[1];
        Pageable pageable;
        if ("DESC".equals(order)) {
            pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, columnName);
        } else {
            pageable = PageRequest.of(page, pageSize, Sort.Direction.ASC, columnName);
        }

        List<AssignmentState> stateList = new ArrayList<>();
        AssignmentState[] assignmentStates = AssignmentState.values();


        if (stateFilterList.isEmpty()) { // if states filter = null, then find all
            for (AssignmentState assignmentState : assignmentStates) {
                    stateList.add(assignmentState);
            }
        } else {
            for (AssignmentState assignmentState : assignmentStates) {
                if (stateFilterList.contains(assignmentState.getName())) {
                    stateList.add(assignmentState);
                }
            }
        }

        Page<Assignment> result;
        result = assignmentRepository.findByPredicates
                (stateList, assignDate, keyword.toLowerCase(), pageable);
        //return null;
        return new APIResponse<>(result.getTotalPages(),
                assignmentMapper.mapAssignmentListToAssignmentListResponseDto(result.toList()));
    }

    @Override
    public AssignmentDetailDto getAssignment(int id) {
        Assignment assetFound = assignmentRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Assignment not exist with id: " + id));

        return assignmentMapper.mapAssignmentToAssignmentDetailDto(assetFound);
    }

    @Override
    public APIResponse<List<AssignmentListResponseDto>> getAssignmentsByUser(String id, int page, String orderBy) {
        String[] parts = orderBy.split("_");
        String columnName = parts[0];
        String order = parts[1];
        Pageable pageable;
        if ("DESC".equals(order)) {
            pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, columnName);
        } else {
            pageable = PageRequest.of(page, pageSize, Sort.Direction.ASC, columnName);
        }

        Page<Assignment> result;
        result = assignmentRepository.findByUserId
                (id, pageable);
        //return null;
        return new APIResponse<>(result.getTotalPages(),
                assignmentMapper.mapAssignmentListToAssignmentListResponseDto(result.toList()));
    }
}
