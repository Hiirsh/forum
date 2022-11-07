package telran.java2022.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.java2022.user.utils.Role;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddRolesDto {
  String login;
  Iterable<Role> roles;
}
