package com.ezio.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class salarySlipHelper {
	
	private Integer basic;
	private Integer hra;
	private Integer additional;
	private Integer tax;
	private Integer bonus;
	private Integer totalEarn;
	private Integer totalDeduct;
	private String netPay;


	

}
