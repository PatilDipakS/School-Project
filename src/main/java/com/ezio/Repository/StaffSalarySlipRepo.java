package com.ezio.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ezio.Modal.StaffSalarySlip;

@Repository
public interface StaffSalarySlipRepo extends JpaRepository<StaffSalarySlip, Long>{

	@Query(value = "select * from schoolmanagement_db.staff_salary_slip where staff_id =:StaffId And salary_month =:salaryMonth",nativeQuery = true)
	StaffSalarySlip findByStaffSalaryData(Long StaffId, String salaryMonth);

	@Modifying
	@Transactional
	@Query("update StaffSalarySlip set absentDay=:absentDay, workDay=:workDay, salaryDate=:dateOfPayment where staffId =:StaffId And salaryMonth =:salaryMonth")
	void updateSalarySlipData(Long StaffId, String salaryMonth, Long absentDay, Long workDay, String dateOfPayment);

}
