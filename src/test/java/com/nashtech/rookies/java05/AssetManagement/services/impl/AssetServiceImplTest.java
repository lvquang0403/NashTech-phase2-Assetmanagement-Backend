package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssetRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.*;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentReturnState;
import com.nashtech.rookies.java05.AssetManagement.exceptions.ResourceNotFoundException;
import com.nashtech.rookies.java05.AssetManagement.mappers.AssetMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.*;
import com.nashtech.rookies.java05.AssetManagement.utils.EntityCheckUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class AssetServiceImplTest {

    CategoryRepository categoryRepository;
    LocationRepository locationRepository;
    PresentIdRepository presentIdRepository;
    EntityCheckUtils entityCheckUtils;
    ModelMapper modelMapper;
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

    AssetRequestDto assetRequestDto;
    Asset initialAsset;
    Category initialCategory;
    PresentId initialPresentId;
    Location initialLocation;
    AssetResponseDto expectedAssetResponseDto;
    Asset expectedAsset;
    Long oneDay = 1000 * 60 * 60 * 24L;
    java.util.Date now;

    @BeforeEach
    void beforeEach() {
        now = new java.util.Date();
        assetRequestDto = new AssetRequestDto();
        assetRequestDto.setName("fridge max5");
        assetRequestDto.setSpecification("as");
        assetRequestDto.setCategoryId("pc");
        assetRequestDto.setState(AssetState.AVAILABLE);
        assetRequestDto.setLocationId(1);
        assetRequestDto.setInstalledDate(new Date(now.getTime() - oneDay));

        entityCheckUtils = mock(EntityCheckUtils.class);
        asset = mock(Asset.class);
        modelMapper = mock(ModelMapper.class);
        returning = mock(Returning.class);
        apiResponse = mock(APIResponse.class);
        assetResponseDto = mock(AssetResponseDto.class);
        categoryRepository = mock(CategoryRepository.class);
        assetRepository = mock(AssetRepository.class);
        assetMapper = mock(AssetMapper.class);
        returningRepository = mock(ReturningRepository.class);
        locationRepository = mock(LocationRepository.class);
        presentIdRepository = mock(PresentIdRepository.class);

        initialAsset = new Asset();
        expectedAsset = new Asset();
        initialCategory = new Category();
        initialLocation = new Location();
        initialPresentId = new PresentId();
        expectedAssetResponseDto = AssetResponseDto.builder().build();

        assetServiceImpl = AssetServiceImpl
                .builder()
                .entityCheckUtils(entityCheckUtils)
                .categoryRepository(categoryRepository)
                .assetMapper(assetMapper)
                .assetRepository(assetRepository)
                .returningRepository(returningRepository)
                .locationRepository(locationRepository)
                .presentIdRepository(presentIdRepository)
                .build();
    }

    @Test
    void create_ShouldAssetResponseDto_WhenRequestValid() {
        assetRequestDto.setName("hahah2");
        assetRequestDto.setCategoryId("pc");
        assetRequestDto.setLocationId(1);
        initialPresentId.setAssetId(15);
        expectedAssetResponseDto.setName("hahah2");

        doNothing().when(entityCheckUtils).assetCheckInsert(assetRequestDto);
        when(categoryRepository.findById("pc")).thenReturn(Optional.ofNullable(initialCategory));
        when(locationRepository.findById(1)).thenReturn(Optional.ofNullable(initialLocation));
        when(presentIdRepository.findById("count")).thenReturn(Optional.ofNullable(initialPresentId));
        when(assetMapper.toEntityCreate(anyString(),any(AssetRequestDto.class),any(Category.class),any(Location.class))).thenReturn(initialAsset);

        when(presentIdRepository.save(any(PresentId.class))).thenReturn(initialPresentId);
        when(assetRepository.save(initialAsset)).thenReturn(initialAsset);
        when(assetMapper.toDto(initialAsset)).thenReturn(expectedAssetResponseDto);

        AssetResponseDto exception = assetServiceImpl.createAsset(assetRequestDto);

        Assertions.assertEquals("hahah2", exception.getName());
    }
    @Test
    void create_ShouldThrowIllegalArgumentException_WhenWarehouseIsFull() {
        assetRequestDto.setName("hahah2");
        assetRequestDto.setCategoryId("pc");
        assetRequestDto.setLocationId(1);
        initialPresentId.setAssetId(1000000);
        expectedAssetResponseDto.setName("hahah2");

        doNothing().when(entityCheckUtils).assetCheckInsert(assetRequestDto);
        when(categoryRepository.findById("pc")).thenReturn(Optional.ofNullable(initialCategory));
        when(locationRepository.findById(1)).thenReturn(Optional.ofNullable(initialLocation));
        when(presentIdRepository.findById("count")).thenReturn(Optional.ofNullable(initialPresentId));
        when(assetMapper.toEntityCreate(anyString(),any(AssetRequestDto.class),any(Category.class),any(Location.class))).thenReturn(initialAsset);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> assetServiceImpl.createAsset(assetRequestDto));

        Assertions.assertEquals("Asset warehouse is full", exception.getMessage());
    }


    @Test
    void update_ShouldAssetResponseDto_WhenRequestValid() {
        assetRequestDto.setName("hahah2");
        expectedAssetResponseDto.setName("hahah2");

        doNothing().when(entityCheckUtils).assetCheckUpdate(assetRequestDto);
        when(assetRepository.findById("PD000001")).thenReturn(Optional.ofNullable(initialAsset));


        when(assetMapper.toEntityUpdate(assetRequestDto,initialAsset)).thenReturn(initialAsset);

        when(presentIdRepository.save(any(PresentId.class))).thenReturn(initialPresentId);
        when(assetRepository.save(initialAsset)).thenReturn(initialAsset);
        when(assetMapper.toDto(initialAsset)).thenReturn(expectedAssetResponseDto);

        AssetResponseDto exception = assetServiceImpl.updateAsset(assetRequestDto, "PD000001");

        Assertions.assertEquals("hahah2", exception.getName());

    }

    @Test
    void update_ShouldThrowResourceNotFoundException_WhenAssetNotExist() {
        assetRequestDto.setName("hahah2");
        expectedAssetResponseDto.setName("hahah2");

        doNothing().when(entityCheckUtils).assetCheckUpdate(assetRequestDto);
        when(assetRepository.findById("PD000002")).thenReturn(Optional.ofNullable(initialAsset));
        when(assetMapper.toEntityUpdate(assetRequestDto,initialAsset)).thenReturn(initialAsset);


        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> assetServiceImpl.updateAsset(assetRequestDto, "PD000001"));
        Assertions.assertEquals("this asset not exist", exception.getMessage());

    }

