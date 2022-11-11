package telran.java2022.account.service;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import telran.java2022.account.dao.UserRepository;
import telran.java2022.account.dto.UserAddRolesDto;
import telran.java2022.account.dto.UserDto;
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
    if (repository.existsById(registerDto.getLogin())) {
      throw new UserAlreadyExests(registerDto.getLogin());
    }
    User user = modelMapper.map(registerDto, User.class);
    String password = BCrypt.hashpw(registerDto.getPassword(), BCrypt.gensalt());
    user.setPassword(password);
    repository.save(user);
    return modelMapper.map(user, UserDto.class);
  }

  @Override
  public UserDto loginUser(String[] credentials) {
    User user = repository.findById(credentials[0])
        .orElseThrow(() -> new UserNotFoundExeprion(credentials[0]));
    if (!BCrypt.checkpw(credentials[1], user.getPassword())) {
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
  public void changePassword(String login, String newPassword) {
    User user = repository.findById(login).orElseThrow(() -> new UserNotFoundExeprion());
    String password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
    user.setPassword(password);
    repository.save(user);
  }

}
