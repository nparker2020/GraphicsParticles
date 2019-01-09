package com.noahparker.graphicsparticles;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class GraphicsParticles extends StateBasedGame {
	GraphicsState gs;
	
	
	public static void main(String[] args) {
		try {
	         AppGameContainer container = new AppGameContainer(new GraphicsParticles("Graphics Particles"));
	         container.setDisplayMode(720, 480, false);
	         container.setVSync(true);
	         container.setTargetFrameRate(60);
	         container.setShowFPS(true);
	         container.start();
	      } catch (SlickException e) {
	         e.printStackTrace();
	      }
	}
	
	public GraphicsParticles(String name) {
		super(name);
		gs = new GraphicsState();
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		// TODO Auto-generated method stub
		addState(gs);
	}

}
