package telran.java2022.security.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import telran.java2022.account.dao.UserRepository;
import telran.java2022.account.dto.extentions.UserNotFoundExeprion;
import telran.java2022.account.model.User;

@Component
@AllArgsConstructor
public class AuthenticationFilter implements Filter {
  final UserRepository repository;

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain next)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    if (checkEndPoint(request.getMethod(), request.getServletPath())) {
      String token = request.getHeader("Authorization");
      if (token == null) {
        response.sendError(401);
        return;
      }
      String[] credentials = getCredentialsFromToken(token);
      User user = repository.findById(credentials[0])
          .orElseThrow(() -> new UserNotFoundExeprion(credentials[0]));
      if (!user.getPassword().equals(credentials[1])) {
        // throw new UnauthorizedException();
        response.sendError(404, "User not found");
        return;
      }
      next.doFilter(request, response);
    }
    next.doFilter(request, response);
  }

  private String[] getCredentialsFromToken(String token) {
    String basicAuth = token.split(" ")[1];
    String decode = new String(Base64.getDecoder().decode(basicAuth));
    return decode.split(":");
  }

  private boolean checkEndPoint(String method, String servletPath) {
    return !(("POST".equalsIgnoreCase(method) && servletPath.equals("/account/register")) ||
        "GET".equalsIgnoreCase(method) ||
        ("POST".equalsIgnoreCase(method) && servletPath.startsWith("/forum/posts")));
  }

}
