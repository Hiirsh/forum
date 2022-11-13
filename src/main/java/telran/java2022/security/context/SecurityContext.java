package telran.java2022.security.context;

public interface SecurityContext {
  UserContext addUser(UserContext user);

  UserContext removeUSer(String userName);

  UserContext getUser(String userName);

}
