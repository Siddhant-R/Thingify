package edu.tamu.cs.codesign.General;

public class SysUtils {
	
	
	/*
	 * Related to tokenization of strings to different data format
	 */
	
	/*
	 * Input a string of length four and return int value
	 * For example ORIGINAL ENCODING 			0x62, 0x63
	 * 						VISIBLE STRING  	bc
	 * 						Returned Short VAL 	25187
	 */
	
	public short getShortFromStringBytes(String s) throws IllegalArgumentException
	{
		if(s.length()!=2) throw new IllegalArgumentException("Length of String Should be 2");
		byte[] byteArray = s.getBytes();
		return  java.nio.ByteBuffer.wrap(byteArray).getShort();
	}
	
	/*
	 * Input a string of length four and return int value
	 * For example ORIGINAL ENCODING 			0x62, 0x63, 0x64, 0x65
	 * 						VISIBLE STRING  	bcde
	 * 						Returned INT VAL 	1650680933
	 */
	
	public int getIntFromStringBytes(String s) throws IllegalArgumentException
	{
		if(s.length()!=4) throw new IllegalArgumentException("Length of String Should be 4");
		byte[] byteArray = s.getBytes();
		return  java.nio.ByteBuffer.wrap(byteArray).getInt();
	}
	
	
	
	

	/*
	 * Related to debug prints
	 */
	
	private static boolean debugEnabled=false;
	
	public  void DebugEnable() {
		debugEnabled = true;
		System.out.println("Debug Prints are Enabled");
	}
	
	private boolean isDebugEnabled() {
		return debugEnabled;
	}
	
	public synchronized void printDebug(String s1) {
		if(isDebugEnabled()) {
			System.out.println("[DEBUG] "+s1);
			
		}
	}
	
	public synchronized void printSystem(String status, String message) {
		message = String.format("%-60s", message);
		message = message + "[" + status.toUpperCase() + "]";
		if(status.equalsIgnoreCase("FAIL")) { System.err.println(message); } else { System.out.println(message);}	

	}
	public synchronized void printEvent(String message) {
		message = String.format("%-60s", message);
		System.out.print(message);

	}
	public synchronized void printStatus(String status) {
		if(status.equalsIgnoreCase("FAIL")) { System.err.println("[FAIL]"); } else { System.out.print("["+status.toUpperCase()+"]\n");}	

	}
	
	public synchronized void print(String message) {
		message = String.format("%-60s", message);
		System.out.println(message);
		
	}
	
	
	

}
