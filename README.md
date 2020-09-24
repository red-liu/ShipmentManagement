
# ShipmentManagement
Design and implement “Shipment Management program with unit tests. 

db:
create table IF NOT EXISTS shipment
(
   SHIPMENTID BIGINT NOT NULL,
   TRADEID BIGINT NOT NULL,
   OP TINYINT NOT NULL, --init, split or merge operation
   OPCOUNT INTEGER NOT NULL, -- INCREASE ONE PER OP
   QUANTITY INTEGER NOT NULL,
   PRIMARY key(SHIPMENTID)
);

create table IF NOT EXISTS shipmentrelation
(
    shipmentid BIGINT NOT NULL,
    child BIGINT NOT NULL
);


work flow:

1.view a trade detail information, you can use restful url : /shipmentmanagement/{id} to access the project server.
   example paramter id:
            input  id = 1, you can get all shipment detail information of the trade.

2.  inital a shipment, url:/shipmentmanagement/initialtrade/
     example json data
          {
	"shipmentid": 2000,
	"opcount": 0,
	"quantity": 1000,
	"tradeid": 5,
	"operator": "Initial",
	"child": []
        }
    
        tradeid must be exist. shipmentid should  be specified by you.

3. split a shipment, url:/shipmentmanagement/splittrade/
    example json data:
        [{
	"shipmentid": 2000,
	"opcount": 0,
	"quantity": 800,
	"tradeid": 5,
	"operator": "Initial",
	"child": [{
		"shipmentid": 2001,
		"opcount": 1,
		"quantity": 500,
		"tradeid": 5,
		"operator": "Split",
		"child": []
	}, {
		"shipmentid": 2002,
		"opcount": 1,
		"quantity": 300,
		"tradeid": 5,
		"operator": "Split",
		"child": []
	}]
      }]

    note: many shipment may be split, so the whole json is a list. one element of the list is the shipment you want to split.
             child element should be a arraylist, the child is that you want to be splited like. 


4. merge shipments, url:/shipmentmanagement/merge/
     example json data:
            [{
	"shipmentid": 2001,
	"opcount": 1,
	"quantity": 500,
	"tradeid": 5,
	"operator": "Split",
	"child": [{
		"shipmentid": 2004,
		"opcount": 2,
		"quantity": 800,
		"tradeid": 5,
		"operator": "Merge",
		"child": []
	}]
	}, {
		"shipmentid": 2002,
		"opcount": 1,
		"quantity": 300,
		"tradeid": 5,
		"operator": "Split",
		"child": [{
			"shipmentid": 2004,
			"opcount": 2,
			"quantity": 800,
			"tradeid": 5,
			"operator": "Merge",
			"child": []
		}]
	}]
    
        note: Arraylist contains many elements which will be merge. every element contains some shipments will be emerged to one shipment.
                example data only contains one element  which be used to merged. for example shipmentid = 2001 and 2002, then be emerged to
               shipmentid = 2004, that is the same child shipment for the two shipments (shipmentid = 2001 and 2002).


5. change Root Quantity : url : /shipmentmanagement/changeQuantity/

       {
	"traceid": 5,
	"quantity": 80,
	"rootShipment": null
    }

    tradecid and quantity must be specified to update, rootShipment can be specified to null.

     
         

