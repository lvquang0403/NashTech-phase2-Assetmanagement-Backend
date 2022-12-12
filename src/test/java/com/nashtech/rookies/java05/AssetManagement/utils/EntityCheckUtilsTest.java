package com.nashtech.rookies.java05.AssetManagement.utils;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssetRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.request.CategoryRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.request.RequestReturnDto;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

public class EntityCheckUtilsTest {
    EntityCheckUtils entityCheckUtils;
    AssetRequestDto assetRequestDto;
    CategoryRequestDto categoryRequestDto;
    RequestReturnDto requestReturnDto;
    Long oneDay = 1000 * 60 * 60 * 24L;
    java.util.Date now;

    @BeforeEach
    void beforeEach() {
        now = new java.util.Date();
        assetRequestDto = new AssetRequestDto();
        assetRequestDto.setName("fridge max5");
        assetRequestDto.setSpecification("as");
        assetRequestDto.setCategoryId("pc");
        assetRequestDto.setState(AssetState.AVAILABLE);
        assetRequestDto.setLocationId(1);
        assetRequestDto.setInstalledDate(new Date(now.getTime() - oneDay));

//        set data for test category
        categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setId("ww");
        categoryRequestDto.setName("computer");

//        set data for test requestReturnDto
        requestReturnDto = new RequestReturnDto();
        requestReturnDto.setRequestById("SD0001");
        requestReturnDto.setAssignmentId(12);

        entityCheckUtils = new EntityCheckUtils();

    }

//    test assetCheckInsert
    @Test
    void assetCheckInsert_ShouldThrowNullPointerException_WhenParamsIsNull() {
        assetRequestDto.setName(null);

        NullPointerException exception = Assertions.assertThrows(NullPointerException.class,
                () -> entityCheckUtils.assetCheckInsert(assetRequestDto));

        Assertions.assertEquals("null name", exception.getMessage());
    }
    @Test
    void assetCheckInsert_ShouldThrowIllegalArgumentException_WhenParamsIsEmptyString() {
        assetRequestDto.setName("");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> entityCheckUtils.assetCheckInsert(assetRequestDto));

        Assertions.assertEquals("empty name", exception.getMessage());
    }
    @Test
    void assetCheckInsert_ShouldThrowIllegalArgumentException_WhenNameTooLong() {
        assetRequestDto.setName("aaaaaaaaaaaKaaaaaaaaaaaKaaaaaaaaaaaKaaaaaaaaaaaKaaaaaaaaaaa");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> entityCheckUtils.assetCheckInsert(assetRequestDto));


        Assertions.assertEquals("name is too long, name up to 50 characters long", exception.getMessage());
    }
    @Test
    void assetCheckInsert_ShouldThrowIllegalArgumentException_WhenSpecificationTooLong() {
        String str = "";
        for (int i = 0; i < 51; i++) {
            str += "1234567890";
        }
        assetRequestDto.setSpecification(str);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> entityCheckUtils.assetCheckInsert(assetRequestDto));


        Assertions.assertEquals("specification is too long, specification up to 500 characters long", exception.getMessage());
    }
    @Test
    void assetCheckInsert_ShouldThrowIllegalArgumentException_WhenInstalledDateNotPastDate() {
        assetRequestDto.setInstalledDate(new Date(now.getTime() + oneDay));

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> entityCheckUtils.assetCheckInsert(assetRequestDto));

        Assertions.assertEquals("installed date must be a date in the past", exception.getMessage());
    }
    @Test
    void assetCheckInsert_ShouldThrowIllegalArgumentException_WhenNameHasSpecialCharacters() {
        assetRequestDto.setName("hahah2~");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> entityCheckUtils.assetCheckInsert(assetRequestDto));

        Assertions.assertEquals("Cannot contain special characters", exception.getMessage());
    }

    //    test assetCheckUpdate
    @Test
    void assetCheckUpdate_ShouldThrowNullPointerException_WhenParamsIsNull() {
        assetRequestDto.setName(null);

        NullPointerException exception = Assertions.assertThrows(NullPointerException.class,
                () -> entityCheckUtils.assetCheckUpdate(assetRequestDto));

        Assertions.assertEquals("null name", exception.getMessage());
    }
    @Test
    void assetCheckUpdate_ShouldThrowIllegalArgumentException_WhenParamsIsEmptyString() {
        assetRequestDto.setName("");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> entityCheckUtils.assetCheckUpdate(assetRequestDto));

        Assertions.assertEquals("empty name", exception.getMessage());
    }
    @Test
    void assetCheckUpdate_ShouldThrowIllegalArgumentException_WhenInstalledDateNotPastDate() {
        assetRequestDto.setInstalledDate(new Date(now.getTime() + oneDay));

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> entityCheckUtils.assetCheckUpdate(assetRequestDto));

        Assertions.assertEquals("installed date must be a date in the past", exception.getMessage());
    }
    @Test
    void assetCheckUpdate_ShouldThrowIllegalArgumentException_WhenNameHasSpecialCharacters() {
        assetRequestDto.setName("hahah2~");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> entityCheckUtils.assetCheckUpdate(assetRequestDto));

        Assertions.assertEquals("Cannot contain special characters", exception.getMessage());
    }


