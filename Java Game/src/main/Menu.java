package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import main.Game.STATE;

public class Menu extends MouseAdapter{

	private Game game;
	private Handler handler;
	private HUD hud;
	private Shop shop;
	private Random r = new Random();
	
	
	public Menu(Game game, Handler handler, HUD hud, Shop shop){
		this.game = game;
		this.handler = handler;
		this.hud = hud;
		this.shop = shop;
	}
	public void mousePressed(MouseEvent e){
		int mx = e.getX();
		int my = e.getY();
		
		if(game.gameState == STATE.Menu){
			//play button
			if(mouseOver(mx, my, 210, 150, 200, 64)){
				game.gameState = STATE.Select;
				
				if(Game.muted == false){
				AudioPlayer.getSound("menu_sound").play();
				}
				return;
			}
			//help button
			if(mouseOver(mx, my, 210, 250, 200, 64)){
				game.gameState= STATE.Help;
				
				if(Game.muted == false){
					AudioPlayer.getSound("menu_sound").play();
					}
			}
			
			//quit button
			if(mouseOver(mx, my, 210, 350, 200, 64)){
				System.exit(1);
			}
		}
		
		if(game.gameState == STATE.Select){
			//Normal button
			if(mouseOver(mx, my, 210, 150, 200, 64)){
				game.gameState = STATE.Game;
				handler.addObject(new Player(Game.WIDTH/2-32, Game.HEIGHT/2-32, ID.Player, handler));
				handler.clearEnemies();
				handler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH), r.nextInt(Game.HEIGHT), ID.BasicEnemy, handler));
			
