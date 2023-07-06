package com.satyait.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.satyait.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer>{

	
	@Query("select distinct(planName) from Users")
	public List<String> getPlanNames();

	
	@Query("select distinct(planStatus) from Users")
	public List<String> getPlanStatus();
}
