package telran.java2022.user.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import telran.java2022.user.dao.UserRepository;
import telran.java2022.user.dto.UserAddRolesDto;
import telran.java2022.user.dto.UserDto;
import telran.java2022.user.dto.UserLoginPasswordDto;
import telran.java2022.user.dto.UserRegisterDto;
import telran.java2022.user.dto.UserUpdateDto;
import telran.java2022.user.dto.extentions.UnauthorisedExeption;
import telran.java2022.user.dto.extentions.UserAlreadyExests;
import telran.java2022.user.dto.extentions.UserNotFoundExeprion;
import telran.java2022.user.model.User;

@Component
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  final ModelMapper modelMapper;
  final UserRepository repository;

  @Override
  public UserDto registerUser(UserRegisterDto registerDto) {
    User user = repository.findById(registerDto.getLogin()).orElse(null);
    if (user != null) {
      throw new UserAlreadyExests();
    }
    user = (modelMapper.map(registerDto, User.class));
    user.addRole("user");
    repository.save(user);
    return modelMapper.map(user, UserDto.class);
  }

  @Override
  public UserDto loginUser(UserLoginPasswordDto loginDto) {
    User user = repository.findById(loginDto.getLogin())
        .orElseThrow(() -> new UserNotFoundExeprion(loginDto.getLogin()));
    if (!user.getPassword().equals(loginDto.getPassword())) {
      throw new UnauthorisedExeption();
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
