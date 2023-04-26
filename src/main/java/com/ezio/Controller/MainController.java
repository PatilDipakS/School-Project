package com.ezio.Controller;

import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
import com.ezio.Service.AdminServ;
import com.ezio.common.RegistrationCountResponse;
import com.ezio.common.salarySlipHelper;

import jakarta.servlet.http.HttpSession;

@Controller

public class MainController {

	@Autowired
	AdminServ serv;

// This is use for upload Documents files,img or etc
	String uploadProductDirectory = System.getProperty("user.dir") + "/uploads/";

// ---------------------------------------------------------------------------------------------------------------------------//
// ------------------------------------------ ***** Login Module *****-------------------------------------------------------//
	// API for Login Page call
	@GetMapping("/")
	public String AdminPage() {
		return "admin/common/login";
	}

	// API for valid Login Admin ,Teaching staff Or Non Teaching Staff
	@RequestMapping(value = "/valid-LogIn-API", method = RequestMethod.POST)
	public String validLogIn(HttpSession session, @RequestParam("email") String email,
			@RequestParam("password") String password, Model model) {

		Staff staff = serv.validatedAdmin(email, password);

		if (staff != null) {

			if (staff.getStatus().equals("Active") && staff.getRole().equals("Admin")) {

				session.setAttribute("email", staff.getEmail());
				session.setAttribute("password", staff.getPassword());

				session.setAttribute("role", staff.getRole());
				session.setAttribute("id", staff.getId());

				// model.addAttribute("Log in", adminEntity.getId());

				return "admin/dashboard";

			} else if (staff.getStatus().equals("Active") && staff.getRole().equals("Teaching")) {
				session.setAttribute("email", staff.getEmail());
				session.setAttribute("password", staff.getPassword());

				// this is get form user id and name for leave application
				session.setAttribute("id", staff.getId());
				session.setAttribute("name", staff.getName());
				session.setAttribute("role", staff.getRole());
				
				session.setAttribute("designation", staff.getDesignation());


				// model.addAttribute("Log in", adminEntity.getId());

				return "staff/staffAttendance";

			} else if (staff.getStatus().equals("Active") && staff.getRole().equals("Non Teaching")) {
				session.setAttribute("email", staff.getEmail());
				session.setAttribute("password", staff.getPassword());

				// this is get form user id and name for leave application
				session.setAttribute("id", staff.getId());
				session.setAttribute("name", staff.getName());
				session.setAttribute("role", staff.getRole());
				session.setAttribute("designation", staff.getDesignation());


				// model.addAttribute("Log in", adminEntity.getId());

				return "staff/staffAttendance";
			}

		} else {

			return "redirect:/";
		}
		return "redirect:/";
	}
// ---------------------------------------------------------------------------------------------------------------------------//

// ---------------------------------------------------------------------------------------------------------------------------//
// ------------------------------------------ ***** DashBord  *****-------------------------------------------------------//
	// API For a call DashBoard page
	@RequestMapping("/dashboard-API")
	public String adminDashboard() {

		return "admin/dashboard.html";
	}

	
	// API for Count Student Registration
	@RequestMapping(value = "/count-registarion-API", method = RequestMethod.POST)
	@ResponseBody
	public RegistrationCountResponse CountRegistration() {
		Long studentCount = serv.countStudent();
		Long staffCount = serv.countStaff();
		Long totalCount = studentCount + staffCount;

		String status = "Pending";
		Long staffLeaveCount = serv.countStaffLeave(status);

		RegistrationCountResponse count = new RegistrationCountResponse();

		count.setStaffCount(staffCount);
		count.setStudentCount(studentCount);
		count.setTotalCount(totalCount);
		count.setStaffLeaveCount(staffLeaveCount);

		return count;

	}
// ----------------------------------------------------Staff Modal-----------------------------------------------------------------------//
// ------------------------------------------ ***** Staff registration  *****-------------------------------------------------------//	

    // API for Staff Registration Details page call
	@RequestMapping("/staff-Details-API")
	public String newStaff() {

		return "admin/staffRegistration.html";
	}

	// API for add staff and save Document files
	@RequestMapping(value = "/add-Staff-API", method = RequestMethod.POST)
	@ResponseBody

