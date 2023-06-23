package com.devnatao.crud.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devnatao.crud.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long>{

	public Optional<UserModel> findUserByLogin(String login);
}
