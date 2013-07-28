/**
 * GameInfoTickBoard.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library;

public class GameInfoTickBoard  extends org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.GameInfo  implements java.io.Serializable {
    private org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Board tickBoard;

    public GameInfoTickBoard() {
    }

    public GameInfoTickBoard(
           java.lang.String boardName,
           java.lang.Integer currentTick,
           java.lang.String[] enemies,
           java.lang.Integer engineTime,
           org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Events events,
           java.lang.Integer ID,
           java.lang.Boolean isBussy,
           java.lang.Boolean isComplete,
           java.lang.Boolean isRunning,
           java.lang.Boolean isTimeOut,
           java.util.Calendar nextTickTime,
           java.util.Calendar requestTime,
           org.apache.axis.types.Duration timeToNextTick,
           org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Unit[] units,
           org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Board tickBoard) {
        super(
            boardName,
            currentTick,
            enemies,
            engineTime,
            events,
            ID,
            isBussy,
            isComplete,
            isRunning,
            isTimeOut,
            nextTickTime,
            requestTime,
            timeToNextTick,
            units);
        this.tickBoard = tickBoard;
    }


    /**
     * Gets the tickBoard value for this GameInfoTickBoard.
     * 
     * @return tickBoard
     */
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Board getTickBoard() {
        return tickBoard;
    }


    /**
     * Sets the tickBoard value for this GameInfoTickBoard.
     * 
     * @param tickBoard
     */
    public void setTickBoard(org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Board tickBoard) {
        this.tickBoard = tickBoard;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GameInfoTickBoard)) return false;
        GameInfoTickBoard other = (GameInfoTickBoard) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.tickBoard==null && other.getTickBoard()==null) || 
             (this.tickBoard!=null &&
              this.tickBoard.equals(other.getTickBoard())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getTickBoard() != null) {
            _hashCode += getTickBoard().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GameInfoTickBoard.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "GameInfoTickBoard"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tickBoard");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "TickBoard"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Board"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
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
