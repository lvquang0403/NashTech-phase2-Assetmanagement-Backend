package com.nashtech.rookies.java05.AssetManagement.dtos.request;

import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import com.nashtech.rookies.java05.AssetManagement.exceptions.RepeatDataException;
import com.nashtech.rookies.java05.AssetManagement.repository.CategoryRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Setter
@Getter
@NoArgsConstructor
public class CategoryRequestDto{

    private  String id;
    private  String name;




    public void validateInsert(Optional<List<Category>> optional){
        if (name == null) {
            throw new NullPointerException("null name");
        }if (id == null) {
            throw new NullPointerException("null prefix");
        }
        if (name.trim().equals("")) {
            throw new IllegalArgumentException("empty name");
        }
        if (id.trim().equals("")) {
            throw new IllegalArgumentException("empty prefix");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("name is too long, name up to 50 characters long");
        }
        if (id.trim().length() != 2) {
            throw new IllegalArgumentException("prefix only 2 characters");
        }
        //        special characters
        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~]");
        //        accented Vietnamese
        Pattern accented = Pattern.compile ("[áàảạãăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵđ]");
        Matcher checkSpecialName = special.matcher(name);
        Matcher checkAccentedName = accented.matcher(name.toLowerCase());
        Matcher checkSpecialId = special.matcher(id);
        Matcher checkAccentedId = accented.matcher(id.toLowerCase());
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


        if (!optional.isEmpty()) {
            for (Category category : optional.get()) {
                if(category.getId().equals(id)){
                    throw new RepeatDataException("prefix already exists");
                }
                if(category.getName().equals(name)){
                    throw new RepeatDataException("category already exists");
                }
            }
        }
    }
}