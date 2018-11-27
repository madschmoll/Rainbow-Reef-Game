/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author madelineschmoll
 */
public class Map {
    private int x, y;
    private int tileSize;
    private int[][] map;
    private int mapTileWidth, mapTileHeight;
    
    Map(String s){
        
        try{
            
            BufferedReader br = new BufferedReader(new FileReader(s));
            mapTileWidth = Integer.parseInt(br.readLine());
            mapTileHeight = Integer.parseInt(br.readLine());
            
            map = new int[mapTileHeight][mapTileWidth];
            
            for(int row = 0; row < mapTileHeight; row++){
                String line = br.readLine();
                String[] tokens = line.split("");
                for(int col = 0; col < mapTileWidth; col++){
                    map[row][col] = Integer.parseInt(tokens[col]);
                }
            }
            
        } catch(IOException | NumberFormatException e){
            System.out.println("MAP NOT FUNCTIONING");
        }
        
    }
    
    public int[][] getMap(){
        return this.map;
    }
    
    public int getHeight(){
        return this.mapTileHeight;
    }
    
    public int getWidth(){
        return this.mapTileWidth;
    }
    
}
