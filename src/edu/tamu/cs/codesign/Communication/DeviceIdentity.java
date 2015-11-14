package edu.tamu.cs.codesign.Communication;

import edu.tamu.cs.codesign.Devices.Things;

public class DeviceIdentity {
	
	private Things thing_obj;
	private String thing_name;
	private String IP;
	
	public void setIdentity(String thing_name, Things thing_obj) {
		this.thing_name = thing_name;
		this.thing_obj = thing_obj;
	}
	
	public Things getThingObj() { return thing_obj; }
	public String getThingName() { return thing_name; }
	public void setThingIP(String IP){ this.IP = IP; System.out.println("IP of "+thing_name+" Updated to "+ this.IP);}
	public String getThingIP() { return this.IP; }
	

}
