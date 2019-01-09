package com.noahparker.graphicsparticles;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GraphicsState extends BasicGameState {
	
	SegmentParticle[] sp = new SegmentParticle[10000];
	GraphicsParticle[] data = new GraphicsParticle[20000];
	String[] gSorts = {"Gather","Explode","Line"};
	int[][] gPoints = new int[1000][2];
	int gNumPoints = 0;
	int particles = 20000;
	boolean gGatheringPoints = false;
	Random r = new Random();
	boolean createParticles = false;
	int gSortState = 0; //Gather Sort.
	int gLineCounter = 0;
	public static final int PARTICLE_TYPE = GraphicsParticle.LINE_PARTICLE;
	public static final int STATE_GATHER = 0;
	public static final int STATE_EXPLODE = 1;
	public static final int STATE_LINE = 2;
	
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		//Random r = new Random();
		for(int i = 0; i<particles-1; i++) {
			//sp[i] = new SegmentParticle(r.nextInt(10),720,480);
			data[i] = new GraphicsParticle(PARTICLE_TYPE, r.nextInt(10), 720,480);
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		g.drawString("Rendering", 10, 25);
		g.drawString("Particles: "+particles, 10, 45);
		g.drawString("Tool: "+gSorts[gSortState], 10, 65);
		for(int i = 0; i<particles; i++) {
			if(data[i]!=null) {
				g.setColor(data[i].getColor());
				g.draw(data[i].getShape());
			}
		}
		g.setColor(Color.white);
	
		for(int a = 0; a<gNumPoints; a++) {
			int x = gPoints[a][0];
			int y = gPoints[a][1];
			g.drawRect(x, y, 5, 5);
			if(a<gNumPoints-1) {
				int x2 = gPoints[a+1][0];
				int y2 = gPoints[a+1][1];
				g.drawRect(x2, y2, 5, 5);
				g.drawLine(x, y, x2, y2);
			}
		}
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		//System.out.println("x: "+sp[150].getShape().getCenterX());
		for(int i = 0; i<particles; i++) {
			if(data[i]!=null)
			data[i].update();
		}
		updateParticleCount();
	}

	public void addParticle() {
		
		//SegmentParticle p = new SegmentParticle(r.nextInt(10), 720, 480);
		//sp[particles+1] = p;
		GraphicsParticle p = new GraphicsParticle(PARTICLE_TYPE, r.nextInt(10), 720,480);
		data[particles+1] = p;
		particles++;
	}
	
	public void keyPressed(int key, char c) {
		boolean line = false;
		if(gSortState == STATE_LINE) {
			line = true;
		}
		switch(key) {
		case Input.KEY_SPACE:
			//addParticle();
			createParticles = true;
			break;
		case Input.KEY_1:
			gSortState = STATE_GATHER;
			break;
		case Input.KEY_2:
			gSortState = STATE_EXPLODE;
			break;
		case Input.KEY_3:
			gSortState = STATE_LINE;
			break;
		}
		if(line && gSortState != STATE_LINE) {
			for(int i = 0; i<particles-1; i++) {
				data[i].resetTarget();
			}
			gPoints = null;
			gNumPoints = 0;
			gPoints = new int[1000][2];
			
		}
	}
	
	public void keyReleased(int key, char c) {
		switch(key) {
		case Input.KEY_SPACE:
			createParticles = false;
			break;
		}
	}
	
	public void mousePressed(int button, int x, int y) {
		System.out.println("Moused Pressed: X:"+x+", Y:"+y);
		switch(gSortState) {
		case STATE_GATHER:
			gatherParticles(data, particles, x, y);
			break;
		case STATE_EXPLODE:
			explodeParticles(data, particles, x, y);
			break;
		case STATE_LINE:
			
			break;
		}
	}
	
	public void mouseReleased(int button, int x, int y) {
		gGatheringPoints = false;
		if(gSortState==2) {
			//gather on points!
			gatherParticlesOnPoints(data, particles, gPoints);
		}
	}
	
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		//System.out.println("Dragged, Old: ("+oldx+", "+oldy+")"+"New: ("+newx+", "+newy+")");
		
		if(gSortState==2) {
			gGatheringPoints = true;
			gLineCounter++;
			//if(gLineCounter>1) {
				gLineCounter = 0;
				gPoints[gNumPoints][0] = newx;
				gPoints[gNumPoints][1] = newy;
				gNumPoints++;
			//}
		}
		//gatherParticles(data, particles, newx, newy);
	}
	
	public void gatherParticlesOnPoints(GraphicsParticle array[], int particles, int[][] points) {
		float division = (float) particles/gNumPoints;
		for(int i = 0; i<gNumPoints;i++) { //iterate through all points
			for(int a = (int) division*i; a<(division*(i+1)); a++) {
					if(a<particles-1)
					gatherSingularParticle(data[a], gPoints[i][0], gPoints[i][1]);
					if(a>particles) {
						break;
					}
			}
		}
	}
	
	public void gatherSingularParticle(GraphicsParticle p, int x, int y) {
		float theta = p.getTheta();
		float vx = x-p.getX();
		float vy = y-p.getY();
		float tantheta = (float) Math.atan2(vy, vx);
		//use this value for theta for new velocity values!
		
		float newvx = (float) Math.cos((double) tantheta);
		float newvy = (float) Math.sin((double) tantheta);
		//System.out.println("generated x velocity: "+newvx);
		//System.out.println("generated y velocity: "+newvy);
		p.setVelX(newvx);
		p.setVelY(newvy);
		p.setTarget(x, y);
	}
	
	public void gatherParticles(GraphicsParticle array[], int particles, float x, float y) {
		int count = 0;
		for(GraphicsParticle gp : array) {
			if(gp!=null) {
				float theta = gp.getTheta();
				float vx = x-gp.getX();
				float vy = y-gp.getY();
				
				float tantheta = (float) Math.atan2(vy, vx);
				
				float newvx = (float) Math.cos((double) tantheta);
				float newvy = (float) Math.sin((double) tantheta);
		
				gp.setVelX(newvx);
				gp.setVelY(newvy);
			}
			count++;
			if(count>particles) {
				break;
			}
		}
	}
	
	public void explodeParticles(GraphicsParticle array[], int particles, float x, float y) {
		int count = 0;
		for(GraphicsParticle gp : array) {
			if(gp!=null) {
			//	float theta = sp.getTheta();
				float vx = x-gp.getX();
				float vy = y-gp.getY();
				
				float d = (vx*vx) + (vy*vy);
				float distance = (float) Math.sqrt(d);
				if(distance<100) {
					//System.out.println("Distance < 50");
					float tantheta = (float) Math.atan2(vy, vx);
					//use this value for theta for new velocity values!
					
					float newvx = (float) Math.cos((double) tantheta);
					float newvy = (float) Math.sin((double) tantheta);
					
					gp.setVelX(-newvx);
					gp.setVelY(-newvy);
				}				
			}
			count++;
			if(count>particles) {
				break;
			}
		}
	}
	
	
	
	public void updateParticleCount() {
		if(createParticles) {
			addParticle();
			addParticle();
		}
	}
	
	@Override
	public int getID() {
		
		return 0;
	}

}
