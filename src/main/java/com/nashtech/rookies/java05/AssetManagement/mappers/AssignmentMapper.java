package com.nashtech.rookies.java05.AssetManagement.mappers;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentDetailDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentListResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Asset;
import com.nashtech.rookies.java05.AssetManagement.entities.Assignment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AssignmentMapper {
    public AssignmentResponseDto mapAssignmentEntityToResponseDto(Assignment assignment) {
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

    public List<AssignmentListResponseDto> mapAssignmentListToAssignmentListResponseDto(List<Assignment> assignmentList) {
        List<AssignmentListResponseDto> assignmentListResponseDtos = assignmentList.stream().map(a -> {
            AssignmentListResponseDto assetViewResponseDto = AssignmentListResponseDto.builder()
                    .id(a.getId())
                    .assetId(a.getAsset().getId())
                    .assetName(a.getAsset().getName())
                    .assignTo(a.getAssignedTo().getUsername())
                    .assignBy(a.getAssignedBy().getUsername())
                    .assignedDate(a.getAssignedDate())
                    .state(a.getState())
                    .category(a.getAsset().getCategory().getName())
                    .build();
            return assetViewResponseDto;
        }).collect(Collectors.toList());
        System.out.println(assignmentListResponseDtos);
        return assignmentListResponseDtos;
    }

    public AssignmentDetailDto mapAssignmentToAssignmentDetailDto(Assignment a) {
        AssignmentDetailDto assignmentDetailDto = AssignmentDetailDto.builder()

                .assetId(a.getAsset().getId())
                .assetName(a.getAsset().getName())
                .specification(a.getAsset().getSpecification())
                .assignTo(a.getAssignedTo().getUsername())
                .assignBy(a.getAssignedBy().getUsername())
                .assignedDate(a.getAssignedDate())
                .state(a.getState())
                .note(a.getNote())
                .build();
        return assignmentDetailDto;
    }


}
