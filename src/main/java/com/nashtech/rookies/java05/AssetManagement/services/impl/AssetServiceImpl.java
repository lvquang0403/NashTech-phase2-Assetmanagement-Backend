package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Asset;
import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import com.nashtech.rookies.java05.AssetManagement.mappers.AssetMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.AssetRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.CategoryRepository;
import com.nashtech.rookies.java05.AssetManagement.services.AssetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class AssetServiceImpl implements AssetService {
    private static final int pageSize = 15;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private AssetMapper assetMapper;

    @Override
    public List<AssetViewResponseDto> getListAssets() {
        Optional<List<Asset>> resultList = Optional.of(assetRepository.findAll(Sort.by(Sort.Direction.DESC, "updatedWhen")));
        if (resultList.isEmpty()) {
            return new ArrayList<>();
        }
        return assetMapper.mapAssetListToAssetViewResponseDto(resultList.get());
    }

    @Override
    public APIResponse<List<AssetViewResponseDto>> getAssetsByPredicates
            (List<AssetState> states, List<String> categoryNames, String keyword, int page) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "updatedWhen");

        //By default, filter by all categories, else filter by categories that user choose.
        List<Category> categories;
        if (categoryNames == null) {
            categories = categoryRepository.findAll();
        } else {
            categories = categoryRepository.findCategoriesByNameIsIn(categoryNames);
        }

        Page<Asset> result;
        result = assetRepository.findByKeywordWithFilter
                (categories, states, keyword.toLowerCase(), keyword.toLowerCase(), pageable);

        return new APIResponse<>(result.getTotalPages(), assetMapper.mapAssetListToAssetViewResponseDto(result.toList()));
    }

}
