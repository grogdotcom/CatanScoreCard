package main;

import java.io.FileNotFoundException;
import java.util.List;

import score.Game;

public class Test {
	
	public static void main(String args[]) throws FileNotFoundException
	{
		List<String> gameFromFile = Resources.loadFromFile("CatanTestFile");

		String s = "";
		for (String line : gameFromFile)
		{
			//System.out.println(line);
			s += line + "\n";
		}
		
		Game catan = Game.parse(s);
		CatanLog gameLog = catan.getDetailedGameLog();
		
		for (String i: gameLog.Log)
		{
			System.out.println(i);
		}
		
		Data.SaveGame("/Users/gregoryherman/Desktop/CatanScoreSheet/Xbox/", catan);
	}

}
