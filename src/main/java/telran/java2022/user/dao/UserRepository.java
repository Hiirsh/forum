package telran.java2022.user.dao;

import org.springframework.data.repository.CrudRepository;

import telran.java2022.user.model.User;


public interface UserRepository extends CrudRepository<User, String> {
  void deleteById(String login);

}
