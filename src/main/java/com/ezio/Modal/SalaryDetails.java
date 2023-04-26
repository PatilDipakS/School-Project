package com.ezio.Modal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SalaryDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long staffId;
	private String staffName;
	private String designation;
	private String month;
	private Long workDay;
	private Long AbsentDay;
	private String earn;
	private String deduct;
	private Double tax;
	private Double bonus;
	private String net;
	private String gross;
	}
