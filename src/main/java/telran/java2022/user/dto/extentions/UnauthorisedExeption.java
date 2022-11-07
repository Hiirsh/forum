package telran.java2022.user.dto.extentions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Login or passord wrong")
public class UnauthorisedExeption extends RuntimeException {
}
