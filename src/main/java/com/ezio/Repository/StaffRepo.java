package com.ezio.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ezio.Modal.Staff;

@Repository
public interface StaffRepo extends JpaRepository<Staff, Long> {

// ------------------------------------------ ***** Login Module// *****------------------------------------------------------//
// Method for valid Login Admin ,Teaching staff Or Non Teaching Staff
	Staff findByEmailAndPassword(String email, String password);
// ------------------------------------------------------------------------------------------------//

// ------------------------------------------ ***** Staff Registration Modal *****------------------------------------------------------//
// method for Show staff registration Details
	@Query(value = "SELECT * FROM Staff WHERE role !=:role", nativeQuery = true)
	List<Staff> findAllStaffDetails(String role);
// ------------------------------------------------------------------------------------------------//

}
