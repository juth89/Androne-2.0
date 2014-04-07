package de.dhbw.androne;

public class Androne {

	public enum Command {
		CONNECT,
		DISCONNECT,
		TAKE_OFF,
		LAND
	}
	
	public enum FlyingMode {
		DIRECT,
		SHAPE,
		POLYGON
	}
}
