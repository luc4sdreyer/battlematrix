/**
 * UnitEvent.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package za.co.entelect.challenge;

public class UnitEvent  implements java.io.Serializable {
    private za.co.entelect.challenge.Bullet bullet;

    private int tickTime;

    private za.co.entelect.challenge.Unit unit;

    public UnitEvent() {
    }

    public UnitEvent(
           za.co.entelect.challenge.Bullet bullet,
           int tickTime,
           za.co.entelect.challenge.Unit unit) {
           this.bullet = bullet;
           this.tickTime = tickTime;
           this.unit = unit;
    }


    /**
     * Gets the bullet value for this UnitEvent.
     * 
     * @return bullet
     */
    public za.co.entelect.challenge.Bullet getBullet() {
        return bullet;
    }


    /**
     * Sets the bullet value for this UnitEvent.
     * 
     * @param bullet
     */
    public void setBullet(za.co.entelect.challenge.Bullet bullet) {
        this.bullet = bullet;
    }


    /**
     * Gets the tickTime value for this UnitEvent.
     * 
     * @return tickTime
     */
    public int getTickTime() {
        return tickTime;
    }


    /**
     * Sets the tickTime value for this UnitEvent.
     * 
     * @param tickTime
     */
    public void setTickTime(int tickTime) {
        this.tickTime = tickTime;
    }


    /**
     * Gets the unit value for this UnitEvent.
     * 
     * @return unit
     */
    public za.co.entelect.challenge.Unit getUnit() {
        return unit;
    }


    /**
     * Sets the unit value for this UnitEvent.
     * 
     * @param unit
     */
    public void setUnit(za.co.entelect.challenge.Unit unit) {
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
            this.tickTime == other.getTickTime() &&
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
        _hashCode += getTickTime();
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
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://challenge.entelect.co.za/", "unitEvent"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bullet");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bullet"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://challenge.entelect.co.za/", "bullet"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tickTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tickTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unit");
        elemField.setXmlName(new javax.xml.namespace.QName("", "unit"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://challenge.entelect.co.za/", "unit"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
