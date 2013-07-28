/**
 * UnitEvent.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library;

public class UnitEvent  implements java.io.Serializable {
    private org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Bullet bullet;

    private java.lang.Integer tickTime;

    private org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Unit unit;

    public UnitEvent() {
    }

    public UnitEvent(
           org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Bullet bullet,
           java.lang.Integer tickTime,
           org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Unit unit) {
           this.bullet = bullet;
           this.tickTime = tickTime;
           this.unit = unit;
    }


    /**
     * Gets the bullet value for this UnitEvent.
     * 
     * @return bullet
     */
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Bullet getBullet() {
        return bullet;
    }


    /**
     * Sets the bullet value for this UnitEvent.
     * 
     * @param bullet
     */
    public void setBullet(org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Bullet bullet) {
        this.bullet = bullet;
    }


    /**
     * Gets the tickTime value for this UnitEvent.
     * 
     * @return tickTime
     */
    public java.lang.Integer getTickTime() {
        return tickTime;
    }


    /**
     * Sets the tickTime value for this UnitEvent.
     * 
     * @param tickTime
     */
    public void setTickTime(java.lang.Integer tickTime) {
        this.tickTime = tickTime;
    }


    /**
     * Gets the unit value for this UnitEvent.
     * 
     * @return unit
     */
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Unit getUnit() {
        return unit;
    }


    /**
     * Sets the unit value for this UnitEvent.
     * 
     * @param unit
     */
    public void setUnit(org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Unit unit) {
        this.unit = unit;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UnitEvent)) return false;
        UnitEvent other = (UnitEvent) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.bullet==null && other.getBullet()==null) || 
             (this.bullet!=null &&
              this.bullet.equals(other.getBullet()))) &&
            ((this.tickTime==null && other.getTickTime()==null) || 
             (this.tickTime!=null &&
              this.tickTime.equals(other.getTickTime()))) &&
            ((this.unit==null && other.getUnit()==null) || 
             (this.unit!=null &&
              this.unit.equals(other.getUnit())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getBullet() != null) {
            _hashCode += getBullet().hashCode();
        }
        if (getTickTime() != null) {
            _hashCode += getTickTime().hashCode();
        }
        if (getUnit() != null) {
            _hashCode += getUnit().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UnitEvent.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "UnitEvent"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bullet");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Bullet"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Bullet"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tickTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "TickTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unit");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Unit"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Unit"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