//    test for category
    @Test
    void categoryCheckInsert_ShouldThrowNullPointerException_WhenParamsIsNull(){
    categoryRequestDto.setName(null);
    NullPointerException exception = Assertions.assertThrows(NullPointerException.class,
            ()->entityCheckUtils.categoryCheckInsert(categoryRequestDto));

    Assertions.assertEquals("null name",exception.getMessage());
    }
    @Test
    void categoryCheckInsert_ShouldThrowIllegalArgumentException_WhenParamsIsEmptyString(){
        categoryRequestDto.setId("");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->entityCheckUtils.categoryCheckInsert(categoryRequestDto));

        Assertions.assertEquals("empty prefix",exception.getMessage());
    }
    @Test
    void categoryCheckInsert_ShouldThrowIllegalArgumentException_WhenNameTooLong(){
        categoryRequestDto.setName("aaaaaaaaaaaaaaaaaaaaTaaaaaaaaaaaaaaaaaaaaTaaaaaaaaaaaaaaaaaaaa");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->entityCheckUtils.categoryCheckInsert(categoryRequestDto));

        Assertions.assertEquals("name is too long, name up to 50 characters long",exception.getMessage());
    }
    @Test
    void categoryCheckInsert_ShouldThrowIllegalArgumentException_WhenPrefixTooLong(){
        categoryRequestDto.setId("qws");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->entityCheckUtils.categoryCheckInsert(categoryRequestDto));

        Assertions.assertEquals("prefix only 2 characters",exception.getMessage());
    }
    @Test
    void categoryCheckInsert_ShouldThrowIllegalArgumentException_WhenNameHasSpecialCharacters(){
        categoryRequestDto.setName("mahua~!@");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->entityCheckUtils.categoryCheckInsert(categoryRequestDto));

        Assertions.assertEquals("Name Cannot contain special characters",exception.getMessage());
    }
    @Test
    void categoryCheckInsert_ShouldThrowIllegalArgumentException_WhenIdHasSpecialCharacters(){
        categoryRequestDto.setId("S@");
        categoryRequestDto.setName("ma - hua");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->entityCheckUtils.categoryCheckInsert(categoryRequestDto));

        Assertions.assertEquals("ID Cannot contain special characters",exception.getMessage());
    }
    @Test
    void categoryCheckInsert_ShouldThrowIllegalArgumentException_WhenNameVietnameseWithAccents(){
        categoryRequestDto.setId("SH");
        categoryRequestDto.setName("XE số");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->entityCheckUtils.categoryCheckInsert(categoryRequestDto));

        Assertions.assertEquals("Name Do not use Vietnamese with accents",exception.getMessage());
    }
    @Test
    void categoryCheckInsert_ShouldThrowIllegalArgumentException_WhenIdVietnameseWithAccents(){
        categoryRequestDto.setId("sỐ");
        categoryRequestDto.setName("XE sO");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                ()->entityCheckUtils.categoryCheckInsert(categoryRequestDto));

        Assertions.assertEquals("ID Do not use Vietnamese with accents",exception.getMessage());
    }

//  test returnCheckCreate
    @Test
    void returnCheckCreate_ShouldThrowNullPointerException_WhenParamsRequestByIdIsNull() {
        requestReturnDto.setRequestById(null);

        NullPointerException exception = Assertions.assertThrows(NullPointerException.class,
                () -> entityCheckUtils.returnCheckCreate(requestReturnDto));

        Assertions.assertEquals("null Request sender", exception.getMessage());
    }

    @Test
    void returnCheckCreate_ShouldThrowNullPointerException_WhenParamsAssignmentIdIsNull() {
        requestReturnDto.setAssignmentId(null);

        NullPointerException exception = Assertions.assertThrows(NullPointerException.class,
                () -> entityCheckUtils.returnCheckCreate(requestReturnDto));

        Assertions.assertEquals("null Assignment", exception.getMessage());
    }
}
