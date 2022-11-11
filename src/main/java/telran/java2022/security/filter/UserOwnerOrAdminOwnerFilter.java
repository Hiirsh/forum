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
import telran.java2022.account.model.User;
import telran.java2022.account.utils.Role;
import telran.java2022.forum.dao.ForumRepository;

@Component
@AllArgsConstructor
@Order(50)
public class UserOwnerOrAdminOwnerFilter implements Filter {
  final UserRepository userRepository;
  final ForumRepository forumRepository;

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain nexFilterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    if (checkEndPoint(request.getMethod(), request.getServletPath())) {
      User user = userRepository.findById(request.getUserPrincipal().getName()).get();
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
