package com.ezio.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ezio.Modal.RoleEntity;




@Repository
public interface RoleRepo extends JpaRepository<RoleEntity, Long> {
	
	//show the only selected data in setting role modal
	
	List<RoleEntity> findByStatus(String status);
}
