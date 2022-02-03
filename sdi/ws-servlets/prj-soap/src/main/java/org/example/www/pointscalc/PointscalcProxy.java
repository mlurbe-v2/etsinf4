package org.example.www.pointscalc;

public class PointscalcProxy implements org.example.www.pointscalc.Pointscalc_PortType {
  private String _endpoint = null;
  private org.example.www.pointscalc.Pointscalc_PortType pointscalc_PortType = null;
  
  public PointscalcProxy() {
    _initPointscalcProxy();
  }
  
  public PointscalcProxy(String endpoint) {
    _endpoint = endpoint;
    _initPointscalcProxy();
  }
  
  private void _initPointscalcProxy() {
    try {
      pointscalc_PortType = (new org.example.www.pointscalc.Pointscalc_ServiceLocator()).getpointscalcSOAP();
      if (pointscalc_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)pointscalc_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)pointscalc_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (pointscalc_PortType != null)
      ((javax.xml.rpc.Stub)pointscalc_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.example.www.pointscalc.Pointscalc_PortType getPointscalc_PortType() {
    if (pointscalc_PortType == null)
      _initPointscalcProxy();
    return pointscalc_PortType;
  }
  
  public java.lang.String pointsToMoney(int points, java.lang.String name) throws java.rmi.RemoteException{
    if (pointscalc_PortType == null)
      _initPointscalcProxy();
    return pointscalc_PortType.pointsToMoney(points, name);
  }
  
  
}