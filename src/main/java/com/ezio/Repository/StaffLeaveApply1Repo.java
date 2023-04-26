package com.ezio.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ezio.Modal.StaffLeaveApply1;

import jakarta.transaction.Transactional;

@Repository
public interface StaffLeaveApply1Repo extends JpaRepository<StaffLeaveApply1, Long> {

	// code for Approved and rejected leave status for StaffLeaveApply1 table
	@Modifying
	@Transactional
    @Query("UPDATE StaffLeaveApply1 SET status = :status WHERE leaveId = :id")
	void updateStatus(Long id, String status);

	
    @Query("SELECT COUNT(id) FROM StaffLeaveApply1 WHERE staffId = :staffId AND status = :leaveStatus")
	Long countApprovedLeaves(Long staffId, String leaveStatus);

}
