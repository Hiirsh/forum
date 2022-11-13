package telran.java2022.security.context;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import telran.java2022.account.utils.Role;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(of = "userName")
public class UserContext {
  String userName;
  String password;
  @Singular
  Set<Role> roles;

}
