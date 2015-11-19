package edu.tamu.cs.codesign.Communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


import edu.tamu.cs.codesign.Devices.Things;
import edu.tamu.cs.codesign.FrameworkExceptions.InvalidPacketException;
import edu.tamu.cs.codesign.General.SysUtils;
import edu.tamu.cs.codesign.General.Systemctl;

public class CommunicationHandeler extends Thread {
	/*
	 * Communication Handler is the TCP connection between Device Server and Smart Device.
	 * Smart Device initiates individual TCP connections for all the end devices connected to it
	 * No of end devices connected = No of threads/instances of this class
	 * The object to this class is spawned by CommunicationGateway in parallel.
	 * A reference to this object is given to the thing class via thing.  
	 */
	
	protected Socket socket;
	protected Short deviceID;
	Boolean session = false;
	
	/*
	 * General References
	 */
	Systemctl systemctl;
	SysUtils util;
	DeviceManager deviceManager;
	PacketStructure packetStructure;
	Things thing;
	InputStream inputStream = null;
    BufferedReader bufferedReaderInput = null;
    PrintWriter printWriterOut =null;
    
   
	/*
	 * Constructor
	 */
	public CommunicationHandeler(Socket smartDeviceSocket) {
		this.socket = smartDeviceSocket;
		systemctl = new Systemctl();
		util = systemctl.getInstanceOfSysUtils();
		deviceManager = systemctl.getInstanceOfDeviceManager();
		packetStructure = new PacketStructure();
		thing = null;
		}
	
	
	/*
	 * This is used by things objects to send data to end device.
	 * TODO: Convert this to packet structure and add proper header information
	 */
	public void sendData(String data) {
		printWriterOut.println(data);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 * Execution Starts From Here
	 */
	@Override
	public void run() {
		
		
		
        try {
        	// TCP Connected
        	util.printDebug("TCP Connected: "+ socket.getRemoteSocketAddress());
        	/*
        	 * Create Streams
        	 */
            inputStream = socket.getInputStream();
            bufferedReaderInput = new BufferedReader(new InputStreamReader(inputStream));
            printWriterOut = new PrintWriter(socket.getOutputStream(), true);
            
            
        } catch (IOException e) {
        	/*
        	 * This will rarely fail.
        	 * If creating streams fail nothing can be done further.
        	 * Kill the thread
        	 */
            return;
        }
        
        
        String line = null;
        while (!printWriterOut.checkError())
        {
        	try {
        		line = bufferedReaderInput.readLine();
        		/*
        		 * Our headers are of length PacketStructure.headerSize, If a received packet has length
        		 * less than that we can simply discard it.
        		 * TODO: Log line for records
        		 */
        		if(line.length() < PacketStructure.headerSize){
        			util.printDebug("Invalid Header received from "+ socket.getRemoteSocketAddress());
    				printWriterOut.println("NACK | INVALID HEADER");
    				throw new InvalidPacketException("Invalid Headers");
        		}
        		
        		/*
        		 *  Tokenize the packet 
        		 */
        		
        		TokenizedPacket packet = packetStructure.tokenizePacket(line);
        		
        		/*
        		 *  Check if it is a valid packet type
        		 */
        		if(packet.packetType() == PacketType.UNKNOWN){
        			util.printDebug("Unknown Packet Type received from "+ socket.getRemoteSocketAddress());
    				printWriterOut.println("NACK | UNKNOWN PACKET TYPE");
    				throw new InvalidPacketException("Unknown Packet");
        		}
        		
        		/*
        		 *  SESSION HANDLE
        		 */
        		if(packet.packetType() == PacketType.SESSION_CREATE) {			
        			/*
        			 *  check if it is an active device from device manager, obtain reference to things object
        			 *  if the method returns null, it means no such device exist and you can exit safely.
        			 */
        			thing = deviceManager.getStdEndDeviceThingObj(packet.deviceID());
        			if(thing == null) {
        				util.printDebug("Closing Connection to an unidentified device at"+ socket.getRemoteSocketAddress());
        				printWriterOut.println("NACK | UNIDENTIFIED DEVICE");
        				throw new IOException("Unidentified Device");
        				}
        			
        			
        			
        			/*
        			 * Now try checking reference to Communication Handler object from things object
        			 * If this is not null, a session exists already. you can exit throwing session already exists
        			 * !session is added because the same device of this session can again send a create session request
        			 * in that case we do not close the connection
        			 */
        			else if(thing.checkExistanceOfHandleIncomingDataObj() && !session) {
        				 util.printDebug("Multipe Session Request by "+ packet.deviceID() +" at "+ socket.getRemoteSocketAddress());
         				 printWriterOut.println("NACK | MULTIPLE SESSION REQUESTED");
         				 throw new IOException("MULTIPLE Session Request");
        			 }
        			else {
        			 /*
        			  * Create a session : Hook this device to this thread. hook device id and make session true
        			  * This will prevent other devices from using this thread
        			  * Reply with ACK
        			  * TODO: Reply with proper packet structure
        			  */
        			if(session){
        				if(this.deviceID == packet.deviceID()) {
        					printWriterOut.println("NACK | SESSION ALREADY EXISTS");
        					throw new InvalidPacketException("DUPLICATE SESSION REQUEST");
        				}
        				util.printDebug(packet.deviceID() + "tried overwriting session of "+this.deviceID);
        				printWriterOut.println("NACK | SESSION OVERWRITE REQUESTED | THIS IS ILLEGAL");
        				throw new IOException("ILLEGAL SESSION OPERATION");
        				
        			}
        			 this.deviceID = packet.deviceID();
        			 this.session =true;
        			 thing.getIdentity().setThingIP(socket.getRemoteSocketAddress().toString());
                  	 thing.setHandleIncomingDataObj(this);
        			 printWriterOut.println("ACK");
        			 util.printDebug("SESSION CREATED FOR DEVICE "+this.deviceID);
        			 
        			}
        		}
        		if(packet.packetType() != PacketType.SESSION_CREATE) {
        		/*
        		 * Session creation is managed, now further codes must be executed only if proper session exist
        		 * check hook status 
        		 * if hook fails, it means another device is being injected to this device session. This can be a flaw
        		 * at smart device level or because of a sniffed network 
        		 */
        		if(!isHooked(packet.deviceID())) {
        			util.printDebug("Device Injection Detcted : Injected "+ packet.deviceID() +" at "+ socket.getRemoteSocketAddress());
    				 printWriterOut.println("NACK | ILLEGAL DEVICE INJECTION");
        		}
        		else {
        			/*
            		 * This part is protected by existence of a session
            		 */
        			if(handlePacket(packet)){
            			printWriterOut.println("ACK");
            		}
            		else
            		{
            		printWriterOut.println("NACK | Packet Could Not be Handled");
            		
            		}
        			
        		}
        		}  		
        	}
        	catch (InvalidPacketException ie) {
        		util.printDebug("InvalidPacketException : " + ie.getMessage());
        	}
        	catch (Exception e)
        	{	
        		//e.printStackTrace();
        		printWriterOut.println("NACK | INVALID HEADERS RECEIVED");
                printWriterOut.flush();
                util.printDebug("Closed Connection : "+ socket.getRemoteSocketAddress());
        		printWriterOut.flush();
                try {
					socket.close();
					
					return;
				} catch (IOException e1) {
						e1.printStackTrace();
				}
                finally {
                	if(thing!=null)
    					thing.setHandleIncomingDataObj(null);
                }
                return;
        	}
        		
            
        }
        try {
        	util.printDebug("Client Remotely Disconnected: "+ socket.getRemoteSocketAddress());
			socket.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
        finally {
        	if(thing!=null)
				thing.setHandleIncomingDataObj(null);
        }
        
		
	}
	private boolean handlePacket(TokenizedPacket packet)
	{
		System.out.println("Displaying Packet");
		System.out.println(packet.toString());
		if(packet.packetType() == PacketType.REGULAR_TX){
			thing.onDataReceive(packet.payload());
		}
		
		
		return true;
	}
	
	
	/*
	 * Returns true if the device is hooked to the present thread
	 */
	private boolean isHooked(short deviceID) {
		if(this.session && this.deviceID == deviceID)
			return true;
		return false;
	}
		/*
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
		
	}*/

}
