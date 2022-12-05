package com.nashtech.rookies.java05.AssetManagement.controler.services.impl;

import com.nashtech.rookies.java05.AssetManagement.controler.services.AssignmentService;
import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssignmentRequestPostDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssignmentRequestPutDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.*;
import com.nashtech.rookies.java05.AssetManagement.entities.Assignment;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentReturnState;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentState;
import com.nashtech.rookies.java05.AssetManagement.exceptions.BadRequestException;
import com.nashtech.rookies.java05.AssetManagement.exceptions.ResourceNotFoundException;
import com.nashtech.rookies.java05.AssetManagement.mappers.AssignmentMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.AssignmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        if (!newAssignment.getAsset().getState().equals(AssetState.AVAILABLE)) {
            throw new BadRequestException("This asset isn't available to assign");
        }
        if (newAssignment.getAssignedTo().isDisabled()) {
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
    @Transactional
    public void update(AssignmentRequestPutDto dto, Integer id) {
        Assignment foundAssignment = assignmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Assignment with id %s is not found", id))
        );
        if(!foundAssignment.getState().equals(AssignmentState.WAITING)){
            throw new BadRequestException("Only can update assignment that have state is WATING");
        }
        Assignment updateAssignment = assignmentMapper.mapRequestPutDtoToEntity(dto, foundAssignment);
        Date dateNow = new Date();
        Timestamp now = new Timestamp(dateNow.getTime());
        updateAssignment.setUpdatedWhen(now);
        assignmentRepository.save(updateAssignment);
    }
    @Override
    public void delete(Integer id) {
        Assignment foundAssignment =  assignmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Assignment with id %s is not found", id))
        );
        if(foundAssignment.getState() != AssignmentState.WAITING){
            throw new BadRequestException("Only can delete assignments that have state is WATING");
        }
        foundAssignment.getAsset().setState(AssetState.AVAILABLE);
        assignmentRepository.save(foundAssignment);
        assignmentRepository.delete(foundAssignment);
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
        Optional<Assignment> assignmentFound = assignmentRepository.findById(id);
        if(assignmentFound.isEmpty()){
            return AssignmentDetailDto.builder().build();
        }
        return assignmentMapper.mapAssignmentToAssignmentDetailDto(assignmentFound.get());
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

        LocalDate localDate = LocalDate.now();
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());

        Page<Assignment> assignments;
        assignments = assignmentRepository.findByUserId
                (id, date, pageable);

        List<Assignment> result2 = assignments.stream().filter(a -> a.getReturning() == null ||
                a.getReturning().getState() == AssignmentReturnState.WAITING_FOR_RETURNING).collect(Collectors.toList());

        //return null;
        return new APIResponse<>(result2.toArray().length/15 + 1,
                assignmentMapper.mapAssignmentListToAssignmentListResponseDto(result2));
    }
}
