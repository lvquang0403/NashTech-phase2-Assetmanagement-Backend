package com.nashtech.rookies.java05.AssetManagement.controllers;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.ReportDto;
import com.nashtech.rookies.java05.AssetManagement.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/reports")
public class ReportController {
    @Autowired
    ReportService reportService;

    @GetMapping
    public ResponseEntity getDataReport(@RequestParam(required = false, defaultValue = "") Integer locationId){
        List<ReportDto> listData=reportService.getDataForReport(locationId);
        return ResponseEntity.ok(listData);
    }
}
