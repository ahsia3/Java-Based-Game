package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class DamageOverTimeEnemy extends GameObject {

	private Handler handler;
	
	Random r = new Random();
	
	public DamageOverTimeEnemy(int x, int y, ID id, Handler handler) {
		super(x, y, id);
		
		this.handler = handler;
		velX = 0;
		velY = 0;
	
	}
	
	public Rectangle getBounds(){
		return new Rectangle(0, 125, 633, 200);
	}

	public void tick(){
		x+=velX;
		y+=velY;
		
		//if(y <= 0 || y >=Game.HEIGHT - 32) velY *=-1;
		//if(x <= 0 || x >=Game.WIDTH - 16) velX *=-1;
		
		//if(y >= Game.HEIGHT || y>=Game.WIDTH){
		//	handler.removeObject(this);
		//}
		
		//handler.addObject(new Trail((int)x, (int)y, ID.Trail, Color.red, 16, 16, 0.1f, handler));
	}
	
	public void render(Graphics g){
		g.setColor(Color.cyan);
		g.drawRect(0, 125, 633, 200);
		
		g.setColor(Color.orange);
		
		g.drawString("Cheese THIS huehuehuehuehuehuehuehuehuehuehuehuehuehuehuehuehuehuehuehuehuehuehuehuehuehue", 4, 325);
	}
}
