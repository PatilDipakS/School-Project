package com.ezio.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ezio.Modal.SalaryField;

@Repository
public interface SalaryFieldRepo extends JpaRepository<SalaryField, Long> {

	@Query("SELECT basic FROM SalaryField WHERE designation =:designation")
	Long findByBasic(String designation);

	@Query("SELECT hra FROM SalaryField WHERE designation =:designation")
	Long findByHra(String designation);

	@Query("SELECT additional FROM SalaryField WHERE designation =:designation")
	Long findByAdditional(String designation);

	@Query("SELECT tax FROM SalaryField WHERE designation =:designation")
	Double findByTax(String designation);

	@Query("SELECT bonus FROM SalaryField WHERE designation =:designation")
	Double findByBonus(String designation);

	SalaryField findByDesignation(String designation);

}
