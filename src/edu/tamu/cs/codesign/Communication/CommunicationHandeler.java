package edu.tamu.cs.codesign.Communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


import edu.tamu.cs.codesign.Devices.Things;
import edu.tamu.cs.codesign.General.SysUtils;
import edu.tamu.cs.codesign.General.Systemctl;

public class CommunicationHandeler extends Thread {
	
	protected Socket socket;
	Systemctl systemctl;
	SysUtils util;
	DeviceManager deviceManager;
	Things thing;
	InputStream inp = null;
    BufferedReader brinp = null;
    PrintWriter out =null;
	
	public CommunicationHandeler(Socket clientSocket) {
		this.socket = clientSocket;
		systemctl = new Systemctl();
		util = systemctl.getInstanceOfSysUtils();
		deviceManager = systemctl.getInstanceOfDeviceManager();
		thing = null;
		
		
	}
	
	private boolean handleMessage(String line)
	{
		try { 
            util.printDebug("<<< Received "+socket.getRemoteSocketAddress()+" DATA: "+line);
             if(line.contains("myFridge"))
             {
             	thing = deviceManager.getStdEndDeviceThingObj("myFridge");
             	thing.getIdentity().setThingIP(socket.getRemoteSocketAddress().toString());
             	thing.setHandleIncomingDataObj(this);
             	thing.onDataReceive(line);
             	
             	return true;
             }
             if(line.contains("Reva"))
             {
             	thing = deviceManager.getStdEndDeviceThingObj("Reva");
             	thing.getIdentity().setThingIP(socket.getRemoteSocketAddress().toString());
             	thing.onDataReceive(line);
             	return true;
             }
             return false;
         } catch (Exception e) {
            util.printDebug("HandleIncomingData.handleMessage() Failed ");
            e.printStackTrace();
             return false;
         }
		
	}
	public void sendData(String data) {
		out.print(data);
	}
	
	@Override
	public void run() {
		
		
		
        try {
        	util.printDebug("New Client Connected: "+ socket.getRemoteSocketAddress());
            inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
            out = new PrintWriter(socket.getOutputStream(), true);
            
            
        } catch (IOException e) {
            return;
        }
        
        
        String line = null;
        while (!out.checkError())
        {
        	try {
        		line = brinp.readLine();
        		if(!line.contains("FFFF"))
        			throw new IOException();
        		
        		if(handleMessage(line)){
        			out.print("ACK");
        		}
        		else
        		{
        		out.print("NACK | UNIDENTIFIED DEVICE");
        		}
                out.flush();
                socket.close();
        	}
        	catch (Exception e)
        	{
        		out.print("NACK | INVALID HEADERS RECEIVED");
                out.flush();	
        	}
        		
            
        }
        try {
        	util.printDebug("Client Remotely Disconnected: "+ socket.getRemoteSocketAddress());
			socket.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
        
		
	}

}
