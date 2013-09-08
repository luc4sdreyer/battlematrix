/**
 * Board.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package za.co.entelect.challenge;

public class Board  implements java.io.Serializable {
    private int endGamePoint;

    private za.co.entelect.challenge.StateArray[] states;

    public Board() {
    }

    public Board(
           int endGamePoint,
           za.co.entelect.challenge.StateArray[] states) {
           this.endGamePoint = endGamePoint;
           this.states = states;
    }


    /**
     * Gets the endGamePoint value for this Board.
     * 
     * @return endGamePoint
     */
    public int getEndGamePoint() {
        return endGamePoint;
    }


    /**
     * Sets the endGamePoint value for this Board.
     * 
     * @param endGamePoint
     */
    public void setEndGamePoint(int endGamePoint) {
        this.endGamePoint = endGamePoint;
    }


    /**
     * Gets the states value for this Board.
     * 
     * @return states
     */
    public za.co.entelect.challenge.StateArray[] getStates() {
        return states;
    }


    /**
     * Sets the states value for this Board.
     * 
     * @param states
     */
    public void setStates(za.co.entelect.challenge.StateArray[] states) {
        this.states = states;
    }

    public za.co.entelect.challenge.StateArray getStates(int i) {
        return this.states[i];
    }

    public void setStates(int i, za.co.entelect.challenge.StateArray _value) {
        this.states[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Board)) return false;
        Board other = (Board) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.endGamePoint == other.getEndGamePoint() &&
            ((this.states==null && other.getStates()==null) || 
             (this.states!=null &&
              java.util.Arrays.equals(this.states, other.getStates())));
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
        _hashCode += getEndGamePoint();
        if (getStates() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getStates());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getStates(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Board.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://challenge.entelect.co.za/", "board"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endGamePoint");
        elemField.setXmlName(new javax.xml.namespace.QName("", "endGamePoint"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("states");
        elemField.setXmlName(new javax.xml.namespace.QName("", "states"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://challenge.entelect.co.za/", "stateArray"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
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
