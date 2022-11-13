package telran.java2022.security.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class SecurityContextImpl implements SecurityContext {

  Map<String, UserContext> context = new ConcurrentHashMap<>();
  @Override
  public UserContext addUser(UserContext user) {
    return context.put(user.getUserName(), user);
  }

  @Override
  public UserContext removeUSer(String userName) {
    return context.remove(userName);
  }

  @Override
  public UserContext getUser(String userName) {
    return context.get(userName);
  }
  
}
