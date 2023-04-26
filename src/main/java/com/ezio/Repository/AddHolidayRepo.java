package com.ezio.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ezio.Modal.AddHolidays;

@Repository
public interface AddHolidayRepo extends JpaRepository<AddHolidays, Long> {

	@Query("SELECT holidayDate FROM AddHolidays")
	List<String> findByHolidayDate();

	//code for to check holiday
    
    @Query("SELECT holidayDate FROM AddHolidays WHERE holidayDate =:dateFrom")
	String findByDate(String dateFrom);


	


}
