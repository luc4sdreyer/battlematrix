/**
 * IGameInterface.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public interface IGameInterface extends java.rmi.Remote {
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.ReturnValues doLogin(java.lang.String pName, java.lang.String pKey) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.ReturnValues doLogout(java.lang.String pName, java.lang.String pKey) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.ReturnValues doRemove(java.lang.String pName, java.lang.String pKey) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.ReturnValues doChallenge(java.lang.String pName, java.lang.String pKey, java.lang.String pCName, java.lang.String pBName) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.ReturnValues setActionPos(java.lang.String pName, java.lang.String pKey, org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Point pPos, org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Actions pAction) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.ReturnValues setActionID(java.lang.String pName, java.lang.String pKey, java.lang.Integer pID, org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Actions pAction) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.ReturnValues doStopGame(java.lang.String pName, java.lang.String pKey) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.ReturnValues doStartGame(java.lang.String pName, java.lang.String pKey) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.ReturnValues doResetGame(java.lang.String pName, java.lang.String pKey) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.ReturnValues doSaveBlockEvents(java.lang.String pName, java.lang.String pKey) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.ReturnValues addBoard(java.lang.String pName, java.lang.String pKey, org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Board pBoard) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.ReturnValues removeBoard(java.lang.String pName, java.lang.String pKey, org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Board pBoard) throws java.rmi.RemoteException;
    public java.lang.String[] getBoards(java.lang.String pName, java.lang.String pKey) throws java.rmi.RemoteException;
    public java.lang.String[] getPlayers(java.lang.String pName, java.lang.String pKey) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.PlayerStatuses getPlayerStatus(java.lang.String pName, java.lang.String pKey, java.lang.String pPName) throws java.rmi.RemoteException;
    public java.lang.String[] getOnlinePlayers(java.lang.String pName, java.lang.String pKey) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Board getBoard(java.lang.String pName, java.lang.String pKey, java.lang.String pBoard) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Stat getStats(java.lang.String pName, java.lang.String pKey) throws java.rmi.RemoteException;
    public java.lang.String[] getGamePlayers(java.lang.String pName, java.lang.String pKey, java.lang.Integer pID) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Board getGameTickBoard(java.lang.String pName, java.lang.String pKey) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.GameInfo getGameInfo(java.lang.String pName, java.lang.String pKey) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.GameInfoTickBoard getGameInfoTickBoard(java.lang.String pName, java.lang.String pKey) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.Events getEvents(java.lang.String pName, java.lang.String pKey) throws java.rmi.RemoteException;
    public java.util.Calendar getNextTickTime(java.lang.String pName, java.lang.String pKey) throws java.rmi.RemoteException;
    public java.lang.Integer getCurrentTick(java.lang.String pName, java.lang.String pKey) throws java.rmi.RemoteException;
    public org.apache.axis.types.Duration getTimeToNextTick(java.lang.String pName, java.lang.String pKey) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.RoTeD_BattleCity_Library.DateSync getDateSync(java.lang.String pName, java.lang.String pKey, java.util.Calendar pClientTime) throws java.rmi.RemoteException;
}
