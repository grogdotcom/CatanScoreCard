package gui.game;

import java.awt.Graphics;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import grid.Location;
import gui.HexBoard;
import gui.PanelManager;
import score.CatanScore;
import score.Player;

public class GamePanel extends JPanel {

	JButton next; 
	
	
	
	private CatanScore scoreSheet; 
	private HexBoard board; 
	
	private boolean setUp = false;
	
	private boolean RollDice = false; 
	private boolean PlayedDDcard = false; 
	
	public GamePanel(PanelManager mngr) {
		
	}
	
	public void startGame(List<Location> hexList, int[] hexRes, int[] hexNum, Player[] playerList)
	{
		scoreSheet = new CatanScore(hexRes, hexNum, playerList);
		board = new HexBoard(hexList, 100, 100, hexRes, hexNum);
		setUp = true; 
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
	}
	
	private void generateButtons() 
	{
		
	}
	
	
}
