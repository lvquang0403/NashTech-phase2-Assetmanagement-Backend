package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssignmentDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Asset;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import com.nashtech.rookies.java05.AssetManagement.repository.AssetRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.UserRepository;
import com.nashtech.rookies.java05.AssetManagement.services.AssignmentService;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.*;
import com.nashtech.rookies.java05.AssetManagement.entities.Assignment;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
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
import java.util.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Timestamp;

@Service
@Builder
public class AssignmentServiceImpl implements AssignmentService {
    private static final int pageSize = 15;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private AssignmentMapper assignmentMapper;
    @Override
    public AssignmentResponseDto createAssignment(AssignmentDto dto) {
        User assignTo = userRepository.findById(dto.getAssignTo()).orElseThrow(
                () -> new ResourceNotFoundException(String.format("User with id %s is not found", dto.getAssignTo()))
        );
        User assignBy = userRepository.findById(dto.getAssignBy()).orElseThrow(
                () -> new ResourceNotFoundException(String.format("User with id %s is not found", dto.getAssignBy()))
        );
        Asset foundAsset = assetRepository.findById(dto.getAssetId()).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Asset with id %s is not found", dto.getAssetId()))
        );
        if (!foundAsset.getState().equals(AssetState.AVAILABLE)) {
            throw new BadRequestException("This asset isn't available to assign");
        }
        if (assignTo.isDisabled()) {
            throw new BadRequestException(String.format("User is assigned with id %s is disabled", assignTo.getId()));
        }
        foundAsset.setState(AssetState.ASSIGNED);
        Date dateNow = new Date();
        Timestamp now = new Timestamp(dateNow.getTime());
        Assignment newAssignment =  Assignment.builder()
                .assignedBy(assignBy)
                .assignedTo(assignTo)
                .asset(foundAsset)
                .note(dto.getNote())
                .createdWhen(now)
                .updatedWhen(now)
                .assignedDate(dto.getAssignedDate())
                .state(AssignmentState.WAITING)
                .asset(foundAsset)
                .build();
        return assignmentMapper.ToResponseDto(assignmentRepository.save(newAssignment));
    }

    @Override
    @Transactional
    public void updateAssignment(AssignmentDto dto, Integer id) {
        Assignment foundAssignment = assignmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Assignment with id %s is not found", id))
        );
        User assignTo = userRepository.findById(dto.getAssignTo()).orElseThrow(
                () -> new ResourceNotFoundException(String.format("User with id %s is not found", dto.getAssignTo()))
        );
        Asset foundAsset = assetRepository.findById(dto.getAssetId()).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Asset with id %s is not found", dto.getAssetId()))
        );
        if (!foundAssignment.getState().equals(AssignmentState.WAITING)) {
            throw new BadRequestException("Only can update assignment that have state is WATING");
        }
        foundAsset.setState(AssetState.ASSIGNED);
        Date dateNow = new Date();
        Timestamp now = new Timestamp(dateNow.getTime());
        foundAssignment.getAsset().setState(AssetState.AVAILABLE);
        assignmentRepository.save(foundAssignment);
        foundAssignment.setAssignedTo(assignTo);
        foundAssignment.setAssignedDate(dto.getAssignedDate());
        foundAssignment.setAsset(foundAsset);
        foundAssignment.setNote(dto.getNote());
        foundAssignment.setUpdatedWhen(now);
        assignmentRepository.save(foundAssignment);
    }

    @Override
    public void deleteAssignment(Integer id) {
        Assignment foundAssignment = assignmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Assignment with id %s is not found", id))
        );
        if (foundAssignment.getState() != AssignmentState.WAITING) {
            throw new BadRequestException("Only can delete assignments that have state is WATING");
        }
        foundAssignment.getAsset().setState(AssetState.AVAILABLE);
        assignmentRepository.save(foundAssignment);
        assignmentRepository.delete(foundAssignment);
    }

    @Override
    public APIResponse<List<AssignmentResponseDto>> getAssignmentByPredicates
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
                assignmentMapper.toListResponseDto(result.toList()));
    }

    @Override
    public AssignmentDetailDto getAssignment(int id) {
        Optional<Assignment> assignmentFound = assignmentRepository.findById(id);
        if (assignmentFound.isEmpty()) {
            return AssignmentDetailDto.builder().build();
        }
        return assignmentMapper.ToAssignmentDetailDto(assignmentFound.get());
    }

    @Override
    public APIResponse<List<AssignmentResponseDto>> getAssignmentsByUser(String id, int page, String orderBy) {
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

        return new APIResponse<>(assignments.getTotalPages(),
                assignmentMapper.toListResponseDto(assignments.toList()));
    }

    @Override
    public AssignmentDetailDto changeStateAssignment(int assignmentId, AssignmentDto req) {
        Assignment foundAssignment = assignmentRepository.findById(assignmentId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Assignment not found with id: " + assignmentId ))
        );
        if (!foundAssignment.getState().equals(AssignmentState.WAITING)) {
            throw new BadRequestException("Only can update assignment that have state is WATING");
        }
        Date dateNow = new Date();
        Timestamp now = new Timestamp(dateNow.getTime());

        AssignmentState[] assignmentStates = AssignmentState.values();
        for (AssignmentState assignmentState : assignmentStates) {
            if (req.getState().equals(assignmentState.toString()) ) {
                if(assignmentState == AssignmentState.DECLINED){
                    Asset a = foundAssignment.getAsset();
                    a.setState(AssetState.AVAILABLE);
                    a.setUpdatedWhen(now);
                    assetRepository.save(a);
                }
                foundAssignment.setState(assignmentState);
            }
        }

       
        foundAssignment.setUpdatedWhen(now);
        Assignment result = assignmentRepository.save(foundAssignment);

        return assignmentMapper.ToAssignmentDetailDto(result);
    }
}
