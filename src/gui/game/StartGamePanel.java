package gui.game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

import grid.Location;
import gui.HexBoard;
import gui.PanelManager;
import hexagon.HexagonLocation;
import main.Main;
import main.Resources;

public class StartGamePanel extends JPanel {
	
	private final int hexSize = 11;
	
	int lwidth = 150;
	int lheight = 100;
	
	
	int bwidth = 125;
	int bheight = 30;
	
	int bx = 75;
	int yListbuffer = 75; 
	
	int lx; 
	int ly; 

	
	private JList<String> playerList; 
	private String[] pList = new String[4];
	private int players = 0; 
	
	private JButton exsistingPlayerButton;
	private JButton newPlayerButton;
	private JButton startGameButton; 
	private JButton RandomBoardButton;
	private JButton CustomBoardButton; 
	
	private HexBoard hexBoard;
	
	private PanelManager manager; 
	
	public StartGamePanel(PanelManager mngr) {
		super();
		this.manager = mngr; 
		
		this.setPreferredSize(new Dimension(PanelManager.WIDTH, PanelManager.HEIGHT));
		this.generateButtons();
		
		
	}
	
	public void paintComponent(Graphics g) {
		int x = (PanelManager.WIDTH / 2);
		
		Image resizedImage = Resources.resizeImage(manager.getLogo(), .65f);
		g.drawImage(resizedImage, x, 20, null);
		
		if (hexBoard != null) {
			Image img = hexBoard.getImage();
			
			int lx = PanelManager.WIDTH - playerList.getLocation().x - playerList.getWidth();
			int ly = playerList.getLocation().y; 
			
			g.drawImage(img, lx, ly, img.getWidth(null), img.getHeight(null), null);
		}
	}
	
	private void generateButtons() {
		this.setLayout(null);
		
		ly = PanelManager.HEIGHT - lheight - 75;
		lx = (int)(1.5 * bx) + bwidth;
		
		playerList = new JList(pList);
		playerList.setSize(lwidth, lheight);
		this.add(playerList);
		playerList.setLocation(lx, ly);
		
		hexBoard = new HexBoard(manager.getHexList(), lwidth, lheight);
		hexBoard.setHexSize(hexSize);
		hexBoard.setPosNumbers(true);
		hexBoard.setPosype(HexBoard.CENTER);
		hexBoard.setRollNumbers(false);
		hexBoard.draw();
		
		exsistingPlayerButton = new JButton("Select Player"); 
		exsistingPlayerButton.setSize(bwidth, bheight);
		this.add(exsistingPlayerButton);
		exsistingPlayerButton.setLocation(bx, ly);
		
		newPlayerButton = new JButton("New Player");
		newPlayerButton.setSize(bwidth, bheight);
		this.add(newPlayerButton);
		newPlayerButton.setLocation(bx, ly + 35);
		
		RandomBoardButton = new JButton("Random Board"); 
		RandomBoardButton.setSize(bwidth, bheight);
		this.add(RandomBoardButton);
		RandomBoardButton.setLocation(PanelManager.WIDTH - bwidth - bx, ly);
		
		CustomBoardButton = new JButton("Custom Board"); 
		CustomBoardButton.setSize(bwidth, bheight);
		this.add(CustomBoardButton);
		CustomBoardButton.setLocation(PanelManager.WIDTH - bwidth - bx, ly + 35);
		
		
		
		
		startGameButton = new JButton("Start Game"); 
		startGameButton.setSize(bwidth, bheight);
		startGameButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			
			}
			
		});
		this.add(startGameButton);
		startGameButton.setLocation(PanelManager.WIDTH / 2 - bwidth / 2, PanelManager.HEIGHT - bheight - 35);	
	}
	

	
}
