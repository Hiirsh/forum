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
import telran.java2022.forum.dao.ForumRepository;
import telran.java2022.forum.model.Post;
import telran.java2022.security.context.SecurityContext;
import telran.java2022.security.context.UserContext;

@Order(30)
@RequiredArgsConstructor
@Component
public class OwnerOrModeratorFilter implements Filter {
  final ForumRepository forumRepository;
  final SecurityContext context;

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain nexFilterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    if (checkEndPoint(request.getMethod(), request.getServletPath())) {
      String userId = request.getUserPrincipal().getName();
      UserContext user = context.getUser(userId);
      if (!("DELETE".equalsIgnoreCase(request.getMethod()) && user.getRoles().contains(Role.MODERATOR))) {
        String[] arr = request.getServletPath().split("/");
        // String postId = request.getServletPath().split("/")[3];
        String postId = arr[arr.length - 1];
        Post post = forumRepository.findById(postId).orElse(null);
        if (post == null) {
          response.sendError(404);
          return;
        }
        if (!post.getAuthor().equalsIgnoreCase(userId)) {
          response.sendError(403);
          return;
        }
      }
    }
    nexFilterChain.doFilter(request, response);
  }

  private boolean checkEndPoint(String method, String servletPath) {
    return (("PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method))
        && servletPath.matches("/forum/post/\\w+/?"));
  }
}
