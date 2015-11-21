package edu.tamu.cs.codesign.Communication;

import edu.tamu.cs.codesign.General.SysUtils;
import edu.tamu.cs.codesign.General.Systemctl;

public final class PacketStructure {
	
	public static final int headerSize = 12;
	
	Systemctl systemctl = new Systemctl();
	SysUtils utils = systemctl.getInstanceOfSysUtils();
	
	
	
	/**
	 * @description This method tokenizes a string packet to a TokenizedPacket  
	 * @param stringPacket
	 * @return TokenizedPacket
	 */
	public synchronized TokenizedPacket tokenizePacket(String stringPacket) {
		/*
		 *  Extract Size
		 *  BYTE 0 and BYTE 1 
		 */
			short size = utils.getShortFromStringBytes(stringPacket.substring(0, 2));
				
		/*
		 *  Extract Packet Type
		 *  BYTE 2 BYTE 3
		 *  PacketType is defined as an enum.
		 *  All defined packets are register in enum PackeType with its corresponding ID
		 *  This is for convenience.
		 */
			short packetTypeID = utils.getShortFromStringBytes(stringPacket.substring(2, 4));
			PacketType packetType = PacketType.getPacketType(packetTypeID);	
		
		/*
		 *  Extract Device ID
		 *  BYTE 4 BYTE 5 BYTE 6 BYTE 7 BYTE 8 BYTE 9 BYTE 10 BYTE 11
		 */
			long deviceID = utils.getLongFromStringBytes(stringPacket.substring(4, 12));

		/*
		 *  Extract PayLoad
		 *  BYTE 8 to END of Packet
		 */			
			String payload = stringPacket.substring(12);
	
		/*
		 * Form TokenizedPacket
		 */
			return new TokenizedPacket(size, packetType, deviceID, payload);
			
	}
	
	/*
	 * TODO: Create Packet
	 */
	public synchronized TokenizedPacket createTokenizedPacket(PacketType packetType, long deviceID, String payload) {
		return new TokenizedPacket((short)payload.length(), packetType, deviceID, payload);
	}
	/*
	 * TODO: public String deTokenizePacket(TokenizedPacket packet) 
	 * 
	 */
	public synchronized String deTokenizePacket(TokenizedPacket tokenizedPacket) {
		System.out.println(tokenizedPacket.toString());
		
		String DeviceID = utils.getStringBytesFromLong(tokenizedPacket.deviceID());
		//System.out.println("DevID: Sidze="+DeviceID.length()+" Packet="+DeviceID);
		
		String Payload = tokenizedPacket.payload();
		//System.out.println("Payload: Sidze="+Payload.length()+" Packet="+Payload);

		String PacketTypeID = utils.getStringBytesFromShort((short)tokenizedPacket.packetType().getPacketTypeID());
		//System.out.println("PacketTypeID: Sidze="+PacketTypeID.length()+" Packet="+PacketTypeID);

		String Size = utils.getStringBytesFromShort((short)Payload.length());
		//System.out.println("Size: Sidze="+Size.length()+" Packet="+Size);

		System.out.println(Size+PacketTypeID+DeviceID+Payload);
		return Size+PacketTypeID+DeviceID+Payload;
	}
	
	

}
