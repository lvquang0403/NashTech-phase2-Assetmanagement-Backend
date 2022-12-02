package com.nashtech.rookies.java05.AssetManagement.services.impl;
import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssignmentRequestPostDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssignmentRequestPutDto;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
class AssignmentServiceImplTest {
    private AssignmentMapper assignmentMapper;
    private AssignmentRepository assignmentRepository;
    private AssignmentService assignmentService;
    private AssetRepository assetRepository;
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach(){
        assignmentRepository = Mockito.mock(AssignmentRepository.class);
        assetRepository = Mockito.mock(AssetRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        assignmentMapper = AssignmentMapper.builder()
                .assetRepository(assetRepository)
                .userRepository(userRepository)
                .build();
        assignmentService = AssignmentServiceImpl.builder()
                .assignmentMapper(assignmentMapper)
                .assignmentRepository(assignmentRepository)
                .build();
    }

    @Test
    void testInsertWhenAssetStateNotAvailableShouldThrowException(){
        AssignmentRequestPostDto dto = new AssignmentRequestPostDto(
                "anyone",
                "anyone",
                "LA00001",
                Date.valueOf("2022-12-12"),
                "note"
        );
        Asset notValidAsset = Asset.builder().state(AssetState.ASSIGNED).build();
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(Mockito.mock(User.class)));
        Mockito.when(assetRepository.findById("LA00001")).thenReturn(Optional.of(notValidAsset));
        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class,
                () -> assignmentService.create(dto)
        );
        assertThat(badRequestException.getMessage()).isEqualTo("This asset isn't available to assign");
    }

    @Test
    void testInsertWhenUserDisableShouldThrowException(){
        AssignmentRequestPostDto dto = new AssignmentRequestPostDto(
                "anyone",
                "SD1111",
                "LA00001",
                Date.valueOf("2022-12-12"),
                "note"
        );
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
    void testCreateWhenUserAssignToNotExistShouldThrowException(){
        String userAssignToNotExistId = "SD0000";
        AssignmentRequestPostDto dto = new AssignmentRequestPostDto(
                "anyone",
                userAssignToNotExistId,
                "LA00001",
                Date.valueOf("2022-12-12"),
                "note"
        );
        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> assignmentService.create(dto));
        assertThat(resourceNotFoundException.getMessage()).isEqualTo("User with id SD0000 is not found");
    }
    @Test
    void testCreateWhenAssetNotExistShouldThrowException(){
        String assetNotExistId = "LAA0000";
        AssignmentRequestPostDto dto = new AssignmentRequestPostDto(
                "anyone",
                "anyone",
                assetNotExistId,
                Date.valueOf("2022-12-12"),
                "note"
        );
        Mockito.when(userRepository.findById("anyone")).thenReturn(Optional.of(Mockito.mock(User.class)));
        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> assignmentService.create(dto));
        assertThat(resourceNotFoundException.getMessage()).isEqualTo(String.format("Asset with id %s is not found", assetNotExistId));
    }

    @Test
    void testInsertWhenUserValidShouldInsertSuccess(){
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
    void testUpdateWhenAssignmentNotExistShouldThrowException(){
        Integer assignmentIdNotExist = 999;
        AssignmentRequestPutDto dto = Mockito.mock(AssignmentRequestPutDto.class);
        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> assignmentService.update(dto, assignmentIdNotExist));
        assertThat(resourceNotFoundException.getMessage()).isEqualTo(String.format("Assignment with id %s is not found", assignmentIdNotExist));
    }

    @Test
    void testUpdateWhenAssetNotExistShouldThrowException(){
        String assetIdNotExist = "SD1234";
        AssignmentRequestPutDto dto = AssignmentRequestPutDto.builder()
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
    void testUpdateWhenAssignToNotExistShouldThrowException(){
        String assignToIdNotExist = "SD123449";
        AssignmentRequestPutDto dto = AssignmentRequestPutDto.builder()
                .assetId("any")
                .assignTo(assignToIdNotExist)
                .build();
        Mockito.when(assignmentRepository.findById(any())).thenReturn(Optional.of(Assignment.builder().state(AssignmentState.WAITING).build()));
        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> assignmentService.update(dto, 2));
        assertThat(resourceNotFoundException.getMessage()).isEqualTo(String.format("User with id %s is not found", assignToIdNotExist));
    }

    @Test
    void testUpdateWhenDataValidShouldUpdateNewValue(){
        Integer oldAssignmentId = 1;
        String newUserAssignToId = "SD0002";
        String newAssetId = "LA000001";
        String newNote = "note";
        Date newAssignDate = Date.valueOf("2023-03-31");
        AssignmentRequestPutDto dto = AssignmentRequestPutDto.builder()
                .assignTo(newUserAssignToId)
                .assignedDate(newAssignDate)
                .assetId(newAssetId)
                .note(newNote)
                .build();
        Assignment oldAssignment = Assignment.builder()
                .id(oldAssignmentId)
                .state(AssignmentState.WAITING)
                .build();
        Mockito.when(assignmentRepository.findById(oldAssignmentId))
                .thenReturn(Optional.of(oldAssignment));
        Mockito.when(userRepository.findById(newUserAssignToId))
                .thenReturn(Optional.of(User.builder().id(newUserAssignToId).build()));
        Mockito.when(assetRepository.findById(newAssetId))
                .thenReturn(Optional.of(Asset.builder().id(newAssetId).build()));
        assignmentService.update(dto, oldAssignmentId);
        ArgumentCaptor<Assignment> assignmentArgumentCaptor = ArgumentCaptor.forClass(Assignment.class);
        Mockito.verify(assignmentRepository).save(assignmentArgumentCaptor.capture());
        Assignment actual = assignmentArgumentCaptor.getValue();
        assertThat(actual.getAssignedDate()).isEqualTo(newAssignDate);
        assertThat(actual.getAssignedTo().getId()).isEqualTo(newUserAssignToId);
        assertThat(actual.getNote()).isEqualTo(newNote);
        assertThat(actual.getAsset().getId()).isEqualTo(newAssetId);
    }

}