package telran.java2022.user.service;

import telran.java2022.user.dto.UserAddRolesDto;
import telran.java2022.user.dto.UserDto;
import telran.java2022.user.dto.UserLoginPasswordDto;
import telran.java2022.user.dto.UserRegisterDto;
import telran.java2022.user.dto.UserUpdateDto;

public interface UserService {
  UserDto registerUser(UserRegisterDto registerDto);

  UserDto loginUser(UserLoginPasswordDto loginDto);

  UserDto removeUser(String login);

  UserDto updateUser(String login, UserUpdateDto updateDto);

  UserAddRolesDto addRole(String login, String role);
  
  UserAddRolesDto deleteRole(String login, String role);

  void changePassword(UserLoginPasswordDto loginDto);
}
