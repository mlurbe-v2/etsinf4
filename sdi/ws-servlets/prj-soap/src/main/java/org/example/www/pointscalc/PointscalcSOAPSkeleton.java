/**
 * PointscalcSOAPSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.example.www.pointscalc;

public class PointscalcSOAPSkeleton implements org.example.www.pointscalc.Pointscalc_PortType, org.apache.axis.wsdl.Skeleton {
    private org.example.www.pointscalc.Pointscalc_PortType impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "points"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "name"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("pointsToMoney", _params, new javax.xml.namespace.QName("", "out"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.example.org/pointscalc/", "PointsToMoney"));
        _oper.setSoapAction("http://www.example.org/pointscalc/PointsToMoney");
        _myOperationsList.add(_oper);
        if (_myOperations.get("pointsToMoney") == null) {
            _myOperations.put("pointsToMoney", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("pointsToMoney")).add(_oper);
    }

    public PointscalcSOAPSkeleton() {
        this.impl = new org.example.www.pointscalc.PointscalcSOAPImpl();
    }

    public PointscalcSOAPSkeleton(org.example.www.pointscalc.Pointscalc_PortType impl) {
        this.impl = impl;
    }
    public java.lang.String pointsToMoney(int points, java.lang.String name) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.pointsToMoney(points, name);
        return ret;
    }

}
