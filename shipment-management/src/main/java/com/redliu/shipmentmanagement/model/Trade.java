package com.redliu.shipmentmanagement.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

import java.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Trade {

    public Trade(){

    }

   public Trade(Shipment rootShipment) {
        this.rootShipment = rootShipment;
        traceid = rootShipment.getTradeid();
        quantity = rootShipment.getQuantity();
   }

    private Long traceid;
    private Integer quantity;

    public Long getTraceid() {
        return traceid;
    }

    public void setTraceid(Long traceid) {
        this.traceid = traceid;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    private Shipment rootShipment;

    public Shipment getRootShipment() {
        return rootShipment;
    }

    public void setRootShipment(Shipment rootShipment) {
        this.rootShipment = rootShipment;
    }



    @Override
    public String toString()  {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(this);
            return json;

        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }


}
