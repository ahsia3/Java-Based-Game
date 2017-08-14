package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import javax.swing.JOptionPane;

public class Game extends Canvas implements Runnable{

	private static final long serialVersionUID = 6691247796639148462L;
	
	public static final int WIDTH = 640, HEIGHT = WIDTH / 12 * 9;
	
	private Thread thread;
	private boolean running = false;
	
	public static boolean paused = false;
	public static boolean muted = false;
	public int diff = 0;
	//0 = normal difficulty; 1 = hard difficulty;
	
	private Shop shop;
	
	private Random r;
	private Handler handler; 
	private HUD hud;
	private spawn spawner;
	private Menu menu;
	private String highscore = "";
	private String scoreHARD = "";
	
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
		
	     do{
			AudioPlayer.load();
			AudioPlayer.getMusic("music").loop(1, 0.1f);
		}while(muted);
		
		new Window(WIDTH, HEIGHT, "WASDodge", this);
		
		spawner = new spawn(hud, handler, this);
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
	
	/* ON NORMAL DIFFICULTY
	 * Gets file (highscore.dat) and reads the only line in the file
	 *  @param reader.readLine();
	 */
	public String GetHighScore(){
		//format: 	Brandon:100
		FileReader readFile = null;
		BufferedReader reader = null;
		try{
			
		readFile = new FileReader("highscore.dat");
		reader = new BufferedReader(readFile);
		return reader.readLine();
		
		}catch(Exception e){
			return "Nobody - 0";
		}finally{
			//close the reader
			try{
				if(reader !=null){
					reader.close();
				}
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	
	/* ON NORMAL DIFFICULTY
	 * Check to see if highscore.dat file exists, if not: make one
	 *  and ask the user to input a name into file if highScore is beaten
	 * 
	 */
	
	public void checkScore(){
		if(highscore.equals("")){
			return;
		}
		if(hud.getfinalScore() > Integer.parseInt(highscore.split(" - ")[1])){
			//user has set a new record
			String name = JOptionPane.showInputDialog("You set a new highscore. What is your name?");
			highscore = name + " - " + hud.getfinalScore();
			
			File scoreFile = new File("highscore.dat");
			if(!scoreFile.exists()){
				try {
					scoreFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			FileWriter writeFile = null;
			BufferedWriter writer = null;
			try{
				writeFile = new FileWriter(scoreFile);
				writer = new BufferedWriter(writeFile);
				writer.write(this.highscore);
			}catch(Exception e){
				//errors
			}finally{
				try{
					if(writer != null){
						writer.close();
					}
				}catch (Exception e){
					
				}
			}
		}
	}
	
	/* ON HARD DIFFICULTY
	 * Gets file (highscoreHARD.dat) and reads the only line in the file
	 *  @param reader.readLine();
	 */
	public String GetHighScoreHARD(){
		//format: 	Brandon:100
		FileReader readFile = null;
		BufferedReader reader = null;
		try{
			
		readFile = new FileReader("highscoreHARD.dat");
		reader = new BufferedReader(readFile);
		return reader.readLine();
		
		}catch(Exception e){
			return "Nobody - 0";
		}finally{
			//close the reader
			try{
				if(reader !=null){
					reader.close();
				}
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	
	/* ON HARD DIFFICULTY
	 * Check to see if highscoreHARD.dat file exists, if not: make one
	 *  and ask the user to input a name into file if highScore is beaten
	 * 
	 */
	
	public void checkScoreHARD(){
		if(scoreHARD.equals("")){
			return;
		}
		if(hud.getfinalScore() > Integer.parseInt(scoreHARD.split(" - ")[1])){
			//user has set a new record
			String name = JOptionPane.showInputDialog("You set a new highscore. What is your name?");
			scoreHARD = name + " - " + hud.getfinalScore();
			
			File scoreFile = new File("highscoreHARD.dat");
			if(!scoreFile.exists()){
				try {
					scoreFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			FileWriter writeFile = null;
			BufferedWriter writer = null;
			try{
				writeFile = new FileWriter(scoreFile);
				writer = new BufferedWriter(writeFile);
				writer.write(this.scoreHARD);
			}catch(Exception e){
				//errors
			}finally{
				try{
					if(writer != null){
						writer.close();
					}
				}catch (Exception e){
					
				}
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
				if(muted){
					AudioPlayer.load();
					AudioPlayer.getMusic("music").loop(1, 0.1f);
					}
				if(HUD.HEALTH <= 0){
					HUD.HEALTH = 100;
					gameState = STATE.End;
					handler.clearEnemies();	
					for(int i=0; i< 10; i++){
						handler.addObject(new MenuParticle(r.nextInt(WIDTH), r.nextInt(HEIGHT), ID.MenuParticle, handler));
					}
				}
			}else{
				//When it is in pause mode
				if(muted){
					AudioPlayer.load();
					AudioPlayer.getMusic("music").loop(1, 0.1f);
					}
			}
			
			
		
		}else if(gameState == STATE.Menu || gameState == STATE.End || gameState == STATE.Select || gameState == STATE.Help){
			menu.tick();
			handler.tick();
			if(muted){
				AudioPlayer.load();
				AudioPlayer.getMusic("music").loop(1, 0.1f);
				}
		}else if(gameState == STATE.Shop){
			if(muted){
				AudioPlayer.load();
				AudioPlayer.getMusic("music").loop(1, 0.1f);
				}
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
			g.drawString("PAUSED", 15, 120);
		}
		
		if(muted){
			g.setColor(Color.darkGray);
			g.drawString("Muted", 15, 220);
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
			
			//Normal difficulty
			if(diff == 0){
			//When Game ends check score and display for HighScores
				checkScore();
				if (highscore.equals("")){
					//init highscore
					highscore = GetHighScore();
				}
			}
			
			//Hard difficulty
			if(diff == 1){
			//When Game ends check score and display for HighScores
				checkScoreHARD();
				if (scoreHARD.equals("")){
					//init highscore
					scoreHARD = GetHighScoreHARD();
				}
			}

			Font fnt0 = new Font("Book Antiqua", 1, 15);
			g.setFont(fnt0);
			g.setColor(Color.darkGray);
			g.drawString("HIGH SCORE Normal: ", 5 , 125);
			g.drawString("----------------------------------", 0 , 100);
			g.drawString("----------------------------------", 0 , 165);
			g.drawString("----------------------------------", 0 , 230);
			g.drawString("" + highscore, 10 , 145);
			
			g.drawString("HIGH SCORE Hard: ", 5 , 190);
			g.drawString("" + scoreHARD, 10 , 210);
			
			//handler.removeObject(null);
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
