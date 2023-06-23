package com.devnatao.crud.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devnatao.crud.model.UserModel;
import com.devnatao.crud.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private PasswordEncoder encode;
	
	@PostMapping
	public ResponseEntity<UserModel> create(@RequestBody UserModel data) {
		data.setPassword(encode.encode(data.getPassword()));
		return ResponseEntity.ok().body(repository.save(data));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserModel> findById(@PathVariable Long id) {
		Optional<UserModel> user = repository.findById(id);
		return ResponseEntity.ok(user.get());
	}
	
	@GetMapping
	public ResponseEntity<List<UserModel>> findAll() {
		return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<UserModel> update(@PathVariable Long id, @RequestBody UserModel data) {
		data.setId(id);
		data.setPassword(encode.encode(data.getPassword()));
		var entity = repository.save(data);
		return ResponseEntity.ok(entity);
	}
	
	@GetMapping("/verify")
	public ResponseEntity<Boolean> verificator(@RequestParam String login, @RequestParam String password) {
		Optional<UserModel> optionalUser = repository.findUserByLogin(login);
		
		if (optionalUser.isEmpty()) 
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
		
		boolean isPasswordValid = encode
					.matches(password, optionalUser.get().getPassword());
		
		HttpStatus status = isPasswordValid ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
		return ResponseEntity.status(status).body(isPasswordValid);
	}

}
