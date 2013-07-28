/**
 * ReturnValues.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library;

public class ReturnValues implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ReturnValues(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _SUCCESS = "SUCCESS";
    public static final java.lang.String _INVALID_NAME = "INVALID_NAME";
    public static final java.lang.String _INVALID_KEY = "INVALID_KEY";
    public static final java.lang.String _INVALID_CHALLENGE = "INVALID_CHALLENGE";
    public static final java.lang.String _NOT_ONLINE = "NOT_ONLINE";
    public static final java.lang.String _BUSSY = "BUSSY";
    public static final java.lang.String _INVALID_BOARD = "INVALID_BOARD";
    public static final java.lang.String _NO_GAME = "NO_GAME";
    public static final java.lang.String _HAS_GAME = "HAS_GAME";
    public static final java.lang.String _FAILED = "FAILED";
    public static final ReturnValues SUCCESS = new ReturnValues(_SUCCESS);
    public static final ReturnValues INVALID_NAME = new ReturnValues(_INVALID_NAME);
    public static final ReturnValues INVALID_KEY = new ReturnValues(_INVALID_KEY);
    public static final ReturnValues INVALID_CHALLENGE = new ReturnValues(_INVALID_CHALLENGE);
    public static final ReturnValues NOT_ONLINE = new ReturnValues(_NOT_ONLINE);
    public static final ReturnValues BUSSY = new ReturnValues(_BUSSY);
    public static final ReturnValues INVALID_BOARD = new ReturnValues(_INVALID_BOARD);
    public static final ReturnValues NO_GAME = new ReturnValues(_NO_GAME);
    public static final ReturnValues HAS_GAME = new ReturnValues(_HAS_GAME);
    public static final ReturnValues FAILED = new ReturnValues(_FAILED);
    public java.lang.String getValue() { return _value_;}
    public static ReturnValues fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        ReturnValues enumeration = (ReturnValues)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static ReturnValues fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(ReturnValues.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "ReturnValues"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
