package com.realestate.app.tests;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.realestate.app.entity.TradeEntity;
import com.realestate.app.repository.TradeRepository;
import com.realestate.app.utils.TradeUtil;

@SpringBootTest
@Transactional
class TradeCRUDTest {

	@Autowired
	TradeRepository tradeRepo;


	@Test
	void givenTrade_whenUpdate_thenGetUpdatedTrade() {
		TradeEntity trade = TradeUtil.createTradeTwo();
		tradeRepo.insertTrade(trade);
		trade.setTradeType("Bought");

		tradeRepo.updateTrade(trade);

		Assertions.assertEquals("Bought", tradeRepo.getTradeById(2).getTradeType());
	}

	@Test
	void givenTrade_whenSave_thenGetCreatedTrade() {
		Integer userSize = tradeRepo.getAllTrades().size();
		TradeEntity trade = TradeUtil.createTradeOne();

		tradeRepo.insertTrade(trade);

		Assertions.assertEquals(userSize + 1, tradeRepo.getAllTrades().size());
		System.out.println(tradeRepo.getAllTrades().size());
		Assertions.assertNotNull(tradeRepo.getTradeById(3));
	}

	@Test
	void givenWrongTradeId_whenRetrieved_thenGetNoResult() {


		TradeEntity trade = tradeRepo.getTradeById(20);

		Assertions.assertNull(trade);
	}

	@Test
	void givenTrade_whenDelete_thenGetNoResult() {
		TradeEntity trade = TradeUtil.createTradeOne();
		tradeRepo.insertTrade(trade);

		tradeRepo.deleteTrade(trade);

		Assertions.assertNull(tradeRepo.getTradeById(1));
	}

}