package com.ezio.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ezio.Modal.Student;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {

	// save standards data this is for student fees details incomplate
	@Query(value = "select * from student where id=:id", nativeQuery = true)
	String findByPaidFess(Long id);



}