	public boolean addStaff(Staff staff, BindingResult bindingResult,
			@RequestParam("documents") MultipartFile documents, HttpSession session) throws IOException {

		if (!documents.isEmpty() && documents != null) {

			String dateName = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());

			String originalFileName = dateName + "-" + documents.getOriginalFilename().replace(" ", "-").toLowerCase();

			Path fileNameAndPath = Paths.get(uploadProductDirectory, originalFileName);

			try {
				Files.write(fileNameAndPath, documents.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			staff.setDocuments(originalFileName);
		} else {
			staff.setDocuments(" ");
		}
		serv.addStaff(staff);
		return true;
	}
	

	// API for Show staff registration Details
	@RequestMapping(value = "/show-staff-details-table-API", method = RequestMethod.POST)
	@ResponseBody
	public List<Staff> show() {
		List<Staff> list = serv.findAllStaffDetailsTable();
		return list;
	}

	
	
	// API for Get Staff Data in Edit modal form
	@RequestMapping(value = "/edit-staff-API", method = RequestMethod.POST)
	@ResponseBody
	public Staff edit(Long id) {
		return serv.findstaffId(id);
	}

	// API for update Staff data
	@RequestMapping(value = "/update-staff-API", method = RequestMethod.POST)
	@ResponseBody
	public boolean updateStaffr(Staff staff, BindingResult bindingResult,
			@RequestParam("documents") MultipartFile documents, HttpSession session) throws IOException {

		if (!documents.isEmpty() && documents != null) {

			String dateName = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());

			String originalFileName = dateName + "-" + documents.getOriginalFilename().replace(" ", "-").toLowerCase();
			Path fileNameAndPath = Paths.get(uploadProductDirectory, originalFileName);
			try {
				Files.write(fileNameAndPath, documents.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			staff.setDocuments(originalFileName);
		} else {

			staff.setDocuments(" ");
		}
		serv.addStaff(staff);

		return true;
	}

	// API for delete Staff
	@RequestMapping(value = "/delete-staff-API", method = RequestMethod.POST)
	@ResponseBody
	public void delete(Long id) {
		serv.deleteByStaffId(id);
	}
	
	
	
	
	
	
	
	
	
	

// --------------------------------------------------Staff attendance Details Modal-------------------------------------------------------------------------//

	/// API for Get Staff Details
	@RequestMapping(value = "/getStaffList-API", method = RequestMethod.POST)
	@ResponseBody
	public List<Staff> getStaffList() {
		List<Staff> list = serv.findAllStaffDetailsTable();
		return list;
	}

	// API for search name ,single date attendance and between date attendance
	@RequestMapping(value = "/admin-dateFilter-API", method = RequestMethod.POST)
	@ResponseBody
	public List<StaffAttendanceEntity> searchAndFindDates(HttpSession session, String newToDate, String newFromDate,
			String searchVal) {

		if (searchVal != null) {

			if (newToDate.isEmpty() && newFromDate.isEmpty()) {

				List<StaffAttendanceEntity> list = serv.searchName(searchVal);

				return list;

			} else if (newToDate != null && newFromDate.isEmpty()) {

				List<StaffAttendanceEntity> list = serv.searchNameAndToDate(searchVal, newToDate);

				return list;

			} else if (newFromDate != null && newToDate.isEmpty()) {

				List<StaffAttendanceEntity> list = serv.searchNameAndFromDate(searchVal, newFromDate);
				return list;

			} else if (newFromDate != null && newToDate != null) {

				List<StaffAttendanceEntity> list = serv.searchNameToDateAndFromDate(searchVal, newToDate, newFromDate);
				return list;

			}
		}
		return null;

	}
// --------------------------------------------------Leave Details Modal-------------------------------------------------------------------------//

	// API for Call Staff Leave Page by Staff
	@RequestMapping("/staff-leave-apply-API")
	public String leaveApply() {

		return "staff/leaveApplication.html";
	}
	
	// API for Get Staff leave data Details for admin
	@RequestMapping(value = "/show-staff-Leave-Details", method = RequestMethod.POST)
	@ResponseBody
	public List<StaffLeaveApply> showLeaveDetails() {

		List<StaffLeaveApply> list = serv.getStaffLeaveList();

		return list;
	}

//	API for Approved and rejected leave status
	@RequestMapping(value = "/approved-leave", method = RequestMethod.POST)
	@ResponseBody
	public void leaveApproved(Long id, String status) {

		serv.updateStatus(id, status);

		// method for Approved and rejected leave status for StaffLeaveApply1 table
		serv.updateStatusReferenceTable(id, status);

	}

//---------------------------------- Salary Calculation Details  ------------------------------------------

		@RequestMapping(value = "/Salary-Deatils-API")
		@ResponseBody
		public void showStaffData(String month) {

			List<Staff> staff = serv.FindStaffData();

			synchronized (staff) {

				for (Staff staffs : staff) {

					Long staffId = staffs.getId();
					String staffName = staffs.getName();
					System.out.println("staffName: " + staffName);

					String leaveStatus = "Approved";

					// Long totalDaysInMonth = 30L;
					// System.out.println("total Days In Month : " + totalDaysInMonth);

					Month currentMonth = Month.values()[java.time.LocalDate.now().getMonthValue() - 1];
					String monthName = currentMonth.getDisplayName(TextStyle.FULL, Locale.getDefault());
					// String monthName = "April";

					Long countHoliday = serv.countTotalHolidays();
					// System.out.println("Holiday: " + countHoliday);

					Long countLeave = serv.countApprovedLeaves(staffId, leaveStatus);
					// System.out.println("total Approved Leave: " + countLeave);

					Long countSundays = 0L;
					Calendar calendar = Calendar.getInstance();
					for (int i = 1; i <= 30; i++) {

						calendar.set(Calendar.DAY_OF_MONTH, i);

						if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
							countSundays++;
						}
					}
					// System.out.println("Sundays: " + countSundays);

					Long countPunchDay = serv.countPunch(staffId);
					// System.out.println("Punch Day: " + countPunchDay);

					Long workDay = countHoliday + countSundays + countLeave + countPunchDay;
					// System.err.println("WorkDay: " + workDay);

					Long absentDay = 30 - workDay;
					// System.err.println("absent Day: " + absentDay);

					String designation = staffs.getDesignation();

					DecimalFormat decimalFormat = new DecimalFormat("#.##");
					decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

					Long basic = serv.getBasicSalary(designation);
					if (basic == null) {
						basic = 0L;
					}
					// System.out.println("basic : " + basic + " " + designation);

					Long hra = serv.getHraSalary(designation);
					if (hra == null) {
						hra = 0L;
					}
					// System.out.println("hra : " + hra + " " + designation);

					Long allowance = serv.getAllowanceSalary(designation);
					if (allowance == null) {
						allowance = 0L;
					}
					// System.err.println("Allowance : " + allowance + " " + designation);

					Double tax = serv.getTaxSalary(designation);
					// String taxSalForm = decimalFormat.format(tax);
					System.err.println("call");

					if (tax == null) {
						tax = 0.0;
					}
					System.err.println("tax : " + tax + " " + designation);

					Double bonus = serv.getBonusSalary(designation);
					// String bonusSalForm = decimalFormat.format(bonus);

					if (bonus == null) {
						bonus = 0.0;
					}
					// System.err.println("Allowance : " + bonus + " " + designation);

					Double grossSal = (double) (basic + hra + allowance + tax);
					String grossSalForm = decimalFormat.format(grossSal);
					// System.err.println("Gross Salary : " + grossSal + " " + designation);

					Double netSalary = grossSal - tax;
					String netSalaryForm = decimalFormat.format(netSalary);
					// System.err.println("Net Salary : " + netSalary + " " + designation);

					Double perDaySal = netSalary / 30;

					Double earn = perDaySal * workDay;
					String earnForm = decimalFormat.format(earn);

					Double deduct = netSalary - earn;
					String deductForm = decimalFormat.format(deduct);

					Double netSalaryBonus = earn + bonus;
					String netSalaryBonusForm = decimalFormat.format(netSalaryBonus);

					// System.err.println("Net Salary + bonus : " + netSalary + " " + designation);

					// System.out.println("New ......... record Salary : ");

					SalaryDetails salary = new SalaryDetails();
					Long salaryId = serv.getSalaryId(staffId, monthName);

					// System.out.println("salary Id : "+salaryId);

					String months = serv.findCurrentMonth(month, staffId);
					// System.err.println("CurrentMonth controller : " + months);

					if (salaryId == null) {
						salary.setStaffId(staffId);
						salary.setStaffName(staffName);
						salary.setDesignation(designation);
						salary.setWorkDay(workDay);
						salary.setMonth(monthName);
						salary.setAbsentDay(absentDay);
						salary.setEarn(earnForm);
						salary.setDeduct(deductForm);
						salary.setTax(tax);
						salary.setBonus(bonus);
						salary.setGross(grossSalForm);
						salary.setNet(netSalaryBonusForm);
						serv.saveSalaryDeatils(salary);
						System.out.println("call save...... ");

					} else if (salaryId != null) {

						salary.setWorkDay(workDay);
						salary.setAbsentDay(absentDay);
						salary.setEarn(earnForm);
						salary.setDeduct(deductForm);
						salary.setTax(tax);
						salary.setBonus(bonus);
						salary.setGross(grossSalForm);
						salary.setNet(netSalaryBonusForm);
						serv.updateSalaryDeatils(salaryId, months, workDay, absentDay, earnForm, deductForm, tax, bonus,
								grossSalForm, netSalaryBonusForm);

					}

				}
			}

		}

		@RequestMapping(value = "/salary-Details-Data-API", method = RequestMethod.POST)
		@ResponseBody
		public List<SalaryDetails> showSalaryDetails() {

			List<SalaryDetails> list = serv.findSalaryDetails();

			return list;
		}

//---------------------------------------------------------------------------------------------------------------------------
	
// ---------------------------------------------------------------------------------------------------------------------------//
	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	

	// ---------------------------------------------------------------------------------------------------------------------------//
	// API for Leave Application by Staff
	@RequestMapping(value = "/save-staff-leave-API", method = RequestMethod.POST)
	@ResponseBody
	public void saveLeaveData(StaffLeaveApply leave) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		String fromDate = leave.getFromDate();
		String toDate = leave.getToDate();

		LocalDate fromDates = LocalDate.parse(fromDate, formatter);
		LocalDate toDates = LocalDate.parse(toDate, formatter);

		Long countFromDateToDate = ChronoUnit.DAYS.between(fromDates, toDates);
		Long countFromDateToDates = countFromDateToDate + 1; // total count fromDate to toDate
		leave.setNumLeave(countFromDateToDates);// set Leave count fromDate to ToDate

		LocalDate systemDate = LocalDate.now();
		String applyDate = systemDate.format(formatter);
		leave.setApplyDate(applyDate); // set apply Date using system date

		// System.err.println("Total Leave ToDate to fromDate: " +countFromDateToDates);

		serv.saveLeaveApplyData(leave); // fine code

		// code for count Sunday and sub subtract Sunday
		List<LocalDate> listfromDatesToToDates = new ArrayList<>();

		for (int i = 0; i <= countFromDateToDate; i++) {

			LocalDate dates = fromDates.plusDays(i);

			if (dates.getDayOfWeek() == DayOfWeek.SUNDAY) {

			} else {
				listfromDatesToToDates.add(dates);
			}
		}

		// code for subtract holiday
		List<LocalDate> listfromDatesToToDates2 = new ArrayList<>();

		synchronized (listfromDatesToToDates) {
			for (LocalDate listWithOutsatAndSun : listfromDatesToToDates) {

				String dateFrom = listWithOutsatAndSun.format(formatter);
				// System.err.println("formDate : " +dateFrom);

				String checkHoliday = serv.countHoliday(dateFrom);
				// System.out.println("checkHoliday : " + checkHoliday);

				LocalDate dates = LocalDate.parse(dateFrom);
				// System.out.println("checkHoliday : " + dates);

				if (checkHoliday == null) {

					// System.err.println("if checkHoliday " + checkHoliday);
					listfromDatesToToDates2.add(dates);
				}
			}
		}

		// code for punchDay
		List<LocalDate> listfromDatesToToDates3 = new ArrayList<>();
		synchronized (listfromDatesToToDates2) {
			for (LocalDate listWithOutsatAndSun : listfromDatesToToDates2) {

				String dateFrom = listWithOutsatAndSun.format(formatter);
				// System.err.println("formDate : " +dateFrom);

				Long staffId = leave.getStaffId();
				LocalDate localDate = LocalDate.parse(dateFrom);

				String outputDate = localDate.format(formatter2);
				String punchDate = serv.checkPunchdate(outputDate, staffId);

				// String punchDate = punchDate2.formatted(formatter);

				// System.err.println("punchDate: " + punchDate);
				// System.err.println("staffId: " + staffId);

				LocalDate dates = LocalDate.parse(dateFrom);
				// System.err.println("Local date Parse : " + dates);

				if (punchDate == null) {
					// System.out.println("if punchDate " + punchDate);
					listfromDatesToToDates3.add(dates);
					// System.out.println("punchDate " + punchDate);

				}
			}
		}
		// System.err.println(" listWithOutsatAndSun List: " + listfromDatesToToDates3);

		for (LocalDate listfromDatesToToDate : listfromDatesToToDates3) {

			StaffLeaveApply1 leaveObject = new StaffLeaveApply1();

			LocalDate value = listfromDatesToToDate;

			String listDate = value.format(formatter);
			leaveObject.setDate(listDate);
			// System.out.println("date List: " + listDate);

			Long leaveId = leave.getId();
			// System.out.println("leaveId: " + leaveId);
			leaveObject.setLeaveId(leaveId);

			Long staffId = leave.getStaffId();
			// System.out.println("staffId: " + staffId);
			leaveObject.setStaffId(staffId);

			String status = leave.getStatus();
			// System.out.println("status: " + status);
			leaveObject.setStatus(status);

			leaveObject.setLeaveCount((long) 1);
			// System.err.println("Before save ..." + leaveObject);

			serv.saveLeaveApply1Data(leaveObject);
			// System.err.println("after save ..." + leaveObject);

		}

	}

