package edu.tamu.cs.codesign.Communication;
import edu.tamu.cs.codesign.Devices.Things;
import java.util.Vector;



public class DeviceManager {
	public static volatile Vector<DeviceIdentity> stdEndDevices = new Vector<DeviceIdentity>();
	
	public synchronized void addStdEndDeviceIdentity(DeviceIdentity deviceIdentity) {
		stdEndDevices.add(deviceIdentity);
	}
	
	public synchronized DeviceIdentity getStdDeviceIdentity(String thingName) {
		for (DeviceIdentity deviceIdentity : stdEndDevices) {
			if(deviceIdentity.getThingName().equals(thingName)) { return deviceIdentity; }
		}
			return null;
	}
	
	public synchronized Things getStdEndDeviceThingObj(String thingName){
		for (DeviceIdentity deviceIdentity : stdEndDevices) {
			if(deviceIdentity.getThingName().equals(thingName)) { return deviceIdentity.getThingObj(); }
		}
		return null;
	}
	


}
