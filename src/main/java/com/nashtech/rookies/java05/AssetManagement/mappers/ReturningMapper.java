package com.nashtech.rookies.java05.AssetManagement.mappers;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.ReturningRequestCreateDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.ReturningDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Assignment;
import com.nashtech.rookies.java05.AssetManagement.entities.Returning;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentReturnState;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentState;
import com.nashtech.rookies.java05.AssetManagement.exceptions.ResourceNotFoundException;
import com.nashtech.rookies.java05.AssetManagement.repository.AssignmentRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.UserRepository;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
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
                    .assignmentResponseDto(assignmentMapper.mapAssignmentEntityToResponseDto(returning.getAssignment()))
                    .assetViewResponseDto(assetMapper.mapAssetToAssetViewResponseDto(returning.getAsset()))
                    .build();
            return returningDto;
        }).collect(Collectors.toList());
        System.out.println(returningDtoList);
        return returningDtoList;
    }


    public ReturningDto mapReturningEntityCreateToReturningDto(Returning returning){
        ReturningDto returningDto = ReturningDto.builder()
                .id(returning.getId())
                .state(returning.getState().getName())
                .assignBy(returning.getAssignedBy().getUsername())
                .assignTo(returning.getAssignedTo().getUsername())
                .assignmentResponseDto(assignmentMapper.mapAssignmentEntityToResponseDto(returning.getAssignment()))
                .build();
        if(returning.getAcceptedBy()!=null){
            returningDto.setAcceptedBy(returning.getAcceptedBy().getUsername());
        }
        return returningDto;
    }

    public Returning mapReturningRequestDtoToEntityCreate(ReturningRequestCreateDto requestDto){
        Optional<Assignment> optionalAssignment = assignmentRepository.findById(requestDto.getAssignmentId());
        Optional<User> userOptional = userRepository.findById(requestDto.getRequestById());
        if (optionalAssignment.isEmpty()) {
            throw new ResourceNotFoundException("Assignment not exist");
        }
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("request sender not exist");
        }
        Assignment assignment = optionalAssignment.get();
        User user = userOptional.get();

//      Check state of Assignment
        if(!assignment.getState().equals(AssignmentState.ACCEPTED)){
            throw new IllegalArgumentException("state of assignment not suitable");
        }

        return Returning.builder()
                .assignment(assignment)
                .asset(assignment.getAsset())
                .assignedBy(assignment.getAssignedBy())
                .assignedTo(assignment.getAssignedTo())
                .requestedBy(user)
                .state(AssignmentReturnState.WAITING_FOR_RETURNING)
                .build();
    }
}
