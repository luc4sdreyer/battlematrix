/**
 * ChallengeService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package za.co.entelect.challenge;

public interface ChallengeService extends javax.xml.rpc.Service {
    public java.lang.String getChallengePortAddress();

    public za.co.entelect.challenge.Challenge getChallengePort() throws javax.xml.rpc.ServiceException;

    public za.co.entelect.challenge.Challenge getChallengePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
