package com.redliu.shipmentmanagement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redliu.shipmentmanagement.exception.CustomException;
import com.redliu.shipmentmanagement.exception.ShipmentDataException;
import com.redliu.shipmentmanagement.model.Shipment;
import com.redliu.shipmentmanagement.model.ShipmentOperator;
import com.redliu.shipmentmanagement.model.Trade;
import com.redliu.shipmentmanagement.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShipmentManagementService {

    @Autowired
    private ShipmentRepository repository;

    @Transactional
    public void initialOneShipment(Shipment shipment)  {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Shipment shipment = objectMapper.readValue(json, Shipment.class);
//        if (!shipment.getOperator().equals(ShipmentOperator.values()[0].getName())){
//            throw new ShipmentDataException("json data is wrong.");
//        }

        repository.insertOneShipment(shipment);

    }

    @Transactional
    public void splitShipment(List<Shipment>  shipmentlist)  {

//        ObjectMapper objectMapper = null;
//        List<Shipment> shipmentlist = null;
//        try {
//            objectMapper = new ObjectMapper();
//            shipmentlist = objectMapper.readValue(json, ArrayList.class);
//        }catch(JsonProcessingException e){
//            throw new ShipmentDataException(e.getMessage());
//        }

        for (Shipment shipment:
                shipmentlist) {


            Integer sum = 0;
            List<Shipment> child = shipment.getChild();
            for (Shipment s :
                    child) {
                sum += s.getQuantity();

            }

//            System.out.println("11111:" + sum);
//            System.out.println("11112:" + shipment.getQuantity());
            if (sum.intValue() !=  shipment.getQuantity().intValue()) {
                throw new ShipmentDataException("json data is wrong: Sum of all child shipment quantities should be equal to parent shipment quantity");
            }

            for (Shipment s :
                    child) {
                sum += s.getQuantity();
                repository.insertOneShipment(s);
            }

            repository.insertShipmentRelations(shipment, shipment.getChild());
        }


    }

    @Transactional
    public void mergeShipment(List<Shipment>  shipmentlist){
//        ObjectMapper objectMapper = null;
//        List<Shipment> shipmentlist = null;
//        try {
//            objectMapper = new ObjectMapper();
//            shipmentlist = objectMapper.readValue(json, ArrayList.class);
//        }catch(JsonProcessingException e){
//            throw new ShipmentDataException(e.getMessage());
//        }

        Map<Shipment, ArrayList<Shipment>> parentMap = new HashMap<>();
        for (Shipment shipment:
                shipmentlist) {

            List<Shipment> child = shipment.getChild();


            // construct child - parent relation
            for(Shipment s : child){
                ArrayList parentlist = parentMap.get(s);
                if (parentlist == null){
                    parentlist = new ArrayList<>();
                    parentlist.add(shipment);
                    parentMap.put(s, parentlist);
                }
                else{
                    parentlist.add(shipment);
                }
            }

        }

        // check whether or not Sum of all parent shipment quantities should be equal to child shipment quantity.
        for (Map.Entry<Shipment, ArrayList<Shipment>> entry : parentMap.entrySet()) {

            Shipment child = entry.getKey();
            List<Shipment> parent = entry.getValue();

            Integer sum = 0;
            for(Shipment s : parent){
                sum += s.getQuantity();
            }

            if (sum.intValue() != child.getQuantity().intValue()){
                throw  new ShipmentDataException("json data is wrong: Sum of all parent shipment quantities should be equal to child shipment quantity.");
            }
        }

        // insert child shipment， and insert parent-child into db
        for (Map.Entry<Shipment, ArrayList<Shipment>> entry : parentMap.entrySet()) {

            Shipment child = entry.getKey();
            List<Shipment> parent = entry.getValue();

            List<Shipment> childlist = new ArrayList<>();
            childlist.add(child);

            repository.insertOneShipment(child);
            for(Shipment s: parent){
                repository.insertShipmentRelations(s, childlist);
            }
        }
    }


    @Transactional
    public void changeRootQuantity(Trade trade){
//        ObjectMapper objectMapper = null;
//        Trade trade = null;
//        try {
//            objectMapper = new ObjectMapper();
//            trade = objectMapper.readValue(json, Trade.class);
//        }catch(JsonProcessingException e){
//            throw new ShipmentDataException(e.getMessage());
//        }

        Shipment root = repository.findRootShipmentByTradeid(trade.getTraceid());
        Integer oldRootQuantity = root.getQuantity();
        Integer newRootQuantity = trade.getQuantity();

        Float changeRate = (float)newRootQuantity/oldRootQuantity;

        // according to opcount,

        root.setQuantity(newRootQuantity);

        updateShipment(changeRate, root);


    }

    public void updateShipment(Float changeRate, Shipment root){
        for (Shipment s : root.getChild()){
            s.setQuantity((int)(s.getQuantity().intValue()*changeRate.floatValue()));
            repository.updateOneShipmentQuantity(s);
            updateShipment(changeRate, s);
        }

    }

    public Trade getTrade(Long tradeid){

        return new Trade(repository.findRootShipmentByTradeid(tradeid));
    }
}
