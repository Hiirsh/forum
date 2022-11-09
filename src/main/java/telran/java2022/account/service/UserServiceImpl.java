package telran.java2022.account.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import telran.java2022.account.dao.UserRepository;
import telran.java2022.account.dto.UserAddRolesDto;
import telran.java2022.account.dto.UserDto;
import telran.java2022.account.dto.UserLoginPasswordDto;
import telran.java2022.account.dto.UserRegisterDto;
import telran.java2022.account.dto.UserUpdateDto;
import telran.java2022.account.dto.extentions.UnauthorizedException;
import telran.java2022.account.dto.extentions.UserAlreadyExests;
import telran.java2022.account.dto.extentions.UserNotFoundExeprion;
import telran.java2022.account.model.User;

@Component
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  final ModelMapper modelMapper;
  final UserRepository repository;

  @Override
  public UserDto registerUser(UserRegisterDto registerDto) {
    User user = repository.findById(registerDto.getLogin()).orElse(null);
    if (user != null) {
      throw new UserAlreadyExests(registerDto.getLogin());
    }
    user = (modelMapper.map(registerDto, User.class));
    repository.save(user);
    return modelMapper.map(user, UserDto.class);
  }

  @Override
  public UserDto loginUser(String[] credentials) {
    User user = repository.findById(credentials[0])
        .orElseThrow(() -> new UserNotFoundExeprion(credentials[0]));
    if (!user.getPassword().equals(credentials[1])) {
      throw new UnauthorizedException();
    }
    return modelMapper.map(user, UserDto.class);
  }

  @Override
  public UserDto removeUser(String login) {
    User user = repository.findById(login).orElseThrow(() -> new UserNotFoundExeprion());
    repository.deleteById(login);
    return modelMapper.map(user, UserDto.class);
  }

  @Override
  public UserDto updateUser(String login, UserUpdateDto updateDto) {
    User user = repository.findById(login).orElseThrow(() -> new UserNotFoundExeprion());
    user.setFirstName(updateDto.getFirstName());
    user.setLastName(updateDto.getLastName());
    repository.save(user);
    return modelMapper.map(user, UserDto.class);
  }

  @Override
  public UserAddRolesDto addRole(String login, String role) {
    User user = repository.findById(login).orElseThrow(() -> new UserNotFoundExeprion());
    user.addRole(role);
    repository.save(user);
    return modelMapper.map(user, UserAddRolesDto.class);
  }

  @Override
  public UserAddRolesDto deleteRole(String login, String role) {
    User user = repository.findById(login).orElseThrow(() -> new UserNotFoundExeprion());
    user.deleteRole(role);
    repository.save(user);
    return modelMapper.map(user, UserAddRolesDto.class);
  }

  @Override
  public void changePassword(UserLoginPasswordDto loginDto) {
    User user = repository.findById(loginDto.getLogin()).orElseThrow(() -> new UserNotFoundExeprion());
    user.setPassword(loginDto.getPassword());
  }

}
