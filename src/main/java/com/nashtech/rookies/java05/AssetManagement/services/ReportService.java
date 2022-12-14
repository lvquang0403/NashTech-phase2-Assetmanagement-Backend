package com.nashtech.rookies.java05.AssetManagement.services;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.ReportDto;

import java.util.List;

public interface ReportService {
    public List<ReportDto> getDataForReport(Integer locationId);
}
