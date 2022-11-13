package telran.java2022.security.filter;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import telran.java2022.account.dao.UserRepository;
import telran.java2022.account.model.User;
import telran.java2022.security.context.SecurityContext;
import telran.java2022.security.context.UserContext;

@Component
@AllArgsConstructor
@Order(10)
public class AuthenticationFilter implements Filter {
  final UserRepository repository;
  final SecurityContext context;

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain next)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    if (checkEndPoint(request.getMethod(), request.getServletPath())) {
      String token = request.getHeader("Authorization");
      if (token == null) {
        response.sendError(401, "Login or password invalid.");
        return;
      }
      String[] credentials;
      try {
        credentials = getCredentialsFromToken(token);
      } catch (Exception e) {
        response.sendError(401, "Invalid token.");
        return;
      }
      User user = repository.findById(credentials[0]).orElse(null);

      if (user == null || !BCrypt.checkpw(credentials[1], user.getPassword())) {
        response.sendError(401, "Login or password are invalid");
        return;
      }
      request = new WrappedRequest(request, user.getLogin());
      UserContext userContext = UserContext.builder()
          .userName(user.getLogin())
          .password(user.getPassword())
          .roles(user.getRoles())
          .build();
        context.addUser(userContext);
    }
    next.doFilter(request, response);
  }

  private String[] getCredentialsFromToken(String token) {
    String basicAuth = token.split(" ")[1];
    String decode = new String(Base64.getDecoder().decode(basicAuth));
    return decode.split(":");
  }

  private boolean checkEndPoint(String method, String servletPath) {
    return !(("POST".equalsIgnoreCase(method) && servletPath.matches("/account/register/?")) ||
        ("POST".equalsIgnoreCase(method) && servletPath.matches("/forum/posts/(tags|period)/?")));
  }

  private class WrappedRequest extends HttpServletRequestWrapper {
    String login;

    public WrappedRequest(HttpServletRequest request, String login) {
      super(request);
      this.login = login;
    }

    @Override
    public Principal getUserPrincipal() {
      return () -> login;
    }

  }
}