				game.diff = 0;

					
				if(Game.muted == false){
					AudioPlayer.getSound("menu_sound").play();
					}
			}
			//Hard button
			if(mouseOver(mx, my, 210, 250, 200, 64)){
				game.gameState = STATE.Game;
				handler.addObject(new Player(Game.WIDTH/2-32, Game.HEIGHT/2-32, ID.Player, handler));
				handler.clearEnemies();
				handler.addObject(new HardEnemy(r.nextInt(Game.WIDTH), r.nextInt(Game.HEIGHT), ID.BasicEnemy, handler));
			
				game.diff = 1;

				
				if(Game.muted == false){
					AudioPlayer.getSound("menu_sound").play();
					}
			}
			
			//Back button
			if(mouseOver(mx, my, 210, 350, 200, 64)){
					game.gameState = STATE.Menu;
					if(Game.muted == false){
						AudioPlayer.getSound("menu_sound").play();
						}
					return;
			}
		}
		
		//back button for help
				if(game.gameState == STATE.Help){
					if(mouseOver(mx, my, 210, 350, 200, 64)){
						game.gameState = STATE.Menu;
						if(Game.muted == false){
							AudioPlayer.getSound("menu_sound").play();
							}
						return;
					}
					
				}
				
		//TRT AGAIN button when End game screen
		if(game.gameState == STATE.End){
			if(mouseOver(mx, my, 210, 350, 200, 64)){
				game.gameState = STATE.Menu;
				//reset everything back to normal
				hud.setLevel(1);
				hud.setScore(0);
				hud.setfinalScore(0);
				hud.bounds = 0;
				handler.speed = 5;
				shop.B1 = 1000;
				shop.B2 = 1000;
				shop.B3 = 1000;
				shop.b1click=0;
				shop.b2click=0;
				shop.b3click=0;
				
		
				if(Game.muted == false){
					AudioPlayer.getSound("menu_sound").play();
					}
			}
		}
	}
	public void mouseReleased(MouseEvent e){
		
	}
	private boolean mouseOver(int mx, int my, int x, int y, int width, int height){
		if(mx > x && mx < x + width){
			if(my > y && my < y + height){
				return true;
			}
			else return false;
		}else return false;
	}
	public void tick(){
		
	}
	public void render(Graphics g){
		if(game.gameState == STATE.Menu){
		Font fnt = new Font("arial", 1, 50);
		Font fnt2 = new Font("arial", 1, 30);
		Font fnt0 = new Font("Zapfino", 1, 15);
		
		g.setFont(fnt0);
		g.setColor(Color.BLACK);
		g.drawString("@ahsia   |   Summer 2017", 10, 446);
		g.drawString("Version 0.1.4", 520, 446);
		
		g.setFont(fnt);
		g.setColor(Color.white);
		g.drawString("WASDodge", 180, 70);
		
		g.setFont(fnt2);
		g.drawRect(210, 150, 200, 64);
		g.drawString("Play", 270, 190);
		
		g.setColor(Color.white);
		g.drawRect(210, 250, 200, 64);
		g.drawString("Help", 270, 290);
		
		g.setColor(Color.white);
		g.drawRect(210, 350, 200, 64);
		g.drawString("Quit", 270, 390);
		}else if(game.gameState == STATE.Help){
			Font fnt = new Font("arial", 1, 50);
			Font fnt2 = new Font("arial", 1, 30);
			Font fnt3 = new Font("arial", 1, 15);
			
			g.setFont(fnt);
			g.setColor(Color.white);
			g.drawString("Help", 260, 70);
			
			g.setFont(fnt3);
			g.drawString("Use WASD keys to move player and dodge enemies", 120, 150);
			g.drawString("Press P key to pause the game", 120, 200);
			g.drawString("Press SPACEBAR key to bring up the Shop", 120, 250);
			g.drawString("Press M key to mute music", 120, 300);
			
			g.setFont(fnt2);
			g.drawRect(210, 350, 200, 64);
			g.drawString("Back", 270, 390);
		}else if(game.gameState == STATE.End){
			Font fnt = new Font("arial", 1, 50);
			Font fnt2 = new Font("arial", 1, 30);
			Font fnt3 = new Font("arial", 1, 15);
			
			g.setFont(fnt);
			g.setColor(Color.white);
			g.drawString("GAME OVER", 170, 70);
			
			g.setFont(fnt2);
			g.drawRect(210, 350, 200, 64);
			g.drawString("Main Menu", 235, 392);
			
			g.setFont(fnt3);
			g.drawString("Final Score: ", 213, 130);
			g.drawString("Spent: ", 250, 230);
			g.drawString("Bank: ", 256, 180);
			g.drawString("rekt on level ", 210, 280);
			
			g.drawString("HP x", 370, 230);
			g.drawString("Speed x", 432, 230);
			g.drawString("Restore x", 520, 230);
			
			//IF SPENT nothing (0) = white color, ELSE spent = blue
			if(hud.getspendScore() == 0){
				g.setColor(Color.red);
				g.drawString(" " + hud.getspendScore(), 306, 230);
			}else{
				g.setColor(Color.blue);
				g.drawString(" " + hud.getspendScore(), 306, 230);
			}
			
			//MONEY IN THE BANK THAT YOU LEFT BEHIND
			if(hud.getScore() == 0){
				g.setColor(Color.red);
				g.drawString(" " + hud.getScore(), 305, 180);
			}else{
				g.setColor(Color.blue);
				g.drawString(" " + hud.getScore(), 305, 180);
			}
			
			g.setColor(Color.blue);
			g.drawString(" " + hud.getfinalScore(), 305, 130);
			g.drawString(" " + hud.getLevel(), 305, 280);
			
			//IF Upgraded = blue, ELSE 0 = white
			if(shop.b1click > 0){
				g.setColor(Color.blue);
				g.drawString(" " + shop.b1click, 402, 230);
			}else{
				g.setColor(Color.red);
				g.drawString(" " + shop.b1click, 402, 230);
			}
			
			//IF Upgraded = blue, ELSE 0 = white
			if(shop.b2click > 0){
				g.setColor(Color.blue);
				g.drawString(" " + shop.b2click, 489, 230);
			}else{
				g.setColor(Color.red);
				g.drawString(" " + shop.b2click, 489, 230);
			}
			
			//IF Upgraded = blue, ELSE 0 = white
			if(shop.b3click > 0){
				g.setColor(Color.blue);
				g.drawString(" " + shop.b3click, 588, 230);
			}else{
				g.setColor(Color.red);
				g.drawString(" " + shop.b3click, 588, 230);
			}
			
			g.setColor(Color.black);
			if(game.diff==1){
				g.drawString("Hard difficulty ", 260, 330);
			}else if(game.diff ==0){
				g.drawString("Normal difficulty ", 250, 330);
			}
			
			
					
		}else if(game.gameState == STATE.Select){
			Font fnt = new Font("arial", 1, 45);
			Font fnt2 = new Font("arial", 1, 30);
			Font fnt0 = new Font("Zapfino", 1, 15);
			
			g.setFont(fnt);
			g.setColor(Color.white);
			g.drawString("SELECT DIFFICULTY", 90, 70);
			
			g.setFont(fnt2);
			g.drawRect(210, 150, 200, 64);
			g.drawString("Normal", 260, 192);
			
			g.setColor(Color.white);
			g.drawRect(210, 250, 200, 64);
			g.drawString("Hard", 270, 292);
			
			g.setColor(Color.white);
			g.drawRect(210, 350, 200, 64);
			g.drawString("Back", 270, 392);
			
			}
	}
}
