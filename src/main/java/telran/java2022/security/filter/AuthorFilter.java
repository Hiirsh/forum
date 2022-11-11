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
import telran.java2022.account.dao.UserRepository;
import telran.java2022.forum.dao.ForumRepository;

@Order(40)
@RequiredArgsConstructor
@Component
public class AuthorFilter implements Filter {
  final UserRepository userRepository;
  final ForumRepository forumRepository;

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain nexFilterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    String path = request.getServletPath();
    String method = request.getMethod();
    if (checkEndPoint(method, path)) {
      String user = request.getUserPrincipal().getName();
      String author = path.split("/")["POST".equalsIgnoreCase(method) ? 3 : 5];
      if (!user.equals(author)) {
        response.sendError(403, "Wrong user");
        return;
      }
    }
    nexFilterChain.doFilter(request, response);
  }

  private boolean checkEndPoint(String method, String servletPath) {
    return ("POST".equalsIgnoreCase(method) && servletPath.matches("/forum/post/\\w+/?")) ||
        ("PUT".equalsIgnoreCase(method) && servletPath.matches("/forum/post/\\w+/comment/\\w+/?"));
  }
}
