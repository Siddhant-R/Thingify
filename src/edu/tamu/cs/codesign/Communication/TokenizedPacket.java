package edu.tamu.cs.codesign.Communication;

public class TokenizedPacket {
	private short Size;
	private PacketType packetType;
	private short DeviceID;
	private String Payload;
	
	/*
	 * Parameterized constructor to create a new Tokenized Packet
	 */
	TokenizedPacket(short Size, PacketType packetType, short DeviceID, String Payload) {
		this.Size = Size;
		this.packetType = packetType;
		this.DeviceID = DeviceID;
		this.Payload = Payload;
		
	}
	
	
	public short size() { return Size; }
	
	
	public PacketType packetType() { return packetType; }
	public short deviceID() { return DeviceID; }
	public String payload() { return Payload; }


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return  ("Size=" + Size +
				 " Packet Type=" + packetType.name() +
				 " Device ID=" + DeviceID +
				 " Payload=" + Payload);
	}
}
