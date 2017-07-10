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
			
			g.setFont(fnt3);
			g.drawString("You lost with a score of: " + hud.getfinalScore(), 210, 200);
			
			
			g.drawString("How much you spent: " + hud.getspendScore(), 210, 250);
			
			g.drawString("rekt on level " + hud.getLevel(), 210, 300);
			
			g.setFont(fnt2);
			g.drawRect(210, 350, 200, 64);
			g.drawString("Main Menu", 235, 392);
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
