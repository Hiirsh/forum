package telran.java2022.user.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.java2022.user.dto.UserAddRolesDto;
import telran.java2022.user.dto.UserDto;
import telran.java2022.user.dto.UserLoginPasswordDto;
import telran.java2022.user.dto.UserRegisterDto;
import telran.java2022.user.dto.UserUpdateDto;
import telran.java2022.user.service.UserService;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class UserController {
  final UserService userService;

  @PostMapping("/register")
  public UserDto createUser(@RequestBody UserRegisterDto registerDto){
    return userService.registerUser(registerDto);
  }

  @PostMapping("/login")
  public UserDto loginUser(@RequestBody UserLoginPasswordDto loginPassword) {
    return userService.loginUser(loginPassword);
  }

  @DeleteMapping("/user/{login}")
  public UserDto removeUser(@PathVariable String login) {
    return userService.removeUser(login);
  }

  @PutMapping("/user/{login}")
  public UserDto updateUser(@PathVariable String login, @RequestBody UserUpdateDto updates) {
    return userService.updateUser(login, updates);
  }

  @PutMapping("/user/{login}/role/{role}")
  public UserAddRolesDto addRole(@PathVariable String login, @PathVariable String role){
    return userService.addRole(login, role);
  }

  @DeleteMapping("/user/{login}/role/{role}")
  public UserAddRolesDto deleteRole(@PathVariable String login, @PathVariable String role){
    return userService.deleteRole(login, role);
  }

  @PutMapping("/password")
  public void changePassword(@RequestBody UserLoginPasswordDto loginPassword){
    userService.changePassword(loginPassword);
  }

}
