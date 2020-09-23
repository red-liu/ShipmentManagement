package com.redliu.shipmentmanagement.repository;

import com.redliu.shipmentmanagement.model.Shipment;
import com.redliu.shipmentmanagement.model.Trade;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShipmentRepository {

    @Select("select * from shipment where shipmentid = #{shipmentid}")
    @Results({

            // 对shipmentid进行赋值
            @Result(property = "shipmentid", column = "shipmentid"),

            @Result(property = "child", column = "shipmentid",
                    many = @Many(select = "com.redliui.shipmentmanagement.repository.ShipmentRepository.getChildById"))


    })
    public Shipment findShipmentByShipid(Long shipmentid);



    @Select("select * from shipment where tradeid = #{tradeid} and opcount=0")
    @Results({
            @Result(property = "tradeid", column = "tradeid"),
            @Result(property = "shipmentid", column = "shipmentid"),
            @Result(property = "child", column = "shipmentid",
                    many = @Many(select = "com.redliu.shipmentmanagement.repository.ShipmentRepository.getChildById"))
    })
    public Shipment findRootShipmentByTradeid(Long tradeid);



    @Select("SELECT * FROM SHIPMENT WHERE shipmentid IN (SELECT child FROM shipmentrelation WHERE shipmentid = #{shipmentid})")
    @Results({
            @Result(property = "tradeid", column = "tradeid"),
            @Result(property = "shipmentid", column = "shipmentid"),
            @Result(property = "child", column = "shipmentid",
                    many = @Many(select = "com.redliu.shipmentmanagement.repository.ShipmentRepository.getChildById"))
    })
    public List<Shipment> getChildById(Integer shipmentid);

    @Insert("Insert into shipment (SHIPMENTID, TRADEID, OP, OPCOUNT, QUANTITY) VALUES (#{shipmentid}, #{tradeid}, #{op}, #{opcount}, #{quantity})")
    public int insertOneShipment(Shipment shipment);

    @Insert({
            "<script>",
            "insert into shipmentrelation(shipmentid, child) values ",
            "<foreach collection='child' item='item' index='index' separator=','>",
            "(#{shipment.shipmentid}, #{item.shipmentid})",
            "</foreach>",
            "</script>"
    })
    public int insertShipmentRelations( Shipment shipment, @Param("child")List<Shipment> child);


    @Delete("Delete from shipmentrelation where shipmentid = #{id}")
    public int deleteShipmentRelations(Long shipmentid);

    @Update("Update shipment set quantity = #{shipment.quantity} where shipmentid=#{shipment.shipmentid}")
    public int updateOneShipmentQuantity(Shipment shipment);

}
