package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import score.Game;

public class Data {

	public static void SaveGame(String filePath, Game game) {
		List<String> playerLoader, gameLoader;
		int n = 0; 
		try {
			
			
			gameLoader = Resources.loadFromFile(filePath + Main.GAMES + ".txt");
			for (String games : gameLoader)
			{
				if (games.substring(0, game.gameDate.length()).equals(game.gameDate))
				{
					n += 1;
				}
			}
			
			String gameFileName = game.gameDate + (n + 1);
			
			
			
			playerLoader = Resources.loadFromFile(filePath + Main.PLAYERS + ".txt");

			for (int i = 0; i < game.Players.length; i++) 
			{
				if (!playerLoader.contains(game.Players[i].name)) 
				{
					Resources.writeToFile(filePath + Main.PLAYERS + ".txt", game.Players[i].name + "\n", true);
					File playerFile = new File(filePath + Main.PLAYERS + "/" + game.Players[i].name + ".txt");
					playerFile.createNewFile();
				}
				Resources.writeToFile(filePath + Main.PLAYERS + "/" + game.Players[i].name + ".txt", gameFileName, true);
			}
			
			Resources.writeToFile(filePath + Main.GAMES + ".txt", gameFileName + "\n", true);
			File gameFile = new File(filePath + Main.GAMES + "/" + gameFileName + ".txt");
			gameFile.createNewFile();
			Resources.writeToFile(filePath + Main.GAMES + "/" + gameFileName + ".txt", game.toString(), true);
					
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
