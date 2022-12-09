package com.nashtech.rookies.java05.AssetManagement.services;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.RequestReturnDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.ReturningDto;

import java.util.List;

public interface ReturningService {
    APIResponse<List<ReturningDto>> getReturningByPredicates(List<String> states, String returnedDate, String keyword, int page, String orderBy, int locationId);
    void completeReturning(RequestReturnDto dto, Integer id);
    ReturningDto createReturning(RequestReturnDto requestDto);

    String cancelReturning(Integer id);
}
