/**
 * Player.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package za.co.entelect.challenge;

public class Player  implements java.io.Serializable {
    private za.co.entelect.challenge.Base base;

    private za.co.entelect.challenge.Bullet[] bullets;

    private java.lang.String name;

    private za.co.entelect.challenge.Unit[] units;

    public Player() {
    }

    public Player(
           za.co.entelect.challenge.Base base,
           za.co.entelect.challenge.Bullet[] bullets,
           java.lang.String name,
           za.co.entelect.challenge.Unit[] units) {
           this.base = base;
           this.bullets = bullets;
           this.name = name;
           this.units = units;
    }


    /**
     * Gets the base value for this Player.
     * 
     * @return base
     */
    public za.co.entelect.challenge.Base getBase() {
        return base;
    }


    /**
     * Sets the base value for this Player.
     * 
     * @param base
     */
    public void setBase(za.co.entelect.challenge.Base base) {
        this.base = base;
    }


    /**
     * Gets the bullets value for this Player.
     * 
     * @return bullets
     */
    public za.co.entelect.challenge.Bullet[] getBullets() {
        return bullets;
    }


    /**
     * Sets the bullets value for this Player.
     * 
     * @param bullets
     */
    public void setBullets(za.co.entelect.challenge.Bullet[] bullets) {
        this.bullets = bullets;
    }

    public za.co.entelect.challenge.Bullet getBullets(int i) {
        return this.bullets[i];
    }

    public void setBullets(int i, za.co.entelect.challenge.Bullet _value) {
        this.bullets[i] = _value;
    }


    /**
     * Gets the name value for this Player.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this Player.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the units value for this Player.
     * 
     * @return units
     */
    public za.co.entelect.challenge.Unit[] getUnits() {
        return units;
    }


    /**
     * Sets the units value for this Player.
     * 
     * @param units
     */
    public void setUnits(za.co.entelect.challenge.Unit[] units) {
        this.units = units;
    }

    public za.co.entelect.challenge.Unit getUnits(int i) {
        return this.units[i];
    }

    public void setUnits(int i, za.co.entelect.challenge.Unit _value) {
        this.units[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Player)) return false;
        Player other = (Player) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.base==null && other.getBase()==null) || 
             (this.base!=null &&
              this.base.equals(other.getBase()))) &&
            ((this.bullets==null && other.getBullets()==null) || 
             (this.bullets!=null &&
              java.util.Arrays.equals(this.bullets, other.getBullets()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.units==null && other.getUnits()==null) || 
             (this.units!=null &&
              java.util.Arrays.equals(this.units, other.getUnits())));
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
        if (getBase() != null) {
            _hashCode += getBase().hashCode();
        }
        if (getBullets() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBullets());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBullets(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getUnits() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getUnits());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getUnits(), i);
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
        new org.apache.axis.description.TypeDesc(Player.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://challenge.entelect.co.za/", "player"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("base");
        elemField.setXmlName(new javax.xml.namespace.QName("", "base"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://challenge.entelect.co.za/", "base"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bullets");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bullets"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://challenge.entelect.co.za/", "bullet"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("units");
        elemField.setXmlName(new javax.xml.namespace.QName("", "units"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://challenge.entelect.co.za/", "unit"));
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
