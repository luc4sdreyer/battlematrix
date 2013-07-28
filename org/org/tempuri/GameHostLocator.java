/**
 * GameHostLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public class GameHostLocator extends org.apache.axis.client.Service implements org.tempuri.GameHost {

    public GameHostLocator() {
    }


    public GameHostLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public GameHostLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for BasicHttpBinding_IGameInterface
    private java.lang.String BasicHttpBinding_IGameInterface_address = "http://ec2-176-34-161-166.eu-west-1.compute.amazonaws.com/BattleCity/WebService/BasicGameHost.svc";

    public java.lang.String getBasicHttpBinding_IGameInterfaceAddress() {
        return BasicHttpBinding_IGameInterface_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String BasicHttpBinding_IGameInterfaceWSDDServiceName = "BasicHttpBinding_IGameInterface";

    public java.lang.String getBasicHttpBinding_IGameInterfaceWSDDServiceName() {
        return BasicHttpBinding_IGameInterfaceWSDDServiceName;
    }

    public void setBasicHttpBinding_IGameInterfaceWSDDServiceName(java.lang.String name) {
        BasicHttpBinding_IGameInterfaceWSDDServiceName = name;
    }

    public org.tempuri.IGameInterface getBasicHttpBinding_IGameInterface() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(BasicHttpBinding_IGameInterface_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getBasicHttpBinding_IGameInterface(endpoint);
    }

    public org.tempuri.IGameInterface getBasicHttpBinding_IGameInterface(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.tempuri.BasicHttpBinding_IGameInterfaceStub _stub = new org.tempuri.BasicHttpBinding_IGameInterfaceStub(portAddress, this);
            _stub.setPortName(getBasicHttpBinding_IGameInterfaceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setBasicHttpBinding_IGameInterfaceEndpointAddress(java.lang.String address) {
        BasicHttpBinding_IGameInterface_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.tempuri.IGameInterface.class.isAssignableFrom(serviceEndpointInterface)) {
                org.tempuri.BasicHttpBinding_IGameInterfaceStub _stub = new org.tempuri.BasicHttpBinding_IGameInterfaceStub(new java.net.URL(BasicHttpBinding_IGameInterface_address), this);
                _stub.setPortName(getBasicHttpBinding_IGameInterfaceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("BasicHttpBinding_IGameInterface".equals(inputPortName)) {
            return getBasicHttpBinding_IGameInterface();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "GameHost");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "BasicHttpBinding_IGameInterface"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("BasicHttpBinding_IGameInterface".equals(portName)) {
            setBasicHttpBinding_IGameInterfaceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
