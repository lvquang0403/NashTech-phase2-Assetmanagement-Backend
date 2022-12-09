package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssignmentDto;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.*;
import com.nashtech.rookies.java05.AssetManagement.entities.Asset;
import com.nashtech.rookies.java05.AssetManagement.entities.Assignment;
import com.nashtech.rookies.java05.AssetManagement.entities.Returning;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentState;
import com.nashtech.rookies.java05.AssetManagement.exceptions.BadRequestException;
import com.nashtech.rookies.java05.AssetManagement.exceptions.ResourceNotFoundException;
import com.nashtech.rookies.java05.AssetManagement.mappers.AssignmentMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.AssetRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.AssignmentRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.UserRepository;
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
import java.sql.Timestamp;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AssignmentServiceImplTest {
    private AssignmentRepository assignmentRepository;
    private AssignmentService assignmentService;
    private AssetRepository assetRepository;
    private UserRepository userRepository;
    private AssignmentMapper assignmentMapper2;
    private AssignmentServiceImpl assignmentServiceImpl;
    private Assignment assignment;
    private AssignmentDetailDto assignmentDetailDto;

    @Mock
    private List<AssignmentState> states;
    @Mock
    private List<AssignmentResponseDto> assignmentListResponseDtos;


    @BeforeEach
    void beforeEach() {
        assignmentRepository = Mockito.mock(AssignmentRepository.class);

        assetRepository = Mockito.mock(AssetRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        AssignmentMapper assignmentMapper = AssignmentMapper.builder().build();
        assignmentMapper2 = Mockito.mock(AssignmentMapper.class);
        assignment = Mockito.mock(Assignment.class);
        assignmentDetailDto = Mockito.mock(AssignmentDetailDto.class);
        assignmentServiceImpl = AssignmentServiceImpl
                .builder()
                .assignmentRepository(assignmentRepository)
                .assignmentMapper(assignmentMapper2)
                .build();
        assignmentService = AssignmentServiceImpl.builder()
                .assignmentMapper(assignmentMapper)
                .assignmentRepository(assignmentRepository)
                .assetRepository(assetRepository)
                .userRepository(userRepository)
                .build();
    }

    @Test
    void testInsertWhenAssetStateNotAvailableShouldThrowException() {
        AssignmentDto dto = AssignmentDto.builder()
                .assignTo("anyone")
                .assignBy("anyone")
                .assetId("LA00001")
                .assignedDate(Date.valueOf("2022-12-12"))
                .note("note")
                .build();
        Asset notValidAsset = Asset.builder().state(AssetState.ASSIGNED).build();
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(Mockito.mock(User.class)));
        Mockito.when(assetRepository.findById("LA00001")).thenReturn(Optional.of(notValidAsset));
        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class,
                () -> assignmentService.create(dto)
        );
        assertThat(badRequestException.getMessage()).isEqualTo("This asset isn't available to assign");
    }

    @Test
    void testInsertWhenUserDisableShouldThrowException() {
        AssignmentDto dto = AssignmentDto.builder()
                .assignTo("SD1111")
                .assignBy("anyone")
                .assetId("LA00001")
                .assignedDate(Date.valueOf("2022-12-12"))
                .note("note")
                .build();
        User userDisabled = User.builder().id("SD1111").isDisabled(true).build();
        Asset validAsset = Asset.builder().state(AssetState.AVAILABLE).build();
        Mockito.when(userRepository.findById("SD1111")).thenReturn(Optional.of(userDisabled));
        Mockito.when(userRepository.findById("anyone")).thenReturn(Optional.of(Mockito.mock(User.class)));
        Mockito.when(assetRepository.findById(any())).thenReturn(Optional.of(validAsset));
        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class,
                () -> assignmentService.create(dto)
        );
        assertThat(badRequestException.getMessage()).isEqualTo("User is assigned with id SD1111 is disabled");
    }

    @Test
    void testCreateWhenUserAssignToNotExistShouldThrowException() {
        String userAssignToNotExistId = "SD0000";
        AssignmentDto dto = AssignmentDto.builder()
                .assignTo(userAssignToNotExistId)
                .assignBy("anyone")
                .assetId("LA00001")
                .assignedDate(Date.valueOf("2022-12-12"))
                .note("note")
                .build();
        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> assignmentService.create(dto));
        assertThat(resourceNotFoundException.getMessage()).isEqualTo("User with id SD0000 is not found");
    }

    @Test
    void testCreateWhenAssetNotExistShouldThrowException() {
        String assetNotExistId = "LAA0000";
        AssignmentDto dto = AssignmentDto.builder()
                .assignTo("anyone")
                .assignBy("anyone")
                .assetId(assetNotExistId)
                .assignedDate(Date.valueOf("2022-12-12"))
                .note("note")
                .build();
        Mockito.when(userRepository.findById("anyone")).thenReturn(Optional.of(Mockito.mock(User.class)));
        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> assignmentService.create(dto));
        assertThat(resourceNotFoundException.getMessage()).isEqualTo(String.format("Asset with id %s is not found", assetNotExistId));
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
        AssignmentDto dto = AssignmentDto.builder()
                .assignTo(userAssignToId)
                .assignBy(userAssignById)
                .assetId(assetId)
                .assignedDate(assignDate)
                .note(note)
                .build();
        Assignment assignment = Assignment.builder()
                .id(1)
                .createdWhen(new Timestamp(10000))
                .updatedWhen(new Timestamp(10000))
                .note(note)
                .state(AssignmentState.ACCEPTED)
                .assignedBy(assignTo)
                .assignedTo(assignBy)
                .assignedDate(assignDate)
                .asset(asset)
                .returning(Returning.builder().build())
                .build();
        Mockito.when(userRepository.findById("SD0001")).thenReturn(Optional.of(assignBy));
        Mockito.when(userRepository.findById("SD0002")).thenReturn(Optional.of(assignTo));
        Mockito.when(assetRepository.findById("LA000001")).thenReturn(Optional.of(asset));
        Mockito.when(assignmentRepository.save(any())).thenReturn(assignment);
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
    void testUpdateWhenAssignmentNotExistShouldThrowException() {
        Integer assignmentIdNotExist = 999;
        AssignmentDto dto = Mockito.mock(AssignmentDto.class);
        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> assignmentService.update(dto, assignmentIdNotExist));
        assertThat(resourceNotFoundException.getMessage()).isEqualTo(String.format("Assignment with id %s is not found", assignmentIdNotExist));
    }

    @Test
    void testUpdateWhenAssetNotExistShouldThrowException() {
        String assetIdNotExist = "SD1234";
        AssignmentDto dto = AssignmentDto.builder()
                .assetId(assetIdNotExist)
                .assignTo("any")
                .build();
        Mockito.when(assignmentRepository.findById(any())).thenReturn(Optional.of(Assignment.builder().state(AssignmentState.WAITING).build()));
        Mockito.when(userRepository.findById("any")).thenReturn(Optional.of(Mockito.mock(User.class)));
        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> assignmentService.update(dto, 2));
        assertThat(resourceNotFoundException.getMessage()).isEqualTo(String.format("Asset with id %s is not found", assetIdNotExist));

    }

    @Test
    void testUpdateWhenAssignToNotExistShouldThrowException() {
        String assignToIdNotExist = "SD123449";
        AssignmentDto dto = AssignmentDto.builder()
                .assetId("any")
                .assignTo(assignToIdNotExist)
                .build();
        Mockito.when(assignmentRepository.findById(any())).thenReturn(Optional.of(Assignment.builder().state(AssignmentState.WAITING).build()));
        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> assignmentService.update(dto, 2));
        assertThat(resourceNotFoundException.getMessage()).isEqualTo(String.format("User with id %s is not found", assignToIdNotExist));
    }

    @Test
    void testUpdateWhenDataValidShouldUpdateNewValue() {
        Integer oldAssignmentId = 1;
        String newUserAssignToId = "SD0002";
        String newAssetId = "LA000001";
        String newNote = "note";
        Date newAssignDate = Date.valueOf("2023-03-31");
        AssignmentDto dto = AssignmentDto.builder()
                .assignTo(newUserAssignToId)
                .assetId(newAssetId)
                .assignedDate(newAssignDate)
                .note(newNote)
                .build();
        Assignment oldAssignment = Assignment.builder()
                .id(oldAssignmentId)
                .state(AssignmentState.WAITING)
                .asset(Asset.builder().build())
                .build();
        Mockito.when(assignmentRepository.findById(oldAssignmentId))
                .thenReturn(Optional.of(oldAssignment));
        Mockito.when(userRepository.findById(newUserAssignToId))
                .thenReturn(Optional.of(User.builder().id(newUserAssignToId).build()));
        Mockito.when(assetRepository.findById(newAssetId))
                .thenReturn(Optional.of(Asset.builder().id(newAssetId).build()));
        assignmentService.update(dto, oldAssignmentId);
        ArgumentCaptor<Assignment> assignmentArgumentCaptor = ArgumentCaptor.forClass(Assignment.class);
        Mockito.verify(assignmentRepository,times(2)).save(assignmentArgumentCaptor.capture());
        Assignment actual = assignmentArgumentCaptor.getAllValues().get(1);
        assertThat(actual.getAssignedDate()).isEqualTo(newAssignDate);
        assertThat(actual.getAssignedTo().getId()).isEqualTo(newUserAssignToId);
        assertThat(actual.getNote()).isEqualTo(newNote);
        assertThat(actual.getAsset().getId()).isEqualTo(newAssetId);
    }

    @Test
    void testDeleteWhenAssignmentNotExistShouldThrowException() {
        Integer assignmentIdNotExist = 999;
        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> assignmentService.delete(assignmentIdNotExist));
        assertThat(resourceNotFoundException.getMessage()).isEqualTo(String.format("Assignment with id %s is not found", assignmentIdNotExist));
    }

    @Test
    void testDeleteWhenAssignmentStateNotValidShouldThrowException() {
        Integer assignmentId = 1;
        Mockito.when(assignmentRepository.findById(assignmentId))
                .thenReturn(Optional.of(Assignment.builder().state(AssignmentState.ACCEPTED).build()));
        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class,
                () -> assignmentService.delete(assignmentId));
        assertThat(badRequestException.getMessage()).isEqualTo("Only can delete assignments that have state is WATING");
    }

    @Test
    void testDeleteWhenAssignmentValidShouldDeleteSuccess() {
        Integer assignmentId = 1;
        Asset assignedAsset = Asset.builder().state(AssetState.ASSIGNED).build();
        Assignment assignment = Assignment.builder()
                .id(assignmentId)
                .asset(assignedAsset)
                .state(AssignmentState.WAITING)
                .build();
        Mockito.when(assignmentRepository.findById(assignmentId))
                .thenReturn(Optional.of(assignment));
        assignmentService.delete(assignmentId);
        ArgumentCaptor<Assignment> assignmentArgumentCaptor = ArgumentCaptor.forClass(Assignment.class);
        Mockito.verify(assignmentRepository).save(assignmentArgumentCaptor.capture());
        Assignment actual = assignmentArgumentCaptor.getValue();
        assertThat(actual.getId()).isEqualTo(assignmentId);
        assertThat(actual.getAsset().getState()).isEqualTo(AssetState.AVAILABLE);
        Mockito.verify(assignmentRepository).delete(assignment);

    }

    @Test
    void getAssignment_ShouldReturnValue_WhenAssignmentIdIsIsValid() {

        when(assignmentRepository.findById(assignment.getId())).thenReturn(Optional.of(assignment));
        when(assignmentMapper2.ToAssignmentDetailDto(Optional.of(assignment).get()))
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

        when(assignmentMapper2.toListResponseDto(any()))
                .thenReturn(assignmentListResponseDtos);

        APIResponse<List<AssignmentResponseDto>> expected = new APIResponse<>(result.getTotalPages(), assignmentListResponseDtos);

        APIResponse<List<AssignmentResponseDto>> listResult =
                assignmentServiceImpl.getAssignmentByPredicates
                        (statesString, "2022-11-30", "LA", 0, orderBy);

        MatcherAssert.assertThat(expected, is(listResult));
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

        when(assignmentMapper2.toListResponseDto(any()))
                .thenReturn(assignmentListResponseDtos);

        APIResponse<List<AssignmentResponseDto>> expected = new APIResponse<>(result.getTotalPages(), assignmentListResponseDtos);

        APIResponse<List<AssignmentResponseDto>> listResult =
                assignmentServiceImpl.getAssignmentsByUser(user.getId(), 0, orderBy);
        MatcherAssert.assertThat(listResult, is(expected));
    }

    @Test
    void changeStateAssignment_ShouldReturnResult_WhenAssignmentIdIsValid() {
        AssignmentDto dto = new AssignmentDto();
        dto.setState("ACCEPTED");
        assignment =new Assignment();
        assignment.setState(AssignmentState.WAITING);
        AssignmentDetailDto expected = mock(AssignmentDetailDto.class);

        when(assignmentRepository.findById(1000)).thenReturn(Optional.of(assignment));
        when(assignmentRepository.save(assignment)).thenReturn(assignment);
        when(assignmentMapper2.ToAssignmentDetailDto(assignment)).thenReturn(expected);

        AssignmentDetailDto result = assignmentServiceImpl.changeStateAssignment(1000, dto);
        MatcherAssert.assertThat(result, is(expected));
    }

    @Test
    void changeStateAssignment_ShouldThrowException_WhenAssignmentIdIsNotValid() {
        AssignmentDto dto = mock(AssignmentDto.class);

        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> assignmentServiceImpl.changeStateAssignment(1000, dto));
        Assertions.assertEquals("Assignment not found with id: 1000", resourceNotFoundException.getMessage());
    }
}