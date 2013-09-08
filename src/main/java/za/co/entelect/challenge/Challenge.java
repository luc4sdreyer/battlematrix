/**
 * Challenge.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package za.co.entelect.challenge;

public interface Challenge extends java.rmi.Remote {
    public za.co.entelect.challenge.Game getStatus() throws java.rmi.RemoteException;
    public za.co.entelect.challenge.Delta setActions(za.co.entelect.challenge.Action arg0, za.co.entelect.challenge.Action arg1) throws java.rmi.RemoteException, za.co.entelect.challenge.EndOfGameException;
    public za.co.entelect.challenge.Delta setAction(int arg0, za.co.entelect.challenge.Action arg1) throws java.rmi.RemoteException, za.co.entelect.challenge.EndOfGameException;
    public za.co.entelect.challenge.Board login() throws java.rmi.RemoteException, za.co.entelect.challenge.NoBlameException, za.co.entelect.challenge.EndOfGameException;
}
