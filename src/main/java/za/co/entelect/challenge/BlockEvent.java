/**
 * BlockEvent.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package za.co.entelect.challenge;

public class BlockEvent  implements java.io.Serializable {
    private za.co.entelect.challenge.State newState;

    private za.co.entelect.challenge.Point point;

    public BlockEvent() {
    }

    public BlockEvent(
           za.co.entelect.challenge.State newState,
           za.co.entelect.challenge.Point point) {
           this.newState = newState;
           this.point = point;
    }


    /**
     * Gets the newState value for this BlockEvent.
     * 
     * @return newState
     */
    public za.co.entelect.challenge.State getNewState() {
        return newState;
    }


    /**
     * Sets the newState value for this BlockEvent.
     * 
     * @param newState
     */
    public void setNewState(za.co.entelect.challenge.State newState) {
        this.newState = newState;
    }


    /**
     * Gets the point value for this BlockEvent.
     * 
     * @return point
     */
    public za.co.entelect.challenge.Point getPoint() {
        return point;
    }


    /**
     * Sets the point value for this BlockEvent.
     * 
     * @param point
     */
    public void setPoint(za.co.entelect.challenge.Point point) {
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
            ((this.newState==null && other.getNewState()==null) || 
             (this.newState!=null &&
              this.newState.equals(other.getNewState()))) &&
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
        if (getNewState() != null) {
            _hashCode += getNewState().hashCode();
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
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://challenge.entelect.co.za/", "blockEvent"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("newState");
        elemField.setXmlName(new javax.xml.namespace.QName("", "newState"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://challenge.entelect.co.za/", "state"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("point");
        elemField.setXmlName(new javax.xml.namespace.QName("", "point"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://challenge.entelect.co.za/", "point"));
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
