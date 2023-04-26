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
public class StaffLeaveApply {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long staffId;
	private String name;
	private String applyDate;
	private String fromDate;
	private String toDate;
	private String leaveType;
	private Long numLeave;
	private String reason;
	private String status="Pending"; 
}
