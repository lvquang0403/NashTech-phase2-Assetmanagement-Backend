package com.nashtech.rookies.java05.AssetManagement.controllers;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssignmentDto;
import com.nashtech.rookies.java05.AssetManagement.services.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/assignments")
public class AssignmentController {
    @Autowired
    private AssignmentService assignmentService;


    @PostMapping
    public ResponseEntity<AssignmentResponseDto> create(@RequestBody AssignmentDto dto) {
        return ResponseEntity.ok(assignmentService.createAssignment(dto));
    }

    @GetMapping("")
    public APIResponse<List<AssignmentResponseDto>> getAllAssignment
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

    @GetMapping("/user/{id}")
    public APIResponse<List<AssignmentResponseDto>> getAssignmentByUser
            (@PathVariable String id,
             @RequestParam(value = "page", required = false, defaultValue = "0") int page,
             @RequestParam(defaultValue = "updatedWhen_DESC") String orderBy) {
        return assignmentService.getAssignmentsByUser(id, page, orderBy);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @RequestBody AssignmentDto dto) {
        assignmentService.updateAssignment(dto, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/state")
    public AssignmentDetailDto changeStateAssignment(
            @PathVariable Integer id,
            @RequestBody @Valid AssignmentDto req) {
        return assignmentService.changeStateAssignment(id, req);
    }
}