	// ---------------------------------------------------------------------------------------------------------------------------//
	// API For Show total leave Details on Staff DashBorad
	@RequestMapping(value = "/show-staff-Leave-table-API", method = RequestMethod.POST)
	@ResponseBody
	public List<StaffLeaveApply> showLeave(HttpSession session) {

		// This is use for show data According to Role
		String role = (String) session.getAttribute("role");

		List<StaffLeaveApply> list = null;
		if (role.equals("Admin")) {

			list = serv.getStaffLeaveList();

		} else if (role.equals("Teaching")) {
			Long staffId = (Long) session.getAttribute("id");

			list = serv.getStaffLeaveById(staffId);
			// System.out.println("Controller ............ "+staffId);
		}

		return list;
	}

	// ---------------------------------------------------------------------------------------------------------------------------//

	// ---------------------------------------------------------------------------------------------------------------------------//
	// API for call Leave Details page by admin
	@RequestMapping("/Staff-LeaveDetails-API")
	public String showLeave() {
		return "admin/staffLeaveDetails";

	}
	// ---------------------------------------------------------------------------------------------------------------------------//

	// ---------------------------------------------------------------------------------------------------------------------------//

	// ---------------------------------------------------------------------------------------------------------------------------//
	// This api admin add role of Staff dynamically call role page
	@RequestMapping("/staff-role-add-API")
	public String role() {

		return "Settings/role";

	}
	// ---------------------------------------------------------------------------------------------------------------------------//

