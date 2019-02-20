package main;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import gui.game.BoardDisplay;
import score.Board;
import score.CatanMove;
import score.CatanScore;
import score.Hex;
import score.Player;

public class TextMain {
	
	String filePath; 
	Scanner in;
	Player playerList[];
	
	public TextMain(String filePath)
	{
		this.filePath = filePath;
		in = new Scanner(System.in);
		playerList = new Player[4];
		
		startUp();
	}
			
	public void startUp() {
		printLog("Welcome to the Settlers of Catan Score Sheet");
		printLog("Options:\t1. STATS\t2. New Game");
		int input = in.nextInt();
		if (input == 2) // New Game
		{
			NewGame();
		}
	}
	
	public void NewGame()
	{	
		CatanScore catan = setUpGame();
		playGame(catan);
		endGame(catan);
	}
	
	public Hex[] setUpBoard()
	{
		Hex hexList[] = new Hex[CatanScore.NUM_HEX];
		
		return hexList;
	}
	
	public CatanScore setUpGame()
	{
		printLog("Enter first Player's name"); 
		playerList[0] = new Player(0, in.next());
		printLog("Enter seconds Player's name"); 
		playerList[1] = new Player(1, in.next());
		printLog("Enter third Player's name"); 
		playerList[2] = new Player(2, in.next());
		printLog("Enter fourth Player's name"); 
		playerList[3] = new Player(3, in.next());
		
		printLog("Enter Board: ");
		Board gameBoard = Board.BasicBoardBuilder(Board.CUSTOM_BOARD);
		BoardDisplay display = new BoardDisplay(gameBoard);
		display.createAndShowGUI();
		
		for (int i = 1; i <= gameBoard.numHex; i++)
		{
			printLog("Enter Hex " + i + " Resource: \t1. Wheat 2. Wood 3. Sheep 4. Brick 5. Ore \t 0 if Desert");
			int res = in.nextInt();
			printLog("Enter Hex " + i + " Dice Roll: \t 0 if Desert");
			int roll = in.nextInt();
			//printLog("Adding Hex " + i + "= (" + res + ", " + roll + ")");
			gameBoard.addHex(res - 1, roll);
			display.update();
		}
		
		for (int i = 1; i <= gameBoard.numPort; i++)
		{
			printLog("Enter Port " + i + " Resource: \t1. 3-1\t2. Wheat\t3. Wood\t4. Sheep\t5. Brick\t6. Ore");
			int input = in.nextInt();
			gameBoard.addPort(input - 2);
		}
		
		CatanScore score = new CatanScore(gameBoard, playerList);
		
		/*playerList = new Player[] {new Player(0, "Greg"), new Player(1, "Ronnie"), new Player(2, "Talia"), new Player(3, "Aaron")};
		Board gameBoard = Board.BasicBoardBuilder(Board.BASIC_BOARD);
		BoardDisplay display = new BoardDisplay(gameBoard);
		display.createAndShowGUI();
		CatanScore score = new CatanScore(gameBoard, playerList);*/
		
		String[] setnum = {"first", "second", "third"};
		boolean rise = true;
		for (int i = 0; i < 8; i += (rise ? 1 : -1) ) {
			CatanMove move = new CatanMove();
			int player = i >= 4 ? 3 - (i % 4): i;
			printLog("Enter " + playerList[player].name + "'s " + setnum[i / 4] + " settlement: "); 
			int[] hexes = inputHexSpot();
			move.buildStartingSettlement(playerList[player].pos, hexes);
			score.doMove(move);
		}
		return score;
	}
	
