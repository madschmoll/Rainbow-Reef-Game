package Game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author madelineschmoll
 */
public class GameWorld extends Canvas implements Runnable {
    
    private Thread thread;
    private boolean running;
    private int counter;
    public enum State {
        START, PLAYING, GAMEOVER;
    }
    private State state;
    
    
    private static JFrame frame;
    private final static int height = 600, width = 450;
    private int score; 
    private String scoreString;
    private Map map;
    
    private final String gameTitle = "Rainbow Reef";
    
    private boolean levelOne, levelTwo, levelThree;
    private boolean lost;
    
    private BufferedImage background,title, losingScreen, winningScreen;
    private BufferedImage pink, yellow, red, green, lightBlue, darkBlue, white;
    private BufferedImage borderBlock, blockLife;
    private BufferedImage smallPaddle, largePaddle, ballImage, smallBL, bigBL, life;
    private int bigLegWidth, blockHeight, blockWidth;
    
    private Katch katch;
    private PaddleControl pc;
    private Pop pop;
    private BigLeg l1bigLeg1, l2bigLeg1, l2bigLeg2, l3bigLeg1, l3bigLeg2;
    
    private ArrayList<GameObject> gameObjects;
    private CollisionDetector cd;
    
    private GameEventObservable geo;
    
    public static void main(String args[]) throws IOException{
        GameWorld game = new GameWorld();
        
        game.setPreferredSize(new Dimension(height , width));
        game.setMaximumSize(new Dimension(height, width));
        game.setMinimumSize(new Dimension(height , width));
       
        game.setSize(new Dimension(height, width)); 
        
        frame = new JFrame(game.getTitle());
        frame.setSize(height,width);
        frame.addWindowListener(new WindowAdapter() {  
        @Override
	public void windowGainedFocus(WindowEvent e) {
                        game.requestFocusInWindow();
		    }
	    });
        
	frame.setVisible(true);
	frame.setResizable(false);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
        frame.add(game);
        
        game.init();
        
        game.start();
        
    }
    
    public GameWorld(){
        geo = new GameEventObservable();
        gameObjects = new ArrayList<>();
        this.state = State.START;
        this.score = 0;
        this.scoreString = "Score: " + Integer.toString(score);
        this.levelOne = true;
        this.levelTwo = false;
        this.levelThree = false;
        this.lost = false;
    }
    
