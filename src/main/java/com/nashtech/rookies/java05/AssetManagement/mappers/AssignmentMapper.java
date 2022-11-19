package com.nashtech.rookies.java05.AssetManagement.mappers;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Assignment;
import org.springframework.stereotype.Component;

@Component
public class AssignmentMapper {
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
}
