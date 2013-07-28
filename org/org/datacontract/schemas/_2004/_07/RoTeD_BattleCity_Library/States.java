/**
 * States.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library;

public class States implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected States(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _OPEN = "OPEN";
    public static final java.lang.String _WALL = "WALL";
    public static final java.lang.String _ONE_BASE = "ONE_BASE";
    public static final java.lang.String _ONE_TANK_UP = "ONE_TANK_UP";
    public static final java.lang.String _ONE_TANK_RIGHT = "ONE_TANK_RIGHT";
    public static final java.lang.String _ONE_TANK_DOWN = "ONE_TANK_DOWN";
    public static final java.lang.String _ONE_TANK_LEFT = "ONE_TANK_LEFT";
    public static final java.lang.String _TWO_BASE = "TWO_BASE";
    public static final java.lang.String _TWO_TANK_UP = "TWO_TANK_UP";
    public static final java.lang.String _TWO_TANK_RIGHT = "TWO_TANK_RIGHT";
    public static final java.lang.String _TWO_TANK_DOWN = "TWO_TANK_DOWN";
    public static final java.lang.String _TWO_TANK_LEFT = "TWO_TANK_LEFT";
    public static final java.lang.String _BULLET_UP = "BULLET_UP";
    public static final java.lang.String _BULLET_RIGHT = "BULLET_RIGHT";
    public static final java.lang.String _BULLET_DOWN = "BULLET_DOWN";
    public static final java.lang.String _BULLET_LEFT = "BULLET_LEFT";
    public static final States OPEN = new States(_OPEN);
    public static final States WALL = new States(_WALL);
    public static final States ONE_BASE = new States(_ONE_BASE);
    public static final States ONE_TANK_UP = new States(_ONE_TANK_UP);
    public static final States ONE_TANK_RIGHT = new States(_ONE_TANK_RIGHT);
    public static final States ONE_TANK_DOWN = new States(_ONE_TANK_DOWN);
    public static final States ONE_TANK_LEFT = new States(_ONE_TANK_LEFT);
    public static final States TWO_BASE = new States(_TWO_BASE);
    public static final States TWO_TANK_UP = new States(_TWO_TANK_UP);
    public static final States TWO_TANK_RIGHT = new States(_TWO_TANK_RIGHT);
    public static final States TWO_TANK_DOWN = new States(_TWO_TANK_DOWN);
    public static final States TWO_TANK_LEFT = new States(_TWO_TANK_LEFT);
    public static final States BULLET_UP = new States(_BULLET_UP);
    public static final States BULLET_RIGHT = new States(_BULLET_RIGHT);
    public static final States BULLET_DOWN = new States(_BULLET_DOWN);
    public static final States BULLET_LEFT = new States(_BULLET_LEFT);
    public java.lang.String getValue() { return _value_;}
    public static States fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        States enumeration = (States)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static States fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(States.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "States"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
