/**
 * GameInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library;

public class GameInfo  implements java.io.Serializable {
    private java.lang.String boardName;

    private java.lang.Integer currentTick;

    private java.lang.String[] enemies;

    private java.lang.Integer engineTime;

    private org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Events events;

    private java.lang.Integer ID;

    private java.lang.Boolean isBussy;

    private java.lang.Boolean isComplete;

    private java.lang.Boolean isRunning;

    private java.lang.Boolean isTimeOut;

    private java.util.Calendar nextTickTime;

    private java.util.Calendar requestTime;

    private org.apache.axis.types.Duration timeToNextTick;

    private org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Unit[] units;

    public GameInfo() {
    }

    public GameInfo(
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
           org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Unit[] units) {
           this.boardName = boardName;
           this.currentTick = currentTick;
           this.enemies = enemies;
           this.engineTime = engineTime;
           this.events = events;
           this.ID = ID;
           this.isBussy = isBussy;
           this.isComplete = isComplete;
           this.isRunning = isRunning;
           this.isTimeOut = isTimeOut;
           this.nextTickTime = nextTickTime;
           this.requestTime = requestTime;
           this.timeToNextTick = timeToNextTick;
           this.units = units;
    }


    /**
     * Gets the boardName value for this GameInfo.
     * 
     * @return boardName
     */
    public java.lang.String getBoardName() {
        return boardName;
    }


    /**
     * Sets the boardName value for this GameInfo.
     * 
     * @param boardName
     */
    public void setBoardName(java.lang.String boardName) {
        this.boardName = boardName;
    }


    /**
     * Gets the currentTick value for this GameInfo.
     * 
     * @return currentTick
     */
    public java.lang.Integer getCurrentTick() {
        return currentTick;
    }


    /**
     * Sets the currentTick value for this GameInfo.
     * 
     * @param currentTick
     */
    public void setCurrentTick(java.lang.Integer currentTick) {
        this.currentTick = currentTick;
    }


    /**
     * Gets the enemies value for this GameInfo.
     * 
     * @return enemies
     */
    public java.lang.String[] getEnemies() {
        return enemies;
    }


    /**
     * Sets the enemies value for this GameInfo.
     * 
     * @param enemies
     */
    public void setEnemies(java.lang.String[] enemies) {
        this.enemies = enemies;
    }


    /**
     * Gets the engineTime value for this GameInfo.
     * 
     * @return engineTime
     */
    public java.lang.Integer getEngineTime() {
        return engineTime;
    }


    /**
     * Sets the engineTime value for this GameInfo.
     * 
     * @param engineTime
     */
    public void setEngineTime(java.lang.Integer engineTime) {
        this.engineTime = engineTime;
    }


    /**
     * Gets the events value for this GameInfo.
     * 
     * @return events
     */
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Events getEvents() {
        return events;
    }


    /**
     * Sets the events value for this GameInfo.
     * 
     * @param events
     */
    public void setEvents(org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Events events) {
        this.events = events;
    }


    /**
     * Gets the ID value for this GameInfo.
     * 
     * @return ID
     */
    public java.lang.Integer getID() {
        return ID;
    }


    /**
     * Sets the ID value for this GameInfo.
     * 
     * @param ID
     */
    public void setID(java.lang.Integer ID) {
        this.ID = ID;
    }


    /**
     * Gets the isBussy value for this GameInfo.
     * 
     * @return isBussy
     */
    public java.lang.Boolean getIsBussy() {
        return isBussy;
    }


    /**
     * Sets the isBussy value for this GameInfo.
     * 
     * @param isBussy
     */
    public void setIsBussy(java.lang.Boolean isBussy) {
        this.isBussy = isBussy;
    }


    /**
     * Gets the isComplete value for this GameInfo.
     * 
     * @return isComplete
     */
    public java.lang.Boolean getIsComplete() {
        return isComplete;
    }


    /**
     * Sets the isComplete value for this GameInfo.
     * 
     * @param isComplete
     */
    public void setIsComplete(java.lang.Boolean isComplete) {
        this.isComplete = isComplete;
    }


    /**
     * Gets the isRunning value for this GameInfo.
     * 
     * @return isRunning
     */
    public java.lang.Boolean getIsRunning() {
        return isRunning;
    }


    /**
     * Sets the isRunning value for this GameInfo.
     * 
     * @param isRunning
     */
    public void setIsRunning(java.lang.Boolean isRunning) {
        this.isRunning = isRunning;
    }


    /**
     * Gets the isTimeOut value for this GameInfo.
     * 
     * @return isTimeOut
     */
    public java.lang.Boolean getIsTimeOut() {
        return isTimeOut;
    }


    /**
     * Sets the isTimeOut value for this GameInfo.
     * 
     * @param isTimeOut
     */
    public void setIsTimeOut(java.lang.Boolean isTimeOut) {
        this.isTimeOut = isTimeOut;
    }


    /**
     * Gets the nextTickTime value for this GameInfo.
     * 
     * @return nextTickTime
     */
    public java.util.Calendar getNextTickTime() {
        return nextTickTime;
    }


    /**
     * Sets the nextTickTime value for this GameInfo.
     * 
     * @param nextTickTime
     */
    public void setNextTickTime(java.util.Calendar nextTickTime) {
        this.nextTickTime = nextTickTime;
    }


    /**
     * Gets the requestTime value for this GameInfo.
     * 
     * @return requestTime
     */
    public java.util.Calendar getRequestTime() {
        return requestTime;
    }


    /**
     * Sets the requestTime value for this GameInfo.
     * 
     * @param requestTime
     */
    public void setRequestTime(java.util.Calendar requestTime) {
        this.requestTime = requestTime;
    }


    /**
     * Gets the timeToNextTick value for this GameInfo.
     * 
     * @return timeToNextTick
     */
    public org.apache.axis.types.Duration getTimeToNextTick() {
        return timeToNextTick;
    }


    /**
     * Sets the timeToNextTick value for this GameInfo.
     * 
     * @param timeToNextTick
     */
    public void setTimeToNextTick(org.apache.axis.types.Duration timeToNextTick) {
        this.timeToNextTick = timeToNextTick;
    }


    /**
     * Gets the units value for this GameInfo.
     * 
     * @return units
     */
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Unit[] getUnits() {
        return units;
    }


    /**
     * Sets the units value for this GameInfo.
     * 
     * @param units
     */
    public void setUnits(org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Unit[] units) {
        this.units = units;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GameInfo)) return false;
        GameInfo other = (GameInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.boardName==null && other.getBoardName()==null) || 
             (this.boardName!=null &&
              this.boardName.equals(other.getBoardName()))) &&
            ((this.currentTick==null && other.getCurrentTick()==null) || 
             (this.currentTick!=null &&
              this.currentTick.equals(other.getCurrentTick()))) &&
            ((this.enemies==null && other.getEnemies()==null) || 
             (this.enemies!=null &&
              java.util.Arrays.equals(this.enemies, other.getEnemies()))) &&
            ((this.engineTime==null && other.getEngineTime()==null) || 
             (this.engineTime!=null &&
              this.engineTime.equals(other.getEngineTime()))) &&
            ((this.events==null && other.getEvents()==null) || 
             (this.events!=null &&
              this.events.equals(other.getEvents()))) &&
            ((this.ID==null && other.getID()==null) || 
             (this.ID!=null &&
              this.ID.equals(other.getID()))) &&
            ((this.isBussy==null && other.getIsBussy()==null) || 
             (this.isBussy!=null &&
              this.isBussy.equals(other.getIsBussy()))) &&
            ((this.isComplete==null && other.getIsComplete()==null) || 
             (this.isComplete!=null &&
              this.isComplete.equals(other.getIsComplete()))) &&
            ((this.isRunning==null && other.getIsRunning()==null) || 
             (this.isRunning!=null &&
              this.isRunning.equals(other.getIsRunning()))) &&
            ((this.isTimeOut==null && other.getIsTimeOut()==null) || 
             (this.isTimeOut!=null &&
              this.isTimeOut.equals(other.getIsTimeOut()))) &&
            ((this.nextTickTime==null && other.getNextTickTime()==null) || 
             (this.nextTickTime!=null &&
              this.nextTickTime.equals(other.getNextTickTime()))) &&
            ((this.requestTime==null && other.getRequestTime()==null) || 
             (this.requestTime!=null &&
              this.requestTime.equals(other.getRequestTime()))) &&
            ((this.timeToNextTick==null && other.getTimeToNextTick()==null) || 
             (this.timeToNextTick!=null &&
              this.timeToNextTick.equals(other.getTimeToNextTick()))) &&
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
        if (getBoardName() != null) {
            _hashCode += getBoardName().hashCode();
        }
        if (getCurrentTick() != null) {
            _hashCode += getCurrentTick().hashCode();
        }
        if (getEnemies() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEnemies());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEnemies(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getEngineTime() != null) {
            _hashCode += getEngineTime().hashCode();
        }
        if (getEvents() != null) {
            _hashCode += getEvents().hashCode();
        }
        if (getID() != null) {
            _hashCode += getID().hashCode();
        }
        if (getIsBussy() != null) {
            _hashCode += getIsBussy().hashCode();
        }
        if (getIsComplete() != null) {
            _hashCode += getIsComplete().hashCode();
        }
        if (getIsRunning() != null) {
            _hashCode += getIsRunning().hashCode();
        }
        if (getIsTimeOut() != null) {
            _hashCode += getIsTimeOut().hashCode();
        }
        if (getNextTickTime() != null) {
            _hashCode += getNextTickTime().hashCode();
        }
        if (getRequestTime() != null) {
            _hashCode += getRequestTime().hashCode();
        }
        if (getTimeToNextTick() != null) {
            _hashCode += getTimeToNextTick().hashCode();
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
        new org.apache.axis.description.TypeDesc(GameInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "GameInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("boardName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "BoardName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("currentTick");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "CurrentTick"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("enemies");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Enemies"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.microsoft.com/2003/10/Serialization/Arrays", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("engineTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "EngineTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("events");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Events"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Events"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isBussy");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "IsBussy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isComplete");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "IsComplete"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isRunning");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "IsRunning"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isTimeOut");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "IsTimeOut"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nextTickTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "NextTickTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("requestTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "RequestTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeToNextTick");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "TimeToNextTick"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "duration"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("units");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Units"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Unit"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/RoTeD.BattleCity.Library", "Unit"));
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
