package com.redliu.shipmentmanagement.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redliu.shipmentmanagement.model.Shipment;
import com.redliu.shipmentmanagement.model.Trade;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ShipmentRepositoryTests {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ShipmentRepository repository;

	@Test
	public void queryById()  {

		Long tradeId = 1L;

		Shipment ship = repository.findRootShipmentByTradeid(tradeId);


		Trade trade = new Trade(ship);

		String json = trade.toString();


	}

	@Test
	public void insertShipmentRelations() throws Exception{

		String json = "{\"traceid\":2,\"quantity\":200,\"rootShipment\":{\"shipmentid\":9000,\"opcount\":0,\"quantity\":200,\"tradeid\":2,\"operator\":\"Initial\",\"child\":[{\"shipmentid\":9001,\"opcount\":1,\"quantity\":100,\"tradeid\":2,\"operator\":\"Split\",\"child\":[{\"shipmentid\":9005,\"opcount\":2,\"quantity\":150,\"tradeid\":1,\"operator\":\"Merge\",\"child\":[]}]},{\"shipmentid\":9003,\"opcount\":1,\"quantity\":50,\"tradeid\":1,\"operator\":\"Split\",\"child\":[{\"shipmentid\":9005,\"opcount\":2,\"quantity\":150,\"tradeid\":2,\"operator\":\"Merge\",\"child\":[]}]},{\"shipmentid\":9004,\"opcount\":1,\"quantity\":50,\"tradeid\":2,\"operator\":\"Split\",\"child\":[]}]}}";

		ObjectMapper objectMapper = new ObjectMapper();

		Trade trade = objectMapper.readValue(json, Trade.class);

		String json1 = objectMapper.writeValueAsString(trade);

		assert (json.equals(json1));
		int count = repository.insertShipmentRelations(trade.getRootShipment(), trade.getRootShipment().getChild());

		assert (count == 3);

		List<Shipment> shipments = trade.getRootShipment().getChild();
		assert (shipments.size() == 3);



		count = repository.insertShipmentRelations(trade.getRootShipment().getChild().get(0), trade.getRootShipment().getChild().get(0).getChild());
		assert (count == 1);
	}


	@Test
	public void insertOneTrade() throws Exception{

		String json = "{\"traceid\":2,\"quantity\":200,\"rootShipment\":{\"shipmentid\":9000,\"opcount\":0,\"quantity\":200,\"tradeid\":2,\"operator\":\"Initial\",\"child\":[{\"shipmentid\":9001,\"opcount\":1,\"quantity\":100,\"tradeid\":2,\"operator\":\"Split\",\"child\":[{\"shipmentid\":9005,\"opcount\":2,\"quantity\":150,\"tradeid\":1,\"operator\":\"Merge\",\"child\":[]}]},{\"shipmentid\":9003,\"opcount\":1,\"quantity\":50,\"tradeid\":1,\"operator\":\"Split\",\"child\":[{\"shipmentid\":9005,\"opcount\":2,\"quantity\":150,\"tradeid\":2,\"operator\":\"Merge\",\"child\":[]}]},{\"shipmentid\":9004,\"opcount\":1,\"quantity\":50,\"tradeid\":2,\"operator\":\"Split\",\"child\":[]}]}}";

		ObjectMapper objectMapper = new ObjectMapper();

		Trade trade = objectMapper.readValue(json, Trade.class);

		String json1 = objectMapper.writeValueAsString(trade);

		assert (json.equals(json1));
		int count = repository.insertShipmentRelations(trade.getRootShipment(), trade.getRootShipment().getChild());

		assert (count == 3);

		List<Shipment> shipments = trade.getRootShipment().getChild();
		assert (shipments.size() == 3);



		count = repository.insertShipmentRelations(trade.getRootShipment().getChild().get(0), trade.getRootShipment().getChild().get(0).getChild());
		assert (count == 1);
	}




}
