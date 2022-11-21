package com.nashtech.rookies.java05.AssetManagement.utils;

import com.nashtech.rookies.java05.AssetManagement.entities.User;
import com.nashtech.rookies.java05.AssetManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class UserUtils {

    @Autowired
    UserRepository userRepository;

    static List<User> listUser=null;
    static Integer maxCode=0;
    HashMap<String,Integer> userMap=new HashMap<String,Integer>();
    void init(){
        listUser=userRepository.findAll();
        for (User user:listUser) {
            Integer code = Integer.parseInt(user.getId().substring(2));
            if (code > maxCode) maxCode = code;
            int positionSeperated=seperatedStringAndNumber(user.getUsername());
            String prefixUsername=user.getUsername().substring(0,positionSeperated);
            Integer suffixUsername=Integer.parseInt(user.getUsername().substring(positionSeperated));
            if (!userMap.containsKey(prefixUsername)){
                userMap.put(prefixUsername,suffixUsername);
            }
            else{
                if (userMap.get(prefixUsername)<suffixUsername){
                    userMap.replace(prefixUsername,suffixUsername);
                }
            }
        }
        maxCode++;
        for (String key:userMap.keySet()){
            int value=userMap.get(key);
            userMap.replace(key,value+1);
        }

    }
    int seperatedStringAndNumber(String str){
        for (int i=0;i<str.length();i++){
            if (Character.isDigit(str.charAt(i)) )  return i;
        }
        return -1;
    }
    public Integer getMaxCode(){
        if (listUser==null) init();
        int result=maxCode++;
        return result;
    }
    public Integer getMaxUsernameCode(String name){
        if (listUser==null) init();
        if (!userMap.containsKey(name)){
            userMap.put(name,2);
            return 1;
        }
        int res=userMap.get(name);
        userMap.replace(name,res+1);
        return res;
    }

    public String getPrefixUsername(String firstName,String lastName){
        StringBuilder prefix=new StringBuilder(firstName);
        for (String itemName : lastName.split(" ")){
            prefix.append(itemName.charAt(0));
        }
        return prefix.toString();
    }
}
