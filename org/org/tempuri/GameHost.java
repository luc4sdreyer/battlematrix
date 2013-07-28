/**
 * GameHost.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public interface GameHost extends javax.xml.rpc.Service {
    public java.lang.String getBasicHttpBinding_IGameInterfaceAddress();

    public org.tempuri.IGameInterface getBasicHttpBinding_IGameInterface() throws javax.xml.rpc.ServiceException;

    public org.tempuri.IGameInterface getBasicHttpBinding_IGameInterface(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
