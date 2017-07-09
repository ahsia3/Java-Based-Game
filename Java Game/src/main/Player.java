package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Player extends GameObject {
	
	Random r=new Random();
	Handler handler;
	
	private BufferedImage player_image;

	public Player(int x, int y, ID id, Handler handler) {
		super(x, y, id);
		this.handler = handler;
		
		SpriteSheet ss = new SpriteSheet(Game.sprite_sheet);
		player_image = ss.grabImage(1, 1, 32, 32);
	}
	
	public Rectangle getBounds(){
		return new Rectangle((int)x, (int)y, 32, 32);
	}
	
	public void tick(){
		x += velX;
		y += velY;
		
		x=Game.clamp(x,  0, Game.WIDTH - 37);
		y=Game.clamp(y,  0, Game.HEIGHT - 60);
		
		//handler.addObject(new Trail((int)x, (int)y, ID.Trail, Color.white, 30, 30, 0.1f, handler));
		
		collision();
	}
	
	private void collision(){
		for(int i=0; i < handler.object.size(); i++){
			
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getID()==ID.BasicEnemy || tempObject.getID() == ID.FastEnemy || tempObject.getID() == ID.SmartEnemy){	//tempObject is now basic enemy
				if(getBounds().intersects(tempObject.getBounds())){
					//collision code
					HUD.HEALTH-=2;
				}
			}
			
			//Collision of the Boss = Take lots of damage
			if(tempObject.getID()==ID.EnemyBoss){
				if(getBounds().intersects(tempObject.getBounds())){
					HUD.HEALTH-=20;
				}
			}
			//Collision of the Boss Bullets = Take lots of damage
			if(tempObject.getID()==ID.EnemyBossBullet){
				if(getBounds().intersects(tempObject.getBounds())){
					HUD.HEALTH-=4;
				}
			}
		}
	}
	
	public void render (Graphics g){
		
	//	Graphics2D g2d = (Graphics2D) g;
		
		//g.setColor(Color.green);
		//g2d.draw(getBounds());
		
	//	g.setColor(Color.white);
	//	g.fillRect((int)x,(int) y, 32, 32);
		
		g.drawImage(player_image, (int)x, (int)y, null);
	}

}
