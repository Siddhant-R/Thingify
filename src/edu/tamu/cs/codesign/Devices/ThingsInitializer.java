package edu.tamu.cs.codesign.Devices;


import java.util.Vector;

import edu.tamu.cs.codesign.General.SysUtils;
import edu.tamu.cs.codesign.General.Systemctl;

public class ThingsInitializer {
	
	public  ThingsInitializer()
	{
		Systemctl systemctl = new Systemctl();
		SysUtils sysUtils = systemctl.getInstanceOfSysUtils();
		
		
		//Initalize fridge
		try {
			Vector<Fridge> fridge= new Vector();
			fridge.addElement(new Fridge(3472328296227681584L));
			
			sysUtils.printSystem("OK", "ThingsInitializer : myFridge Initalization");
			//fridge.sendData("hi!");
		}
		catch (Exception e){
			e.printStackTrace();
			sysUtils.printSystem("FAIL", "ThingsInitializer : myFridge Initalization");
		}
		// END
		
		//Initialize Car
		try {
		Car thingCar = new Car(3472328296227681585L);
		sysUtils.printSystem("OK", "ThingsInitializer : Car Reva Initalization");
		}
		catch (Exception e){
			sysUtils.printSystem("FAIL", "ThingsInitializer : Car Reva Initalization");
		}
		// END
		
		
		
		
		
	}

}
