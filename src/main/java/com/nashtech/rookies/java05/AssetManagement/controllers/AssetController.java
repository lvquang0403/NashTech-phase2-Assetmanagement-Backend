package com.nashtech.rookies.java05.AssetManagement.controllers;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssetRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.*;
import com.nashtech.rookies.java05.AssetManagement.services.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping(value = "api/assets")
public class AssetController {
    @Autowired
    private AssetService assetService;

    @GetMapping("")
    public APIResponse<List<AssetViewResponseDto>> getAllAssets
            (@RequestParam(required = false, defaultValue = "") List<String> states,
             @RequestParam(required = false) List<String> categories,
             @RequestParam(required = false, defaultValue = "") String keyword,
             @RequestParam int locationId,
             @RequestParam(value = "page", required = false, defaultValue = "0") int page,
             @RequestParam(defaultValue = "updatedWhen_DESC") String orderBy) {
        if(states.isEmpty()){
            states.addAll(assetService.getAllAssetStates());
        }
        return assetService.getAssetsByPredicates(states, categories, keyword, locationId, page, orderBy);
    }

    @PostMapping("")
    public AssetResponseInsertDto insert(@RequestBody AssetRequestDto dto) {
        return assetService.insert(dto);
    }

    @PutMapping("/{id}")
    public AssetResponseInsertDto update(@RequestBody AssetRequestDto dto, @PathVariable String id) {
        return assetService.update(dto, id);
    }

    @GetMapping("/{id}")
    public AssetResponseDto getAssetById(@PathVariable String id) {
        return assetService.getAssetById(id);
    }

    @GetMapping("/states")
    public Set<String> getAllAssetStates() {
        return assetService.getAllAssetStates();
    }

    @GetMapping("/{id}/check-historical")
    public ResponseEntity checkAssetValidToDelete(@PathVariable String id){
        boolean isDeleted=assetService.checkAssetValidToDelete(id);
        DeleteAssetDto deleteAssetDto=new DeleteAssetDto();
        if (isDeleted){
            deleteAssetDto.setTitle("Asset valid to delete");
            deleteAssetDto.setMessage("That asset has no valid assignments");
            return ResponseEntity.ok(deleteAssetDto);
        }
        else {
            deleteAssetDto.setTitle("Cannot delete Asset");
            deleteAssetDto.setMessage("Cannot delete the asset because it belongs to one or more historical assignments.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(deleteAssetDto);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteAssetById(@PathVariable String id){
        boolean isDeleted=assetService.deleteAssetById(id);
        if (isDeleted)  return ResponseEntity.ok("That asset was deleted.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete that asset.");
    }

}
