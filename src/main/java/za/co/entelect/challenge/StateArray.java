/**
 * StateArray.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package za.co.entelect.challenge;

public class StateArray  implements java.io.Serializable {
    private za.co.entelect.challenge.State[] item;

    public StateArray() {
    }

    public StateArray(
           za.co.entelect.challenge.State[] item) {
           this.item = item;
    }


    /**
     * Gets the item value for this StateArray.
     * 
     * @return item
     */
    public za.co.entelect.challenge.State[] getItem() {
        return item;
    }


    /**
     * Sets the item value for this StateArray.
     * 
     * @param item
     */
    public void setItem(za.co.entelect.challenge.State[] item) {
        this.item = item;
    }

    public za.co.entelect.challenge.State getItem(int i) {
        return this.item[i];
    }

    public void setItem(int i, za.co.entelect.challenge.State _value) {
        this.item[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StateArray)) return false;
        StateArray other = (StateArray) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.item==null && other.getItem()==null) || 
             (this.item!=null &&
              java.util.Arrays.equals(this.item, other.getItem())));
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
        if (getItem() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getItem());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getItem(), i);
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
        new org.apache.axis.description.TypeDesc(StateArray.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://challenge.entelect.co.za/", "stateArray"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item");
        elemField.setXmlName(new javax.xml.namespace.QName("", "item"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://challenge.entelect.co.za/", "state"));
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