	// This method Api for save Role Dynamically for admin
	@RequestMapping(value = "/save-Role-API", method = RequestMethod.POST)
	@ResponseBody
	public void saveRoleAdd(RoleEntity role) {
		// System.err.println("save.... " + role);
		serv.saveRole(role);
	}
	// ---------------------------------------------------------------------------------------------------------------------------//

	// Code for show role data for table
	@RequestMapping(value = "show-role-data-table-API", method = RequestMethod.POST)
	@ResponseBody
	public List<RoleEntity> showRoleData() {
		// System.err.println("list....");
		List<RoleEntity> list = serv.findAllRoleData();
		return list;
	}
	// ---------------------------------------------------------------------------------------------------------------------------//

	// code for save edit role modal data
	@RequestMapping(value = "/get-Data-Edit-Modal-API", method = RequestMethod.POST)
	@ResponseBody
	public RoleEntity editRoleMOdal(Long id) {
		// System.err.println("edit.... " + id);
		return serv.findRoleId(id);
	}
	// ---------------------------------------------------------------------------------------------------------------------------//

	// code for update data role
	@RequestMapping(value = "/update-data-Role-API", method = RequestMethod.POST)
	@ResponseBody
	public void upadateRoleData(RoleEntity role) {
		// System.err.println("update.... " + role);
		serv.upadateRoleData(role);
	}

