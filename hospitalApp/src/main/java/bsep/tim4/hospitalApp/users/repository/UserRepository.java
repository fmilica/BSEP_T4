package bsep.tim4.hospitalApp.users.repository;

import bsep.tim4.hospitalApp.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	List<User> findAll();
	
    User findOneByEmail(String email);

}
