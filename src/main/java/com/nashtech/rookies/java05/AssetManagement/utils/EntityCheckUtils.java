package com.nashtech.rookies.java05.AssetManagement.utils;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssetRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.request.CategoryRequestDto;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EntityCheckUtils {

    public void categoryCheckInsert(CategoryRequestDto dto){
        if (dto.getName() == null) {
            throw new NullPointerException("null name");
        }if (dto.getId() == null) {
            throw new NullPointerException("null prefix");
        }
        if (dto.getName().trim().equals("")) {
            throw new IllegalArgumentException("empty name");
        }
        if (dto.getId().trim().equals("")) {
            throw new IllegalArgumentException("empty prefix");
        }
        if (dto.getName().length() > 50) {
            throw new IllegalArgumentException("name is too long, name up to 50 characters long");
        }
        if (dto.getId().trim().length() != 2) {
            throw new IllegalArgumentException("prefix only 2 characters");
        }
        //        special characters
        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~]");
        //        accented Vietnamese
        Pattern accented = Pattern.compile ("[áàảạãăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵđ]");
        Matcher checkSpecialName = special.matcher(dto.getName());
        Matcher checkAccentedName = accented.matcher(dto.getName().toLowerCase());
        Matcher checkSpecialId = special.matcher(dto.getId());
        Matcher checkAccentedId = accented.matcher(dto.getId().toLowerCase());
        if (checkSpecialName.find()) {
            throw new IllegalArgumentException("Name cannot contain special characters:! @ # $ % & * ( )  _ + = |  < > ? { } [ ] ~");
        }
        if (checkAccentedName.find()) {
            throw new IllegalArgumentException("Name Do not use Vietnamese with accents");
        }
        if (checkSpecialId.find()) {
            throw new IllegalArgumentException("ID cannot contain special characters:! @ # $ % & * ( )  _ + = |  < > ? { } [ ] ~");
        }
        if (checkAccentedId.find()) {
            throw new IllegalArgumentException("ID Do not use Vietnamese with accents");
        }
    }

    public void assetCheckInsert(AssetRequestDto dto){
        java.util.Date now = new java.util.Date();
        if (dto.getName() == null) {
            throw new NullPointerException("null name");
        }
        if (dto.getCategoryId() == null) {
            throw new NullPointerException("null category");
        }
        if (dto.getState() == null) {
            throw new NullPointerException("null state");
        }
        if (dto.getSpecification() == null) {
            throw new NullPointerException("null specification");
        }
        if (dto.getInstalledDate() == null) {
            throw new NullPointerException("null installedDate");
        }
        if (dto.getLocationId() == null) {
            throw new NullPointerException("null locationId");
        }

        if (dto.getName().trim().equals("")) {
            throw new IllegalArgumentException("empty name");
        }
        if (dto.getCategoryId().trim().equals("")) {
            throw new IllegalArgumentException("empty category");
        }

        if (dto.getLocationId() == 0) {
            throw new IllegalArgumentException("location does not match");
        }

        if (dto.getName().length() > 50) {
            throw new IllegalArgumentException("name is too long, name up to 50 characters long");
        }
        if (dto.getSpecification().length() >500 ) {
            throw new IllegalArgumentException("specification is too long, specification up to 500 characters long");
        }
//        special characters
        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~]");
        Matcher check = special.matcher(dto.getName());
        if (check.find()) {
            throw new IllegalArgumentException("Name cannot contain special characters:! @ # $ % & * ( )  _ + = |  < > ? { } [ ] ~");
        }

        if(now.compareTo(dto.getInstalledDate()) < 0){
            throw new IllegalArgumentException("installed date must be a date in the past");
        }
    }
}
