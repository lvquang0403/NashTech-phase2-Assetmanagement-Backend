package com.nashtech.rookies.java05.AssetManagement.mappers;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.ReturningDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Returning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReturningMapper {
    @Autowired
    private AssignmentMapper assignmentMapper;
    @Autowired
    private AssetMapper assetMapper;
    public List<ReturningDto> mapReturningEntityToReturningDto(List<Returning> returningList){
        List<ReturningDto> returningDtoList = returningList.stream().map(returning -> {
            ReturningDto returningDto = ReturningDto.builder()
                    .id(returning.getId())
                    .state(returning.getState().getName())
                    .assignBy(returning.getAssignedBy().getUsername())
                    .assignTo(returning.getAssignedTo().getUsername())
                    .acceptedBy(returning.getAcceptedBy() == null ? "" : returning.getAcceptedBy().getUsername())
                    .returnedDate(returning.getReturnedDate())
                    .assignmentResponseDto(assignmentMapper.mapAssignmentEntityToResponseDto(returning.getAssignment()))
                    .assetViewResponseDto(assetMapper.mapAssetToAssetViewResponseDto(returning.getAsset()))
                    .build();
            return returningDto;
        }).collect(Collectors.toList());
        System.out.println(returningDtoList);
        return returningDtoList;
    }
}
