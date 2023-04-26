package com.ezio.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ezio.Modal.standardEntity;

@Repository
public interface StandardRepo extends JpaRepository <standardEntity, Long>{

	
	// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//code start for add settings Standards filed
	List<standardEntity> findByStandards(String search);

	// get standard list for selector
	@Query("SELECT standards FROM standardEntity")
	List<String> findByStandards();
	
	// get Medium list for medium selector in Student registration form
	@Query("SELECT medium FROM standardEntity")
	List<String> findByMedium();

	// get Division list for Division selector in Student registration form
	@Query("SELECT division FROM standardEntity")
	List<String> findByDivision();

	// get totalFees list for total fees in Student registration form
	@Query("SELECT fees FROM standardEntity")
	List<String> findByFees();

}
// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
