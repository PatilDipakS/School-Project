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
public class Staff {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String email;
	private String address;
	private String qualification;
	private String role;
	private String birth;
	private String lastWorkExpe;
	private String joinDate;
	private String mobile;
	private String password;
	private String gender;
	private String documents;
	private String designation;
	private String workExpNum;
	private String status="Active";
}
