/**
 * Game.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package za.co.entelect.challenge;

public class Game  implements java.io.Serializable {
    private int currentTick;

    private za.co.entelect.challenge.Events events;

    private long millisecondsToNextTick;

    private java.util.Calendar nextTickTime;

    private java.lang.String playerName;

    private za.co.entelect.challenge.Player[] players;

    public Game() {
    }

    public Game(
           int currentTick,
           za.co.entelect.challenge.Events events,
           long millisecondsToNextTick,
           java.util.Calendar nextTickTime,
           java.lang.String playerName,
           za.co.entelect.challenge.Player[] players) {
           this.currentTick = currentTick;
           this.events = events;
           this.millisecondsToNextTick = millisecondsToNextTick;
           this.nextTickTime = nextTickTime;
           this.playerName = playerName;
           this.players = players;
    }


    /**
     * Gets the currentTick value for this Game.
     * 
     * @return currentTick
     */
    public int getCurrentTick() {
        return currentTick;
    }


    /**
     * Sets the currentTick value for this Game.
     * 
     * @param currentTick
     */
    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }


    /**
     * Gets the events value for this Game.
     * 
     * @return events
     */
    public za.co.entelect.challenge.Events getEvents() {
        return events;
    }


    /**
     * Sets the events value for this Game.
     * 
     * @param events
     */
    public void setEvents(za.co.entelect.challenge.Events events) {
        this.events = events;
    }


    /**
     * Gets the millisecondsToNextTick value for this Game.
     * 
     * @return millisecondsToNextTick
     */
    public long getMillisecondsToNextTick() {
        return millisecondsToNextTick;
    }


    /**
     * Sets the millisecondsToNextTick value for this Game.
     * 
     * @param millisecondsToNextTick
     */
    public void setMillisecondsToNextTick(long millisecondsToNextTick) {
        this.millisecondsToNextTick = millisecondsToNextTick;
    }


    /**
     * Gets the nextTickTime value for this Game.
     * 
     * @return nextTickTime
     */
    public java.util.Calendar getNextTickTime() {
        return nextTickTime;
    }


    /**
     * Sets the nextTickTime value for this Game.
     * 
     * @param nextTickTime
     */
    public void setNextTickTime(java.util.Calendar nextTickTime) {
        this.nextTickTime = nextTickTime;
    }


    /**
     * Gets the playerName value for this Game.
     * 
     * @return playerName
     */
    public java.lang.String getPlayerName() {
        return playerName;
    }


    /**
     * Sets the playerName value for this Game.
     * 
     * @param playerName
     */
    public void setPlayerName(java.lang.String playerName) {
        this.playerName = playerName;
    }


    /**
     * Gets the players value for this Game.
     * 
     * @return players
     */
    public za.co.entelect.challenge.Player[] getPlayers() {
        return players;
    }


    /**
     * Sets the players value for this Game.
     * 
     * @param players
     */
    public void setPlayers(za.co.entelect.challenge.Player[] players) {
        this.players = players;
    }

    public za.co.entelect.challenge.Player getPlayers(int i) {
        return this.players[i];
    }

    public void setPlayers(int i, za.co.entelect.challenge.Player _value) {
        this.players[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Game)) return false;
        Game other = (Game) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.currentTick == other.getCurrentTick() &&
            ((this.events==null && other.getEvents()==null) || 
             (this.events!=null &&
              this.events.equals(other.getEvents()))) &&
            this.millisecondsToNextTick == other.getMillisecondsToNextTick() &&
            ((this.nextTickTime==null && other.getNextTickTime()==null) || 
             (this.nextTickTime!=null &&
              this.nextTickTime.equals(other.getNextTickTime()))) &&
            ((this.playerName==null && other.getPlayerName()==null) || 
             (this.playerName!=null &&
              this.playerName.equals(other.getPlayerName()))) &&
            ((this.players==null && other.getPlayers()==null) || 
             (this.players!=null &&
              java.util.Arrays.equals(this.players, other.getPlayers())));
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
        _hashCode += getCurrentTick();
        if (getEvents() != null) {
            _hashCode += getEvents().hashCode();
        }
        _hashCode += new Long(getMillisecondsToNextTick()).hashCode();
        if (getNextTickTime() != null) {
            _hashCode += getNextTickTime().hashCode();
        }
        if (getPlayerName() != null) {
            _hashCode += getPlayerName().hashCode();
        }
        if (getPlayers() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPlayers());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPlayers(), i);
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
        new org.apache.axis.description.TypeDesc(Game.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://challenge.entelect.co.za/", "game"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("currentTick");
        elemField.setXmlName(new javax.xml.namespace.QName("", "currentTick"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("events");
        elemField.setXmlName(new javax.xml.namespace.QName("", "events"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://challenge.entelect.co.za/", "events"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("millisecondsToNextTick");
        elemField.setXmlName(new javax.xml.namespace.QName("", "millisecondsToNextTick"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nextTickTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nextTickTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("playerName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "playerName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("players");
        elemField.setXmlName(new javax.xml.namespace.QName("", "players"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://challenge.entelect.co.za/", "player"));
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
