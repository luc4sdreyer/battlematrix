/**
 * Stat.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library;

public class Stat  implements java.io.Serializable {
    private java.lang.Integer drew;

    private java.lang.Integer lost;

    private java.lang.Integer won;

    public Stat() {
    }

    public Stat(
           java.lang.Integer drew,
           java.lang.Integer lost,
           java.lang.Integer won) {
           this.drew = drew;
           this.lost = lost;
           this.won = won;
    }


    /**
     * Gets the drew value for this Stat.
     * 
     * @return drew
     */
    public java.lang.Integer getDrew() {
        return drew;
    }


    /**
     * Sets the drew value for this Stat.
     * 
     * @param drew
     */
    public void setDrew(java.lang.Integer drew) {
        this.drew = drew;
    }


    /**
     * Gets the lost value for this Stat.
     * 
     * @return lost
     */
    public java.lang.Integer getLost() {
        return lost;
    }


    /**
     * Sets the lost value for this Stat.
     * 
     * @param lost
     */
    public void setLost(java.lang.Integer lost) {
        this.lost = lost;
    }


    /**
     * Gets the won value for this Stat.
     * 
     * @return won
     */
    public java.lang.Integer getWon() {
        return won;
    }


    /**
     * Sets the won value for this Stat.
     * 
     * @param won
     */
    public void setWon(java.lang.Integer won) {
        this.won = won;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Stat)) return false;
        Stat other = (Stat) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.drew==null && other.getDrew()==null) || 
             (this.drew!=null &&
              this.drew.equals(other.getDrew()))) &&
            ((this.lost==null && other.getLost()==null) || 
             (this.lost!=null &&
              this.lost.equals(other.getLost()))) &&
            ((this.won==null && other.getWon()==null) || 
             (this.won!=null &&
              this.won.equals(other.getWon())));
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
        if (getDrew() != null) {
            _hashCode += getDrew().hashCode();
        }
        if (getLost() != null) {
            _hashCode += getLost().hashCode();
        }
        if (getWon() != null) {
            _hashCode += getWon().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Stat.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Stat"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("drew");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Drew"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Lost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("won");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Won"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
