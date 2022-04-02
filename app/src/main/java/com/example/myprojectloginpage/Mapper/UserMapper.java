package com.example.myprojectloginpage.Mapper;

import com.example.myprojectloginpage.Dto.UserDto;
import com.example.myprojectloginpage.myprojectentity.User;

public class UserMapper
{
    private  User user;
    private  UserDto userDto;

    public UserMapper(User user) {
        this.user = user;
    }
    public UserMapper(UserDto userDto) {this.userDto = userDto;}
    public UserDto MapToUserDto()
    {
        UserDto userDto = new UserDto();
        userDto.setuId(user.getuId());
        userDto.setUserName(user.getUserName());
        userDto.setUserSurname(user.getUserSurname());
        userDto.setUserEmail(user.getUserEmail());
        userDto.setUserPassword(user.getUserPassword());
        userDto.setuId(user.getuId());
        return userDto;
    }
    public User MapToUser()
    {
        User user = new User();
        user.setuId(userDto.getuId());
        user.setUserName(userDto.getUserName());
        user.setUserSurname(userDto.getUserSurname());
        user.setUserEmail(userDto.getUserEmail());
        user.setUserPassword(userDto.getUserPassword());
        user.setuId(userDto.getuId());
        return user;
    }
}
