package main;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import gui.PanelManager;

public class Main {
	
	public static final int TEXT = 0;
	public static final int GUI = 1;
	
	public static final String PLAYERS = "players";
	public static final String GAMES = "games";
	public static final String INFO = "info";
	
	public static void main(String args[])
	{
		
		int scoreType = new Integer(args[0]); 
		String filePath = args.length > 1 ? args[1] : "CatanScore/";
		
		System.out.println(scoreType + " " + filePath);
		
		if (new File(filePath).mkdirs()) 
		{
			try {
				new File(filePath + PLAYERS + ".txt").createNewFile();
				new File(filePath + GAMES + ".txt").createNewFile();
				new File(filePath + INFO + ".txt").createNewFile();
				new File(filePath + PLAYERS).mkdirs();
				new File(filePath + GAMES).mkdirs();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		switch (scoreType) {
		case TEXT:
			new TextMain(filePath);
			break;
		case GUI:
			break; 
		}

	}

}
