package telran.java2022.account.dto.extentions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User already exists")
@NoArgsConstructor
public class UserAlreadyExests extends RuntimeException {

}
