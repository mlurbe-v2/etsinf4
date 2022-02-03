package com.dataaccess.www.webservicesserver;

public class NumberConversionSoapTypeProxy implements com.dataaccess.www.webservicesserver.NumberConversionSoapType {
  private String _endpoint = null;
  private com.dataaccess.www.webservicesserver.NumberConversionSoapType numberConversionSoapType = null;
  
  public NumberConversionSoapTypeProxy() {
    _initNumberConversionSoapTypeProxy();
  }
  
  public NumberConversionSoapTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initNumberConversionSoapTypeProxy();
  }
  
  private void _initNumberConversionSoapTypeProxy() {
    try {
      numberConversionSoapType = (new com.dataaccess.www.webservicesserver.NumberConversionLocator()).getNumberConversionSoap();
      if (numberConversionSoapType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)numberConversionSoapType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)numberConversionSoapType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (numberConversionSoapType != null)
      ((javax.xml.rpc.Stub)numberConversionSoapType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.dataaccess.www.webservicesserver.NumberConversionSoapType getNumberConversionSoapType() {
    if (numberConversionSoapType == null)
      _initNumberConversionSoapTypeProxy();
    return numberConversionSoapType;
  }
  
  public java.lang.String numberToWords(org.apache.axis.types.UnsignedLong ubiNum) throws java.rmi.RemoteException{
    if (numberConversionSoapType == null)
      _initNumberConversionSoapTypeProxy();
    return numberConversionSoapType.numberToWords(ubiNum);
  }
  
  public java.lang.String numberToDollars(java.math.BigDecimal dNum) throws java.rmi.RemoteException{
    if (numberConversionSoapType == null)
      _initNumberConversionSoapTypeProxy();
    return numberConversionSoapType.numberToDollars(dNum);
  }
  
  
}