package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImage;

public class FastEnemy extends GameObject {

	private Handler handler;
	
	private BufferedImage fast_enemy;
	
	public FastEnemy(int x, int y, ID id, Handler handler) {
		super(x, y, id);
		
		this.handler = handler;
		velX=2;
		velY=9;
		
		SpriteSheet ss = new SpriteSheet(Game.sprite_sheet);
		fast_enemy = ss.grabImage(3, 1, 16, 16);
	
	}
	
	public Rectangle getBounds(){
		return new Rectangle((int)x, (int)y, 16, 16);
	}

	public void tick(){
		x+=velX;
		y+=velY;
		
		if(y <= 0 || y >=Game.HEIGHT - 32) velY *=-1;
		if(x <= 0 || x >=Game.WIDTH - 16) velX *=-1;
		
		handler.addObject(new Trail((int)x, (int)y, ID.Trail, Color.black, 16, 16, 0.1f, handler));
	}
	
	public void render(Graphics g){
		g.drawImage(fast_enemy, (int)x, (int)y, null);
	}
}
