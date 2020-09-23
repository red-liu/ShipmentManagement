package com.redliu.shipmentmanagement.controller;

import com.redliu.shipmentmanagement.model.Shipment;
import com.redliu.shipmentmanagement.model.Trade;
import com.redliu.shipmentmanagement.service.ShipmentManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping(path = "/shipmentmanagement")
public class ShipmentController
{
    @Autowired
    private ShipmentManagementService shipService;

    @GetMapping(path="/{id}", produces = "application/json")
    public Trade getTrade(@PathVariable Long id)
    {
        return shipService.getTrade(id);
    }


    @PostMapping(path="/initialtrade/", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Object> initialShipment(@RequestBody Shipment shipment){
        shipService.initialOneShipment(shipment);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path="/splittrade/", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Object> splitShipment(@RequestBody List<Shipment> shipmentlist){
        shipService.splitShipment(shipmentlist);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path="/merge/", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Object> mergeShipment(@RequestBody List<Shipment> shipmentlist){
        shipService.mergeShipment(shipmentlist);
        return ResponseEntity.ok().build();
    }

//    @PostMapping(path="/", produces = "application/json", consumes = "application/json")
//    public ResponseEntity<Object> InitialShipment(@RequestBody Shipment shipment){
//        shipService.initialOneShipment(shipment);
//        return ResponseEntity.ok().build();
//    }

    @PostMapping(path = "/changeQuantity/", produces = "application/json", consumes = "application/json")
    public Trade updateRootQuanntity( @RequestBody Trade trade) {
        shipService.changeRootQuantity(trade);
        return shipService.getTrade(trade.getTraceid());

    }
}