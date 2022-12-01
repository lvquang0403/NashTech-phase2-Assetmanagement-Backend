package com.nashtech.rookies.java05.AssetManagement.services.impl;
import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssignmentRequestPostDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssignmentResponseInsertDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Asset;
import com.nashtech.rookies.java05.AssetManagement.entities.Assignment;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentState;
import com.nashtech.rookies.java05.AssetManagement.exceptions.BadRequestException;
import com.nashtech.rookies.java05.AssetManagement.mappers.AssignmentMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.AssignmentRepository;
import com.nashtech.rookies.java05.AssetManagement.services.AssignmentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.sql.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
class AssignmentServiceImplTest {
    private AssignmentMapper assignmentMapper;
    private AssignmentRepository assignmentRepository;
    private AssignmentService assignmentService;

    @BeforeEach
    void beforeEach(){
        assignmentRepository = Mockito.mock(AssignmentRepository.class);
//        assignmentMapper = AssignmentMapper.builder()
//                .assetRepository(assetRepository)
//                .userRepository(userRepository)
//                .build();
        assignmentMapper = Mockito.mock(AssignmentMapper.class);
        assignmentService = AssignmentServiceImpl.builder()
                .assignmentMapper(assignmentMapper)
                .assignmentRepository(assignmentRepository)
                .build();
    }

    @Test
    void testInsertWhenAssetStateNotAvailableShouldThrowException(){
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
    void testInsertWhenUserDisableShouldThrowException(){
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

}