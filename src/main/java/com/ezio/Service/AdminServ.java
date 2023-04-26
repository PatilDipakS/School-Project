package com.ezio.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezio.Modal.AddHolidays;
import com.ezio.Modal.Staff;
import com.ezio.Modal.RoleEntity;
import com.ezio.Modal.SalaryDetails;
import com.ezio.Modal.SalaryField;
import com.ezio.Modal.StaffAttendanceEntity;
import com.ezio.Modal.StaffLeaveApply;
import com.ezio.Modal.StaffLeaveApply1;
import com.ezio.Modal.StaffSalarySlip;
import com.ezio.Modal.Student;
import com.ezio.Modal.standardEntity;
import com.ezio.Repository.AddHolidayRepo;
import com.ezio.Repository.StaffRepo;
import com.ezio.Repository.StaffSalarySlipRepo;
import com.ezio.Repository.RoleRepo;
import com.ezio.Repository.SalaryDetailsRepo;
import com.ezio.Repository.SalaryFieldRepo;
import com.ezio.Repository.StaffAttendanceRepo;
import com.ezio.Repository.StaffLeaveApply1Repo;
import com.ezio.Repository.StaffLeaveApplyRepo;
import com.ezio.Repository.StandardRepo;
import com.ezio.Repository.StudentRepo;

@Service
public class AdminServ {

	@Autowired
	StaffRepo staffRepo;

	@Autowired
	StaffLeaveApplyRepo leaveApplyRepo;

	@Autowired
	StaffLeaveApply1Repo leaveApply1Repo;

	@Autowired
	RoleRepo roleRepo;

	@Autowired
	StaffAttendanceRepo StffAttenRepo;

	@Autowired
	StandardRepo standardRepo;

	@Autowired
	StudentRepo studentRepo;

	@Autowired
	SalaryFieldRepo salaryFieldRepo;

	@Autowired
	SalaryDetailsRepo salaryDetailsRepo;

	@Autowired
	AddHolidayRepo holidaysRepo;

	@Autowired
	StaffSalarySlipRepo staffSalarySlipRepo;

// ---------------------------------------------------------------------------------------------------------------------------//
// ------------------------------------------ ***** Login Module *****---------------------------------------------------------------------------------//

// Method for valid Login Admin ,Teaching staff Or Non Teaching Staff 
	public Staff validatedAdmin(String email, String password) {
		return staffRepo.findByEmailAndPassword(email, password);
	}

// ---------------------------------------------------------------------------------------------------------------------------//
// ------------------------------------------ ***** DashBord  *****-------------------------------------------------------//
	// Method for Count Student Registration
	public Long countStudent() {
		return studentRepo.count();
	}

	// Method for Count Staff Registration
	public Long countStaff() {
		return staffRepo.count();
	}

	// Method for Count Staff pending Leave
	public Long countStaffLeave(String status) {
		return leaveApplyRepo.countLeave(status);
	}
// -------------------------------------------------Staff Modal--------------------------------------------------------------------------//
// ------------------------------------------ ***** Staff Registration Model  *****-------------------------------------------------------//

// Method for add staff and save Document files
	public void addStaff(Staff ob) {
		staffRepo.save(ob);
	}

// method for for Show staff registration Details 
	public List<Staff> findAllStaffDetailsTable() {
		String role = "Admin";
		return staffRepo.findAllStaffDetails(role);
	}

	// get Staff Data For edit Modul
	public Staff findstaffId(Long id) {

		return staffRepo.findById(id).get();
	}

	// method for delete Staff

	public void deleteByStaffId(Long id) {
		staffRepo.deleteById(id);
	}

// --------------------------------------------------Staff attendance Details Modal-------------------------------------------------------------------------//

	// Method for search name ,single date attendance and between date attendance
	// search name
	public List<StaffAttendanceEntity> searchName(String searchVal) {
		return StffAttenRepo.findByStaffName(searchVal);
	}

	// search name and To date
	public List<StaffAttendanceEntity> searchNameAndToDate(String searchVal, String newToDate) {
		return StffAttenRepo.findByNameAndToDate(searchVal, newToDate);
	}

	// search name and To date
	public List<StaffAttendanceEntity> searchNameAndFromDate(String searchVal, String newFromDate) {
		return StffAttenRepo.findNameAndFromDate(searchVal, newFromDate);
	}

	// search name To date and form date
	public List<StaffAttendanceEntity> searchNameToDateAndFromDate(String searchVal, String newToDate,
			String newFromDate) {
		return StffAttenRepo.findNameAndFromDate(searchVal, newToDate, newFromDate);
	}

