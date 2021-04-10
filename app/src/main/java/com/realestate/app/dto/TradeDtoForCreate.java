package com.realestate.app.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.realestate.app.entity.enums.TradeTypeEnum;

import lombok.Data;

@Data
public class TradeDtoForCreate {
	
	@NotNull(message = "Client Id is mandatory !")
	private int client;
	
	@NotNull(message = "Property Id is mandatory !")
	private int properties;

	private TradeTypeEnum tradeType;

	@NotBlank(message = "Payment type is mandatory !")
	private String paymentType;

}