	// ---------------------------------------------------------------------------------------------------------------------------//
	// code for delete data role
	@RequestMapping(value = "/delete-Role-Data-API", method = RequestMethod.POST)
	@ResponseBody
	public void deleteRoleData(Long id) {
		// System.err.println("delete.... " + id);
		serv.deleteRoleData(id);
	}
	// ---------------------------------------------------------------------------------------------------------------------------//

	// show the data only selected data in setting role modal
	@RequestMapping(value = "/send-value-API", method = RequestMethod.POST)
	@ResponseBody
	public List<RoleEntity> sendData(String status) {
		// System.err.println("call........"+status);
		List<RoleEntity> list = serv.FindByRoleStatus(status);
		return list;
	}
	// ---------------------------------------------------------------------------------------------------------------------------//

	// code for get Leave type data for leave Application Modal
	@RequestMapping(value = "showLeaveType-API", method = RequestMethod.POST)
	@ResponseBody
	public List<RoleEntity> getLeaveType() {

		String status = "Leave Type";

		List<RoleEntity> list = serv.FindByLeaveType(status);

		// System.err.println("list......." + list.size());

		return list;
	}
	// ---------------------------------------------------------------------------------------------------------------------------//

	// code for get dynamically added role for staff details
	@RequestMapping(value = "/GetRoleAdded-API", method = RequestMethod.POST)
	@ResponseBody
	public List<RoleEntity> roleGet() {

		String status = "Role";

		List<RoleEntity> list = serv.getRoleAdded(status);
		// System.err.println("contro slist....."+list);
		return list;
	}
	// ---------------------------------------------------------------------------------------------------------------------------//

	// code for get dynamically added designation for staff details
	@RequestMapping(value = "/GetDesignationAdded-API", method = RequestMethod.POST)
	@ResponseBody
	public List<RoleEntity> getRole() {

		String status = "Designation";

		List<RoleEntity> list = serv.getDesignationAdded(status);

		// System.err.println("contros list....."+list);
		return list;
	}
	// ---------------------------------------------------------------------------------------------------------------------------//

