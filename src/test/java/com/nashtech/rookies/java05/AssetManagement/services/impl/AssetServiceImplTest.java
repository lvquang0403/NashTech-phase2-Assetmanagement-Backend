package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Asset;
import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import com.nashtech.rookies.java05.AssetManagement.entities.Returning;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentReturnState;
import com.nashtech.rookies.java05.AssetManagement.mappers.AssetMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.AssetRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.CategoryRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.ReturningRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

class AssetServiceImplTest {
    ModelMapper modelMapper;
    private CategoryRepository categoryRepository;
    private AssetRepository assetRepository;
    private AssetMapper assetMapper;
    private ReturningRepository returningRepository;
    private AssetServiceImpl assetServiceImpl;
    private Asset asset;
    private AssetResponseDto assetResponseDto;
    private Returning returning;
    @Mock
    private List<AssetState> states;
    @Mock
    private List<String> categoryNames;
    @Mock
    private List<Category> categories;
    @Mock
    private List<AssetViewResponseDto> assetViewResponseDtos;

    private APIResponse apiResponse;
    @BeforeEach
    void beforeEach(){
        asset = mock(Asset.class);
        modelMapper = mock(ModelMapper.class);
        returning = mock(Returning.class);
        apiResponse = mock(APIResponse.class);
        assetResponseDto = mock(AssetResponseDto.class);
        categoryRepository = mock(CategoryRepository.class);
        assetRepository = mock(AssetRepository.class);
        assetMapper = mock(AssetMapper.class);
        returningRepository = mock(ReturningRepository.class);
        assetServiceImpl = AssetServiceImpl
                .builder()
                .modelMapper(modelMapper)
                .categoryRepository(categoryRepository)
                .assetMapper(assetMapper)
                .assetRepository(assetRepository)
                .returningRepository(returningRepository)
                .build();

    }

    @ExtendWith(MockitoExtension.class)
    @Test
    void getAssetsByPredicates_WhenCategoryNamesParamsIsNotNull() {
        Page<Asset> result = mock(Page.class);
        String keyword = "";
        int locationId = 0;
        int page = 0;
        int pageSize = 15;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "updatedWhen");
        when(categoryRepository.findCategoriesByNameIsIn(categoryNames)).thenReturn(categories);
        when(assetRepository.findByKeywordWithFilter
                (categories, states, keyword.toLowerCase(), keyword.toLowerCase(), locationId, pageable))
                .thenReturn(result);
        when(assetMapper.mapAssetListToAssetViewResponseDto(result.toList())).thenReturn(assetViewResponseDtos);
        APIResponse<List<AssetViewResponseDto>> expected = new APIResponse<>(page, assetViewResponseDtos);

        APIResponse<List<AssetViewResponseDto>> listResult = assetServiceImpl.getAssetsByPredicates(states, categoryNames, keyword, locationId, page);
        assertThat(expected, is(listResult));
    }

    @Test
    void getAssetsByPredicates_WhenCategoryNamesParamsIsNull() {
        Page<Asset> result = mock(Page.class);
        String keyword = "";
        int locationId = 0;
        int page = 0;
        int pageSize = 15;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "updatedWhen");
        when(categoryRepository.findAll()).thenReturn(categories);

        when(assetRepository.findByKeywordWithFilter
                (categories, states, keyword.toLowerCase(), keyword.toLowerCase(), locationId, pageable))
                .thenReturn(result);
        when(assetMapper.mapAssetListToAssetViewResponseDto(result.toList())).thenReturn(assetViewResponseDtos);
        APIResponse<List<AssetViewResponseDto>> expected = new APIResponse<>(page, assetViewResponseDtos);

        APIResponse<List<AssetViewResponseDto>> listResult = assetServiceImpl.getAssetsByPredicates(states, null, keyword, locationId, page);
        assertThat(expected, is(listResult));
    }

    @Test
    void getAssetById_WhenAssetIsNotNull() {
        List<Returning> returningHistoryList = new ArrayList<>();
        returningHistoryList.add(returning);
        when(assetRepository.findById(asset.getId())).thenReturn(Optional.of(asset));
        when(returningRepository.findByAssetIdAndState(asset.getId(), AssignmentReturnState.WAITING_FOR_RETURNING))
                .thenReturn(returningHistoryList);

        when(assetMapper.mapAssetEntityToAssetResponseDto(Optional.of(asset).get())).thenReturn(assetResponseDto);

        AssetResponseDto result = assetServiceImpl.getAssetById(asset.getId());
        assertThat(assetResponseDto, is(result));
    }

    @Test
    void getAssetById_WhenAssetIsNull() {
        AssetResponseDto expected = AssetResponseDto.builder().build();
        when(assetRepository.findById(asset.getId())).thenReturn(Optional.empty());
        AssetResponseDto result = assetServiceImpl.getAssetById(asset.getId());
        assertThat(expected, is(result));
    }

}