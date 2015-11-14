package edu.tamu.cs.codesign.Devices;

import java.io.IOException;

import edu.tamu.cs.codesign.Communication.DeviceIdentity;
import edu.tamu.cs.codesign.Communication.DeviceManager;
import edu.tamu.cs.codesign.Communication.CommunicationHandeler;
import edu.tamu.cs.codesign.FrameworkExceptions.DeviceOfflineException;
import edu.tamu.cs.codesign.General.Systemctl;

public abstract class Things {
	
	private String ThingName;
	private CommunicationHandeler hd=null;
	
	
	public Things(String name)
	{
		createIdentity(name);
	}
	
	public void setHandleIncomingDataObj(CommunicationHandeler obj) {
		hd = obj;
	}
	
	public void sendData(String data) throws DeviceOfflineException{
		if (hd == null) {
			throw new DeviceOfflineException("Uplink has not been initated by end device");
		}
		hd.sendData(data);
	}

	private  void setName(String ThingName) { this.ThingName = ThingName; }
	public  String getName() { return ThingName; }
	
	
	public void createIdentity(String name){
		DeviceIdentity di = new DeviceIdentity();
		di.setIdentity(name, this);
		setIdentity(di);
		setName(name);
	}
	
	public void setIdentity(DeviceIdentity deviceIdentity)
	{
		Systemctl systemctl = new Systemctl();
		DeviceManager deviceManager = systemctl.getInstanceOfDeviceManager();
		deviceManager.addStdEndDeviceIdentity(deviceIdentity);
		
	}
	public DeviceIdentity getIdentity(){
		Systemctl systemctl = new Systemctl();
		DeviceManager deviceManager = systemctl.getInstanceOfDeviceManager();
		return deviceManager.getStdDeviceIdentity(getName());
		
	}

	
	public abstract boolean onDataReceive(String data);
	

}
