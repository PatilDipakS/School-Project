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
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String birth;
	private String mobile;
	private String gender;
	private String standards;
	private String medium;
	private String classDiv;
	private String address;
	private String paidFessDate;
	private String pin;
	private String documents;
	private String email;
	private String password;
	private String installment;
	private String totalFees;
	private String admissionDate;
	private String remainingFees;
	private String paidFess;
	private String nextInstall;
	private String status = "Active";

}
