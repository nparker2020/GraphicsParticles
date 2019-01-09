package com.noahparker.graphicsparticles;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

public class SegmentParticle {
	int length;
	Color color;
	Line l;
	float xvel = 0;
	float yvel = 0;
	float rotation = 0;
	int xres;
	int yres;
	float theta = 0.0f;
	float x;
	float y;
	
	public SegmentParticle(int length, int xres, int yres) {
		this.xres = xres;
		this.yres = yres;
		
		Random r = new Random();
		
		//Generating first two points inside of resolution.
		int x1 = r.nextInt(xres);
		int y1 = r.nextInt(yres);
		
		//random length, added or 
		int xadd = r.nextInt(3);
		//System.out.println("xadd: "+xadd);
		int x2;
		int y2;
		
		if(xadd>1) {
			x2 = x1-length;
		}else{
			x2 = x1+length;
		}
		
		int yadd = r.nextInt(3);
		//System.out.println("yadd: "+yadd);
		if(yadd>1) {
			y2 = y1-length;
		}else{
			y2 = y1+length;
		}
		
		l = new Line(x1,y1,x2,y2);
		
		r.setSeed(3);
		int color = (int) (Math.random()*4);
		switch(color) {
		case 0:
			this.color = Color.red;
			break;
		case 1:
			this.color = Color.green;
			break;
		case 2:
			this.color = Color.blue;
			break;
		case 3:
			this.color = Color.green;
			break;
		default:
			this.color = Color.red;
			break;
		}
		
		float rot = (float) r.nextInt(10);
		float rot2 = (float) Math.random();
		float rotco = 0.0f;
		float vcoy = 0.0f;
		float vcox = 0.0f;
		int s = (int)(Math.random()*5f);
		
		
		if(s%2==0 || s==0) {
			/*vcoy = -1f;
			vcox = 1f;
			rotco = -1f;
			*/
		}else{
			/*vcoy = -1f;
			vcox = 1f;
			rotco = -1f;
			*/
		}
		switch(s) {
		case 0:
			vcoy = -1f;
			vcox = 1f;
			rotco = -1f;
			break;
		case 1:
			vcoy = 1f;
			vcox = -1f;
			rotco = 1f;
			break;
		case 2:
			vcoy = -1f;
			vcox = 1f;
			rotco = -1f;
			break;
		case 3:
			vcoy = 1f;
			vcox = -1f;
			rotco = 1f;
			break;
		}
		
		
		this.rotation = (rot2 * 0.1f)*rotco;
		
		//this.xvel = (float) (r.nextInt(5))*0.5f*vcox;
		//this.yvel = (float) (r.nextInt(6))*0.5f*vcoy;
		this.yvel = (float) Math.random() * 0.5f * vcoy;
		this.xvel = (float) Math.random() * 0.5f * vcox;
		//System.out.println("xvel generated: "+xvel+", yvel generated:"+yvel);
	}
	
	public Shape getShape() {
		return l;
	}
	
	public void setVelX(float x) {
		this.xvel = x;
	}
	
	public void setVelY(float y) {
		this.yvel = y;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public Color getColor() {
		return color;
	}
	
	public float getTheta() {
		return theta;
	}
	
	public void update() {
		//translate/rotate l variable
		//System.out.println("rotation: "+rotation);
		//rotation = 1.0f;
		// l = Transform.createRotateTransform(rotation, l.getCenterX(), l.getCenterY());
		l = (Line) l.transform(Transform.createRotateTransform(rotation, l.getCenterX(), l.getCenterY()));
		theta += rotation;
		if(theta>=2*Math.PI) {
			theta -= 2*Math.PI;
		}
		if(theta<0) {
			theta += 2*Math.PI;
		}
		//do same for sideways translation
		l = (Line) l.transform(Transform.createTranslateTransform(xvel, yvel));
	
		x = l.getCenterX();
		y = l.getCenterY();
		
		if(l.getCenterX() > xres) {
			l = (Line) l.transform(Transform.createTranslateTransform(-xres, 0));
		}
		if(l.getCenterX()< 0) {
			l = (Line) l.transform(Transform.createTranslateTransform(xres, 0));
		}
		if(l.getCenterY() > yres) {
			l = (Line) l.transform(Transform.createTranslateTransform(0, -yres));
		}
		if(l.getCenterY() < 0) {
			l = (Line) l.transform(Transform.createTranslateTransform(0, yres));
		}
		
	}
}
