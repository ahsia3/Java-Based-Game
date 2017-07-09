package last.main;

import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {
	
	private boolean clearing = false;

	//contains all the game objects
	LinkedList<GameObject> object = new LinkedList<GameObject>();
	
	//ticks - updates all game objects
	public int speed = 5;
	
	public void tick(){
		for(int i=0; i < object.size(); i++){
			GameObject tempObject = object.get(i);
			
			tempObject.tick();
		}
	}
	
	//renders all game objects @param g
	public void render(Graphics g){
		for(int i=0; i < object.size(); i++){
			if(clearing){  //When clicking Play button while rendering we get a NullPointerException so we should exit method immediately. 
				return;
			}
			GameObject tempObject = object.get(i);
			
			tempObject.render(g);
		}
	}
	
	
	//deletes all enemies on screen
	public void clearEnemies(){
		clearing = true;
		for(int i=0; i < object.size(); i++){
			GameObject tempObject = object.get(i);
			
			if(tempObject.getID() == ID.Player){
				object.clear();
				
				if(Game.gameState != Game.STATE.End){
					addObject(new Player((int)tempObject.getX(),(int)tempObject.getY(),ID.Player,this));
				}
				break; //no need to continue the loop. The list is now cleared.
			}
		}
		clearing=false;
	}
	
	public void addObject(GameObject object){
		this.object.add(object);
	}
	
	public void removeObject(GameObject object){
		this.object.remove(object);
	}
}
