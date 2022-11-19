package com.nashtech.rookies.java05.AssetManagement.controler;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.CategoryRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.CategoryResponseInsertDto;
import com.nashtech.rookies.java05.AssetManagement.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "categories")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @PostMapping(value = "")
    @ResponseBody
    public CategoryResponseInsertDto insert(@RequestBody CategoryRequestDto dto){
        return service.insert(dto);
    }

    @GetMapping(value = "/to-insert")
    @ResponseBody
    public List<CategoryResponseInsertDto> getToInsert(){
        return service.getToInsert();
    }
}
