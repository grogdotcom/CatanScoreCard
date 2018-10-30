package gui.game;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import hex.hexmech;
import score.Board;
import score.Hex;

public class BoardDisplay {

	public static final Color COLORBACK = Color.CYAN;
	public static final Color EMPTY = new Color(250, 250, 210);
	public static final Color DESERT = Color.ORANGE;
	public static final Color WHEAT = Color.YELLOW;
	public static final Color WOOD = new Color(34 ,139, 34);
	public static final Color SHEEP = Color.GREEN;
	public static final Color BRICK = Color.RED;
	public static final Color ORE = Color.LIGHT_GRAY;
	public static final Color[] COLORARRAY = {DESERT, WHEAT, WOOD, SHEEP, BRICK, ORE};
	
	final static int BSIZE = 5; // board size.
	final static int HEXSIZE = 90; // hex size in pixels
	final static int BORDERS = 15;
	final static int NUM_HEX = 19;
	final static int SCRSIZE = HEXSIZE * (BSIZE + 1) + BORDERS * 3; // screen
																	// size
																	// (vertical
																	// dimension).
	
	public static final int VERTEX_RADIUS = 10;

	final int[] i = { 2, 3, 4, 4, 4, 3, 2, 1, 0, 0, 0, 1, 2, 3, 3, 2, 1, 1, 2 };
	final int[] j = { 0, 0, 1, 2, 3, 3, 4, 3, 3, 2, 1, 0, 1, 1, 2, 3, 2, 1, 2 };

	private final Board gameBoard;
	private boolean numSwitch = true;	//True = number
										//False = roll
	
	private boolean settlementSwitch = false;
	
	private DrawingPanel panel = new DrawingPanel();

	public BoardDisplay(Board board) {
		this.gameBoard = board;
		initBoard();
	}

	public void initBoard() {
		hexmech.setXYasVertex(false); // RECOMMENDED: leave this as FALSE.

		hexmech.setHeight(HEXSIZE); // Either setHeight or setSize must be run
									// to initialize the hex
		hexmech.setBorders(BORDERS);
		
		
	}
	
	public void createAndShowGUI() {

		// JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Board");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container content = frame.getContentPane();
		content.add(panel);
		// this.add(panel); -- cannot be done in a static context
		// for hexes in the FLAT orientation, the height of a 10x10 grid is
		// 1.1764 * the width. (from h / (s+t))
		frame.setSize((int) (SCRSIZE / 1.23), SCRSIZE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void update()
	{
		panel.repaint();
	}

	class DrawingPanel extends JPanel {
		// mouse variables here
		// Point mPt = new Point(0,0);

		public DrawingPanel() {
			setBackground(COLORBACK);

			setFocusable(true);
			MyMouseListener ml = new MyMouseListener();
			addMouseListener(ml);
			MyKeyBoardListener kl = new MyKeyBoardListener();
			addKeyListener(kl);
		}

		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			super.paintComponent(g2);
			// draw grid
			for (int k = 0; k < NUM_HEX; k++) 
			{	
				hexmech.drawHex(i[k], j[k], g2);	
			}
			
			// fill in hexes
			for (int k = 0; k < NUM_HEX; k++) {
				
				int res, num;
				Hex curHex = gameBoard.Hex(k);
				if (curHex == null)
				{
					res = -1; 
					num = -1;
				}
				else 
				{
					res = curHex.Resource + 1;
					num = curHex.roll;
				}
				
				if (numSwitch)
				{
					num = k + 1;
				}
				hexmech.fillHex(i[k], j[k], res, num, g2);
				
			}
			
			if (settlementSwitch)
			{	
				g2.setColor(Color.BLACK);
				for (int k = 0; k < NUM_HEX; k++)
				{	
					
					Polygon hexPoly = hexmech.hex(i[k], j[k]);
					for (int c = 0; c < 6; c++)
					{
					
						int x = hexPoly.xpoints[c];
						int y = hexPoly.ypoints[c];
						
						g2.drawOval(x - VERTEX_RADIUS, y - VERTEX_RADIUS, 2 * VERTEX_RADIUS, 2 * VERTEX_RADIUS);
					}
				}
			}
			
			

			// g.setColor(Color.RED);
			// g.drawLine(mPt.x,mPt.y, mPt.x,mPt.y);
		}

		class MyMouseListener extends MouseAdapter { // inner class inside
			// DrawingPanel

			int n = 1;

			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				// mPt.x = x;
				// mPt.y = y;
				Point p = new Point(hexmech.pxtoHex(e.getX(), e.getY()));
				if (p.x < 0 || p.y < 0 || p.x >= BSIZE || p.y >= BSIZE)
					return;

				// DEBUG: colour in the hex which is supposedly the one clicked
				// on
				// clear the whole screen first.
				/*
				 * for (int i=0;i<BSIZE;i++) { for (int j=0;j<BSIZE;j++) {
				 * board[i][j]=EMPTY; } }
				 */
				//System.out.println(p);
				// What do you want to do when a hexagon is clicked?
				
				System.out.println();
				
				for (int k = 0; k < NUM_HEX; k++)
				{
					if (i[k] == p.x && j[k] == p.y)
					{
						//System.out.println(k);
					}
				}
				
				Polygon hex = hexmech.hex(p.x, p.y);
				for (int c = 0; c < 6; c++)
				{
					int xdiff = x - hex.xpoints[c];
					//System.out.println(xdiff);
					int ydiff = y - hex.ypoints[c];
					//System.out.println(ydiff);
					
					int dist = (int)Math.sqrt(xdiff * xdiff + ydiff * ydiff);
					//System.out.println(dist);
					if (dist < VERTEX_RADIUS)
					{
						//System.out.println("This is a hex");
					}
				}
				
				repaint();
			}
		} // end of MyMouseListener class
		
		class MyKeyBoardListener extends KeyAdapter 
		{
			public void keyTyped(KeyEvent e)
			{
				char key = e.getKeyChar();
				if (key == KeyEvent.VK_SPACE)
				{
					numSwitch = !numSwitch;
				}
				
				if (key == 's')
				{
					settlementSwitch = !settlementSwitch;
				}
				
				repaint();
			}
			
		}
	} // end of DrawingPanel class

}
