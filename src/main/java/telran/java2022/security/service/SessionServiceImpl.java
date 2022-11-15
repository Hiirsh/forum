package telran.java2022.security.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import telran.java2022.account.dao.UserRepository;
import telran.java2022.account.model.User;

@Service
public class SessionServiceImpl implements SessionService, CommandLineRunner {
  Map<String, User> users = new ConcurrentHashMap<>();
  @Autowired
	UserRepository repository;

  @Override
  public User addUser(String sessionId, User user) {
    return users.put(sessionId, user);
  }

  @Override
  public User getUser(String sessionId) {
    return users.get(sessionId);
  }

  @Override
  public User removeUser(String sessionId) {
    return users.remove(sessionId);
  }

  @Override
  public void run(String... args) throws Exception {
    if (!repository.existsById("admin")) {
      String password = BCrypt.hashpw("admin", BCrypt.gensalt());
      User user = new User("admin", password, "admin", "admin");
      user.addRole("admin");
      user.addRole("moderator");
      repository.save(user);
    }
  }
}
