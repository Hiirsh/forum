package telran.java2022.security.service;

import telran.java2022.account.model.User;

public interface SessionService {
  User addUser(String sessionId, User user);

  User getUser(String sessionId);

  User removeUser(String sessionId);
}
