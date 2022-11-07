package telran.java2022.user.dto.extentions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Role not exist")
public class RoleNotExistsExeption extends IllegalArgumentException {

  public RoleNotExistsExeption(String role) {
    super("Role \"" + role + "\" not exist");
  }
}
