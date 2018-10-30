package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

import grid.Location;
import hexagon.HexagonGridDisplay;
import score.CatanScore;

public class HexBoard {
	
	public final static int LEFT = 0; 
	public final static int RIGHT = 1; 
	public final static int CENTER = 2; 
	
	
	// CanatScore.resource + 2
	// 0 - beach
	// 1 - desert
	// 2 - wheat
	// 3 - wood
	// 4 - sheep
	// 5 - brick
	// 6 - ore
	public final static Color[] c_RESOURCE_COLORS= {new Color(250, 250, 210), Color.ORANGE, Color.YELLOW, 
			new Color(34 ,139, 34), Color.GREEN, Color.RED, Color.LIGHT_GRAY};
	
	private int numHexes; 
	private int[] hexRes; 
	private int[] hexNum; 
	
	private Image hexImage;
	
	private Graphics g; 
	private HexagonGridDisplay gridDisplay;
	
	private boolean rollNumbers = true;
	int rollType = LEFT; 
	private boolean posNumbers = true;
	int posType = RIGHT; 
	
	public HexBoard(List<Location> hexList, int width, int height) 
	{	
		gridDisplay = new HexagonGridDisplay(hexList); 
		gridDisplay.init();
		
		hexImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g = hexImage.getGraphics();
		
		numHexes = hexList.size();
		hexRes = new int[numHexes];
		hexNum = new int[numHexes];
		for (int i = 0; i < numHexes; i++)
		{
			hexRes[i] = -2; 
			hexNum[i] = -2; 
		}
	}
	
	public HexBoard(List<Location> hexList, int width, int height, int[] hexRes, int[] hexNum) 
	{	
		this(hexList, width, height);
		
		this.hexRes = hexRes;
		this.hexNum = hexNum;
	}
	
	public void draw()
	{
		g.setColor(Color.cyan);
		g.fillRect(0, 0, hexImage.getWidth(null), hexImage.getHeight(null));
		
		for (int i = 0; i < CatanScore.NUM_HEX; i++)
		{
			fillHex(g, i);
			if (rollNumbers && hexRes[i] >= 0)
			{
				drawNumber(g, i, hexNum[i], rollType);
			}
			if (posNumbers)
			{
				drawNumber(g, i, i + 1, posType);
			}
		
		}
	}
	
	private void fillHex(Graphics g, int hex)
	{
		Location loc = gridDisplay.getLocation(hex);
		Point center = gridDisplay.pointForHex(loc, true);
		Point[] shape = gridDisplay.hexCorners(center, gridDisplay.size, true);
		int x[] = new int[shape.length]; 
		int y[] = new int[shape.length];
		for (int i = 0; i < shape.length; i++)
		{
			x[i] = shape[i].x;
			y[i] = shape[i].y;
		}
		g.setColor(c_RESOURCE_COLORS[hexRes[hex] + 2]);
		g.fillPolygon(x, y, shape.length);
		g.setColor(Color.BLACK);
		g.drawPolygon(x, y, shape.length);
	}
	
	private void drawNumber(Graphics g, int hex, int num, int type)
	{
		Point center = gridDisplay.pointForHex(gridDisplay.getLocation(hex), true);
		int x, y; 
		switch(type) {
		case LEFT: 
			x = center.x - gridDisplay.size;
			y = center.y; 
			break; 
		case RIGHT: 
			x = center.x + 20; 
			y = center.y; 
			break;
		case CENTER:
		default: 
			x = center.x;
			y = center.y; 
		}
		g.setColor(Color.BLACK);
		g.drawString("" + num, x, y);
	}
	
	public void setHexNumRes(int hex, int num, int resource) {
		
		hexRes[hex] = resource;
		hexNum[hex] = resource < 0 ? -1 : num;
	}
	
	public boolean isRollNumbers() {
		return rollNumbers;
	}

	public void setRollNumbers(boolean rollNumbers) {
		this.rollNumbers = rollNumbers;
	}

	public boolean isPosNumbers() {
		return posNumbers;
	}

	public void setPosNumbers(boolean posNumbers) {
		this.posNumbers = posNumbers;
	}
	
	public void setRollType(int type) {
		this.rollType = type; 
	}
	
	public void setPosype(int type) {
		this.posType = type; 
	}
	
	public Image getImage()
	{
		return hexImage; 
	}
	
	public int getHexSize() 
	{
		return gridDisplay.size; 
	}
	
	public void setHexSize(int hexSize)
	{
		gridDisplay.size = hexSize; 
	}
	

}
