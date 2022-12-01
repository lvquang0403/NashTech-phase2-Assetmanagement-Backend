package com.nashtech.rookies.java05.AssetManagement.controllers;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.CategoryRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.CategoryResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.ReturningDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Returning;
import com.nashtech.rookies.java05.AssetManagement.mappers.ReturningMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.ReturningRepository;
import com.nashtech.rookies.java05.AssetManagement.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/returns")
public class ReturningController {

    @Autowired
    private ReturningRepository returningRepository;
    @Autowired
    private ReturningMapper returningMapper;

    //temp api
    @GetMapping("")
    public List<ReturningDto> getAllReturns() {
        List<Returning> list = returningRepository.findAll();

        List<ReturningDto> result = new ArrayList<>();
        result = returningMapper.mapReturningEntityToReturningDto(list);
        return  result;
    }


}
