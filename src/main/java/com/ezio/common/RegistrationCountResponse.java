package com.ezio.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegistrationCountResponse {
	
	private Long totalCount;
	private Long studentCount;
	private Long staffCount;
	private Long staffLeaveCount;


}
