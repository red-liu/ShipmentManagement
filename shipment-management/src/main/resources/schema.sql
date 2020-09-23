drop table IF EXISTS shipment;
drop table IF EXISTS trade;
drop table IF EXISTS shipmentrelation;

create table IF NOT EXISTS trade
(
  TRADEID BIGINT NOT NULL,
  QUANTITY INTEGER not null, --QUANTITY
  PRIMARY  key(TRADEID)
);


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
