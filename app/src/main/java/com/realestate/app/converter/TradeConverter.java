package com.realestate.app.converter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.realestate.app.dto.TradeDto;
import com.realestate.app.dto.TradeForCreateDto;
import com.realestate.app.entity.PropertyEntity;
import com.realestate.app.entity.TradeEntity;
import com.realestate.app.entity.UserEntity;
import com.realestate.app.entity.enums.TradeType;

public class TradeConverter {
	
	private TradeConverter() {}
	
	public static TradeDto toDto(TradeEntity trade) {
		TradeDto toReturn = new TradeDto();
		toReturn.setClient(UserConverter.toDto(trade.getClient()));
		toReturn.setProperties(FullPropertyConverter.singleToDto(trade.getProperty()));
		toReturn.setTradeDate(trade.getTradeDate());
		toReturn.setEndTradeDate(trade.getEndTradeDate());
		toReturn.setPaymentType(trade.getPaymentType());
		toReturn.setTradeType(trade.getTradeType());
		toReturn.setVersion(trade.getVersion());
		return toReturn;
	}
	
	public static List<TradeDto> toDto(List<TradeEntity> trade) {
		List<TradeDto> toReturn = new ArrayList<>();
		for(TradeEntity ue : trade) {
			toReturn.add(toDto(ue));
		}
		return toReturn;
	}

	public static TradeEntity toEntityForCreate(TradeForCreateDto dto, UserEntity client, PropertyEntity property) {
		TradeEntity toReturn=new TradeEntity();
		toReturn.setTradeId(null);
		toReturn.setClient(client);
		toReturn.setProperty(property);
		toReturn.setTradeDate(LocalDateTime.now());
		toReturn.setEndTradeDate(null);
		toReturn.setPaymentType(dto.getPaymentType());
		toReturn.setTradeType(TradeType.valueOf(dto.getTradeType()));
		return toReturn;
	}
}
