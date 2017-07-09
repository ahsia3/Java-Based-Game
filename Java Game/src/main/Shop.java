package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Shop extends MouseAdapter{

	Handler handler;
	HUD hud;
	
	public int B1 = 100;
	public int B2 = 100;
	public int B3 = 100;
	
	public Shop(Handler handler, HUD hud){
		this.handler = handler;
		this.hud = hud;
	}
	
	public void render(Graphics g){
		g.setColor(Color.white);
		g.setFont(new Font("arial", 0, 48));
		g.drawString("SHOP", (Game.WIDTH/2)-80, 50);
		
		
		//box 1
		g.setFont(new Font("arial",0, 17));
		g.drawString("Upgrade ", 105, 180);
		g.drawString("Cost: ", 120, 205);
		g.drawRect(100, 150, 120, 80);
		
		//box 2
		
		g.drawString("Upgrade ", 255, 180);
		g.drawString("Cost: ", 270, 205);
		g.drawRect(250, 150, 120, 80);
				
		//box 3
		g.setColor(Color.red);
		g.drawString("Refill Health: ", 415, 180);
		g.setColor(Color.white);
		g.drawString("Cost: ", 420, 205);
		g.drawRect(400, 150, 120, 80);
		
		g.setFont(new Font("arial",0, 20));
		g.drawString("SCORE: " , Game.WIDTH/2-75, 302);
		
		g.setFont(new Font("arial",0, 18));
		g.drawString("Press SPACE to go back " , Game.WIDTH/2-115, 400);
		
		g.setFont(new Font("arial",0, 17));
		g.setColor(Color.red);
		g.drawString("Health ", 170, 180);
		g.drawString("Speed ", 320, 180);
		g.drawString(" " + hud.getScore() , Game.WIDTH/2, 300);
		g.drawString(" " + B1, 160, 205);
		g.drawString(" " + B2, 310, 205);
		g.drawString(" " + B3, 460, 205);
		
	}
	
	public void mousePressed(MouseEvent e){
		int mx = e.getX();
		int my = e.getY();
		
		//box 1
		if(mx >= 100 && mx <= 220){
			if(my >= 150 && my <= 230){
				//you selected box 1
				if(hud.getScore() >= B1){
					hud.setScore(hud.getScore() - B1);
					B1 += 100;
					hud.bounds += 20;
				
				}
				
			}
		}

		//box 2
		if(mx >= 250 && mx <= 370){
			if(my >= 150 && my <= 230){
				//you selected box 2
				if(hud.getScore() >= B2){
					hud.setScore(hud.getScore() - B2);
					B2 += 100;
					handler.speed++;
				}
			}
		}

		//box 3
		if(mx >= 400 && mx <= 520){    //add from box's g.drawRect() X's and Y's
			if(my >= 150 && my <= 230){
				//you selected box 3
				if(hud.getScore() >= B3 && !(hud.HEALTH == 100 + (hud.bounds/2))){ //no need to waste score for already max health
					hud.setScore(hud.getScore() - B3);
					B3 += 50;
					hud.HEALTH = (100 + (hud.bounds/2));
					
				}
			}
		}
	}
}
