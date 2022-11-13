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
import lombok.RequiredArgsConstructor;
import telran.java2022.account.utils.Role;
import telran.java2022.security.context.SecurityContext;
import telran.java2022.security.context.UserContext;

@Order(20)
@RequiredArgsConstructor
@Component
public class AdminFilter implements Filter {
  final SecurityContext context;

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain nexFilterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    if (checkEndPoint(request.getMethod(), request.getServletPath())) {
      UserContext user = context.getUser(request.getUserPrincipal().getName());
      if (!user.getRoles().contains(Role.ADMIN)) {
        response.sendError(403);
        return;
      }
    }
    nexFilterChain.doFilter(request, response);
  }

  private boolean checkEndPoint(String method, String servletPath) {
    return servletPath.matches("/account/user/\\w+/role/\\w+/?");
  }
}
