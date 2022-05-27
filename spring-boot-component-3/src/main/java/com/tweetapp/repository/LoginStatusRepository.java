package com.tweetapp.repository;

import com.tweetapp.model.LoginStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginStatusRepository extends JpaRepository<LoginStatus, Long> {

}
