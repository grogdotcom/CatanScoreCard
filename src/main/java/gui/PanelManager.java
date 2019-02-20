package gui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import grid.Location;
import gui.game.*;
import hexagon.HexagonLocation;
import main.Main;
import main.Resources;
import main.ScoreSheetManager;

public class PanelManager extends ScoreSheetManager{
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 450;
	
	public static final int p_OPENING = 0; 
	public static final int p_START_GAME = 1; 
	
	public static final int p_PLAY_GAME = 2;
	
	private JFrame window; 
	private String filePath;
	
	private List<JPanel> panelList = new ArrayList();
	private int currentPanel; 
	
	private BufferedImage logo; 
	private List<Location> hexList; 
	
			
	public PanelManager(String filePath) 
	{
		super(filePath);
		
		loadImage(filePath);
		panelList.add(new OpeningPanel(this));
		panelList.add(new StartGamePanel(this));
		
		this.window = new JFrame();
		window.setDefaultCloseOperation(3);
		window.setVisible(true);

		setCurrentPanel(p_OPENING);
		
		
	}
	
	public void setCurrentPanel(int panel) 
	{
		this.currentPanel = panel; 
		window.setContentPane(panelList.get(currentPanel));
		window.pack();
	}
	
	public int getCurrentPanel() 
	{
		return currentPanel; 
	}
	
	public BufferedImage getLogo() 
	{
		return logo; 
	}

	public boolean loadImage(String path) 
	{
		this.logo = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);
		try {
			this.logo = Resources.loadImage("logo.gif");
		} catch (IOException e) {
			return false;
		}
		return true; 
	}
	
	
	


}


