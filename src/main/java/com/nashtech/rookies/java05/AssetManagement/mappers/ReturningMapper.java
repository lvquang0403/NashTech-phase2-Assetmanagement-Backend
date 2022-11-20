package com.nashtech.rookies.java05.AssetManagement.mappers;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.ReturningDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Returning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReturningMapper {
    @Autowired
    private AssignmentMapper assignmentMapper;
    public List<ReturningDto> mapReturningEntityToReturningDto(List<Returning> returningList){
        List<ReturningDto> result = new ArrayList<>();
        returningList.forEach(returning -> {
            ReturningDto returningDto = ReturningDto.builder()
                    .id(returning.getId())
                    .state(returning.getState())
                    .AssignBy(returning.getAssignedBy().getUsername())
                    .AssignTo(returning.getAssignedTo().getUsername())
                    .returnedDate(returning.getReturnedDate())
                    .assignmentResponseDto(assignmentMapper.mapAssignmentEntityToResponseDto(returning.getAssignment()))
                    .build();

            result.add(returningDto);
        });
        return result;
    }
}
