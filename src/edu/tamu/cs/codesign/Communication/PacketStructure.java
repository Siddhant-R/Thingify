package edu.tamu.cs.codesign.Communication;

public final class PacketStructure {
	
	
	private static String KEEP_ALIVE_TX_RAW = "FFFF|KA" ;
	
	public String form(Packet packet) {
		
		switch (packet) {
		 
		case KEEP_ALIVE_TX:
			return KEEP_ALIVE_TX_RAW;
			
		default:
			break;
				
		}
		
		return null;
	}

}
