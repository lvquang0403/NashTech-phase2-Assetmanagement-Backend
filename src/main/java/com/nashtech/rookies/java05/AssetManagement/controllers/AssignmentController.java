package com.nashtech.rookies.java05.AssetManagement.controllers;


import com.nashtech.rookies.java05.AssetManagement.dtos.response.*;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentState;
import com.nashtech.rookies.java05.AssetManagement.services.AssetService;
import com.nashtech.rookies.java05.AssetManagement.services.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/assignments")
public class AssignmentController {
    @Autowired
    private AssignmentService assignmentService;

    @GetMapping("")
    public APIResponse<List<AssignmentListResponseDto>> getAllAssignment
            (@RequestParam(required = false, defaultValue = "") List<String> states,
             @RequestParam(required = false) String assignDate,
             @RequestParam(required = false, defaultValue = "") String keyword,
             @RequestParam(value = "page", required = false, defaultValue = "0") int page,
             @RequestParam(defaultValue = "updatedWhen_DESC") String orderBy) {

        return assignmentService.getAssignmentByPredicates(states, assignDate, keyword, page, orderBy);
    }

    @GetMapping("/{id}")
    public AssignmentDetailDto getAssignment(@PathVariable int id) {
        return assignmentService.getAssignment(id);
    }
}
