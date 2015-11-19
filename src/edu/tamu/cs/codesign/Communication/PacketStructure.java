package edu.tamu.cs.codesign.Communication;

import edu.tamu.cs.codesign.General.SysUtils;
import edu.tamu.cs.codesign.General.Systemctl;

public final class PacketStructure {
	
	public static final int headerSize = 6;
	
	Systemctl systemctl = new Systemctl();
	SysUtils utils = systemctl.getInstanceOfSysUtils();
	
	
	
	/**
	 * @description This method tokenizes a string packet to a TokenizedPacket  
	 * @param stringPacket
	 * @return TokenizedPacket
	 */
	public TokenizedPacket tokenizePacket(String stringPacket) {
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
		 *  BYTE 4 BYTE 5
		 */
		 
			short deviceID = utils.getShortFromStringBytes(stringPacket.substring(4, 6));

		/*
		 *  Extract PayLoad
		 *  BYTE 6 to END of Packet
		 */
			
			String payload = stringPacket.substring(6);
		
		
		/*
		 * Form TokenizedPacket
		 */
			return new TokenizedPacket(size, packetType, deviceID, payload);
			
	}
	
	/*
	 * TODO: Create Packet
	 */
	
	/*
	 * TODO: Detokenizer convert tokenized packet to string
	 */

}
