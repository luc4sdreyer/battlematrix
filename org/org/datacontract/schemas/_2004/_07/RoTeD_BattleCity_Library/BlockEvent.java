/**
 * BlockEvent.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library;

public class BlockEvent  implements java.io.Serializable {
    private java.lang.Integer eventTick;

    private org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.States newState;

    private java.lang.Boolean newStateSpecified;

    private org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Point point;

    public BlockEvent() {
    }

    public BlockEvent(
           java.lang.Integer eventTick,
           org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.States newState,
           java.lang.Boolean newStateSpecified,
           org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Point point) {
           this.eventTick = eventTick;
           this.newState = newState;
           this.newStateSpecified = newStateSpecified;
           this.point = point;
    }


    /**
     * Gets the eventTick value for this BlockEvent.
     * 
     * @return eventTick
     */
    public java.lang.Integer getEventTick() {
        return eventTick;
    }


    /**
     * Sets the eventTick value for this BlockEvent.
     * 
     * @param eventTick
     */
    public void setEventTick(java.lang.Integer eventTick) {
        this.eventTick = eventTick;
    }


    /**
     * Gets the newState value for this BlockEvent.
     * 
     * @return newState
     */
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.States getNewState() {
        return newState;
    }


    /**
     * Sets the newState value for this BlockEvent.
     * 
     * @param newState
     */
    public void setNewState(org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.States newState) {
        this.newState = newState;
    }


    /**
     * Gets the newStateSpecified value for this BlockEvent.
     * 
     * @return newStateSpecified
     */
    public java.lang.Boolean getNewStateSpecified() {
        return newStateSpecified;
    }


    /**
     * Sets the newStateSpecified value for this BlockEvent.
     * 
     * @param newStateSpecified
     */
    public void setNewStateSpecified(java.lang.Boolean newStateSpecified) {
        this.newStateSpecified = newStateSpecified;
    }


    /**
     * Gets the point value for this BlockEvent.
     * 
     * @return point
     */
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Point getPoint() {
        return point;
    }


    /**
     * Sets the point value for this BlockEvent.
     * 
     * @param point
     */
    public void setPoint(org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Point point) {
        this.point = point;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BlockEvent)) return false;
        BlockEvent other = (BlockEvent) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.eventTick==null && other.getEventTick()==null) || 
             (this.eventTick!=null &&
              this.eventTick.equals(other.getEventTick()))) &&
            ((this.newState==null && other.getNewState()==null) || 
             (this.newState!=null &&
              this.newState.equals(other.getNewState()))) &&
            ((this.newStateSpecified==null && other.getNewStateSpecified()==null) || 
             (this.newStateSpecified!=null &&
              this.newStateSpecified.equals(other.getNewStateSpecified()))) &&
            ((this.point==null && other.getPoint()==null) || 
             (this.point!=null &&
              this.point.equals(other.getPoint())));
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
        if (getEventTick() != null) {
            _hashCode += getEventTick().hashCode();
        }
        if (getNewState() != null) {
            _hashCode += getNewState().hashCode();
        }
        if (getNewStateSpecified() != null) {
            _hashCode += getNewStateSpecified().hashCode();
        }
        if (getPoint() != null) {
            _hashCode += getPoint().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BlockEvent.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "BlockEvent"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("eventTick");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "EventTick"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("newState");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "NewState"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "States"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("newStateSpecified");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "NewStateSpecified"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("point");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Point"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Point"));
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
