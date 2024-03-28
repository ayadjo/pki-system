package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByMail(String mail);
}