	public void playGame(CatanScore catan)
	{
		while(!catan.GameOver)
		{
			Player curPlayer = catan.Players[catan.currentPlayer];
			
			
			printLog("\nIt's " + curPlayer.name + "'s turn!");
			CatanMove move = new CatanMove();
			boolean turn = true; 
			while (turn)
			{
				if (!move.diceRolled && move.canPlayDCard)
				{
					printLog("1. RollDice\t2. Play Knight");
					int input = in.nextInt();
					if (input == 1)
					{
						RollDice(catan, move);
					}
					else if (input == 2)
					{
						if (!move.PlayDevelopmentCard(CatanScore.KNIGHT, moveRobber(catan)))
						{
							printLog("Error: Play Development Card- Knight");
						}
					}
				}
				else if (!move.diceRolled)
				{
					printLog("1. RollDice");
					int input = in.nextInt();
					if (input == 1)
					{
						RollDice(catan, move);
					}
				}
				else if (move.diceRolled && move.canPlayDCard) {
					printLog("1. Build\t2. Trade\t3. Play Development Card\t4. Port Trade\t5. End Turn\t\t0. Undo");
					int input = in.nextInt();
					if (input == 1)
					{
						Build(move);
					}
					else if (input == 2)
					{
						Trade(catan, move);
					}
					else if (input == 3)
					{
						PlayDCard(catan, move);
					}
					else if (input == 4)
					{
						PortTrade(catan, move);
					}
					else if (input == 5)
					{
						turn = false; 
					}
					else if (input == 0)
					{
						move.undoLastEvent();
					}
				}
				else if (move.diceRolled && !move.canPlayDCard) {
					printLog("1. Build\t2. Trade\t3. Port Trade\t4. Reveal Victory Point\t5. End Turn\t\t0. Undo");
					int input = in.nextInt();
					if (input == 1)
					{
						Build(move);
					}
					else if (input == 2)
					{
						Trade(catan, move);
					}
					else if (input == 3)
					{
						PortTrade(catan, move);
					}
					else if (input == 4)
					{
						if (!move.PlayDevelopmentCard(CatanScore.POINT, null))
						{
							printLog("Error: Play Development Card- Point");
						}
					}
					else if (input == 5)
					{
						turn = false; 
					}
					else if (input == 0)
					{
						move.undoLastEvent();
					}
				}
			}
			catan.doMove(move);
		}
		
		for (int i = 0; i < catan.numPlayers - 1; i++)
		{
			CatanMove move = new CatanMove();
			Player player = catan.Players[catan.currentPlayer];
			printLog("How many unrevealed points does " + player.name + " have?");
			int points = in.nextInt();
			for (int k = 0; k < points; k++)
			{
				if (!move.PlayDevelopmentCard(CatanScore.POINT, null))
				{
					printLog("Error: Play Development Card- Point");
				}
			}
			catan.currentPlayer++;
			catan.currentPlayer = catan.currentPlayer % catan.numPlayers;
			
			catan.doMove(move);
		}	
	}
	
	public void endGame(CatanScore catan)
	{
		printLog("Save game?\t1. Yes\t2. No");
		int input = in.nextInt();
		if (input == 1)
		{
			//Data.SaveGame(filePath, catan);
		}
	}
		
	private void RollDice(CatanScore catan, CatanMove move)
	{		
		printLog("Input DiceRoll: ");
		int input = in.nextInt();
		if (input != 7)
		{
			if (!move.diceRoll(input))
			{
				printLog("Error: Dice Roll");
			}
		}
		else {
			int[] cardsLost = new int[4];
			for (int i = 0; i < 4; i++)
			{
				printLog("How many cards were stolen from " + catan.Players[i].name + "?");
				cardsLost[i] = in.nextInt();
			}
			int[] robber = moveRobber(catan);
			if(!move.sevenRoll(robber[0], robber[1], cardsLost))
			{
				printLog("Error: Seven Roll");
			}
			
		}
	}
	
	private void PlayDCard(CatanScore catan, CatanMove move)
	{
		printLog("Select Development Card Type: 1. Knight\t2. Road Builder\t3. Year of Plenty\t4. Monopoly\t5. Point");
		int input = in.nextInt();
		if (input == 1)
		{
			if (!move.PlayDevelopmentCard(CatanScore.KNIGHT, moveRobber(catan)))
			{
				printLog("Error: Play Development Card- Knight");
			}
		}
		else if (input == 2)
		{
			printLog("Enter longest Continous road length");
			int roadLength = in.nextInt();
			if (!move.PlayDevelopmentCard(CatanScore.ROAD_BUILDER, new int[] {roadLength}))
			{
				printLog("Error: Play Development Card- Road Builder");
			}
		}
		else if (input == 3)
		{
			if (!move.PlayDevelopmentCard(CatanScore.YEAR_OF_PLENTY, null))
			{
				printLog("Error: Play Development Card- Year of Plenty");
			}
		}
		else if (input == 4)
		{
			int[] cardsLost = new int[4];
			for (int i = 0; i < 4; i++)
			{
				printLog("How many cards did " + catan.Players[i].name + " lose?");
				cardsLost[i] = in.nextInt();
			}
			
			if (!move.PlayDevelopmentCard(CatanScore.MONOPOLY, cardsLost))
			{
				printLog("Error: Play Development Card- Monopoly");
			}
		}
		else if (input == 5)
		{
			if (!move.PlayDevelopmentCard(CatanScore.POINT, null))
			{
				printLog("Error: Play Development Card- Point");
			}
		}
	}
	
