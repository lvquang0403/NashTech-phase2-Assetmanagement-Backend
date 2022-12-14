package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.entities.Asset;
import com.nashtech.rookies.java05.AssetManagement.services.ReportService;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.ReportDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import com.nashtech.rookies.java05.AssetManagement.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    CategoryRepository categoryRepository;

    public List<ReportDto> getDataForReport(Integer locationId){
        List<ReportDto> result = new ArrayList<ReportDto>();
        List<Category> listCategories = categoryRepository.findAll();

        listCategories.forEach(category -> {
            ReportDto reportDto=new ReportDto();
            HashMap<AssetState,Integer> map = new HashMap<AssetState,Integer>();

//            set data default  with 0 for each state
            for (AssetState state:AssetState.values()){
                map.put(state,0);
            }

//          calculate the number of categories by state
            category.getListAssets().forEach(asset -> {
                if (locationId == asset.getLocation().getId()) {
                    Integer amount = map.get(asset.getState());
                    map.replace(asset.getState(),++amount);
                }

            });
//            set total for thi asset
            int sum=map.get(AssetState.AVAILABLE) + map.get(AssetState.ASSIGNED) + map.get(AssetState.NOT_AVAILABLE)
                    +map.get(AssetState.RECYCLED) + map.get(AssetState.RECYCLING);
//            set category name
            reportDto.setCategory(category.getName());
            reportDto.setAvailable(map.get(AssetState.AVAILABLE));
            reportDto.setAssigned(map.get(AssetState.ASSIGNED));
            reportDto.setNotAvailable(map.get(AssetState.NOT_AVAILABLE));
            reportDto.setRecycled(map.get(AssetState.RECYCLED));
            reportDto.setWaitingForRecycling(map.get(AssetState.RECYCLING));
            reportDto.setTotal(sum);
            result.add(reportDto);
        });
        return result;
    }
}
