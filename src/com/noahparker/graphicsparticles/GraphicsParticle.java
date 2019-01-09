package com.noahparker.graphicsparticles;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

public class GraphicsParticle {
	int length;
	Color color;
	Shape s;
	float xvel = 0;
	float yvel = 0;
	float gStoredXVel = 0;
	float gStoredYVel = 0;
	float rotation = 0;
	int xres;
	int yres;
	float gTheta = 0.0f;
	float x;
	float y;
	int gType;
	float gTargetx;
	float gTargety;
	boolean gTargetSet = false;
	

	public static final int LINE_PARTICLE = 0;
	public static final int TRIANGLE_PARTICLE = 1;
	public static final int CIRCLE_PARTICLE = 2;
	public static final int DOT_PARTICLE = 3;
	

	public GraphicsParticle(int type, int length, int xres, int yres) {
		this.xres = xres;
		this.yres = yres;
		gType = type;
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
		
		switch(type) {
		case LINE_PARTICLE:
			s = new Line(x1,y1,x2,y2);
			break;
		case TRIANGLE_PARTICLE:
			s = new Polygon();
			((Polygon) s).addPoint(x1,y1);
			((Polygon) s).addPoint(x1+(length/2.0f),y1+length);
			((Polygon) s).addPoint(x1-(length/2.0f),y1+length);
			((Polygon) s).addPoint(x1,y1);
			break;
		case CIRCLE_PARTICLE:
			s = new Ellipse(x1,y1,length,length);
			break;
		case DOT_PARTICLE:
			s = new Rectangle(x1,y1,5,5);
			break;
		default:
			s = new Line(x1,y1,x2,y2);
			break;
		}
		
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
		case 4:
			vcoy = -1f;
			vcox = 1f;
			rotco = -1f;
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
		return s;
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
		return gTheta;
	}
	
	public void setTarget(float x, float y) {
		this.gTargetx = x;
		this.gTargety = y;
		gTargetSet = true;
		gStoredXVel = xvel;
		gStoredYVel = yvel;
	}
	
	public void resetTarget() {
		gTargetSet = false;
		xvel = gStoredXVel;
		yvel = gStoredYVel;
	}
	
	public void update() {
		//translate/rotate l variable
		//System.out.println("rotation: "+rotation);
		//rotation = 1.0f;
		// l = Transform.createRotateTransform(rotation, l.getCenterX(), l.getCenterY());
		
		if(gType!=CIRCLE_PARTICLE) {
			s = (Shape) s.transform(Transform.createRotateTransform(rotation, s.getCenterX(), s.getCenterY()));
			gTheta += rotation;
			if(gTheta>=2*Math.PI) {
				gTheta -= 2*Math.PI;
			}
			if(gTheta<0) {
				gTheta += 2*Math.PI;
			}
		}
		
		
		//do same for sideways translation
		if(!gTargetSet) {
			s = (Shape) s.transform(Transform.createTranslateTransform(xvel, yvel));
		}else{
			//calculate distance, if getting close slow down/stop, etc...
			float dx = gTargetx - x;
			float dy = gTargety - y;
			float d = (dx*dx) + (dy*dy);
			float distance = (float) Math.sqrt(d);
			//System.out.println("Coefficient: "+((distance * 0.4f)));
			if(distance < 10) { //15
				if(distance<5) { //10
					xvel = xvel*0.01f;
					yvel = yvel*0.01f;
				}else{
					xvel = xvel*0.5f;
					yvel = yvel*0.5f;
				}
			}
			s = (Shape) s.transform(Transform.createTranslateTransform(xvel, yvel));
			
			//xvel = xvel *(distance * 0.01f);
			//yvel = yvel *(distance * 0.01f);
			//s = (Shape) s.transform(Transform.createTranslateTransform(xvel, yvel));
		}
		
		x = s.getCenterX();
		y = s.getCenterY();
		
		if(s.getCenterX() > xres) {
			s = (Shape) s.transform(Transform.createTranslateTransform(-xres, 0));
		}
		if(s.getCenterX()< 0) {
			s = (Shape) s.transform(Transform.createTranslateTransform(xres, 0));
		}
		if(s.getCenterY() > yres) {
			s = (Shape) s.transform(Transform.createTranslateTransform(0, -yres));
		}
		if(s.getCenterY() < 0) {
			s = (Shape) s.transform(Transform.createTranslateTransform(0, yres));
		}
		
	}
}
