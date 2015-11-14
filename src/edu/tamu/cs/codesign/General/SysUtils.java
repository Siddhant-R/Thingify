package edu.tamu.cs.codesign.General;

public class SysUtils {
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
