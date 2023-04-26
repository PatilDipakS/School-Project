package com.ezio.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ezio.Modal.StaffAttendanceEntity;

import jakarta.transaction.Transactional;

@Repository
public interface StaffAttendanceRepo extends JpaRepository<StaffAttendanceEntity, Long> {

	StaffAttendanceEntity findByDateAndStaffId(String date, Long staffId);

// -------------------------------------------------------------------------------------------------------------------------------------------------------------------
// --------------------------------------------------Staff attendance DetailsModal-------------------------------------------------------------------------//

	// Method for admin search name ,single date attendance and between date
	// attendance
	// search
	List<StaffAttendanceEntity> findByStaffName(String searchVal);

	// search name and To date
	@Query(value = "select * from staff_attendance_entity where staff_name=:searchVal and date=:newToDate", nativeQuery = true)
	List<StaffAttendanceEntity> findByNameAndToDate(String searchVal, String newToDate);

	// search name and FromDate
	@Query(value = "select * from staff_attendance_entity where staff_name=:searchVal and date=:newFromDate", nativeQuery = true)
	List<StaffAttendanceEntity> findNameAndFromDate(String searchVal, String newFromDate);

	// search name To date and form date
	@Query(value = "select * from staff_attendance_entity where staff_name=:searchVal and date BETWEEN :newToDate And :newFromDate", nativeQuery = true)
	List<StaffAttendanceEntity> findNameAndFromDate(String searchVal, String newToDate, String newFromDate);

	// to date
	@Query(value = "select * from staff_attendance_entity where date=:newToDate", nativeQuery = true)
	List<StaffAttendanceEntity> findByToDate(String newToDate);
// -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	
	
	// ------------------------------------------------------------------------------------------------------------------------------------------------------------//
	// methods for staff attendance punch in and out
	// methods for out time
	@Modifying
	@Transactional
	@Query("update StaffAttendanceEntity set punchOut =:punchOut ,status=:status  where staffId=:staffid And date=:newdate")
	void UpdateOutTime(String punchOut, Long staffid, String newdate, String status);

	// query for calculate total hours
	@Query("SELECT TIMEDIFF(punchOut,punchIn)  FROM StaffAttendanceEntity where staffId=:staffid And date=:newdate")
	String calTotalHr(Long staffid, String newdate);

	@Modifying
	@Transactional
	@Query("update StaffAttendanceEntity set totalHr =:totalhr where staffId=:staffid And date=:newdate")
	void UpdateTotalHr(String totalhr, Long staffid, String newdate);

	// method for staff punching Buttons hide and show
	StaffAttendanceEntity findByStaffIdAndDate(Long staffId, String date);

	List<StaffAttendanceEntity> findByStaffId(Long staffId);

	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Method for Staff Staff check Attendence According to select date
	// method for find single date
	@Query(value = "select * from staff_attendance_entity where staff_id=:staffId and date=:newToDate", nativeQuery = true)
	List<StaffAttendanceEntity> findNewDate(Long staffId, String newToDate);

	// method for find Between date
	@Query(value = "select * from staff_attendance_entity where staff_id=:staffId and date BETWEEN :newToDate And :newFromDate", nativeQuery = true)
	List<StaffAttendanceEntity> findDateBetween(Long staffId, String newToDate, String newFromDate);

//=======------------------ salary calculation ---------------------

	@Query("SELECT COUNT(id) FROM StaffAttendanceEntity  WHERE staffId = :staffId")
	Long countPunch(Long staffId);

	// check punch date for staffLeaveAppy! table not save Repeat date
	@Query("SELECT date FROM StaffAttendanceEntity WHERE date = :dateFrom And staffId =:staffId")
	String findByDate(String dateFrom, Long staffId);

}
