package gui.game;

import javax.swing.JButton;
import javax.swing.JPanel;

public class GameButtonController extends JPanel
{
	JButton next, cancel,  rollDiceButton, playKnightButton, buildButton, tradeButton, playDCardButton; 
	JPanel consolePanel, dicePanel, dice7Panel, knightPanel, buildPanel, tradePanel, playDCardPanel; 
	
	public void generateButtons()
	{
		consolePanel = new JPanel(); 
		
		next = new JButton("Next");
	}
}
