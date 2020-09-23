package com.redliu.shipmentmanagement.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redliu.shipmentmanagement.model.Shipment;
import com.redliu.shipmentmanagement.model.Trade;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = Application.class)

@SpringBootTest
@AutoConfigureMockMvc
class ShipmentControllerTest {

    @Autowired
    private ShipmentController restController;

    @Autowired
    private MockMvc mvc;


    @Test
    public void testGetTrade() throws Exception {
        Trade trade = new Trade();
        Shipment s = new Shipment();
        trade.setQuantity(100);
        trade.setTraceid(1L);
        s.setQuantity(100);
        s.setTradeid(1L);
        Short op = 0;
        s.setOp(op);
        s.setShipmentid(1L);
        s.setOpcount(0);

        trade.setRootShipment(s);


        ObjectMapper objectMapper = null;
        String trdejson = "";
        try {
            objectMapper = new ObjectMapper();
            trdejson = objectMapper.writeValueAsString(trade);
        }catch(JsonProcessingException e){
            assert(0 == 1);
        }

        System.out.println(trdejson);
        RequestBuilder builder = MockMvcRequestBuilders
                .get("/shipmentmanagement/" + trade.getTraceid())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(trdejson);

        MvcResult result = mvc.perform(builder).andReturn();
        System.out.println(result.getResponse().getContentAsString());

    }


    @Test
    public void testInitial() throws Exception {
        Trade trade = new Trade();
        Shipment s = new Shipment();
        trade.setQuantity(800);
        trade.setTraceid(5L);
        s.setQuantity(1000);
        s.setTradeid(5L);
        Short op = 0;
        s.setOp(op);
        s.setShipmentid(2000L);
        s.setOpcount(0);

        trade.setRootShipment(s);


        ObjectMapper objectMapper = null;
        String shipmentjson = "";
        try {
            objectMapper = new ObjectMapper();
            shipmentjson = objectMapper.writeValueAsString(s);
        }catch(JsonProcessingException e){
            assert(0 == 1);
        }

        System.out.println(shipmentjson);
        RequestBuilder builder = MockMvcRequestBuilders
                .post("/shipmentmanagement/initialtrade/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(shipmentjson);

        MvcResult result = mvc.perform(builder).andReturn();
        System.out.println(result.getResponse().getContentAsString());
        System.out.println(result.getResponse().getHeaderNames());


    }


    @Test
    public void testSplit() throws Exception {


        //testInitial();

        List<Shipment> root = new ArrayList<>();
        Shipment s = new Shipment();
        s.setQuantity(800);
        s.setTradeid(5L);
        Short op = 0;
        s.setOp(op);
        s.setShipmentid(2000L);
        s.setOpcount(0);

        Shipment s1 = new Shipment();
        s1.setQuantity(500);
        s1.setTradeid(5L);
        op = 1;
        s1.setOp(op);
        s1.setShipmentid(2001L);
        s1.setOpcount(1);

        Shipment s2 = new Shipment();
        s2.setQuantity(300);
        s2.setTradeid(5L);
        op = 1;
        s2.setOp(op);
        s2.setShipmentid(2002L);
        s2.setOpcount(1);

        List<Shipment> l = new ArrayList<>();
        l.add(s1);
        l.add(s2);
        s.setChild(l);

        root.add(s);

        ObjectMapper objectMapper = null;
        String shipmentjson = "";
        try {
            objectMapper = new ObjectMapper();
            shipmentjson = objectMapper.writeValueAsString(root);
        }catch(JsonProcessingException e){
            assert(0 == 1);
        }

        System.out.println(shipmentjson);
        RequestBuilder builder = MockMvcRequestBuilders
                .post("/shipmentmanagement/splittrade/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(shipmentjson);

        MvcResult result = mvc.perform(builder).andReturn();
        System.out.println(result.getResponse().getContentAsString());
        System.out.println(result.getResponse().getHeaderNames());


    }


    @Test
    public void testMerge() throws Exception {


        //testInitial();
        //testSplit();

        List<Shipment> root = new ArrayList<>();


        Shipment s1 = new Shipment();
        s1.setQuantity(500);
        s1.setTradeid(5L);
        Short op = 1;
        s1.setOp(op);
        s1.setShipmentid(2001L);
        s1.setOpcount(1);

        Shipment s2 = new Shipment();
        s2.setQuantity(300);
        s2.setTradeid(5L);
        op = 1;
        s2.setOp(op);
        s2.setShipmentid(2002L);
        s2.setOpcount(1);

        List<Shipment> l = new ArrayList<>();
        l.add(s1);
        l.add(s2);



        Shipment s3 = new Shipment();
        s3.setQuantity(800);
        s3.setTradeid(5L);
        op = 2;
        s3.setOp(op);
        s3.setShipmentid(2004L);
        s3.setOpcount(2);
        List<Shipment> list1 = new ArrayList<Shipment>();
        list1.add(s3);
        s1.setChild(list1);
        List<Shipment> list2 = new ArrayList<Shipment>();
        list2.add(s3);
        s1.setChild(list2);

        List<Shipment> list3 = new ArrayList<Shipment>();
        list3.add(s1);
        list3.add(s2);



        ObjectMapper objectMapper = null;
        String shipmentjson = "";
        try {
            objectMapper = new ObjectMapper();
            shipmentjson = objectMapper.writeValueAsString(list3);
        }catch(JsonProcessingException e){
            assert(0 == 1);
        }

        System.out.println(shipmentjson);
        RequestBuilder builder = MockMvcRequestBuilders
                .post("/shipmentmanagement/merge/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(shipmentjson);

        MvcResult result = mvc.perform(builder).andReturn();
        System.out.println(result.getResponse().getContentAsString());
        System.out.println(result.getResponse().getHeaderNames());


    }



    @Test
    public void testUpdateRootQuanntity() throws Exception {


        //testInitial();
        //testSplit();

        Trade trade = new Trade();
        //Shipment s = new Shipment();
        trade.setQuantity(80);
        trade.setTraceid(5L);
       // s.setQuantity(100);
       // s.setTradeid(1L);
        Short op = 0;
        //s.setOp(op);
        //s.setShipmentid(1L);
        //s.setOpcount(0);

        //trade.setRootShipment(s);


        ObjectMapper objectMapper = null;
        String trdejson = "";
        try {
            objectMapper = new ObjectMapper();
            trdejson = objectMapper.writeValueAsString(trade);
        }catch(JsonProcessingException e){
            assert(0 == 1);
        }



        System.out.println(trdejson);
        RequestBuilder builder = MockMvcRequestBuilders
                .post("/shipmentmanagement/merge/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(trdejson);

        MvcResult result = mvc.perform(builder).andReturn();
        System.out.println(result.getResponse().getContentAsString());
        System.out.println(result.getResponse().getHeaderNames());


    }





}


