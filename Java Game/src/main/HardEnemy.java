package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

public class HardEnemy extends GameObject {

	private Handler handler;
	private Random r = new Random();
	
	private BufferedImage hard_enemy;
	
	public HardEnemy(int x, int y, ID id, Handler handler) {
		super(x, y, id);
		
		this.handler = handler;
		velX=5;
		velY=5;
		
		SpriteSheet ss = new SpriteSheet(Game.sprite_sheet);
		
		hard_enemy= ss.grabImage(4, 1, 16, 16);
	}
	
	public Rectangle getBounds(){
		return new Rectangle((int)x, (int)y, 16, 16);
	}

	public void tick(){
		x+=velX;
		y+=velY;
		
		if(y <= 0 || y >=Game.HEIGHT - 32){
			if(y<0){
				velY = -(r.nextInt(7)+1)*-1;
			}else{
				velY = (r.nextInt(7)+1)*-1;
			}
		}
		if(x <= 0 || x >=Game.WIDTH - 16){
			if(x<0){
				velX = -(r.nextInt(7)+100)*-1;
			}else{
				velX = (r.nextInt(7)+100)*-1;
			}
		}
		
		handler.addObject(new Trail((int)x, (int)y, ID.Trail, Color.yellow, 16, 16, 0.1f, handler));
	}
	
	public void render(Graphics g){
		g.drawImage(hard_enemy, (int)x, (int)y, null);
	}
}
