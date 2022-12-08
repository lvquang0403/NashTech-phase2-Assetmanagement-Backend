package com.nashtech.rookies.java05.AssetManagement.controllers;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.ReturningRequestCreateDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentListResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.ReturningDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Returning;
import com.nashtech.rookies.java05.AssetManagement.mappers.ReturningMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.ReturningRepository;
import com.nashtech.rookies.java05.AssetManagement.services.ReturningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/returns")
public class ReturningController {

    @Autowired
    ReturningService returningService;

    @GetMapping("")
    public APIResponse<List<ReturningDto>> getAllAssignment
            (@RequestParam(required = false, defaultValue = "") List<String> states,
             @RequestParam(required = false) String returnedDate,
             @RequestParam(required = false, defaultValue = "") String keyword,
             @RequestParam(value = "page", required = false, defaultValue = "0") int page,
             @RequestParam(defaultValue = "asset.id_DESC") String orderBy,
             @RequestParam int locationId) {

        return returningService.getReturningByPredicates(states, returnedDate, keyword, page, orderBy, locationId);
    }

    @PostMapping("")
    public ReturningDto create(@RequestBody ReturningRequestCreateDto dto) {
        return returningService.create(dto);
    }

}
