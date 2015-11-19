package edu.tamu.cs.codesign.Communication;

public enum PacketType {
	UNKNOWN(0),
	KEEP_ALIVE_TX(12337),
	KEEP_ALIVE_RX(12338),
	REGULAR_TX(12339),
	SESSION_CREATE(12340);
	
	
	
	private final int PacketTypeID;
	private PacketType(final int PacketTypeID){
		this.PacketTypeID = PacketTypeID;
		
	}
	
	public static PacketType getPacketType(int packetTypeID)
	{
	for(PacketType p: PacketType.values()) {
	    if(p.PacketTypeID == packetTypeID) {
	      return p;
	    }
	  }
	  return UNKNOWN;
	}
	
	
}

