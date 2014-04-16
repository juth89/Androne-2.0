package de.dhbw.androne.model;


public class Vector2Utils {

	public static float scalar(Vector2 v1, Vector2 v2) {
		return v1.x * v2.x + v1.y * v2.y;
	}
	
	
	public static float angleBetween(Vector2 v1, Vector2 v2) {
		float angleV1x = angleToX(v1);
		float angleV2x = angleToX(v2);
		float angle = (float) Math.toDegrees(Math.acos(scalar(v1, v2) / (v1.absolute() * v2.absolute())));
		
		if(angleV1x > angleV2x) {
			return -angle;
		}
		return angle;
	}
	
	
	private static float angleToX(Vector2 v) {
		Vector2 vX = new Vector2(1, 0);
		return (float) Math.toDegrees(Math.acos(scalar(v, vX) / (v.absolute() * vX.absolute())));
	}
}
