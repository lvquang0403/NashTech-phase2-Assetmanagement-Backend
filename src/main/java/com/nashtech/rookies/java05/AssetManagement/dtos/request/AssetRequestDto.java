package com.nashtech.rookies.java05.AssetManagement.dtos.request;

import com.nashtech.rookies.java05.AssetManagement.entities.Asset;
import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import com.nashtech.rookies.java05.AssetManagement.exceptions.RepeatDataException;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Setter
@Getter
@Builder
public class AssetRequestDto {
    private String name;
    private String specification;
    private String categoryId;
    private AssetState state;
    private Integer locationId;
    private Date installedDate;

    public AssetRequestDto(String name, String specification, String categoryId, AssetState state, Integer locationId, Date installedDate) {
        this.name = name;
        this.specification = specification;
        this.categoryId = categoryId;
        this.state = state;
        this.locationId = locationId;
        this.installedDate = installedDate;
    }
    public AssetRequestDto() {
    }

    public void validateInsert(){
        java.util.Date now = new java.util.Date();
        if (name == null) {
            throw new NullPointerException("null name");
        }
        if (categoryId == null) {
            throw new NullPointerException("null category");
        }
        if (state == null) {
            throw new NullPointerException("null state");
        }
        if (specification == null) {
            throw new NullPointerException("null specification");
        }
        if (installedDate == null) {
            throw new NullPointerException("null installedDate");
        }
        if (locationId == null) {
            throw new NullPointerException("null locationId");
        }

        if (name.trim().equals("")) {
            throw new IllegalArgumentException("empty name");
        }
        if (categoryId.trim().equals("")) {
            throw new IllegalArgumentException("empty category");
        }

        if (locationId == 0) {
            throw new IllegalArgumentException("location does not match");
        }

        if (name.length() > 50) {
            throw new IllegalArgumentException("name is too long, name up to 50 characters long");
        }
        if (specification.length() >500 ) {
            throw new IllegalArgumentException("specification is too long, specification up to 500 characters long");
        }
//        special characters
        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~]");
        Matcher check = special.matcher(name);
        if (check.find()) {
            throw new IllegalArgumentException("Name cannot contain special characters:! @ # $ % & * ( )  _ + = |  < > ? { } [ ] ~");
        }

        if(now.compareTo(installedDate) < 0){
            throw new IllegalArgumentException("installed date must be a date in the past");
        }
    }
}