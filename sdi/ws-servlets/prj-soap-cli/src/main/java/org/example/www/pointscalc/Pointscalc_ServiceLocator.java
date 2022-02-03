/**
 * Pointscalc_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.example.www.pointscalc;

public class Pointscalc_ServiceLocator extends org.apache.axis.client.Service implements org.example.www.pointscalc.Pointscalc_Service {

    public Pointscalc_ServiceLocator() {
    }


    public Pointscalc_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public Pointscalc_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for pointscalcSOAP
    private java.lang.String pointscalcSOAP_address = "http://localhost:8080/prj-soap/services/pointscalcSOAP";

    public java.lang.String getpointscalcSOAPAddress() {
        return pointscalcSOAP_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String pointscalcSOAPWSDDServiceName = "pointscalcSOAP";

    public java.lang.String getpointscalcSOAPWSDDServiceName() {
        return pointscalcSOAPWSDDServiceName;
    }

    public void setpointscalcSOAPWSDDServiceName(java.lang.String name) {
        pointscalcSOAPWSDDServiceName = name;
    }

    public org.example.www.pointscalc.Pointscalc_PortType getpointscalcSOAP() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(pointscalcSOAP_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getpointscalcSOAP(endpoint);
    }

    public org.example.www.pointscalc.Pointscalc_PortType getpointscalcSOAP(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.example.www.pointscalc.PointscalcSOAPStub _stub = new org.example.www.pointscalc.PointscalcSOAPStub(portAddress, this);
            _stub.setPortName(getpointscalcSOAPWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setpointscalcSOAPEndpointAddress(java.lang.String address) {
        pointscalcSOAP_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.example.www.pointscalc.Pointscalc_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                org.example.www.pointscalc.PointscalcSOAPStub _stub = new org.example.www.pointscalc.PointscalcSOAPStub(new java.net.URL(pointscalcSOAP_address), this);
                _stub.setPortName(getpointscalcSOAPWSDDServiceName());
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
        if ("pointscalcSOAP".equals(inputPortName)) {
            return getpointscalcSOAP();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.example.org/pointscalc/", "pointscalc");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.example.org/pointscalc/", "pointscalcSOAP"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("pointscalcSOAP".equals(portName)) {
            setpointscalcSOAPEndpointAddress(address);
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
