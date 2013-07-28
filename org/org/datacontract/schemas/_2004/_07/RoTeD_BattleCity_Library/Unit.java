/**
 * Unit.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library;

public class Unit  implements java.io.Serializable {
    private org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Actions action;

    private java.lang.Boolean actionSpecified;

    private java.lang.Boolean canFire;

    private org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Directions direction;

    private java.lang.Integer ID;

    private org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Point point;

    public Unit() {
    }

    public Unit(
           org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Actions action,
           java.lang.Boolean actionSpecified,
           java.lang.Boolean canFire,
           org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Directions direction,
           java.lang.Integer ID,
           org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Point point) {
           this.action = action;
           this.actionSpecified = actionSpecified;
           this.canFire = canFire;
           this.direction = direction;
           this.ID = ID;
           this.point = point;
    }


    /**
     * Gets the action value for this Unit.
     * 
     * @return action
     */
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Actions getAction() {
        return action;
    }


    /**
     * Sets the action value for this Unit.
     * 
     * @param action
     */
    public void setAction(org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Actions action) {
        this.action = action;
    }


    /**
     * Gets the actionSpecified value for this Unit.
     * 
     * @return actionSpecified
     */
    public java.lang.Boolean getActionSpecified() {
        return actionSpecified;
    }


    /**
     * Sets the actionSpecified value for this Unit.
     * 
     * @param actionSpecified
     */
    public void setActionSpecified(java.lang.Boolean actionSpecified) {
        this.actionSpecified = actionSpecified;
    }


    /**
     * Gets the canFire value for this Unit.
     * 
     * @return canFire
     */
    public java.lang.Boolean getCanFire() {
        return canFire;
    }


    /**
     * Sets the canFire value for this Unit.
     * 
     * @param canFire
     */
    public void setCanFire(java.lang.Boolean canFire) {
        this.canFire = canFire;
    }


    /**
     * Gets the direction value for this Unit.
     * 
     * @return direction
     */
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Directions getDirection() {
        return direction;
    }


    /**
     * Sets the direction value for this Unit.
     * 
     * @param direction
     */
    public void setDirection(org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Directions direction) {
        this.direction = direction;
    }


    /**
     * Gets the ID value for this Unit.
     * 
     * @return ID
     */
    public java.lang.Integer getID() {
        return ID;
    }


    /**
     * Sets the ID value for this Unit.
     * 
     * @param ID
     */
    public void setID(java.lang.Integer ID) {
        this.ID = ID;
    }


    /**
     * Gets the point value for this Unit.
     * 
     * @return point
     */
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Point getPoint() {
        return point;
    }


    /**
     * Sets the point value for this Unit.
     * 
     * @param point
     */
    public void setPoint(org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Point point) {
        this.point = point;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Unit)) return false;
        Unit other = (Unit) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.action==null && other.getAction()==null) || 
             (this.action!=null &&
              this.action.equals(other.getAction()))) &&
            ((this.actionSpecified==null && other.getActionSpecified()==null) || 
             (this.actionSpecified!=null &&
              this.actionSpecified.equals(other.getActionSpecified()))) &&
            ((this.canFire==null && other.getCanFire()==null) || 
             (this.canFire!=null &&
              this.canFire.equals(other.getCanFire()))) &&
            ((this.direction==null && other.getDirection()==null) || 
             (this.direction!=null &&
              this.direction.equals(other.getDirection()))) &&
            ((this.ID==null && other.getID()==null) || 
             (this.ID!=null &&
              this.ID.equals(other.getID()))) &&
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
        if (getAction() != null) {
            _hashCode += getAction().hashCode();
        }
        if (getActionSpecified() != null) {
            _hashCode += getActionSpecified().hashCode();
        }
        if (getCanFire() != null) {
            _hashCode += getCanFire().hashCode();
        }
        if (getDirection() != null) {
            _hashCode += getDirection().hashCode();
        }
        if (getID() != null) {
            _hashCode += getID().hashCode();
        }
        if (getPoint() != null) {
            _hashCode += getPoint().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Unit.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Unit"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("action");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Action"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Actions"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actionSpecified");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "ActionSpecified"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("canFire");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "CanFire"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("direction");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Direction"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Directions"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
