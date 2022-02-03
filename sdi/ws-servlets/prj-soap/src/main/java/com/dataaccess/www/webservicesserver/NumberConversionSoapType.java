/**
 * NumberConversionSoapType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.dataaccess.www.webservicesserver;

public interface NumberConversionSoapType extends java.rmi.Remote {

    /**
     * Returns the word corresponding to the positive number
     * 				passed as parameter. Limited to quadrillions.
     */
    public java.lang.String numberToWords(org.apache.axis.types.UnsignedLong ubiNum) throws java.rmi.RemoteException;

    /**
     * Returns the non-zero dollar amount of the passed
     * 				number.
     */
    public java.lang.String numberToDollars(java.math.BigDecimal dNum) throws java.rmi.RemoteException;
}
