package com.nashtech.rookies.java05.AssetManagement.mappers;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssignmentRequestPostDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssignmentRequestPutDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentResponseInsertDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentDetailDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentListResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Asset;
import com.nashtech.rookies.java05.AssetManagement.entities.Assignment;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import com.nashtech.rookies.java05.AssetManagement.exceptions.ResourceNotFoundException;
import com.nashtech.rookies.java05.AssetManagement.repository.AssetRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentMapper {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AssetRepository assetRepository;
    public AssignmentResponseDto mapAssignmentEntityToResponseDto(Assignment assignment){
        AssignmentResponseDto result = AssignmentResponseDto
                .builder()
                .updatedWhen(assignment.getUpdatedWhen())
                .id(assignment.getId())
                .createdWhen(assignment.getCreatedWhen())
                .state(assignment.getState())
                .note(assignment.getNote())
                .build();
        return result;
    }

    public Assignment mapAssignmentRequestPostDtoToAssignmentEntity(AssignmentRequestPostDto dto){
        User assignTo = userRepository.findById(dto.getAssignTo()).orElseThrow(
                () -> new ResourceNotFoundException(String.format("User with id %s is not found", dto.getAssignTo()))
        );
        User assignBy = userRepository.findById(dto.getAssignBy()).orElseThrow(
                () -> new ResourceNotFoundException(String.format("User with id %s is not found", dto.getAssignBy()))
        );
        Asset foundAsset = assetRepository.findById(dto.getAssetId()).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Asset with id %s is not found", dto.getAssetId()))
        );
        return Assignment.builder()
                .assignedBy(assignBy)
                .assignedTo(assignTo)
                .asset(foundAsset)
                .note(dto.getNote())
                .build();
    }
    public AssignmentResponseInsertDto mapEntityToResponseInsertDto(Assignment assignment){
        return AssignmentResponseInsertDto.builder()
                .id(assignment.getId())
                .createdWhen(assignment.getCreatedWhen().toString())
                .updatedWhen((assignment.getUpdatedWhen().toString()))
                .note(assignment.getNote())
                .state(assignment.getState().getName())
                .assignedTo(assignment.getAssignedTo().getId())
                .assignedBy(assignment.getAssignedBy().getId())
                .assignedDay(assignment.getAssignedDate())
                .assetId(assignment.getAsset().getId())
                .returningId(null)
                .build();
    }
    public List<AssignmentListResponseDto> mapAssignmentListToAssignmentListResponseDto(List<Assignment> assignmentList) {
        List<AssignmentListResponseDto> assignmentListResponseDtos = assignmentList.stream().map(a -> {
            AssignmentListResponseDto assetViewResponseDto = AssignmentListResponseDto.builder()
                    .id(a.getId())
                    .assetId(a.getAsset().getId())
                    .assetName(a.getAsset().getName())
                    .assignTo(a.getAssignedTo().getUsername())
                    .assignBy(a.getAssignedBy().getUsername())
                    .assignedDate(a.getAssignedDate())
                    .state(a.getState().getName())
                    .category(a.getAsset().getCategory().getName())
                    .isRequestForReturn(a.getReturning() != null)
                    .build();
            return assetViewResponseDto;
        }).collect(Collectors.toList());
        System.out.println(assignmentListResponseDtos);
        return assignmentListResponseDtos;
    }

    public AssignmentDetailDto mapAssignmentToAssignmentDetailDto(Assignment a) {
        AssignmentDetailDto assignmentDetailDto = AssignmentDetailDto.builder()
                .id(a.getId())
                .assetId(a.getAsset().getId())
                .assetName(a.getAsset().getName())
                .categoryName(a.getAsset().getCategory().getName())
                .specification(a.getAsset().getSpecification())
                .assignToUsername(a.getAssignedTo().getUsername())
                .assignByUsername(a.getAssignedBy().getUsername())
                .assignedDate(a.getAssignedDate())
                .state(a.getState().getName())
                .note(a.getNote())
                .assignToId(a.getAssignedTo().getId())
                .assignToFirstName(a.getAssignedTo().getFirstName())
                .assignToLastName(a.getAssignedTo().getLastName())
                .build();
        return assignmentDetailDto;
    }
    public Assignment mapRequestPutDtoToEntity(AssignmentRequestPutDto dto, Assignment foundAssignment){
        User assignTo = userRepository.findById(dto.getAssignTo()).orElseThrow(
                () -> new ResourceNotFoundException(String.format("User with id %s is not found", dto.getAssignTo()))
        );
        Asset foundAsset = assetRepository.findById(dto.getAssetId()).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Asset with id %s is not found", dto.getAssetId()))
        );
        foundAssignment.setAssignedTo(assignTo);
        foundAssignment.setAssignedDate(dto.getAssignedDate());
        foundAssignment.setAsset(foundAsset);
        foundAssignment.setNote(dto.getNote());
        return foundAssignment;
    }
}