	// to date
	public List<StaffAttendanceEntity> toDate(String newToDate) {
		return StffAttenRepo.findByToDate(newToDate);
	}

// teacher get and name data for leave modal
	public Staff findstaffIdAndName(Long id) {
		return staffRepo.findById(id).get();
	}

//---------------------------------------------Leave Details Modal-----------------------------------------------------------------------------------------------------------------------------------
	// save Staff leave Application Data
	public void saveLeaveApplyData(StaffLeaveApply leave) {
		leaveApplyRepo.save(leave);
	}

	// save Staff leave Application Data but another for calculation purpose
	public void saveLeaveApply1Data(StaffLeaveApply1 leaveObject) {
		leaveApply1Repo.save(leaveObject);
	}

	// code for Show staff leave Details table in Staff Page
	public List<StaffLeaveApply> getStaffLeaveById(Long staffId) {
		// System.err.println("service.............." + staffId);
		return leaveApplyRepo.findAllByStaffId(staffId);
	}

	// code for Show staff leave Details table in admin page

	public List<StaffLeaveApply> getStaffLeaveList() {
		return leaveApplyRepo.findAll();
	}

	// code for Approved and rejected leave status for admin leave details table
	public void updateStatus(Long id, String status) {
		// System.err.println(id + " " + status);
		leaveApplyRepo.updateLeaveStatus(id, status);

	}

	// code for Approved and rejected leave status for StaffLeaveApply1 table
	public void updateStatusReferenceTable(Long id, String status) {
		leaveApply1Repo.updateStatus(id, status);

	}

	public List<String> ShowHolidayTableDateList() {
		return holidaysRepo.findByHolidayDate();
	}

	// code for check holiday dates and substract
	public String countHoliday(String dateFrom) {
		return holidaysRepo.findByDate(dateFrom);
	}

	// code for check punch dates and substract
	public String checkPunchdate(String dateFrom, Long staffId) {
		return StffAttenRepo.findByDate(dateFrom, staffId);
	}

//----------------------------------------------------------------------------------------------------------------------------------
	// code for Add Role Dynamically by admin
	public void saveRole(RoleEntity role) {
		// System.err.println("save serv.... " + role);
		roleRepo.save(role);
	}

	// Code for show role data for table
	public List<RoleEntity> findAllRoleData() {
		// System.err.println("get list serv.... ");
		return roleRepo.findAll();
	}

	// code for admin get the role data in edit modal
	public RoleEntity findRoleId(Long id) {
		// System.err.println("edit.... serv " +id);
		return roleRepo.findById(id).get();
	}

	// code for update edit role modal data
	public void upadateRoleData(RoleEntity role) {
		// System.err.println("update serv.... " + role);
		roleRepo.save(role);
	}

	// code for delete role data
	public void deleteRoleData(Long id) {
		// System.err.println("delete serv.... " + id);
		roleRepo.deleteById(id);
	}

	// show the only selected data on role details page in setting role modal
	public List<RoleEntity> FindByRoleStatus(String status) {
		// System.err.println("serv......"+status);
		return roleRepo.findByStatus(status);
	}

	// code for get Leave type data for leave Application Modal
	public List<RoleEntity> FindByLeaveType(String status) {

		// System.err.println("call serv........" + status);
		return roleRepo.findByStatus(status);

	}

	// code for add role of staff details form /showRoleAdd-API
	public List<RoleEntity> getRoleAdded(String status) {

		// System.err.println("service call .." + status);

		return roleRepo.findByStatus(status);

	}

	// code for get dynamically added designation for staff details
	public List<RoleEntity> getDesignationAdded(String status) {
		return roleRepo.findByStatus(status);
	}

	// ---------------------------------------------------------------------------------------------------------------------------//

	// API for Show attendance Details by staff
	public List<StaffAttendanceEntity> FindStaffPunchInAndOutTable() {
		return StffAttenRepo.findAll();
	}

	// This Is API for Show Atttendence details according to staff id by Staff
	public List<StaffAttendanceEntity> getStaffAttendenceById(Long staffId) {
		return StffAttenRepo.findByStaffId(staffId);
	}
	// ---------------------------------------------------------------------------------------------------------------------------//

//----------------------------------------------------------------------------------------------------------------------------------------------------------------//
	// This methods for staff attendance punch in and out

	// method for to check date and Staff Id in SytaffAttendenceEntity Data table
	public StaffAttendanceEntity loginCheck(String date, Long staffId) {
		// System.err.println(date+" "+staffId);
		return StffAttenRepo.findByDateAndStaffId(date, staffId);
	}

