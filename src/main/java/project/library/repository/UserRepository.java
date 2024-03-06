package project.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.library.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {


    User findByUsername(String username);
    User findById(int id);

    List<User> findAll();
}
