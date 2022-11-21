package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssetRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Asset;
import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import com.nashtech.rookies.java05.AssetManagement.entities.Returning;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentReturnState;
import com.nashtech.rookies.java05.AssetManagement.mappers.AssetMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AssetServiceImplTest {
    @Autowired
    AssetServiceImpl service;
    @MockBean
    AssetRepository repository;
    @MockBean
    CategoryRepository categoryRepository;
    @MockBean
    LocationRepository locationRepository;
    @MockBean
    PresentIdRepository presentIdRepository;

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
                .categoryRepository(categoryRepository)
                .assetMapper(assetMapper)
                .assetRepository(assetRepository)
                .returningRepository(returningRepository)
                .build();

        initialAsset = new Asset();
        expectedAsset = new Asset();
    }

    @Test
    void insert_ShouldThrowNullPointerException_WhenParamsIsNull() {
        assetRequestDto.setName(null);

        NullPointerException exception = Assertions.assertThrows(NullPointerException.class,
                () -> service.insert(assetRequestDto));

        Assertions.assertEquals("null name", exception.getMessage());
    }

    @Test
    void insert_ShouldThrowIllegalArgumentException_WhenParamsIsEmptyString() {
        assetRequestDto.setName("");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.insert(assetRequestDto));

        Assertions.assertEquals("empty name", exception.getMessage());
    }

    @Test
    void insert_ShouldThrowIllegalArgumentException_WhenNameTooLong() {
        assetRequestDto.setName("aaaaaaaaaaaKaaaaaaaaaaaKaaaaaaaaaaaKaaaaaaaaaaaKaaaaaaaaaaa");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.insert(assetRequestDto));


        Assertions.assertEquals("name is too long, name up to 50 characters long", exception.getMessage());
    }

    @Test
    void insert_ShouldThrowIllegalArgumentException_WhenSpecificationTooLong() {
        String str = "";
        for (int i = 0; i < 51; i++) {
            str += "1234567890";
        }
        assetRequestDto.setSpecification(str);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.insert(assetRequestDto));


        Assertions.assertEquals("specification is too long, specification up to 500 characters long", exception.getMessage());
    }

    @Test
    void insert_ShouldThrowIllegalArgumentException_WhenInstalledDateNotPastDate() {
        assetRequestDto.setInstalledDate(new Date(now.getTime() + oneDay));

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.insert(assetRequestDto));

        Assertions.assertEquals("installed date must be a date in the past", exception.getMessage());
    }

    @Test
    void insert_ShouldThrowIllegalArgumentException_WhenNameHasSpecialCharacters() {
        assetRequestDto.setName("hahah2~");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.insert(assetRequestDto));

        Assertions.assertEquals("Name cannot contain special characters:! @ # $ % & * ( )  _ + = |  < > ? { } [ ] ~", exception.getMessage());
    }


    @ExtendWith(MockitoExtension.class)
    @Test
    void getAssetsByPredicates_WhenCategoryNamesParamsIsNotNull() {
        Page<Asset> result = mock(Page.class);
        String keyword = "";
        int locationId = 0;
        int page = 0;
        int pageSize = 15;
        String orderBy = "";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "updatedWhen");
        when(categoryRepository.findCategoriesByNameIsIn(categoryNames)).thenReturn(categories);
        when(assetRepository.findByKeywordWithFilter
                (categories, states, keyword.toLowerCase(), keyword.toLowerCase(), locationId, pageable))
                .thenReturn(result);
        when(assetMapper.mapAssetListToAssetViewResponseDto(result.toList())).thenReturn(assetViewResponseDtos);
        APIResponse<List<AssetViewResponseDto>> expected = new APIResponse<>(page, assetViewResponseDtos);

        APIResponse<List<AssetViewResponseDto>> listResult = assetServiceImpl.getAssetsByPredicates(states, categoryNames, keyword, locationId, page, orderBy);
        assertThat(expected, is(listResult));
    }

    @Test
    void getAssetsByPredicates_WhenCategoryNamesParamsIsNull() {
        Page<Asset> result = mock(Page.class);
        String keyword = "";
        int locationId = 0;
        int page = 0;
        int pageSize = 15;
        String orderBy = "";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "updatedWhen");
        when(categoryRepository.findAll()).thenReturn(categories);

        when(assetRepository.findByKeywordWithFilter
                (categories, states, keyword.toLowerCase(), keyword.toLowerCase(), locationId, pageable))
                .thenReturn(result);
        when(assetMapper.mapAssetListToAssetViewResponseDto(result.toList())).thenReturn(assetViewResponseDtos);
        APIResponse<List<AssetViewResponseDto>> expected = new APIResponse<>(page, assetViewResponseDtos);

        APIResponse<List<AssetViewResponseDto>> listResult = assetServiceImpl.getAssetsByPredicates(states, null, keyword, locationId, page, orderBy);
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
