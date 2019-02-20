package sheet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ScoreSheet {
	
	public static final int WIDTH = 2550;
	public static final int HEIGHT = 3300;
	
	public static void main(String args[]) {
		ScoreSheet sheet = new ScoreSheet();
		boolean[] headers = {true, false, false, false};
		String[] file = {"front1", "back1", "front2", "back2"};
		for (int i = 0; i < 4; i++) {
			BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
			sheet.draw(image, headers[i]);
			File outfile = new File(file[i]);
			try {
				ImageIO.write(image, "png", outfile);
			} catch(IOException e) {
			}
		}
	}
	
	private int rows = 0;
	
	public void draw(BufferedImage image, boolean withHeader) {
		Graphics2D g = (Graphics2D)image.getGraphics();
		Font cur = g.getFont();
		g.setFont(new Font(cur.getFontName(), cur.getStyle(), cur.getSize() + 20));
		
		int y = 0;
		drawBackground(g);
		if (withHeader) {
			drawHeader(y, g);
			y = 300;
		}
		
		drawTitles(y + 70, g);
		drawHorizontalLines(y, g);
		drawVerticalLines(y, g);	
	
	}
	
	private void drawBackground(Graphics2D g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, WIDTH, HEIGHT);
	}
	
	private void drawHeader(int y, Graphics2D g) {
		g.setColor(Color.black);
		for (int i = 1; i <= 4; i++) {
			g.drawLine(0, y, 550, y);
			y += 75;
			g.drawString("" + i, 15, y - 10);
		}
		y += 75;
		g.drawLine(0, y, 450, y);
		
		g.drawLine(0, 0, 0, y);
		g.drawLine(50, 0, 50, y);
		g.drawLine(550, 0, 550, y);
	}
	
	private void drawTitles(int y, Graphics2D g) {
		g.setColor(Color.black);
		
		g.drawString("rolls", 55, y);
		g.drawString("resources", 155, y);
		g.drawString("7", 580, y);
		g.drawString("trading", 655, y);
		g.drawString("port", 1155, y);
		g.drawString("d-card", 1355, y);
		
		g.drawString("roads", 1555, y);
		g.drawString("settlement/ city", 1655, y);
		g.drawString("D", 2480, y);
		
	}
	
	private void drawHorizontalLines(int y, Graphics2D g) {
		g.drawLine(0, y, WIDTH, y);
		y += 75;
		
		while (y + 150 < HEIGHT) {
			g.setColor(Color.black);
			g.drawLine(0, y, WIDTH, y);
			
			y += 75;
			g.drawString("" + ((rows % 4) + 1), 15, y);
			rows++;
			
			g.setColor(Color.lightGray);
			g.drawLine(150, y, 550, y);
			g.drawLine(650, y, 1350, y);
			g.drawLine(1650, y, 2450, y);
			y += 75;
			
		}
		g.setColor(Color.black);
		g.drawLine(0, y, WIDTH, y);
		
		System.out.println(rows);
	}
	
	private void drawVerticalLines(int y, Graphics2D g) {
		g.setColor(Color.black);
		
		// first line
		g.drawLine(0, y, 0, HEIGHT);
		// number 
		g.drawLine(50, y, 50, HEIGHT);
		// roll 
		g.drawLine(150, y, 150, HEIGHT);
		// resource 
		g.drawLine(550, y, 550, HEIGHT);
		// 7 
		g.drawLine(650, y, 650, HEIGHT);
		// trading 
		g.drawLine(1150, y, 1150, HEIGHT);
		// port 
		g.drawLine(1350, y, 1350, HEIGHT);
		// d card 
		g.drawLine(1550, y, 1550, HEIGHT);
		// roads
		g.drawLine(1650, y, 1650, HEIGHT);
		// settlements/ cities
		g.drawLine(2050, y + 75, 2050, HEIGHT);
		g.drawLine(2450, y, 2450, HEIGHT);
		// d card
		g.drawLine(2550, y, 2550, HEIGHT);
		g.drawLine(2549, y, 2549, HEIGHT);
		
		y += 75;
		
		g.setColor(Color.lightGray);
		int x = 250;
		for (int i = 0; i < 3; i++) {
			g.drawLine(x, y, x, HEIGHT);
			g.drawLine(x + 1500, y, x + 1500, HEIGHT);
			g.drawLine(x + 1900, y, x + 1900, HEIGHT);
			x += 100;
		}
		
		g.drawLine(850, y, 850, HEIGHT);
		g.drawLine(1050, y, 1050, HEIGHT);
		g.drawLine(1250, y, 1250, HEIGHT);
		g.drawLine(1450, y, 1450, HEIGHT);
		
		
		
		
	}
	
	
	
}
