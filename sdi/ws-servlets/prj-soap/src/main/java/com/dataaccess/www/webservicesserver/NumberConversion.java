/**
 * NumberConversion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.dataaccess.www.webservicesserver;

public interface NumberConversion extends javax.xml.rpc.Service {

/**
 * The Number Conversion Web Service, implemented with
 * 			Visual DataFlex, provides functions that convert numbers into words
 * 			or dollar amounts.
 */
    public java.lang.String getNumberConversionSoapAddress();

    public com.dataaccess.www.webservicesserver.NumberConversionSoapType getNumberConversionSoap() throws javax.xml.rpc.ServiceException;

    public com.dataaccess.www.webservicesserver.NumberConversionSoapType getNumberConversionSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
