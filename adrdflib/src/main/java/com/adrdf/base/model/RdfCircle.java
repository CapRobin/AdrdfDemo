package com.adrdf.base.model;

public class RdfCircle {
	
	public RdfPoint point;
	public double r;
	
	public RdfCircle() {
		super();
	}

	public RdfCircle(RdfPoint point, double r) {
		super();
		this.point = point;
		this.r = r;
	}

	@Override
	public String toString() {
		return "(" + point.x + "," + point.y + "),r="+r;
	}

}