	// API for Punching In and Out
	@RequestMapping(value = "/punch-API", method = RequestMethod.POST)
	@ResponseBody
	public void staffAttendancePunchIn(String status, HttpSession session) throws ParseException {

		// System.err.println("status : "+status);
		// System.err.println("punch Staff name .... " + session.getAttribute("name"));
		// System.err.println("punch Staff id .... " + session.getAttribute("id"));

		if (session.getAttribute("name") != null) {

			StaffAttendanceEntity attendence = new StaffAttendanceEntity();

			Long staffId = (Long) session.getAttribute("id");
			String staffName = (String) session.getAttribute("name");

			Date date2 = new Date();
			SimpleDateFormat Dateformate = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat Timeformate = new SimpleDateFormat("HH:mm:ss");

			String date = Dateformate.format(date2);
			String punchIn = Timeformate.format(date2);

			attendence.setStaffId(staffId);
			attendence.setStaffName(staffName);
			attendence.setDate(date);

			StaffAttendanceEntity dateCheck = serv.loginCheck(date, staffId);

			// System.err.println("Punching ........ " + dateCheck);

			if (status.equals("PunchIn")) {

				// System.err.println("call inside Punch in");

				if (dateCheck == null) {

					String punchOut = "00:00:00";
					String totalHr = "00:00:00";

					attendence.setPunchIn(punchIn);
					attendence.setPunchOut(punchOut);
					attendence.setPunchOut(punchOut);
					attendence.setStatus(status);
					attendence.setTotalHr(totalHr);

					serv.saveAttendance(attendence, status);
					// System.err.println("punch In Done " + attendence + " "+status);

				}
			} else if (status.equals("PunchOut")) {

				if (dateCheck != null) {
					String punchOut = Timeformate.format(date2);

					Long staffid = attendence.getStaffId();
					String newdate = attendence.getDate();

					serv.SavePunchOut(punchOut, staffid, newdate, status);
					// System.err.println("punchOut " + punchOut + " " + status);

					String totalhr = serv.calculateTime(staffid, newdate);
					serv.SaveTotalhr(totalhr, staffid, newdate);

					// System.err.println("total hr... " + totalhr);
				}
			}
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------//

//API for Punching Buttons
	@RequestMapping(value = "/button-API", method = RequestMethod.POST)
	@ResponseBody
	public StaffAttendanceEntity button(HttpSession session) {

		// System.err.println("punching API Staff name .... " +
		// session.getAttribute("name"));
		// System.err.println("punch Staff id .... " + session.getAttribute("id"));

		Long staffId = (Long) session.getAttribute("id");

		java.util.Date date2 = new java.util.Date(System.currentTimeMillis());
		SimpleDateFormat Dateformate = new SimpleDateFormat("dd-MM-yyyy");
		String date = Dateformate.format(date2);

		StaffAttendanceEntity status = serv.statusCheck(staffId, date);
		// System.err.println("button-API " + status);
		return status;
	}

	// ---------------------------------------------------------------------------------------------------------------------------//

	// This is API for call staffDailyAttendanceDetails page on Admin DashBoard
	@RequestMapping("/Admin-staff-Attendence-API")
	public String attendence() {
		// System.err.println("calllllllllllll");
		return "admin/staffDailyAttendanceDetails";
	}

	// Teacher Staff Attendance Page call punch in
	@RequestMapping("/staff-staff-Attendence-API")
	public String staffAttendance() {
		return "staff/staffAttendance";
	}

	// API for Show attendance Details by staff
	@RequestMapping(value = "/show-Staff-In-Out-table-API", method = RequestMethod.POST)
	@ResponseBody
	public List<StaffAttendanceEntity> showAttendence(HttpSession session) {

		// This is use for show data According to staff
		String role = (String) session.getAttribute("role");

		List<StaffAttendanceEntity> list = null;

		if (role.equals("Admin")) {

			list = serv.FindStaffPunchInAndOutTable();
			// System.err.println("Data Admin: " + list);

		} else if (role.equals("Teaching")) {

			Long staffId = (Long) session.getAttribute("id");

			list = serv.getStaffAttendenceById(staffId);

			// System.err.println("Data Staff: " + list);
		}
		return list;
	}

//---------------------------------------------------------------------------------------------------------------------------------------------------------
	// API for Staff Staff check Attendence According to select date
	@RequestMapping(value = "/staff-dateFilter-API", method = RequestMethod.POST)
	@ResponseBody
	public List<StaffAttendanceEntity> findDate(HttpSession session, String newToDate, String newFromDate) {

		// System.err.println("1...... " + newToDate);
		// System.err.println("2...... " + newFromDate);

		Long staffId = (Long) session.getAttribute("id");

		// System.err.println("id...... "+staffId);

		if (newToDate != null && staffId != null && newFromDate.isEmpty()) {

			// System.err.println("1 Controller call...");
			List<StaffAttendanceEntity> list = serv.ToDate(staffId, newToDate);
			// System.err.println(list);
			return list;

		} else if ((staffId != null) && (newToDate != null) && (newFromDate != null)) {

			// System.err.println(" 2 Controller call...");
			List<StaffAttendanceEntity> list = serv.ToDateFromDate(staffId, newToDate, newFromDate);
			// System.err.println(list);
			return list;
		}
		return null;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

//-----------------------------------------------------------------------------------------------------------------------------------------//	
	// salary details both API for salary calculation

	/// Call SalaryDetails Page
	@RequestMapping(value = "/salary-details-API")
	public String salaryPage() {

		return "admin/SalaryDetails";
	}

//-----------------------------------------------------------------------------------------------------------------------------------------//	

	// setting standards filed

	/// Call SalaryDetails Page
	@GetMapping("/standards-API")
	public String standardsPage() {
		// System.err.println("calll");
		return "Settings/standards";
	}

	// save standards data
	@RequestMapping(value = "/add-Standards-API", method = RequestMethod.POST)
	@ResponseBody
	public void addStandards(standardEntity standards) {

		serv.saveStandards(standards);
	}

	// show standards table data
	@RequestMapping(value = "/show-Standards-table-API", method = RequestMethod.POST)
	@ResponseBody
	public List<standardEntity> showStandardsTable() {
		List<standardEntity> list = serv.showStandardsTable();
		return list;
	}

	// Edit standards table data update-Standards-API
	@RequestMapping(value = "/get-standard-data-API", method = RequestMethod.POST)
	@ResponseBody
	public standardEntity getStandardsData(Long id) {

		return serv.findStandardsDataById(id);
	}

	// save standards data
	@RequestMapping(value = "/update-Standards-API", method = RequestMethod.POST)
	@ResponseBody
	public void updateStandards(standardEntity standards) {

		serv.saveStandards(standards);
	}

	// delete standards data
	@RequestMapping(value = "/delete-standard-data-API", method = RequestMethod.POST)
	@ResponseBody
	public void updateStandards(Long id) {
		serv.deleteStandards(id);
	}

	// search standards list for selector
	@RequestMapping(value = "/search-standard-data-API", method = RequestMethod.POST)
	@ResponseBody
	public List<standardEntity> searchStandardsList(String search) {
		List<standardEntity> list = serv.findSearchStandardsDetails(search);
		return list;
	}

	// get standards list for selector
	@RequestMapping(value = "/getStandardsList-API", method = RequestMethod.POST)
	@ResponseBody
	public List<String> getStandardsList() {

		List<String> originalList = serv.findAllStandardsDetailsList();

		Set<String> set = new HashSet<>(originalList);
		List<String> list = new ArrayList<>(set);

		// System.err.println(list);
		return list;
	}

	// get Medium list for medium selector in Student registration form
	@RequestMapping(value = "/getMediumList-API", method = RequestMethod.POST)
	@ResponseBody
	public List<String> getMediumList() {

		List<String> originalList = serv.findAllMediumList();

		Set<String> set = new HashSet<>(originalList);
		List<String> list = new ArrayList<>(set);

		// System.err.println(list);
		return list;
	}

	// get Division list for medium selector in Student registration form
	@RequestMapping(value = "/getDivisionList-API", method = RequestMethod.POST)
	@ResponseBody
	public List<String> getDivisionList() {

		List<String> originalList = serv.findAllDivisonList();

		Set<String> set = new HashSet<>(originalList);
		List<String> list = new ArrayList<>(set);

		// System.err.println(list);
		return list;
	}

	// get totalFees list for total fees in Student registration form
	@RequestMapping(value = "/getTotalFeesList-API", method = RequestMethod.POST)
	@ResponseBody
	public List<String> getTotalFeesList() {

		List<String> originalList = serv.findAllTotalFessList();

		Set<String> set = new HashSet<>(originalList);
		List<String> list = new ArrayList<>(set);

		// System.err.println(list);
		return list;
	}

//-----------------------------------------------------------------------------------------------------------------------------------------//

	// ---------------------------------- Start Student Modal
	// -------------------------------------------------------------------------------//

	// -----------------------------Start student
	// registration--------------------------------------------------------------------//
	// registration page call
	@RequestMapping(value = "/student-Registration-API")
	public String studentPage() {
		return "admin/studentRegistration";
	}

	// Add Student
	@RequestMapping(value = "/add-student-API", method = RequestMethod.POST)
	@ResponseBody
	public boolean addStudent(Student student, BindingResult bindingResult,
			@RequestParam("documents") MultipartFile documents, HttpSession session) throws IOException {

		if (!documents.isEmpty() && documents != null) {

			String dateName = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());

			String originalFileName = dateName + "-" + documents.getOriginalFilename().replace(" ", "-").toLowerCase();
			Path fileNameAndPath = Paths.get(uploadProductDirectory, originalFileName);
			try {
				Files.write(fileNameAndPath, documents.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}

			student.setDocuments(originalFileName);
		} else {

			student.setDocuments(" ");
		}

		Integer total = Integer.parseInt(student.getTotalFees());
		// System.err.println(total);

		Integer paid = Integer.parseInt(student.getPaidFess());
		// System.err.println(paid);

		String remain = Integer.toString(total - paid);
		student.setRemainingFees(remain);

		serv.SaveStudent(student);
		System.err.println(student);

		return true;
	}

	// show student detail table
	@RequestMapping(value = "/show-student-API", method = RequestMethod.POST)
	@ResponseBody
	public List<Student> showStudent() {

		List<Student> list = serv.showStudentTable();
		return list;

	}

	// Get data edit Modal
	@RequestMapping(value = "/edit-student-API", method = RequestMethod.POST)
	@ResponseBody
	public Student getData(Long id) {
		return serv.getStudentFormData(id);
	}

	// update student
	@RequestMapping(value = "/update-student-API", method = RequestMethod.POST)
	@ResponseBody
	public boolean updateStudent(Student student, BindingResult bindingResult,
			@RequestParam("documents") MultipartFile documents, HttpSession session) throws IOException {

		if (!documents.isEmpty() && documents != null) {

			String dateName = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());

			String originalFileName = dateName + "-" + documents.getOriginalFilename().replace(" ", "-").toLowerCase();
			Path fileNameAndPath = Paths.get(uploadProductDirectory, originalFileName);
			try {
				Files.write(fileNameAndPath, documents.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}

			student.setDocuments(originalFileName);
		} else {

			student.setDocuments(" ");
		}

		Integer total = Integer.parseInt(student.getTotalFees());
		// System.err.println(total);

		Integer paid = Integer.parseInt(student.getPaidFess());
		// System.err.println(paid);

		String remain = Integer.toString(total - paid);
		student.setRemainingFees(remain);

		serv.SaveStudent(student);
		System.err.println(student);

		return true;
	}

	// delete student
	@RequestMapping(value = "/delete-student-API", method = RequestMethod.POST)
	@ResponseBody
	public void deleteStudent(Long id) {
		serv.deleteStudent(id);
	}

	// Student fees details

	// call Student fees details page
	@RequestMapping(value = "/student-fees-deatils-API", method = RequestMethod.GET)
	public String StudentFess() {
		return "admin/studentFeesDetails";
	}

//get the student details table data
	@RequestMapping(value = "/show-student-Fess-API", method = RequestMethod.POST)
	@ResponseBody
	public List<Student> StudentData() {
		List<Student> list = serv.showStudentTable();
		// System.err.println(list.size());
		return list;

	}

	// Update student edit fees details

	// save standards data this is for student fees details incomplate
	@RequestMapping(value = "/update-student-fees-API", method = RequestMethod.POST)
	@ResponseBody
	public void updateStudentFees(Student student) {

		Long id = student.getId();
		// String paidFess = student.getPaidFess();

		System.err.println(id);

		String UpdateFess = serv.getPaidFess(id);

		System.err.println(UpdateFess);

		// serv.updateStudentgFeesDetails(student);
	}

	// End Student Modal
//-----------------------------------------------------------------------------------------------------------------------------------------//

//-----------------------------------------------------------------------------------------------------------------------------------------//

// ---------------------------------- Start Setting Modal------------------------------------------------------------//

//-----------------------------Salary Filed Add--------------------------------------------------------------------//

// call Salary setting page 
	@RequestMapping(value = "/salary-calculation-API", method = RequestMethod.GET)
	public String salaryCalculationSetting() {
		return "Settings/salary";
	}

	// /show-salary-filed-table-API
	@RequestMapping(value = "/add-salary-field-API", method = RequestMethod.POST)
	@ResponseBody
	public void addSalary(SalaryField salaryField) {

		serv.addSalary(salaryField);
	}

//  /
	@RequestMapping(value = "/show-salary-filed-table-API", method = RequestMethod.POST)
	@ResponseBody
	public List<SalaryField> showSalaryField() {

		List<SalaryField> list = serv.ShowSalaryFiledList();

		System.err.println(list.size());

		return list;
	}

	// "/get-Data-Edit-SalaryField-Modal-API" "/delete-salaryField-Data-API"

	@RequestMapping(value = "/get-Data-Edit-SalaryField-Modal-API", method = RequestMethod.POST)
	@ResponseBody
	public SalaryField editModalData(Long id) {

		return serv.editModalData(id);

	}

	// "/delete-salaryField-Data-API"
	@RequestMapping(value = "/delete-salaryField-Data-API", method = RequestMethod.POST)
	@ResponseBody
	public void deleteSalaryField(Long id) {

		serv.salaryFieldDeleteByID(id);

	}
//-----------------------------Add holidays--------------------------------------------------------------------//

	// call holidays setting page
	@RequestMapping(value = "/holidays-API", method = RequestMethod.GET)
	public String holidays() {
		return "Settings/addHolidays";
	}

	// save holidays setting page
	@RequestMapping(value = "/save-Holiday-API", method = RequestMethod.POST)
	@ResponseBody
	public void Saveholidays(AddHolidays holidays) {

		serv.SaveHoliday(holidays);
	}

	// show holidays table data setting page
	@RequestMapping(value = "/show-holidays-data-table-API", method = RequestMethod.POST)
	@ResponseBody
	public List<AddHolidays> showHolidayTable() {

		List<AddHolidays> list = serv.ShowHolidayTableDate();
		return list;
	}

	// Get Edit Modal data setting page
	@RequestMapping(value = "/edit-modal-data-API", method = RequestMethod.POST)
	@ResponseBody
	public AddHolidays editHolidayModalData(Long id) {

		return serv.editHolidayModalData(id);

	}

	// Delete holiday setting page
	@RequestMapping(value = "/delete-holiday-API", method = RequestMethod.POST)
	@ResponseBody
	public void deleteHoliday(Long id) {

		serv.deleteHoliday(id);

	}

//-----------------------------------------------------------------------------------------------------------------------------------------//


//--------------------------------------------salary slip data----------------------------------------------------------------

	@RequestMapping(value = "/salary-Slip-API", method = RequestMethod.GET)
	public String staffSalarySlip() {

		return "staff/salarySlip";
	}

	@RequestMapping(value = "/showSalarySlip-API", method = RequestMethod.POST)
	@ResponseBody
	public List<SalaryDetails> staffSalarySlipTable(HttpSession session) {

		Long id = (Long) session.getAttribute("id");
		String name = (String) session.getAttribute("name");
		List<SalaryDetails> list = serv.getStaffSalarySlipData(id, name);
		return list;

	}

	@RequestMapping(value = "/viewSlip-API", method = RequestMethod.POST)
	@ResponseBody
	public StaffSalarySlip salarySlip(HttpSession session, String salaryMonth) {
		System.err.println(salaryMonth);

		Long StaffId = (Long) session.getAttribute("id");
		String StaffName = (String) session.getAttribute("name");

		SalaryDetails staffData = serv.getStaffSalarySlipData(salaryMonth, StaffId, StaffName);
		StaffSalarySlip salaryDataCheck = serv.getStaffSalaryMonthData(StaffId, salaryMonth);
		
		
		StaffSalarySlip object = new StaffSalarySlip();

		Long staffIds = staffData.getStaffId();
		object.setStaffId(staffIds);

		String staffName = staffData.getStaffName();
		object.setName(staffName);

		String designation = staffData.getDesignation();
		object.setDesignation(designation);

		Long absentDay = staffData.getAbsentDay();
		object.setAbsentDay(absentDay);

		Long workDay = staffData.getWorkDay();
		object.setWorkDay(workDay);

		String salaryMonths = staffData.getMonth();
		object.setSalaryMonth(salaryMonths);

		object.setModeOfPayment("cash");

		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String dateOfPayment = currentDate.format(formatter);
		object.setSalaryDate(dateOfPayment);
		

		if (salaryDataCheck == null) {
	
			serv.saveSalaryData(object);
			StaffSalarySlip staffSalayMonthData = serv.getStaffSalaryMonthData(StaffId, salaryMonth);
			System.err.println("staffSalayMonthData :  " + staffSalayMonthData);
			return staffSalayMonthData;

		}
		else {
			
			 serv.updateSalaryMonthData(StaffId,salaryMonth,absentDay,workDay,dateOfPayment);
			 StaffSalarySlip SalayDataUpdate = serv.getStaffSalaryMonthData(StaffId, salaryMonth);			
			return SalayDataUpdate;

		}
		
	}
	
	@RequestMapping(value = "/slipData-API", method = RequestMethod.POST)
	@ResponseBody
	public salarySlipHelper slipData(HttpSession session,String salaryMonth) {

		Long StaffId = (Long) session.getAttribute("id");
		String StaffName = (String) session.getAttribute("name");

		
		String designation = serv.getStaffDesignation(StaffId,salaryMonth);
		SalaryField data = serv.getDataUsingDesignation(designation);
		
		
		Integer totalEarn = (data.getBasic() + data.getHra() + data.getAdditional()+ data.getBonus());
		Integer totalDeduct = data.getTax();
		
		
		SalaryDetails staffData = serv.getStaffSalarySlipData(salaryMonth, StaffId, StaffName);
		
		salarySlipHelper total = new salarySlipHelper();
		
		total.setBasic(data.getBasic());
		total.setHra(data.getHra());
		total.setAdditional(data.getAdditional());
		total.setBonus(data.getBonus());
		total.setTax(data.getTax());
		total.setTotalEarn(totalEarn);
		total.setTotalDeduct(totalDeduct);
		
		String netPay = staffData.getNet();
		total.setNetPay(netPay);
		System.err.println("staffData:  "+staffData);
		return total;
	}
	

}
