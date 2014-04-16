package de.dhbw.androne.model;

import java.util.ArrayList;
import java.util.List;

public class Polygon {

	private List<Vector2> vectors = new ArrayList<Vector2>();
	
	
	public Polygon() {
		super();
	}
	
	
	public void addVector(Vector2 vector) {
		vectors.add(vector);
	}
	
	public List<Vector2> getVectors() {
		return vectors;
	}
	
	public int getVectorCount() {
		return vectors.size();
	}
	
	public void deleteLastvector() {
		vectors.remove(vectors.size() - 1);
	}
	
	public void deleteAllVectorss() {
		vectors.clear();
	}
}
