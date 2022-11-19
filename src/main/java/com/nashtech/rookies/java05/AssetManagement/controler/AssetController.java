package com.nashtech.rookies.java05.AssetManagement.controler;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssetRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetResponseInsertDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import com.nashtech.rookies.java05.AssetManagement.services.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/assets")
public class AssetController {
    @Autowired
    private AssetService assetService;

    @GetMapping("")
    public APIResponse<List<AssetViewResponseDto>> getAllAssets
            (@RequestParam List<AssetState> states, @RequestParam (required = false) List<String> categoryNames
                    , @RequestParam(required = false, defaultValue = "") String keyword, @RequestParam (value = "page", required = false, defaultValue = "0") int page) {
        return assetService.getAssetsByPredicates(states, categoryNames, keyword, page);
    }


    @PostMapping("")
    public AssetResponseInsertDto insert(@RequestBody AssetRequestDto dto) {
        return assetService.insert(dto);
    }



}
