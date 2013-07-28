/**
 * Actions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library;

public class Actions implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected Actions(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _UP = "UP";
    public static final java.lang.String _RIGHT = "RIGHT";
    public static final java.lang.String _DOWN = "DOWN";
    public static final java.lang.String _LEFT = "LEFT";
    public static final java.lang.String _FIRE = "FIRE";
    public static final java.lang.String _NONE = "NONE";
    public static final Actions UP = new Actions(_UP);
    public static final Actions RIGHT = new Actions(_RIGHT);
    public static final Actions DOWN = new Actions(_DOWN);
    public static final Actions LEFT = new Actions(_LEFT);
    public static final Actions FIRE = new Actions(_FIRE);
    public static final Actions NONE = new Actions(_NONE);
    public java.lang.String getValue() { return _value_;}
    public static Actions fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        Actions enumeration = (Actions)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static Actions fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Actions.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Actions"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
