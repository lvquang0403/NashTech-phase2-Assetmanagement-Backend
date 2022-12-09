package com.nashtech.rookies.java05.AssetManagement.mappers;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentDetailDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Assignment;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Builder
@NoArgsConstructor
public class AssignmentMapper {
    public AssignmentResponseDto ToResponseDto(Assignment assignment){
        AssignmentResponseDto result = AssignmentResponseDto
                .builder()
                .updatedWhen(assignment.getUpdatedWhen())
                .id(assignment.getId())
                .assignedDate(assignment.getAssignedDate())
                .createdWhen(assignment.getCreatedWhen())
                .state(assignment.getState().getName())
                .note(assignment.getNote())
                .build();
        return result;
    }
    public List<AssignmentResponseDto> toListResponseDto(List<Assignment> assignmentList) {
        List<AssignmentResponseDto> assignmentListResponseDtos = assignmentList.stream().map(a -> {
            AssignmentResponseDto assetViewResponseDto = AssignmentResponseDto.builder()
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

    public AssignmentDetailDto ToAssignmentDetailDto(Assignment a) {
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
}
