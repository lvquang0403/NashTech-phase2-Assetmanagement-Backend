package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.ResetPasswordDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.request.UserRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.*;
import com.nashtech.rookies.java05.AssetManagement.entities.Assignment;
import com.nashtech.rookies.java05.AssetManagement.entities.Location;
import com.nashtech.rookies.java05.AssetManagement.entities.Role;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentReturnState;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentState;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.UserState;
import com.nashtech.rookies.java05.AssetManagement.mappers.AssignmentMapper;
import com.nashtech.rookies.java05.AssetManagement.mappers.UserMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.LocationRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.RoleRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.UserRepository;
import com.nashtech.rookies.java05.AssetManagement.services.UserService;
import com.nashtech.rookies.java05.AssetManagement.utils.UserUtils;
import lombok.Builder;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Builder
@Service
public class UserServiceImpl implements UserService {
    private static final int pageSize = 15;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    UserUtils userUtils;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public UserResponseDto getUserById(String id) {
        Optional<User> option = userRepository.findById(id);
        if (option.isEmpty()) return null;
        return UserMapper.mapFromEntityToUserResponseDto(option.get());
    }

    @Override
    public APIResponse<List<UserViewResponseDto>> getUsersByPredicates(List<String> types, String keyword, int locationId, int page, String orderBy) {

        String[] parts = orderBy.split("_");
        String columnName = parts[0];
        String order = parts[1];

        Pageable pageable;
        if("DESC".equals(order)){
            pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC , columnName);
        }else{
            pageable = PageRequest.of(page, pageSize, Sort.Direction.ASC , columnName);
        }

        List<Role> roleList;
        if (types == null) {
            roleList = roleRepository.findAll();
        } else {
            roleList = roleRepository.findByNameIsIn(types);
        }
        Page<User> result;

        result = userRepository.findUsersWithFilter(roleList, keyword.toLowerCase(), locationId, pageable);
        return new APIResponse<>(result.getTotalPages(), userMapper.mapUserEntityListToUserViewResponseDtoList(result.toList()));
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) throws ParseException {
        User user = UserMapper.mapFromUserRequestDtoToEntity(userRequestDto);

        Integer maxCode = userUtils.getMaxCode();
        String id = "SD" + String.format("%04d", maxCode);

        String prefixUsername = userUtils.getPrefixUsername(userRequestDto.getFirstName(), userRequestDto.getLastName());
        Integer maxCodeUsername = userUtils.getMaxUsernameCode(prefixUsername);
        String username = prefixUsername + maxCodeUsername;

        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
        String password = prefixUsername + "@" + format.format(user.getBirth());

        Role role = roleRepository.findById(userRequestDto.getRoleId()).get();
        Location location = locationRepository.findById(userRequestDto.getLocationId()).get();

        user.setId(id);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setLocation(location);
        if (user.getListAssignmentsBy()==null)  user.setListAssignmentsBy(new ArrayList<Assignment>());
        if (user.getListAssignmentsTo()==null)  user.setListAssignmentsTo(new ArrayList<Assignment>());

        userRepository.save(user);

        return UserMapper.mapFromEntityToUserResponseDto(user);
    }

    public UserResponseDto updateUser(String id, UserRequestDto userRequestDto) throws ParseException {
        Optional<User> option = userRepository.findById(id);
        if (option.isEmpty()) return null;
        User user = option.get();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date birth = format.parse(userRequestDto.getBirth());
        Date joinedDate = format.parse(userRequestDto.getJoinedDate());
        Date date=new Date();

        Role role = roleRepository.findById(userRequestDto.getRoleId()).get();
//        Location location=locationRepository.findById(userRequestDto.getLocationId()).get();

        user.setGender(userRequestDto.getGender());
        user.setBirth(birth);
        user.setJoinedDate(joinedDate);
        user.setRole(role);
        user.setUpdatedWhen(new Timestamp(date.getTime()));
        userRepository.save(user);

        return UserMapper.mapFromEntityToUserResponseDto(user);
    }

    @Override
    public boolean disableUserById(String id) {
        Optional<User> optionalUser=userRepository.findById(id);
        if (optionalUser.isEmpty()) return false;
        User user=optionalUser.get();
        List<Assignment> listAssignments=user.getListAssignmentsTo();
        for (Assignment assignment : listAssignments){
            if (assignment.getState()== AssignmentState.ACCEPTED){
                if (assignment.getReturning().getState() != AssignmentReturnState.COMPLETED){
                    return false;
                }
            } else if (assignment.getState()== AssignmentState.WAITING) {
                return false;
            }
        }
        user.setDisabled(true);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean checkUserValidToDisableById(String id) {
        Optional<User> optionalUser=userRepository.findById(id);
        if (optionalUser.isEmpty()) return false;
        User user=optionalUser.get();
        List<Assignment> listAssignments=user.getListAssignmentsTo();
        for (Assignment assignment : listAssignments){
            if (assignment.getState()== AssignmentState.ACCEPTED){
                if (assignment.getReturning().getState() != AssignmentReturnState.COMPLETED){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public List<AssignmentResponseDto> getListAssignmentsToOfUser(String id) {
        Optional<User> optionalUser=userRepository.findById(id);
        if (optionalUser.isEmpty()) return null;
        User user=optionalUser.get();
        AssignmentMapper mapper=new AssignmentMapper();
        return user.getListAssignmentsTo().stream().map((assignment)-> mapper.mapAssignmentEntityToResponseDto(assignment)).collect(Collectors.toList());
    }

    @Override
    public ChangePasswordDto changePassword(Authentication authentication, ResetPasswordDto resetPasswordDto) {
        System.out.println(passwordEncoder.encode("12345"));
        String username=authentication.getName();
        Optional<User> optionalUser =userRepository.findUsersByUsername(username);
        ChangePasswordDto changePasswordDto=new ChangePasswordDto();
        if (optionalUser.isEmpty()){
            changePasswordDto.setStatus(HttpStatus.BAD_REQUEST);
            changePasswordDto.setMessage("Cannot find that user.");
            return changePasswordDto;
        }
        User user=optionalUser.get();
        String passwordEncrypted=user.getPassword();
//        if (resetPasswordDto.getOldPassword()==null){
//            String prefixUsername = userUtils.getPrefixUsername(user.getFirstName(), user.getLastName());
//            SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
//            resetPasswordDto.setOldPassword(prefixUsername + "@" + format.format(user.getBirth()));
//        }
        if (resetPasswordDto.getNewPassword().equals(resetPasswordDto.getOldPassword())){
            changePasswordDto.setStatus(HttpStatus.BAD_REQUEST);
            changePasswordDto.setTitle("Failed to change password");
            changePasswordDto.setMessage("New password must different old password.");
            return changePasswordDto;
        }
        if (resetPasswordDto.getOldPassword()==null)    resetPasswordDto.setOldPassword("");
        if (passwordEncoder.matches(resetPasswordDto.getOldPassword(),passwordEncrypted) || user.getState()!=UserState.ACTIVE){
            user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
            if (user.getState()!=UserState.ACTIVE)   user.setState(UserState.ACTIVE);
            userRepository.save(user);
            changePasswordDto.setStatus(HttpStatus.OK);
            changePasswordDto.setTitle("Change password");
            changePasswordDto.setMessage("Your password has been changed successfully!");
            return changePasswordDto;
        }
        changePasswordDto.setStatus(HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
        changePasswordDto.setTitle("Change password");
        changePasswordDto.setMessage("Password is incorrect");
        return changePasswordDto;
    }
}