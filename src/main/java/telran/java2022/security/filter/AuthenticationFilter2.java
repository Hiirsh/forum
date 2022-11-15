package telran.java2022.security.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
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
import telran.java2022.security.context.SecurityContext;
import telran.java2022.security.service.SessionService;

@Component
@Order(5)
@AllArgsConstructor
public class AuthenticationFilter2 implements Filter {
  final UserRepository repository;
  final SecurityContext context;
  final SessionService sessionService;

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain next)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    if (request.getHeader("Authorization") != null) {
      String sessionId = request.getSession().getId();
      sessionService.removeUser(sessionId);
    }
    next.doFilter(request, response);
  }
}
