package com.nashtech.rookies.java05.AssetManagement.mappers;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.ReturningDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Assignment;
import com.nashtech.rookies.java05.AssetManagement.entities.Returning;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentReturnState;
import com.nashtech.rookies.java05.AssetManagement.repository.AssignmentRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReturningMapper {
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AssignmentMapper assignmentMapper;
    @Autowired
    private AssetMapper assetMapper;
    public List<ReturningDto> toDtoList(List<Returning> returningList){
        List<ReturningDto> returningDtoList = returningList.stream().map(returning -> {
            ReturningDto returningDto = ReturningDto.builder()
                    .id(returning.getId())
                    .state(returning.getState().getName())
                    .assignBy(returning.getAssignedBy().getUsername())
                    .assignTo(returning.getAssignedTo().getUsername())
                    .requestedBy(returning.getRequestedBy().getUsername())
                    .acceptedBy(returning.getAcceptedBy() == null ? "" : returning.getAcceptedBy().getUsername())
                    .returnedDate(returning.getReturnedDate() == null ? null : Date.valueOf(returning.getReturnedDate().toLocalDateTime().toLocalDate()))
                    .assignmentResponseDto(assignmentMapper.ToResponseDto(returning.getAssignment()))
                    .assetViewResponseDto(assetMapper.mapAssetToAssetViewResponseDto(returning.getAsset()))
                    .build();
            return returningDto;
        }).collect(Collectors.toList());
        System.out.println(returningDtoList);
        return returningDtoList;
    }


    public ReturningDto toDto(Returning returning){
        ReturningDto returningDto = ReturningDto.builder()
                .id(returning.getId())
                .state(returning.getState().getName())
                .assignBy(returning.getAssignedBy().getUsername())
                .assignTo(returning.getAssignedTo().getUsername())
                .requestedBy(returning.getRequestedBy().getUsername())
                .acceptedBy(returning.getAcceptedBy() == null ? "" : returning.getAcceptedBy().getUsername())
                .returnedDate(returning.getReturnedDate() == null ? null : Date.valueOf(returning.getReturnedDate().toLocalDateTime().toLocalDate()))
                .assignmentResponseDto(assignmentMapper.ToResponseDto(returning.getAssignment()))
                .assetViewResponseDto(assetMapper.mapAssetToAssetViewResponseDto(returning.getAsset()))
                .build();
        return returningDto;
    }

    public Returning toEntity(Assignment assignment ,User requestedBy){
        return Returning.builder()
                .assignment(assignment)
                .asset(assignment.getAsset())
                .assignedBy(assignment.getAssignedBy())
                .assignedTo(assignment.getAssignedTo())
                .requestedBy(requestedBy)
                .state(AssignmentReturnState.WAITING_FOR_RETURNING)
                .build();
    }
}
