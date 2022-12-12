package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.RequestReturnDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.*;
import com.nashtech.rookies.java05.AssetManagement.entities.Asset;
import com.nashtech.rookies.java05.AssetManagement.entities.Assignment;
import com.nashtech.rookies.java05.AssetManagement.entities.Returning;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentReturnState;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentState;
import com.nashtech.rookies.java05.AssetManagement.exceptions.RepeatDataException;
import com.nashtech.rookies.java05.AssetManagement.mappers.AssetMapper;
import com.nashtech.rookies.java05.AssetManagement.mappers.AssignmentMapper;
import com.nashtech.rookies.java05.AssetManagement.mappers.ReturningMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.AssignmentRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.ReturningRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.UserRepository;
import com.nashtech.rookies.java05.AssetManagement.utils.EntityCheckUtils;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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
    @Mock
    private List<AssignmentReturnState> states;
    @Mock
    private List<ReturningDto> returningDtoList;

    private ReturningMapper returningMapper ;
    @Autowired
    private EntityCheckUtils entityCheckUtils;
    private ReturningServiceImpl returningServiceImpl ;

    private RequestReturnDto dataRequest;
    private Returning initialReturning = new Returning();
    private Returning expectedReturning = new Returning();

    @BeforeEach
    void beforeEach() {
        returningMapper = mock(ReturningMapper.class);
        returningRepository = mock(ReturningRepository.class);
        assignmentRepository = mock(AssignmentRepository.class);
        userRepository = mock(UserRepository.class);
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
                () -> returningServiceImpl.createReturning(dataRequest));

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
                () -> returningServiceImpl.createReturning(dataRequest));

        Assertions.assertEquals("Request already exists", exception.getMessage());
    }


    @Test
    void getReturningRequests_ShouldReturnValue() {
        Page<Returning> result = mock(Page.class);
        String orderBy = "asset.id_DESC";
        String keyword = "";
        int locationId = 0;
        int page = 0;
        int pageSize = 15;
        states = new ArrayList<>();
        AssignmentReturnState[] returnStates = AssignmentReturnState.values();
        for (AssignmentReturnState returnState : returnStates) {
            states.add(returnState);
        }
        List<String> statesString = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "asset.id");
        when(returningRepository.findByPredicates(states, "2022-11-30", keyword,locationId, pageable))
                .thenReturn(result);
        when(returningMapper.toDtoList(result.toList()))
                .thenReturn(returningDtoList);
        APIResponse<List<ReturningDto>> expected = new APIResponse<>(page, returningDtoList);

        APIResponse<List<ReturningDto>> listResult = returningServiceImpl.getReturningByPredicates
                (statesString, "2022-11-30", keyword, 0, orderBy, locationId);

        MatcherAssert.assertThat(expected, is(listResult));
    }

}
