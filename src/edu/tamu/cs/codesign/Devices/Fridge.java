package edu.tamu.cs.codesign.Devices;

import edu.tamu.cs.codesign.FrameworkExceptions.*;

public class Fridge extends Things{

	
	Fridge(String Name)
	{
		super(Name);
	}
	

	@Override
	public boolean onDataReceive(String data) {
		System.out.println("From Fridge: onDataReceive() I am Chilled!");
		try {
		sendData("Hola!");
		}
		catch (DeviceOfflineException e){
			e.printStackTrace();
		}
		return true;
	}
	
	public void someOtherFunctionOfFridge()
	{
		System.out.println("Hello");
	}

}