//    @Test
//    void getAssetsByPredicates_WhenCategoryNamesParamsIsNotNull() {
//        Page<Asset> result = mock(Page.class);
//        String keyword = "";
//        int locationId = 0;
//        int page = 0;
//        int pageSize = 15;
//        states = new ArrayList<>();
//        List<String> statesString = new ArrayList<>();
//        String orderBy = "updatedWhen_DESC";
//        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "updatedWhen");
//        when(categoryRepository.findCategoriesByNameIsIn(categoryNames)).thenReturn(categories);
//        when(assetRepository.findByKeywordWithFilter
//                (categories, states, keyword.toLowerCase(), keyword.toLowerCase(), locationId, pageable))
//                .thenReturn(result);
//        when(assetMapper.toDtoViewList(result.toList())).thenReturn(assetViewResponseDtos);
//        APIResponse<List<AssetViewResponseDto>> expected = new APIResponse<>(page, assetViewResponseDtos);
//
//        APIResponse<List<AssetViewResponseDto>> listResult = assetServiceImpl.getAssetsByPredicates(statesString, categoryNames, keyword, locationId, page, orderBy);
//        assertThat(expected, is(listResult));
//    }

    @Test
    void getAssetsByPredicates_WhenCategoryNamesParamsIsNull() {
        Page<Asset> result = mock(Page.class);
        String keyword = "";
        int locationId = 0;
        int page = 0;
        int pageSize = 15;
        states = new ArrayList<>();
        List<String> statesString = new ArrayList<>();
        String orderBy = "updatedWhen_DESC";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "updatedWhen");
        when(categoryRepository.findAll()).thenReturn(categories);

        when(assetRepository.findByKeywordWithFilter
                (categories, states, keyword.toLowerCase(), keyword.toLowerCase(), locationId, pageable))
                .thenReturn(result);
        when(assetMapper.toDtoViewList(result.toList())).thenReturn(assetViewResponseDtos);
        APIResponse<List<AssetViewResponseDto>> expected = new APIResponse<>(page, assetViewResponseDtos);

        APIResponse<List<AssetViewResponseDto>> listResult = assetServiceImpl.getAssetsByPredicates(statesString, null, keyword, locationId, page, orderBy);
        assertThat(expected, is(listResult));
    }

    @Test
    void getAssetById_WhenAssetIsNotNull() {
        List<Returning> returningHistoryList = new ArrayList<>();
        returningHistoryList.add(returning);
        when(assetRepository.findById(asset.getId())).thenReturn(Optional.of(asset));
        when(returningRepository.findByAssetIdAndState(asset.getId(), AssignmentReturnState.WAITING_FOR_RETURNING))
                .thenReturn(returningHistoryList);

        when(assetMapper.toDto(Optional.of(asset).get())).thenReturn(assetResponseDto);

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
