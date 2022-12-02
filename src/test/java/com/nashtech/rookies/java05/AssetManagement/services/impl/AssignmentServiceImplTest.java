package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssignmentRequestPostDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.*;
import com.nashtech.rookies.java05.AssetManagement.entities.Asset;
import com.nashtech.rookies.java05.AssetManagement.entities.Assignment;
import com.nashtech.rookies.java05.AssetManagement.entities.Returning;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentReturnState;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentState;
import com.nashtech.rookies.java05.AssetManagement.exceptions.BadRequestException;
import com.nashtech.rookies.java05.AssetManagement.mappers.AssignmentMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.AssignmentRepository;
import com.nashtech.rookies.java05.AssetManagement.services.AssignmentService;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AssignmentServiceImplTest {
    private AssignmentMapper assignmentMapper;
    private AssignmentRepository assignmentRepository;
    private AssignmentService assignmentService;
    private AssignmentServiceImpl assignmentServiceImpl;

    private Assignment assignment;
    private AssignmentDetailDto assignmentDetailDto;

    @Mock
    private List<AssignmentState> states;
    @Mock
    private List<AssignmentListResponseDto> assignmentListResponseDtos;
    private APIResponse apiResponse;

    @BeforeEach
    void beforeEach() {
        assignmentRepository = Mockito.mock(AssignmentRepository.class);
//        assignmentMapper = AssignmentMapper.builder()
//                .assetRepository(assetRepository)
//                .userRepository(userRepository)
//                .build();
        assignmentMapper = Mockito.mock(AssignmentMapper.class);


        assignment = Mockito.mock(Assignment.class);
        assignmentDetailDto = Mockito.mock(AssignmentDetailDto.class);
        assignmentServiceImpl = AssignmentServiceImpl
                .builder()
                .assignmentRepository(assignmentRepository)
                .assignmentMapper(assignmentMapper)
                .build();
        assignmentService = AssignmentServiceImpl.builder()
                .assignmentMapper(assignmentMapper)
                .assignmentRepository(assignmentRepository)
                .build();
    }

    @Test
    void testInsertWhenAssetStateNotAvailableShouldThrowException() {
        AssignmentRequestPostDto mockDto = Mockito.mock(AssignmentRequestPostDto.class);
        Assignment notValidAssignment = Assignment.builder()
                .id(1)
                .asset(Asset.builder().state(AssetState.ASSIGNED).build())
                .state(AssignmentState.WAITING)
                .build();
        Mockito.when(assignmentMapper.mapAssignmentRequestPostDtoToAssignmentEntity(any(AssignmentRequestPostDto.class)))
                .thenReturn(notValidAssignment);
        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class,
                () -> assignmentService.create(mockDto)
        );
        assertThat(badRequestException.getMessage()).isEqualTo("This asset isn't available to assign");
    }

    @Test
    void testInsertWhenUserDisableShouldThrowException() {
        AssignmentRequestPostDto mockDto = Mockito.mock(AssignmentRequestPostDto.class);
        User userDisabled = User.builder().id("SD1111").isDisabled(true).build();
        Asset validAsset = Asset.builder().state(AssetState.AVAILABLE).build();
        Assignment notValidAssignment = Assignment.builder()
                .id(1)
                .assignedTo(userDisabled)
                .asset(validAsset)
                .state(AssignmentState.WAITING)
                .build();
        Mockito.when(assignmentMapper.mapAssignmentRequestPostDtoToAssignmentEntity(any(AssignmentRequestPostDto.class)))
                .thenReturn(notValidAssignment);
        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class,
                () -> assignmentService.create(mockDto)
        );
        assertThat(badRequestException.getMessage()).isEqualTo("User is assigned with id SD1111 is disabled");
    }

    @Test
    void testInsertWhenUserValidShouldInsertSuccess() {
        String userAssignById = "SD0001";
        String userAssignToId = "SD0002";
        String assetId = "LA000001";
        String note = "note";
        Date assignDate = Date.valueOf("2023-03-31");
        User assignBy = User.builder()
                .id(userAssignById)
                .build();
        User assignTo = User.builder()
                .id(userAssignToId)
                .build();
        Asset asset = Asset.builder().id(assetId).state(AssetState.AVAILABLE).build();
        AssignmentRequestPostDto dto = new AssignmentRequestPostDto(
                userAssignById,
                userAssignToId,
                assetId,
                assignDate,
                note
        );
        Assignment validAssignment = Assignment.builder()
                .id(1)
                .assignedBy(assignBy)
                .assignedTo(assignTo)
                .asset(asset)
                .note(note)
                .assignedDate(assignDate)
                .state(AssignmentState.WAITING)
                .build();
        Mockito.when(assignmentMapper.mapAssignmentRequestPostDtoToAssignmentEntity(dto))
                .thenReturn(validAssignment);
        Mockito.when(assignmentMapper.mapEntityToResponseInsertDto(any(Assignment.class)))
                .thenReturn(Mockito.mock(AssignmentResponseInsertDto.class));
        Mockito.when(assignmentRepository.save(validAssignment))
                .thenReturn(validAssignment);
        assignmentService.create(dto);
        ArgumentCaptor<Assignment> assignmentArgumentCaptor = ArgumentCaptor.forClass(Assignment.class);
        Mockito.verify(assignmentRepository).save(assignmentArgumentCaptor.capture());
        Assignment actual = assignmentArgumentCaptor.getValue();
        assertThat(actual.getAssignedTo().getId()).isEqualTo(userAssignToId);
        assertThat(actual.getAssignedBy().getId()).isEqualTo(userAssignById);
        assertThat(actual.getAssignedDate()).isEqualTo(assignDate);
        assertThat(actual.getState()).isEqualTo(AssignmentState.WAITING);

    }


    @Test
    void getAssignment_ShouldReturnValue_WhenAssignmentIdIsIsValid() {

        when(assignmentRepository.findById(assignment.getId())).thenReturn(Optional.of(assignment));
        when(assignmentMapper.mapAssignmentToAssignmentDetailDto(Optional.of(assignment).get()))
                .thenReturn(assignmentDetailDto);

        AssignmentDetailDto result = assignmentServiceImpl.getAssignment(assignment.getId());
        MatcherAssert.assertThat(assignmentDetailDto, is(result));
    }

    @Test
    void getAssignment_ShouldReturnEmptyObject_WhenAssignmentIdIsIsNotValid() {
        AssignmentDetailDto expected = AssignmentDetailDto.builder().build();
        when(assignmentRepository.findById(assignment.getId())).thenReturn(Optional.empty());
        AssignmentDetailDto result = assignmentServiceImpl.getAssignment(assignment.getId());
        MatcherAssert.assertThat(expected, is(result));
    }

    @Test
    void getAssignmentByUserId_ShouldReturnValue_WhenUserIdIsIsValid() {
        Page<Assignment> result = mock(Page.class);
        User user = Mockito.mock(User.class);
        String orderBy = "updatedWhen_DESC";


        LocalDate localDate = LocalDate.now();
        ZoneId defaultZoneId = ZoneId.systemDefault();
        java.util.Date date = java.util.Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());

        Pageable pageable = PageRequest.of(0, 15, Sort.Direction.DESC, "updatedWhen");
        when(assignmentRepository.findByUserId(user.getId(), date, pageable))
                .thenReturn(result);

        when(assignmentMapper.mapAssignmentListToAssignmentListResponseDto(any()))
                .thenReturn(assignmentListResponseDtos);

        APIResponse<List<AssignmentListResponseDto>> expected = new APIResponse<>(0, assignmentListResponseDtos);

        expected.setTotalPage(1);
        APIResponse<List<AssignmentListResponseDto>> listResult =
                assignmentServiceImpl.getAssignmentsByUser(user.getId(), 0, orderBy);
        MatcherAssert.assertThat(expected, is(listResult));
    }

    @Test
    void getAssignmentByUserId_ShouldReturnEmptyObject_WhenUserIdIsNotValid() {
        Page<Assignment> result = mock(Page.class);
        User user = Mockito.mock(User.class);
        String orderBy = "updatedWhen_DESC";


        LocalDate localDate = LocalDate.now();
        ZoneId defaultZoneId = ZoneId.systemDefault();
        java.util.Date date = java.util.Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());

        Pageable pageable = PageRequest.of(0, 15, Sort.Direction.DESC, "updatedWhen");
        when(assignmentRepository.findByUserId(user.getId(), date, pageable))
                .thenReturn(result);

        when(assignmentMapper.mapAssignmentListToAssignmentListResponseDto(any()))
                .thenReturn(assignmentListResponseDtos);

        APIResponse<List<AssignmentListResponseDto>> expected = new APIResponse<>(0, assignmentListResponseDtos);

        expected.setTotalPage(1);
        APIResponse<List<AssignmentListResponseDto>> listResult =
                assignmentServiceImpl.getAssignmentsByUser(user.getId(), 0, orderBy);
        MatcherAssert.assertThat(expected, is(listResult));
    }

    @Test
    void getAssignments_ShouldReturnValue() {
        Page<Assignment> result = Mockito.mock(Page.class);
        String orderBy = "updatedWhen_DESC";

        states = new ArrayList<>();
        AssignmentState[] assignmentStates = AssignmentState.values();
        for (AssignmentState assignmentState : assignmentStates) {
            states.add(assignmentState);
        }
        List<String> statesString = new ArrayList<>();

        Pageable pageable = PageRequest.of(0, 15, Sort.Direction.DESC, "updatedWhen");
        when(assignmentRepository.findByPredicates(states, "2022-11-30", "la", pageable))
                .thenReturn(result);

        when(assignmentMapper.mapAssignmentListToAssignmentListResponseDto(any()))
                .thenReturn(assignmentListResponseDtos);

        APIResponse<List<AssignmentListResponseDto>> expected = new APIResponse<>(result.getTotalPages(), assignmentListResponseDtos);

        APIResponse<List<AssignmentListResponseDto>> listResult =
                assignmentServiceImpl.getAssignmentByPredicates
                        (statesString, "2022-11-30", "LA", 0, orderBy);

        MatcherAssert.assertThat(expected, is(listResult));
    }
}