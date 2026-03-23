package com.padel.repository;
import com.padel.model.entity.User;
import com.padel.model.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMatricule(String matricule);
    List<User> findByUserType(UserType userType);
    List<User> findBySiteId(Long siteId);
    boolean existsByMatricule(String matricule);
    boolean existsByEmail(String email);
}
