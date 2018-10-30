package gui.game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import gui.PanelManager;
import main.Resources;

public class OpeningPanel extends JPanel {
	
	private JButton recordButton; 
	private JButton dataButton;
	
	private PanelManager manager; 

	public OpeningPanel(PanelManager mngr) {
		super(); 
		this.manager = mngr;
		
		this.setPreferredSize(new Dimension(PanelManager.WIDTH, PanelManager.HEIGHT));
		this.generateButtons();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		int x = (PanelManager.WIDTH / 2) - (manager.getLogo().getWidth() / 2);
		g.drawImage(manager.getLogo(), x, 20, null);
	}
	
	private void generateButtons() {
		int width = 120;
		int height = 50; 
		
		int x = 200; 
		int y = PanelManager.HEIGHT - height - 100; 
	
		this.setLayout(null);
		
		generateRecordButton(x, y, width, height);
		generateDataBaseButton(x, y, width, height);
	}
	
	private void generateRecordButton(int x, int y, int width, int height)
	{
		recordButton = new JButton("Record Game");
		recordButton.setSize(width, height);
		recordButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				manager.setCurrentPanel(PanelManager.p_START_GAME);
			}
			
		});
		this.add(recordButton);
		recordButton.setLocation(PanelManager.WIDTH - width - x, y);
	}
	
	private void generateDataBaseButton(int x, int y, int width, int height) 
	{
		dataButton = new JButton("Database"); 
		dataButton.setSize(width, height);
		this.add(dataButton);
		dataButton.setLocation(x, y);
	}
	
	

	
	
}
