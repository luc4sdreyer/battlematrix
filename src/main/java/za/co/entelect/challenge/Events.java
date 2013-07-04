/**
 * Events.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package za.co.entelect.challenge;

public class Events  implements java.io.Serializable {
    private za.co.entelect.challenge.BlockEvent[] blockEvents;

    private za.co.entelect.challenge.UnitEvent[] unitEvents;

    public Events() {
    }

    public Events(
           za.co.entelect.challenge.BlockEvent[] blockEvents,
           za.co.entelect.challenge.UnitEvent[] unitEvents) {
           this.blockEvents = blockEvents;
           this.unitEvents = unitEvents;
    }


    /**
     * Gets the blockEvents value for this Events.
     * 
     * @return blockEvents
     */
    public za.co.entelect.challenge.BlockEvent[] getBlockEvents() {
        return blockEvents;
    }


    /**
     * Sets the blockEvents value for this Events.
     * 
     * @param blockEvents
     */
    public void setBlockEvents(za.co.entelect.challenge.BlockEvent[] blockEvents) {
        this.blockEvents = blockEvents;
    }

    public za.co.entelect.challenge.BlockEvent getBlockEvents(int i) {
        return this.blockEvents[i];
    }

    public void setBlockEvents(int i, za.co.entelect.challenge.BlockEvent _value) {
        this.blockEvents[i] = _value;
    }


    /**
     * Gets the unitEvents value for this Events.
     * 
     * @return unitEvents
     */
    public za.co.entelect.challenge.UnitEvent[] getUnitEvents() {
        return unitEvents;
    }


    /**
     * Sets the unitEvents value for this Events.
     * 
     * @param unitEvents
     */
    public void setUnitEvents(za.co.entelect.challenge.UnitEvent[] unitEvents) {
        this.unitEvents = unitEvents;
    }

    public za.co.entelect.challenge.UnitEvent getUnitEvents(int i) {
        return this.unitEvents[i];
    }

    public void setUnitEvents(int i, za.co.entelect.challenge.UnitEvent _value) {
        this.unitEvents[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Events)) return false;
        Events other = (Events) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.blockEvents==null && other.getBlockEvents()==null) || 
             (this.blockEvents!=null &&
              java.util.Arrays.equals(this.blockEvents, other.getBlockEvents()))) &&
            ((this.unitEvents==null && other.getUnitEvents()==null) || 
             (this.unitEvents!=null &&
              java.util.Arrays.equals(this.unitEvents, other.getUnitEvents())));
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
        if (getBlockEvents() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBlockEvents());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBlockEvents(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getUnitEvents() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getUnitEvents());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getUnitEvents(), i);
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
        new org.apache.axis.description.TypeDesc(Events.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://challenge.entelect.co.za/", "events"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("blockEvents");
        elemField.setXmlName(new javax.xml.namespace.QName("", "blockEvents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://challenge.entelect.co.za/", "blockEvent"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unitEvents");
        elemField.setXmlName(new javax.xml.namespace.QName("", "unitEvents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://challenge.entelect.co.za/", "unitEvent"));
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
