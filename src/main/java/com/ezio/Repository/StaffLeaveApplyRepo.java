package com.ezio.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ezio.Modal.StaffLeaveApply;

import jakarta.transaction.Transactional;

@Repository
public interface StaffLeaveApplyRepo extends JpaRepository<StaffLeaveApply, Long> {
	
	
	// Method for Count Staff pending Leave
    @Query("SELECT COUNT(id) FROM StaffLeaveApply WHERE status = :status")
	Long countLeave(String status);

	// This is use to show leave details
	List<StaffLeaveApply> findAllByStaffId(Long staffId);

	// code for Approved and rejected leave status for admin leave details table
	@Modifying
	@Transactional
	@Query("update StaffLeaveApply set status =:status where id=:id")
	void updateLeaveStatus(Long id, String status);

    @Query("SELECT COUNT(id) FROM StaffLeaveApply WHERE staffId = :staffId AND fromDate = : fromDate")
	String counDateToDateFrom(Long staffId);

	

	
}
