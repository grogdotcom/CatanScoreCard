package gui.game;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import gui.PanelManager;
import score.CatanMove;
import score.Game;

public class PlayGame extends JPanel {

	public static final int BUTTON_WIDTH = 100;
	public static final int BUTTON_HEIGHT = 30;
	
	// Move Buttons 
	private JButton jb_Roll;
	private JButton jb_Build;
	private JButton jb_Trade;
	private JButton jb_Play_Development;
	private JButton jb_Port_Trade;
	private JButton jb_End_Turn;
	
	// Build Buttons
	private JButton jb_b_Settlement;
	private JButton jb_b_City;
	private JButton jb_b_Development;
	private JButton jb_b_Road;
	
	// Development Card Buttons
	private JButton jb_d_Knight;
	private JButton jb_d_Road_Builder;
	private JButton jb_d_Year_Of_Plenty;
	private JButton jb_d_Monopoly;
	private JButton jb_d_Point;
	
	
	
	public PlayGame(PanelManager manager)
	{
		
	}
	
	
	@Override
	public void repaint()
	{
		super.repaint();
		
		
	}
	
	private void generateButtons()
	{
		jb_Roll = new JButton("Roll");
	}
	
	private class ButtonAlignment {
		
		public final static int GAP_DISTANCE = 20; 
		
		private final int width, height;
		private int maxButtonsInRow;
		
		
		List<JButton> buttons = new ArrayList();
		
		public ButtonAlignment(int width, int height)
		{
			this.width = width;
			this.height = height;
			
			this.maxButtonsInRow = width / (PlayGame.BUTTON_WIDTH + GAP_DISTANCE);
		}
		
		public void addButton(JButton button)
		{
			buttons.add(button);
		}
		
		public void showButtons()
		{
			int i = 0;
			int numRows = buttons.size() / maxButtonsInRow;
			for (int row = 0; row <= numRows; row++)
			{
				
			}
		}
		
		public void clear()
		{
			buttons.clear();
		}
	}
	
}
