package com.nashtech.rookies.java05.AssetManagement.utils;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssetRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.request.CategoryRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.request.RequestReturnDto;
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
        String special = "^[a-zA-Z0-9\\- \\s]+$";
        //        accented Vietnamese
        Pattern accented = Pattern.compile ("[áàảạãăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵđ]");

        Matcher checkAccentedName = accented.matcher(dto.getName().toLowerCase());
        Matcher checkAccentedId = accented.matcher(dto.getId().toLowerCase());
        if (checkAccentedName.find()) {
            throw new IllegalArgumentException("Name Do not use Vietnamese with accents");
        }
        if (checkAccentedId.find()) {
            throw new IllegalArgumentException("ID Do not use Vietnamese with accents");
        }
        if(!Pattern.matches(special, dto.getName())) {
            throw new IllegalArgumentException("Name Cannot contain special characters");
        }
        if(!Pattern.matches(special, dto.getId())) {
            throw new IllegalArgumentException("ID Cannot contain special characters");
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
        String special = "^[a-zA-Z0-9\\- \\s]+$";
        String specialSpecification = "^[a-zA-Z0-9\\- \\s,.;:^/\"'!@#$%&*()_+=|<>?{}\\[\\]~]+$";
        if(!Pattern.matches(special, dto.getName())) {
            throw new IllegalArgumentException("Cannot contain special characters");
        }
        if(!Pattern.matches(specialSpecification, dto.getSpecification())) {
            throw new IllegalArgumentException("Cannot contain special characters");
        }

        if(now.compareTo(dto.getInstalledDate()) < 0){
            throw new IllegalArgumentException("installed date must be a date in the past");
        }
    }

    public void assetCheckUpdate(AssetRequestDto dto){
        java.util.Date now = new java.util.Date();
        if (dto.getName() == null) {
            throw new NullPointerException("null name");
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

        if (dto.getName().trim().equals("")) {
            throw new IllegalArgumentException("empty name");
        }
        if (dto.getSpecification().trim().equals("")) {
            throw new IllegalArgumentException("empty specification");
        }


        if (dto.getName().length() > 50) {
            throw new IllegalArgumentException("name is too long, name up to 50 characters long");
        }
        if (dto.getSpecification().length() >500 ) {
            throw new IllegalArgumentException("specification is too long, specification up to 500 characters long");
        }
//        special characters
        String special = "^[a-zA-Z0-9\\- \\s]+$";
        String specialSpecification = "^[a-zA-Z0-9\\- \\s,.;:^/\"'!@#$%&*()_+=|<>?{}\\[\\]~]+$";
        Pattern accented = Pattern.compile ("[áàảạãăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵđ]");
        Matcher checkAccentedName = accented.matcher(dto.getName().toLowerCase());

        if (checkAccentedName.find()) {
            throw new IllegalArgumentException("Name Do not use Vietnamese with accents");
        }
        if(!Pattern.matches(special, dto.getName())) {
            throw new IllegalArgumentException("Cannot contain special characters");
        }
        if(!Pattern.matches(specialSpecification, dto.getSpecification())) {
            throw new IllegalArgumentException("Cannot contain special characters");
        }
        if(now.compareTo(dto.getInstalledDate()) < 0){
            throw new IllegalArgumentException("installed date must be a date in the past");
        }
    }

    public void returnCheckCreate(RequestReturnDto dto){
        java.util.Date now = new java.util.Date();
        if (dto.getRequestById() == null || dto.getRequestById().trim().equals("")) {
            throw new NullPointerException("null Request sender");
        }
        if (dto.getAssignmentId() == null) {
            throw new NullPointerException("null Assignment");
        }
    }
}