	// method for Save punch In and Out data
	public void saveAttendance(StaffAttendanceEntity attendence, String status) {
		StffAttenRepo.save(attendence);
		// System.err.println("service ....." + attendence + " " + status);
	}

	// methods for Update Punch Out time
	public void SavePunchOut(String punchOut, Long staffid, String newdate, String status) {
		StffAttenRepo.UpdateOutTime(punchOut, staffid, newdate, status);

	}

	// methods for calculate total working hours
	public String calculateTime(Long staffid, String newdate) {

		return StffAttenRepo.calTotalHr(staffid, newdate);

	}

	// Methods For Update Total Hours
	public void SaveTotalhr(String totalhr, Long staffid, String newdate) {

		StffAttenRepo.UpdateTotalHr(totalhr, staffid, newdate);

	}
//-----------------------------------------------------------------------------------------------------------------------------------------------------//

	// API for Punching Buttons hide
	public StaffAttendanceEntity statusCheck(Long staffId, String date) {
		// System.err.println("Service call Punching Buttons");
		return StffAttenRepo.findByStaffIdAndDate(staffId, date);
	}

//-------------------------------------------------------------------------------------------------------------------------------------------------

	// API for Staff Staff check Attendance According to select date
	/// find singale date
	public List<StaffAttendanceEntity> ToDate(Long staffId, String newToDate) {

		// System.err.println("1 service call...");
		return StffAttenRepo.findNewDate(staffId, newToDate);
	}

	// method for find date between
	public List<StaffAttendanceEntity> ToDateFromDate(Long staffId, String newToDate, String newFromDate) {
		// System.err.println("2 service call...");
		return StffAttenRepo.findDateBetween(staffId, newToDate, newFromDate);
	}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//code start for add settings Standards filed

	// code for save Standards
	public void saveStandards(standardEntity standards) {
		standardRepo.save(standards);
	}

	// show standards table data
	public List<standardEntity> showStandardsTable() {
		return standardRepo.findAll();
	}

	public standardEntity findStandardsDataById(Long id) {
		return standardRepo.findById(id).get();
	}

	public void deleteStandards(Long id) {

		standardRepo.deleteById(id);

	}

	// search standard data for method
	public List<standardEntity> findSearchStandardsDetails(String search) {
		return standardRepo.findByStandards(search);
	}

	// get standard list for selector
	public List<String> findAllStandardsDetailsList() {
		return standardRepo.findByStandards();
	}

	// get Medium list for medium selector in Student registration form
	public List<String> findAllMediumList() {
		return standardRepo.findByMedium();
	}

	// get Division list for Division selector in Student registration form
	public List<String> findAllDivisonList() {
		return standardRepo.findByDivision();
	}

	// get totalFees list for total fees in Student registration form
	public List<String> findAllTotalFessList() {
		return standardRepo.findByFees();
	}

// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Student Modal Start

	// student Registration Modal
	// Add Student
	public void SaveStudent(Student student) {
		studentRepo.save(student);

	}

	// Show Student detail table
	public List<Student> showStudentTable() {
		return studentRepo.findAll();
	}

	// Edit Modal Data
	public Student getStudentFormData(Long id) {
		return studentRepo.findById(id).get();
	}

	// delete student
	public void deleteStudent(Long id) {
		studentRepo.deleteById(id);

	}

//	//update student fees details
//	public void updateStudentFeesDetails(Student student) {
//		studentRepo.updateStudentFess
//		
//	}

	// save standards data this is for student fees details incomplate
	// get student fees already get
	public String getPaidFess(Long id) {
		return studentRepo.findByPaidFess(id);
	}

	// Student Modal End
// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	// add salary field in setting Field Modal

	public void addSalary(SalaryField salaryField) {

		System.err.println("call serv ............" + salaryField);
		salaryFieldRepo.save(salaryField);

	}

	public List<SalaryField> ShowSalaryFiledList() {
		return salaryFieldRepo.findAll();
	}

	public SalaryField editModalData(Long id) {
		return salaryFieldRepo.findById(id).get();
	}

	public void salaryFieldDeleteByID(Long id) {
		salaryFieldRepo.deleteById(id);
	}

//-----------------------------Add holidays setting filed--------------------------------------------------------------------//
	public void SaveHoliday(AddHolidays holidays) {
		holidaysRepo.save(holidays);
	}

	public List<AddHolidays> ShowHolidayTableDate() {
		return holidaysRepo.findAll();

	}

