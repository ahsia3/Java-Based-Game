package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Game extends Canvas implements Runnable{

	private static final long serialVersionUID = 6691247796639148462L;
	
	public static final int WIDTH = 640, HEIGHT = WIDTH / 12 * 9;
	
	private Thread thread;
	private boolean running = false;
	
	public static boolean paused = false;
	public int diff = 0;
	//0 = normal difficulty; 1 = hard difficulty;
	
	private Shop shop;
	
	private Random r;
	private Handler handler; 
	private HUD hud;
	private spawn spawner;
	private Menu menu;
	
	public enum STATE {
		Menu,
		Select,
		Help,
		End,
		Shop,
		Game
	};
	
	public static STATE gameState = STATE.Menu;
	
	public static BufferedImage sprite_sheet;
	
	public Game(){
		
		BufferedImageLoader loader = new BufferedImageLoader();
		
		sprite_sheet = loader.loadImage("/sprite_sheet.png");
		
		handler = new Handler();
		hud = new HUD();
		shop = new Shop(handler, hud);
		menu = new Menu(this, handler, hud, shop);
		this.addKeyListener(new KeyInput(handler, this));
		this.addMouseListener(menu);
		this.addMouseListener(shop);
		
		AudioPlayer.load();
		AudioPlayer.getMusic("music").loop(1, 0.1f);
		
		new Window(WIDTH, HEIGHT, "WASDodge", this);
		
		spawner = new spawn(handler, hud, this);
		r = new Random();
		
		if(gameState == STATE.Game){
			handler.addObject(new Player(WIDTH/2-32, HEIGHT/2-32, ID.Player, handler));
			handler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 45), ID.BasicEnemy, handler));
		}else{
			for(int i=0; i< 10; i++){
				handler.addObject(new MenuParticle(r.nextInt(WIDTH), r.nextInt(HEIGHT), ID.MenuParticle, handler));
			}
		}
	}
		
	
	public synchronized void start(){
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop(){
		try{
			thread.join();
			running = false;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void run(){
		this.requestFocus(); //Dont need to click on screen to put in keyboard input, happens automatically.
		long lastTime = System.nanoTime();
		double amountofTicks = 60.0;
		double ns = 1000000000 / amountofTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime)/ns;
			lastTime = now;
			while(delta >=1){
				tick();
				delta--;
			}
			if(running)
				render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}
	
	private void tick(){
		
		if (gameState== STATE.Game){
			if(!paused){
				hud.tick();
				spawner.tick();
				handler.tick();
				
				if(HUD.HEALTH <= 0){
					HUD.HEALTH = 100;
					gameState = STATE.End;
					handler.clearEnemies();	
					for(int i=0; i< 10; i++){
						handler.addObject(new MenuParticle(r.nextInt(WIDTH), r.nextInt(HEIGHT), ID.MenuParticle, handler));
					}
					
				}
			}
			
		
		}else if(gameState == STATE.Menu || gameState == STATE.End || gameState == STATE.Select || gameState == STATE.Help){
			menu.tick();
			handler.tick();
		}
		
	}
	
	private void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.cyan);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		
		if(paused){
			g.setColor(Color.darkGray);
			g.drawString("PAUSED", 10, 120);
		}
		
		if(gameState == STATE.Game){
			hud.render(g);
			handler.render(g);
		}else if(gameState == STATE.Shop){
			shop.render(g);
		}else if(gameState == STATE.Menu || gameState == STATE.Help || gameState == STATE.Select){
			menu.render(g);
			handler.render(g);
		}else if(gameState == STATE.End){
			menu.render(g);
			handler.removeObject(null);
		}
			
		g.dispose();
		bs.show();
	}
	
	public static float clamp(float var, float min, float max){  
		if(var >= max)
			return var = max;
		else if(var <= min)
			return var = min;
		else
			return var;
	}
	public static void main(String args[]){
		new Game();
	}
}