	private void Build(CatanMove move)
	{
		printLog("1. Settlement\t2. City\t3. Development Card\t4. Road");
		int input = in.nextInt();
		if (input == 1)
		{
			if (!move.build(CatanScore.SETTLEMENT, inputHexSpot()))
			{
				printLog("Error: Build- Settlement");
			}
		}
		else if (input == 2)
		{
			if (!move.build(CatanScore.CITY, inputHexSpot()))
			{
				printLog("Error: Build- City");
			}
		}
		else if (input == 3)
		{
			if (!move.build(CatanScore.DEVELOPMENT_CARD, null))
			{
				printLog("Error: Build- Development Card");
			}
		}
		else if (input == 4)
		{
			printLog("Enter longest Continous road length");
			int roadLength = in.nextInt();
			if (!move.build(CatanScore.ROAD, new int[] {roadLength}))
			{
				printLog("Error: Build- Road");
			}
		}
	}
	
	private void Trade(CatanScore catan, CatanMove move)
	{
		Player curPlayer = catan.Players[catan.currentPlayer];
		
		String s = "";
		for (int i = 0; i < 4; i++)
		{
			s += i + ". " + catan.Players[i].name + "\t"; 
		}
		
		printLog(curPlayer.name + " is trading with? \t" + s);
		int tradePartner = in.nextInt();
		printLog(curPlayer.name + " is receiving how many cards?");
		int cardsRecieved = in.nextInt();
		printLog(curPlayer.name + " traded away how many cards?");
		int cardsTraded = in.nextInt();
		if (!move.Trade(tradePartner, cardsRecieved, cardsTraded))
		{
			printLog("Error: Trade");
		}
	}
	
	private void PortTrade(CatanScore catan, CatanMove move)
	{
		Player curPlayer = catan.Players[catan.currentPlayer];
		String s = "";
		for (int i = 1; i <= CatanScore.NUM_RESOURCES; i++)
		{
			s += i + ". " + CatanScore.RESOURCES[i - 1] + "\t";
		}
		
		
		printLog(curPlayer.name + " is trading in \t" + s);
		int portRes = in.nextInt();
		int cardsTraded = 0;
		if (curPlayer.hasPort(portRes))
		{
			cardsTraded = 2;
		}
		else if (curPlayer.hasPort(0))
		{
			cardsTraded = 3; 
		}
		else
		{
			cardsTraded = 4;
		}
		
		if (!move.PortTrade(portRes - 1, cardsTraded))
		{
			printLog("Error: Port Trade");
		}
	}
	
	private int[] inputHexSpot()
	{
		String[] setnum = {"first", "second", "third"};
		int first, second, third, k; 
		first = second = third = 0; 
		for (k = 0; k < 3; k++)
		{
			printLog("Enter " + setnum[k] + " hex number: \t0 if less hexes");
			if (k == 0)
				first = in.nextInt();
			if (k == 1) {
				second = in.nextInt();
				if (second == 0)
					return new int[]{first - 1};
			}
			if (k == 2) {
				third = in.nextInt();
				if (third == 0)
					return new int[]{first - 1, second - 1};
			}
		}
		int[] hexes = new int[]{first - 1, second - 1, third - 1};
		Arrays.sort(hexes);
		return hexes;
	}
	
	private int[] moveRobber(CatanScore catan)
	{
		printLog("Enter new Robber location:"); 
		int robberPlacement = in.nextInt() - 1;
		List<Integer> playersOnHex = catan.gameBoard.getPlayersOnHex(robberPlacement);
		if (playersOnHex.isEmpty() || (playersOnHex.size() == 1 && playersOnHex.get(0) == catan.currentPlayer)) 
		{
			return new int[] {robberPlacement, -1};
		}
		else 
		{
			String s = "Enter Player stolen from: ";
			for (int i: playersOnHex) 
			{
				if (i != catan.currentPlayer)
				{
					s += i + ": " + catan.Players[i].name + " ";
				}	
			}
			printLog(s);
			int playerStole = in.nextInt();
			return new int[] {robberPlacement, playerStole};
		} 
	}
	
	public void printLog(String log) {
		System.out.println(log);
	}
	
	public void printLog(List<String> log) {
		for (String s: log){
			printLog(s);
		}
	}
	
	public static void main(String args[])
	{
		new TextMain("");
	}
	
	
	
	
}
