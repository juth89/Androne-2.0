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
public abstract class Androne {

	public enum Command {
		CONNECT,
		DISCONNECT,
		TAKE_OFF,
		LAND,
		START_FLY_SHAPE,
		STOP_FLY_SHAPE,
		START_FLY_POLYGON,
		STOP_FLY_POLYGON
	}
	
	public enum FlyingMode {
		DIRECT,
		SHAPE,
		POLYGON
	}
}
