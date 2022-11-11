package telran.java2022.account.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import telran.java2022.account.utils.Role;

@Getter
@EqualsAndHashCode(of = "login")
@Document(collection = "users")
public class User {
  @Id
  String login;
  @Setter
  String password;
  @Setter
  String firstName;
  @Setter
  String lastName;
  Set<Role> roles;

  
  public User() {
    roles = new HashSet<>();
    roles.add(Role.USER);
  }

  @Builder
  public User(String login, String password, String firstName, String lastName) {
    this();
    this.login = login;
    this.password = password; // for encription this.password = encrypt(password)
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public boolean addRole(String role) {
    Role newRole = Role.valueOf(role.toUpperCase());
    return this.roles.add(newRole);
  }


  public boolean deleteRole(String role) {
    return this.roles.remove(Role.valueOf(role.toUpperCase()));
  }
}
