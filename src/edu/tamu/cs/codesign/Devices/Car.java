package edu.tamu.cs.codesign.Devices;

public class Car extends Things{
	Car(String Name)
	{
		super(Name);
	}
	

	@Override
	public boolean onDataReceive(String data) {
		System.out.println("From Car: onDataReceive() I am Driving");
		return true;
	}
	
	public void someOtherFunctionOfCar()
	{
		System.out.println("Hello");
	}

}
