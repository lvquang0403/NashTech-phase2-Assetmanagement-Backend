package com.nashtech.rookies.java05.AssetManagement.controler.services.impl;

import com.nashtech.rookies.java05.AssetManagement.controler.services.ReportService;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.ReportDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import com.nashtech.rookies.java05.AssetManagement.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    CategoryRepository categoryRepository;

    public List<ReportDto> getDataForReport(){
        List<ReportDto> result=new ArrayList<ReportDto>();
        List<Category> listCategories=categoryRepository.findAll();
        listCategories.forEach(category -> {
            ReportDto reportDto=new ReportDto();
            HashMap<AssetState,Integer> map=new HashMap<AssetState,Integer>();
            for (AssetState state:AssetState.values()){
                map.put(state,0);
            }
            reportDto.setCategory(category.getName());
            category.getListAssets().forEach(asset -> {
                Integer amount=map.get(asset.getState());
                map.replace(asset.getState(),++amount);
            });
            reportDto.setAvailable(map.get(AssetState.AVAILABLE));
            reportDto.setAssigned(map.get(AssetState.ASSIGNED));
            reportDto.setNotAvailable(map.get(AssetState.NOT_AVAILABLE));
            reportDto.setRecycled(map.get(AssetState.RECYCLED));
            reportDto.setWaitingForRecycling(map.get(AssetState.RECYCLING));
            reportDto.setTotal(category.getListAssets().size());
            result.add(reportDto);
        });
        return result;
    }
}