	public AddHolidays editHolidayModalData(Long id) {
		return holidaysRepo.findById(id).get();
	}

	public void deleteHoliday(Long id) {
		holidaysRepo.deleteById(id);
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------//

	// ---------------------------------- Code for Salary Details Page
	// ------------------------------------------
	// ---------------------------------------- admin
	// Modal------------------------------------------------------------//

	// admin Entity data
	public List<Staff> FindStaffData() {
		return staffRepo.findAll();
	}

	// Count From Date to To Date
	public String getToDate(Long staffId) {
		return leaveApplyRepo.counDateToDateFrom(staffId);
	}

	// count holidays
	public Long countTotalHolidays() {
		return holidaysRepo.count();
	}

	// count Leave
	public Long countTotalLeave() {
		return null;
	}

	// count total Approved leave
	public Long countApprovedLeaves(Long staffId, String leaveStatus) {
		return leaveApply1Repo.countApprovedLeaves(staffId, leaveStatus);
	}

	// count punch Day
	public Long countPunch(Long staffId) {
		return StffAttenRepo.countPunch(staffId);
	}

	// get Basic
	public Long getBasicSalary(String designation) {
		return salaryFieldRepo.findByBasic(designation);
	}

	// get Hra
	public Long getHraSalary(String designation) {
		return salaryFieldRepo.findByHra(designation);
	}

	//
	public Long getAllowanceSalary(String designation) {
		return salaryFieldRepo.findByAdditional(designation);
	}

	public Double getTaxSalary(String designation) {
		return salaryFieldRepo.findByTax(designation);
	}

	public Double getBonusSalary(String designation) {
		return salaryFieldRepo.findByBonus(designation);
	}

	// save Salary
	public void saveSalaryDeatils(SalaryDetails salary) {
		System.err.println("calll service..............");
		salaryDetailsRepo.save(salary);

	}

	public List<SalaryDetails> findSalaryDetails() {

		return salaryDetailsRepo.findAll();
	}

	public String findCurrentMonth(String month, Long staffId) {
		return salaryDetailsRepo.findByMonth(month, staffId);
	}

	public Long getSalaryId(Long staffId, String monthName) {
		return salaryDetailsRepo.findBySalaryId(staffId, monthName);
	}

	public void updateSalaryDeatils(Long staffId, String months, Long workDay, Long absentDay, String earnForm,
			String deductForm, Double tax, Double bonus, String grossSalForm, String netSalaryBonusForm) {
		salaryDetailsRepo.updateSalary(staffId, months, workDay, absentDay, earnForm, deductForm, tax, bonus,
				grossSalForm, netSalaryBonusForm);

	}

//		public void updateSalary(SalaryDetails salary ,Long salaryIDs) {
//			salaryDetailsRepo.updateSalary(salary,salaryIDs)	;
//
//		}

//------------------------------------------- salary Slip data-------------------------------------------------------

	public List<SalaryDetails> getStaffSalarySlipData(Long id, String name) {
		return salaryDetailsRepo.findByStaffName(id, name);
	}

	public SalaryDetails getStaffSalarySlipData(String month, Long StaffId, String StaffName) {
		return salaryDetailsRepo.findByStaffSalaryDetils(month, StaffId, StaffName);
	}

	public void saveSalaryData(StaffSalarySlip object) {

		staffSalarySlipRepo.save(object);

	}

	public StaffSalarySlip getStaffSalaryMonthData(Long StaffId, String salaryMonth) {
		return staffSalarySlipRepo.findByStaffSalaryData(StaffId, salaryMonth);
	}

	public void updateSalaryMonthData(Long StaffId, String salaryMonth, Long absentDay, Long workDay,
			String dateOfPayment) {

		StaffSalarySlip object = new StaffSalarySlip();

		object.setAbsentDay(absentDay);
		object.setWorkDay(workDay);
		object.setSalaryDate(dateOfPayment);

		System.err.println("service..............  " + StaffId + " " + salaryMonth + " " + absentDay + " " + workDay
				+ " " + dateOfPayment);

		staffSalarySlipRepo.updateSalarySlipData(StaffId, salaryMonth, absentDay, workDay, dateOfPayment);

		// return staffSalarySlipDate;
	}

	public String getStaffDesignation(Long staffId, String salaryMonth) {
		return salaryDetailsRepo.findByDesignation(staffId, salaryMonth);
	}

	public SalaryField getDataUsingDesignation(String designation) {
		return salaryFieldRepo.findByDesignation(designation);
	}

}
