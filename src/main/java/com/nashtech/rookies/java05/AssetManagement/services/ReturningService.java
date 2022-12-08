package com.nashtech.rookies.java05.AssetManagement.services;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.ReturningRequestCreateDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.ReturningDto;

import java.util.List;

public interface ReturningService {
    APIResponse<List<ReturningDto>> getReturningByPredicates(List<String> states, String returnedDate, String keyword, int page, String orderBy, int locationId);

    public  ReturningDto  create(ReturningRequestCreateDto requestDto);
}