    public void init(){
        
        try {
            
            this.smallBL = ImageIO.read(new File("Resources/BigLeg_small.gif"));
            this.bigBL = ImageIO.read(new File("Resources/BigLeg.gif"));
            
            this.background = ImageIO.read(new File("Resources/Background1.bmp"));
            this.title = ImageIO.read(new File("Resources/Title.bmp"));
            
            this.ballImage = ImageIO.read(new File("Resources/Pop.png")); 
            
            this.pink = ImageIO.read(new File("Resources/PinkBlock.gif"));
            this.green = ImageIO.read(new File("Resources/GreenBlock.gif"));
            this.red = ImageIO.read(new File("Resources/RedBlock.gif"));
            this.white = ImageIO.read(new File("Resources/WhiteBlock.gif"));
            this.lightBlue = ImageIO.read(new File("Resources/LBlueBlock.gif"));
            this.darkBlue = ImageIO.read(new File("Resources/DBlueBlock.gif"));
            this.yellow = ImageIO.read(new File("Resources/YellowBlock.gif"));
            this.blockLife = ImageIO.read(new File("Resources/Block_Life.gif"));
            this.borderBlock = ImageIO.read(new File("Resources/Wall.gif"));
           
            this.blockHeight = yellow.getHeight();
            this.blockWidth = yellow.getWidth();
            this.bigLegWidth = smallBL.getWidth();
            
            this.smallPaddle = ImageIO.read(new File("Resources/Katch_small.gif"));
            this.largePaddle = ImageIO.read(new File("Resources/Katch.gif"));
            
            this.life = ImageIO.read(new File("Resources/heart.gif"));
            
            this.losingScreen = ImageIO.read(new File("Resources/gameOverScreen.jpeg"));
            this.winningScreen = ImageIO.read(new File("Resources/Congratulation.gif"));
            
        
        
        } catch (IOException ex) {
            Logger.getLogger(GameWorld.class.getName()).log(Level.SEVERE, null, ex);
        }
        
     //   map = new Map("levelOne.txt");
        
        
        katch = new Katch(250,375,largePaddle);
        
        int popX = katch.getX();
        pop = new Pop(popX, (katch.getY() - 50) , ballImage);
        katch.getPop(pop);
        pop.getKatch(katch);
        pc = new PaddleControl(katch, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE);
        
        
        this.gameObjects.add(pop);
        this.gameObjects.add(katch);
        
        addLevelOne();
        
        frame.add(katch);
        addKeyListener(pc);
        this.geo.addObserver(katch);
        
        cd = new CollisionDetector();
           
    }
    
    
    // from tank game
    @Override
    public void run(){ // run will execute when thread starts
       
       long lastTime = System.nanoTime(); // operates in nano seconds
       final int numberOfTicks = 60; // frames per second
       double ns = 1000000000 / numberOfTicks;
       double delta = 0; 
       
        while(running){
            long currTime = System.nanoTime();
            delta += (currTime - lastTime) / ns;
            lastTime = currTime;
            if(delta > 0){
                update();
                counter++;
                
            }
            
            switch(state)
            {
            case START:
                drawStartPage();
                break;
            case PLAYING:
                //DRAW GAME
                drawGame();
                break;
            case GAMEOVER:
                drawGameOverPage();
                //DRAW THE GAME OVER SCREEN
                break;
            default:
                throw new RuntimeException("Unknown state: " + state);
            }
            
         //   drawGame();
        }
        
        stop();
    }
   
    // from tank game
    public synchronized void start() {
        
        if(running){
            return;
        } 
        
        running = true;
        System.out.println();
        thread = new Thread(this); //this changed
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
        
    }
    
    
    // from tank game
    public synchronized void stop(){
        
        if(!running){
            return;
        }
        
        running = false;
        
        try {
            thread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(GameWorld.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.exit(1);
        
    }
    
    
    public void update(){
        
        if(pop.getLives() < 1){
            this.lost = true;
            this.state = State.GAMEOVER;
        }
        
        if(!l1bigLeg1.getShow()){
            levelOne = false;
            levelTwo = true;
        }
        
        for(int i = 0; i < gameObjects.size(); i++){
            gameObjects.get(i).update();
            if(!gameObjects.get(i).getShow()){
                gameObjects.remove(i);
            }
        }
        for(int i = 0; i < gameObjects.size(); i++){
            for(int j = i + 1; j < gameObjects.size(); j++)
                cd.checkCollision(gameObjects.get(i), gameObjects.get(j));
        }
        
    }
    
    public void drawStartPage(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            createBufferStrategy(2);
            return;
        }
        
        Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();
        
        g2d.drawImage(title, 0, 0, this);
        
        g2d.dispose();
        bs.show();
        
        if(counter > 100)
            this.state = State.PLAYING;
        
    }
    
    public void drawGameOverPage(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            createBufferStrategy(2);
            return;
        }
        
        Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();
        if(this.lost){
            g2d.drawImage(losingScreen, 0, 0, this);
        } else {
            g2d.drawImage(winningScreen, 0, 0, this);
        }
        
        g2d.dispose();
        bs.show();
    }
    
    public void drawGame(){
       
        if(levelTwo){
            for(int i = 2; i < gameObjects.size(); i++){
                gameObjects.remove(i);
            }
            this.pop.popReset();
            this.katch.unToggleFirePressed();
            addLevelTwo();
        }
        else if(levelThree){
            addLevelThree();
        }
        else if(!levelOne && !levelTwo && !levelThree){
            state = State.GAMEOVER;
        }
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            createBufferStrategy(2);
            return;
        }
        
        Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();
        
