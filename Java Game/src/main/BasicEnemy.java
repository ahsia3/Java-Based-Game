package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class BasicEnemy extends GameObject {

	private Handler handler;
	
	private BufferedImage enemy_image;
	
	public BasicEnemy(int x, int y, ID id, Handler handler) {
		super(x, y, id);
		
		this.handler = handler;
		velX=5;
		velY=5;
		
		SpriteSheet ss = new SpriteSheet(Game.sprite_sheet);
		
		enemy_image = ss.grabImage(2, 1, 16, 16);
	
	}
	
	public Rectangle getBounds(){
		return new Rectangle((int)x, (int)y, 16, 16);
	}

	public void tick(){
		x+=velX;
		y+=velY;
		
		if(y <= 0 || y >=Game.HEIGHT - 32) velY *=-1;
		if(x <= 0 || x >=Game.WIDTH - 16) velX *=-1;
		
		handler.addObject(new Trail((int)x, (int)y, ID.Trail, Color.red, 16, 16, 0.1f, handler));
	}
	
	public void render(Graphics g){
		g.drawImage(enemy_image, (int)x, (int)y, null);
	}
}
