package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.RequestReturnDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Assignment;
import com.nashtech.rookies.java05.AssetManagement.entities.Returning;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentState;
import com.nashtech.rookies.java05.AssetManagement.exceptions.RepeatDataException;
import com.nashtech.rookies.java05.AssetManagement.mappers.AssetMapper;
import com.nashtech.rookies.java05.AssetManagement.mappers.AssignmentMapper;
import com.nashtech.rookies.java05.AssetManagement.mappers.ReturningMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.AssignmentRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.ReturningRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.UserRepository;
import com.nashtech.rookies.java05.AssetManagement.utils.EntityCheckUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class ReturningServiceImplTest {

    @MockBean
    private AssignmentRepository assignmentRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AssignmentMapper assignmentMapper;
    @MockBean
    private AssetMapper assetMapper;
    @MockBean
    private ReturningRepository returningRepository;

    private ReturningMapper returningMapper ;
    @Autowired
    private EntityCheckUtils entityCheckUtils;
    private ReturningServiceImpl returningServiceImpl ;

    private RequestReturnDto dataRequest;
    private Returning initialReturning = new Returning();
    private Returning expectedReturning = new Returning();

    @BeforeEach
    void beforeEach() {
        dataRequest = new RequestReturnDto();
        dataRequest.setRequestById("SD00006");
        dataRequest.setAssignmentId(48);
        Assignment assignment = Assignment.builder()
                .state(AssignmentState.ACCEPTED)
                .build();

        initialReturning.setId(12);
        initialReturning.setAssignment(assignment);

// Set Service
        returningServiceImpl = ReturningServiceImpl.builder()
                .returningMapper(returningMapper)
                .returningRepository(returningRepository)
                .assignmentRepository(assignmentRepository)
                .userRepository(userRepository)
                .entityCheckUtils(entityCheckUtils)
                .build();
    }

    @Test
    void create_ShouldThrowIllegalArgumentException_WhenStateOfAssignmentNotSuitable() {
        Assignment assignment = Assignment.builder()
                .state(AssignmentState.WAITING)
                .build();
        User user = new User();
        initialReturning.setAssignment(assignment);
        when(returningRepository.findByAssignmentId(48)).thenReturn(null);
        when(assignmentRepository.findById(48)).thenReturn(Optional.ofNullable(assignment));
        when(userRepository.findById("SD00006")).thenReturn(Optional.ofNullable(user));

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> returningServiceImpl.create(dataRequest));

        Assertions.assertEquals("state of assignment not suitable", exception.getMessage());
    }

    @Test
    void create_ShouldThrowRepeatDataException_WhenRequestAlreadyExists() {
        Assignment assignment = Assignment.builder()
                .state(AssignmentState.WAITING)
                .build();
        User user = new User();
        initialReturning.setAssignment(assignment);
        when(returningRepository.findByAssignmentId(48)).thenReturn(initialReturning);
        when(assignmentRepository.findById(48)).thenReturn(Optional.ofNullable(assignment));
        when(userRepository.findById("SD00006")).thenReturn(Optional.ofNullable(user));

        RepeatDataException exception = Assertions.assertThrows(RepeatDataException.class,
                () -> returningServiceImpl.create(dataRequest));

        Assertions.assertEquals("Request already exists", exception.getMessage());
    }



}