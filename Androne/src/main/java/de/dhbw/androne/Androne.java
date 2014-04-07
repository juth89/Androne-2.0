package de.dhbw.androne;
/*
 * TODO:
 * - Rectangle
 * 		- drawDrone
 * - Triangle
 * 		- drawDrone
 * 		- drawWidthHeight
 * 		- flyTriangle
 * - Polygon
 * 		- drawMaßstab
 * 		- flyPolygon
 */
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
