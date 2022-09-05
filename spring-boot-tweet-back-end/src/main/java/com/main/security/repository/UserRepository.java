package com.main.security.repository;

import com.main.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Modifying
    @Query(value = "DELETE FROM lw_user WHERE user_name = ?1", nativeQuery = true)
    void deleteSpecificUser(String userName);

    @Query("SELECT u FROM User u WHERE u.username = ?1 AND u.systemUser =?2")
    User findUserForNotAdmin(String username, String systemUser);
}