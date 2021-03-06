package com.example.tttn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.tttn.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByUsername(String userName);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	
	@Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
	public User findByVerificationCode(String code);
	
	@Query("SELECT u FROM User u WHERE u.firstName = ?1")
	List<User> searchUserByFirstName(String firstName);
	
	@Query(value = "Select day(u.create_date) from user u where YEAR(u.create_date) = :year and MONTH(u.create_date)= :month group by day(u.create_date)", nativeQuery = true)
	List<Integer> searchDayNumberUserForMonth(long year, long month);
	
	@Query(value = "Select count(u.username) from user u where YEAR(u.create_date) = :year and MONTH(u.create_date)= :month group by day(u.create_date)", nativeQuery = true)
	List<Integer> searchNumberUserForMonth(long year, long month);
}
