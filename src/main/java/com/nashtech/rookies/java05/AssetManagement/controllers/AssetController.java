package com.nashtech.rookies.java05.AssetManagement.controllers;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import com.nashtech.rookies.java05.AssetManagement.services.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/assets")
public class AssetController {
    @Autowired
    private AssetService assetService;

    @GetMapping("")
    public APIResponse<List<AssetViewResponseDto>> getAllAssets
            (@RequestParam (required = false) List<AssetState> states, @RequestParam (required = false) List<String> categories
                    , @RequestParam(required = false, defaultValue = "") String keyword, @RequestParam int locationId, @RequestParam (value = "page", required = false, defaultValue = "0") int page) {
        return assetService.getAssetsByPredicates(states, categories, keyword, locationId, page);
    }

    @GetMapping("/states")
    public List<String> getAllAssetStates(){
        return assetService.getAllAssetStates();
    }

    @GetMapping("/{id}")
    public AssetResponseDto getAssetById(@PathVariable String id){
        return assetService.getAssetById(id);
    }

}
