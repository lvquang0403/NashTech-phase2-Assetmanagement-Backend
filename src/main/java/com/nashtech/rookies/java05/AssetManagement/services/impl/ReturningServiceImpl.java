package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.RequestReturnDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.request.ReturningRequestCreateDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.ReturningDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Returning;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentReturnState;
import com.nashtech.rookies.java05.AssetManagement.exceptions.BadRequestException;
import com.nashtech.rookies.java05.AssetManagement.exceptions.ResourceNotFoundException;
import com.nashtech.rookies.java05.AssetManagement.mappers.ReturningMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.ReturningRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.UserRepository;
import com.nashtech.rookies.java05.AssetManagement.services.ReturningService;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Service
public class ReturningServiceImpl implements ReturningService {
    private static final int pageSize = 15;

    @Autowired
    private ReturningRepository returningRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ReturningMapper returningMapper;
    @Override
    public APIResponse<List<ReturningDto>> getReturningByPredicates(List<String> stateFilterList, String returnedDate, String keyword, int page, String orderBy, int locationId) {
        String[] parts = orderBy.split("_");
        String columnName = parts[0];
        String order = parts[1];
        Pageable pageable;
        if ("DESC".equals(order)) {
            pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, columnName);
        } else {
            pageable = PageRequest.of(page, pageSize, Sort.Direction.ASC, columnName);
        }

        List<AssignmentReturnState> stateList = new ArrayList<>();
        AssignmentReturnState[] returnStates = AssignmentReturnState.values();
        if (stateFilterList.isEmpty()) { // if states filter = null, then find all
            for (AssignmentReturnState state : returnStates) {
                stateList.add(state);
            }
        } else {
            for (AssignmentReturnState state : returnStates) {
                if (stateFilterList.contains(state.getName())) {
                    stateList.add(state);
                }
            }
        }
        Page<Returning> result;
        result = returningRepository.findByPredicates
                (stateList, returnedDate, keyword.toLowerCase(), locationId, pageable);
        //return null;
        return new APIResponse<>(result.getTotalPages(),
                returningMapper.toDtoList(result.toList()));

    }

    @Override
    public void completeRequest(RequestReturnDto dto, Integer id) {
        Returning foundReturning = returningRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Return request with id %s is not found", id))
        );
        User acceptBy = userRepository.findById(dto.getAcceptBy()).orElseThrow(
                () -> new ResourceNotFoundException(String.format("User with id %s is not found", dto.getAcceptBy()))
        );
        if (!foundReturning.getState().equals(AssignmentReturnState.WAITING_FOR_RETURNING)) {
            throw new BadRequestException("Only complete request returning have state is wating for returning");
        }
        Date dayNow = new Date();
        Timestamp now = new Timestamp(dayNow.getTime());
        foundReturning.setReturnedDate(now);
        foundReturning.setState(AssignmentReturnState.COMPLETED);
        foundReturning.getAsset().setState(AssetState.AVAILABLE);
        foundReturning.setAcceptedBy(acceptBy);
        returningRepository.save(foundReturning);
    }
    @Override
    public ReturningDto create(ReturningRequestCreateDto requestDto) {
        Returning returning = returningMapper.mapReturningRequestDtoToEntityCreate(requestDto);
        Returning newReturning = returningRepository.save(returning);
        return returningMapper.mapReturningEntityCreateToReturningDto(newReturning);
    }
}
