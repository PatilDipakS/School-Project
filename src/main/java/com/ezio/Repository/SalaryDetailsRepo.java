package com.ezio.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ezio.Modal.SalaryDetails;

import jakarta.transaction.Transactional;

@Repository
public interface SalaryDetailsRepo extends JpaRepository<SalaryDetails, Long> {

	@Query("select month from SalaryDetails Where month=:month And staffId=:staffId")
	String findByMonth(String month, Long staffId);

	@Query("select id from SalaryDetails Where staffId=:staffId And month=:monthName")
	Long findBySalaryId(Long staffId, String monthName);

	
	@Modifying
	@Transactional
	@Query("UPDATE SalaryDetails  SET workDay =:workDay, AbsentDay =:absentDay, earn=:earnForm, deduct=:deductForm, tax=:tax, bonus=:bonus, gross=:grossSalForm, net=:netSalaryBonusForm  WHERE staffId = :staffId AND month = :months")
	void updateSalary(Long staffId, String months, Long workDay, Long absentDay, String earnForm, String deductForm, Double tax,
			Double bonus, String grossSalForm, String netSalaryBonusForm);

	//@Query(value = "select * from SalaryDetails Where staffId=:id And staffName=:name",nativeQuery = true)
	
	@Query(value = "select * from schoolmanagement_db.salary_details Where staff_id =:id And staff_name =:name",nativeQuery = true)
	List<SalaryDetails> findByStaffName(Long id, String name);

	@Query(value = "select * from schoolmanagement_db.salary_details Where month =:month And staff_id =:StaffId And staff_name =:StaffName",nativeQuery = true)
	SalaryDetails findByStaffSalaryDetils(String month, Long StaffId, String StaffName);

	@Query("select designation from SalaryDetails Where staffId=:staffId And month=:salaryMonth")
	String findByDesignation(Long staffId, String salaryMonth);



}