        g2d.drawImage(background, 0, 0, this);
        
        for(int i = 0, x = 0; i < pop.getLives(); i++, x += 30){
            g2d.drawImage(life, x, 400, this);
        }
        
        this.scoreString = "Score: " + Integer.toString(pop.getScore());
        g2d.drawString(scoreString, 100, 420);
        
        for(int i = 0; i < gameObjects.size(); i++){
            if(this.gameObjects.get(i).getShow())
                this.gameObjects.get(i).draw(g2d,this);
        }
        
        g2d.dispose();
        bs.show();
    }

    private String getTitle() {
        return this.gameTitle;
    }
    
    public void addLevelTwo(){
        
        l2bigLeg1 = new BigLeg(bigLegWidth*4, 0, 2, smallBL);
        l2bigLeg2 = new BigLeg(bigLegWidth*10, 0, 2, smallBL); 
        
        this.gameObjects.add(l2bigLeg1);
        this.gameObjects.add(l2bigLeg2);
        
        this.gameObjects.add(new Block(3*bigLegWidth,0,pink, false));
        this.gameObjects.add(new Block(3*bigLegWidth,(blockHeight),lightBlue, false));
        this.gameObjects.add(new Block(3*bigLegWidth,(2*blockHeight),green, false));
        this.gameObjects.add(new Block(3*bigLegWidth,(3*blockHeight),blockLife, true));
        this.gameObjects.add(new Block(3*bigLegWidth,(4*blockHeight),white, false));
        this.gameObjects.add(new Block(3*bigLegWidth,(5*blockHeight),yellow, false));
        this.gameObjects.add(new Block(3*bigLegWidth,(6*blockHeight),red, false));
        
        this.gameObjects.add(new Block(5*bigLegWidth,0,pink, false));
        this.gameObjects.add(new Block(5*bigLegWidth,(blockHeight),lightBlue, false));
        this.gameObjects.add(new Block(5*bigLegWidth,(2*blockHeight),green, false));
        this.gameObjects.add(new Block(5*bigLegWidth,(3*blockHeight),darkBlue, false));
        this.gameObjects.add(new Block(5*bigLegWidth,(4*blockHeight),white, false));
        this.gameObjects.add(new Block(5*bigLegWidth,(5*blockHeight),yellow, false));
        this.gameObjects.add(new Block(5*bigLegWidth,(6*blockHeight),red, false));
        
        this.gameObjects.add(new Block(7*bigLegWidth,0,pink, false));
        this.gameObjects.add(new Block(7*bigLegWidth,(blockHeight),lightBlue, false));
        this.gameObjects.add(new Block(7*bigLegWidth,(2*blockHeight),green, false));
        this.gameObjects.add(new Block(7*bigLegWidth,(3*blockHeight),darkBlue, false));
        this.gameObjects.add(new Block(7*bigLegWidth,(4*blockHeight),white, false));
        this.gameObjects.add(new Block(7*bigLegWidth,(5*blockHeight),yellow, false));
        this.gameObjects.add(new Block(7*bigLegWidth,(6*blockHeight),red, false));
        
        this.gameObjects.add(new Block(9*bigLegWidth,0,pink, false));
        this.gameObjects.add(new Block(9*bigLegWidth,(blockHeight),lightBlue, false));
        this.gameObjects.add(new Block(9*bigLegWidth,(2*blockHeight),green, false));
        this.gameObjects.add(new Block(9*bigLegWidth,(3*blockHeight),darkBlue, false));
        this.gameObjects.add(new Block(9*bigLegWidth,(4*blockHeight),white, false));
        this.gameObjects.add(new Block(9*bigLegWidth,(5*blockHeight),yellow, false));
        this.gameObjects.add(new Block(9*bigLegWidth,(6*blockHeight),red, false));
        
        this.gameObjects.add(new Block(11*bigLegWidth,0,pink, false));
        this.gameObjects.add(new Block(11*bigLegWidth,(blockHeight),lightBlue, false));
        this.gameObjects.add(new Block(11*bigLegWidth,(2*blockHeight),green, false));
        this.gameObjects.add(new Block(11*bigLegWidth,(3*blockHeight),blockLife, true));
        this.gameObjects.add(new Block(11*bigLegWidth,(4*blockHeight),white, false));
        this.gameObjects.add(new Block(11*bigLegWidth,(5*blockHeight),yellow, false));
        this.gameObjects.add(new Block(11*bigLegWidth,(6*blockHeight),red, false));
         
    }
    
    public void addLevelOne(){
        l1bigLeg1 = new BigLeg(250, 0, 2, bigBL);
        this.gameObjects.add(l1bigLeg1);
        
        this.gameObjects.add(new Block(0*blockWidth,(4*blockHeight),pink, false));
        this.gameObjects.add(new Block(blockWidth,(4*blockHeight),lightBlue, false));
        this.gameObjects.add(new Block(2*blockWidth,(4*blockHeight),green, false));
        this.gameObjects.add(new Block(3*blockWidth,(4*blockHeight),blockLife, true));
        this.gameObjects.add(new Block(4*blockWidth,(4*blockHeight),white, false));
        this.gameObjects.add(new Block(5*blockWidth,(4*blockHeight),yellow, false));
        this.gameObjects.add(new Block(6*blockWidth,(4*blockHeight),red, false));
        this.gameObjects.add(new Block(7*blockWidth,(4*blockHeight),pink, false));
        this.gameObjects.add(new Block(8*blockWidth,(4*blockHeight),lightBlue, false));
        this.gameObjects.add(new Block(9*blockWidth,(4*blockHeight),green, false));
        this.gameObjects.add(new Block(10*blockWidth,(4*blockHeight),blockLife, true));
        this.gameObjects.add(new Block(11*blockWidth,(4*blockHeight),white, false));
        this.gameObjects.add(new Block(12*blockWidth,(4*blockHeight),yellow, false));
        this.gameObjects.add(new Block(13*blockWidth,(4*blockHeight),red, false));
        this.gameObjects.add(new Block(14*blockWidth,(4*blockHeight),pink, false));
        
        this.gameObjects.add(new Block(0*blockWidth,(7*blockHeight),pink, false));
        this.gameObjects.add(new Block(blockWidth,(7*blockHeight),lightBlue, false));
        this.gameObjects.add(new Block(2*blockWidth,(7*blockHeight),green, false));
        this.gameObjects.add(new Block(3*blockWidth,(7*blockHeight),blockLife, true));
        this.gameObjects.add(new Block(4*blockWidth,(7*blockHeight),white, false));
        this.gameObjects.add(new Block(5*blockWidth,(7*blockHeight),yellow, false));
        this.gameObjects.add(new Block(6*blockWidth,(7*blockHeight),red, false));
        this.gameObjects.add(new Block(7*blockWidth,(7*blockHeight),pink, false));
        this.gameObjects.add(new Block(8*blockWidth,(7*blockHeight),lightBlue, false));
        this.gameObjects.add(new Block(9*blockWidth,(7*blockHeight),green, false));
        this.gameObjects.add(new Block(10*blockWidth,(7*blockHeight),blockLife, true));
        this.gameObjects.add(new Block(11*blockWidth,(7*blockHeight),white, false));
        this.gameObjects.add(new Block(12*blockWidth,(7*blockHeight),yellow, false));
        this.gameObjects.add(new Block(13*blockWidth,(7*blockHeight),red, false));
        this.gameObjects.add(new Block(14*blockWidth,(7*blockHeight),pink, false));
    }
    
    public void addLevelThree(){
        l3bigLeg1 = new BigLeg(bigLegWidth*4, 0, 2, bigBL);
        l3bigLeg2 = new BigLeg(bigLegWidth*10, 0, 2, bigBL); 
        
        this.gameObjects.add(l3bigLeg1);
        this.gameObjects.add(l3bigLeg2);
    }
    
}
