package com.nashtech.rookies.java05.AssetManagement.controllers;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.RequestReturnDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.ReturningDto;
import com.nashtech.rookies.java05.AssetManagement.services.ReturningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/returns")
public class ReturningController {

    @Autowired
    ReturningService returningService;

    @GetMapping("")
    public APIResponse<List<ReturningDto>> getAllReturningRequests
            (@RequestParam(required = false, defaultValue = "") List<String> states,
             @RequestParam(required = false) String returnedDate,
             @RequestParam(required = false, defaultValue = "") String keyword,
             @RequestParam(value = "page", required = false, defaultValue = "0") int page,
             @RequestParam(defaultValue = "asset.id_DESC") String orderBy,
             @RequestParam int locationId) {

        return returningService.getReturningByPredicates(states, returnedDate, keyword, page, orderBy, locationId);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> completeRequest(@PathVariable Integer id,
                                             @RequestBody RequestReturnDto dto)
    {
        returningService.completeReturning(dto, id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("")
    public ReturningDto create(@RequestBody RequestReturnDto dto) {
        return returningService.createReturning(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity cancelRequest(@PathVariable Integer id){
        return ResponseEntity.ok(returningService.cancelReturning(id));
    }
}
