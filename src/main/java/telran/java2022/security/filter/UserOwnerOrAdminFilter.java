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
import telran.java2022.account.utils.Role;
import telran.java2022.forum.dao.ForumRepository;
import telran.java2022.security.context.SecurityContext;
import telran.java2022.security.context.UserContext;

@Component
@AllArgsConstructor
@Order(50)
public class UserOwnerOrAdminFilter implements Filter {
  final ForumRepository forumRepository;
  final SecurityContext context;

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain nexFilterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    if (checkEndPoint(request.getMethod(), request.getServletPath())) {
      UserContext user = context.getUser(request.getUserPrincipal().getName());

      if (!("DELETE".equalsIgnoreCase(request.getMethod()) && user.getRoles().contains(Role.ADMIN))) {
        String login = request.getServletPath().split("/")[3];
        
        if (!(login.equals(request.getUserPrincipal().getName()))) {
          response.sendError(403);
          return;
        }
      }
    }
    nexFilterChain.doFilter(request, response);
  }

  private boolean checkEndPoint(String method, String servletPath) {
    return (("PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method))
        && servletPath.matches("/account/user/\\w+/?"));
  }

}
