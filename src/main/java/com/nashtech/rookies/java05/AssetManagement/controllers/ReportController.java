package com.nashtech.rookies.java05.AssetManagement.controllers;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.ReportDto;
import com.nashtech.rookies.java05.AssetManagement.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/reports")
public class ReportController {
    @Autowired
    ReportService reportService;

    @GetMapping
    public ResponseEntity getDataReport(){
        List<ReportDto> listData=reportService.getDataForReport();
        return ResponseEntity.ok(listData);
    }
}
