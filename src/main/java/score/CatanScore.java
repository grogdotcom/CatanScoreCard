package score;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import main.CatanLog;

public class CatanScore {

	public static final int NUM_HEX = 19; 
	public static final int NUM_RESOURCES = 5; 
	public static final int NUM_D_CARDS = 5; 
	public static final int NUM_BUILD = 4; 
	
	public static final String[] RESOURCES = {"Wheat", "Wood", "Sheep", "Brick", "Ore"}; 
	public static final int WHEAT = 0; 
	public static final int WOOD = 1;
	public static final int SHEEP = 2;
	public static final int BRICK = 3;
	public static final int ORE = 4;
	public static final int DESERT = -1; 
	public static final int[] BASIC_BOARD_RESOURCE_SETUP = {-1, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 4, 4, 4};
	public static final int[] BASIC_BOARD_NUM_SETUP = {-1, 2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12};
	
	public static final int KNIGHT = 0; 
	public static final int ROAD_BUILDER = 1;
	public static final int YEAR_OF_PLENTY = 2;
	public static final int MONOPOLY = 3;
	public static final int POINT = 4; 
	
	public static final String[] TITLES = {"Settlement", "City", "Road", "Development Card"}; 
	public static final int SETTLEMENT = 0; 
	public static final int CITY = 1; 
	public static final int ROAD = 2; 
	public static final int DEVELOPMENT_CARD = 3; 
	
	public Player[] Players = new Player[4]; 

	// position in array based on position on board; 
	public Board gameBoard; 
	public boolean GameOver = false;
	
	private Stack<Integer> DiceStack = new Stack<Integer>(); 
	private int[] Rolls = new int[11]; 
	private int[] PortResourcesTradedIn = new int[NUM_RESOURCES];
	public int Time7Rolled = 0; 
	
	public int Robber = -1;
	public int currentPlayer; 
	
	public int numPlayers; 
	
	private int longestRoad = -1;
	private int largestArmy = -1; 
	
	public List<String> moveLog = new ArrayList();
	public CatanLog Log = new CatanLog(); 
	
	public CatanScore (Board board, Player[] playerList) 
	{
		this.gameBoard = board;
		this.Players = playerList; 
		this.numPlayers = Players.length; 
		this.currentPlayer = 0;
		Robber = board.desert;
	}
	
	public void doMove(CatanMove move)
	{
		moveLog.add(move.toString());
		while (move.hasNext())
		{
			TurnEvent event = move.next();
			
			int[] payload = event.getPayload();
			int[] payloadArray = event.getPayloadArray();
			
			switch (event.type) {
			case ROLL: 
				CollectResources(payload[0]);
				break; 
			case ROLL_7: 
				Rolled_7(payload[0], payload[1], payloadArray);
				break; 
			case BUILD:
				Build(payload[0], payloadArray);
				break; 
			case PLAY_DCARD: 
				PlayDevelopmentCard(payload[0], payloadArray);
				break; 
			case TRADE:
				int[] cardsRecieved = new int[] { payloadArray[0],  payloadArray[1],  payloadArray[2],  payloadArray[3],  payloadArray[4]};
				int[] cardsSent = new int[] {payloadArray[5],  payloadArray[6],  payloadArray[7],  payloadArray[8],  payloadArray[9]};
				Trade(payload[0], cardsRecieved, cardsSent);
				break; 
			case PORT_TRADE:
				Port(payload[0], payload[1]);
				break;
			case SETUP:
				SetUp(payload[0], payloadArray);
			}
				
		}
		EndTurn();
	}
	
	public String toString()
	{
		String s = "";
		for (Player player: Players)
		{
			s += player.name + ",";
		}
		s += "\n";
		s += gameBoard + "\n";
		
		for (String move: moveLog)
		{
			s += move + "\n";
		}
		return s;
	}
	
	
		
	private void CollectResources (int diceRoll) 
	{
		Log.add(Players[currentPlayer].name + " rolled a " + diceRoll + "."); 
		
		Rolls [diceRoll - 2]++; 
		DiceStack.push(diceRoll); 
		
		for (int i = 0; i < NUM_HEX; i++) {
			if (i != Robber && gameBoard.Hex(i).roll == diceRoll) {
				for (int r = 0; r < Players.length; r++) {
					if (gameBoard.Hex(i).PlayerOnHex[r] > 0) {
						Players[r].ResourcesCollectedFromRoll[gameBoard.Hex(i).Resource] += gameBoard.Hex(i).PlayerOnHex[r];
						Log.add (Players[r].name + " collected " + gameBoard.Hex(i).PlayerOnHex[r] + " " + RESOURCES[gameBoard.Hex(i).Resource] + "s.");
					}
				}
			}		
		}
	}
	
	private void Rolled_7 (int robberLocation, int playerStole, int[] cardsLost) 
	{
		Log.add(Players[currentPlayer].name + " rolled a 7.");
		
		Rolls [7 - 2]++; 
		DiceStack.push(7); 
		
		for (int i = 0; i < cardsLost.length; i++) {
			if (cardsLost[i] > 0) {
				Players[i].Resources_Lost_7 += cardsLost[i]; 
				Log.add(Players[i].name + " lost " + cardsLost[i] + " Resources"); 
			}
		}
		Log.add("Robber was moved from " + (Robber + 1) + " to " + (robberLocation + 1));
		if (playerStole >= 0) {
			Players[currentPlayer].Resources_Gain_K++; 
			Players[playerStole].Resources_Lost_K++; 
			Log.add(Players[currentPlayer].name + " stole from " + Players[playerStole].name);
		}
		
		Robber = robberLocation; 
		Time7Rolled++;
	}
	
	// if Longest Road, hexes[0] = 1
	private void Build (int buildType, int[] buildArray) 
	{
		Player currPlayer = Players[currentPlayer];
	
		switch (buildType) 
		{
		case ROAD: 
			Log.add(currPlayer.name + " built a " + TITLES[buildType]);
			currPlayer.RoadLength = buildArray[0];
			if (currPlayer.RoadLength >= 5 && (longestRoad < 0 || currPlayer.RoadLength > Players[longestRoad].RoadLength))
				LongestRoad();	
			break; 
		case CITY: 
			currPlayer.Built[SETTLEMENT]--;	
		case SETTLEMENT:
			currPlayer.SettlementPoints++; 
			for (int i: buildArray) 
			{
				if (gameBoard.Hex(i).Resource < 0)
				{
					continue;
				}
				currPlayer.Dots [gameBoard.Hex(i).Resource] += gameBoard.Hex(i).dot;
				currPlayer.TotalDots += gameBoard.Hex(i).dot; 
				gameBoard.Hex(i).AddPlayer(currPlayer.pos);
			}
			if (gameBoard.isPort(buildArray) && !currPlayer.hasPort(gameBoard.getPortType(buildArray) + 1))
			{
				int portType = gameBoard.getPortType(buildArray) + 1;
				currPlayer.Ports[portType] += 1; 
				String port = portType == 0 ? "3 for 1" : RESOURCES[portType - 1];
				
				Log.add(currPlayer.name + " built on a " + port + " port");
			}
		case DEVELOPMENT_CARD:
			Log.add(currPlayer.name + " built a " + TITLES[buildType]);
			break; 
		default:
			return; 	
		}
		
		currPlayer.Built[buildType]++;
	}
	
	// Knight: 0- robberLocation, 1- playerStole (-1 if no card stole), 2- Largest Army (0 or 1) 
	// RoadBuilder: 0- Longest Road (0 or 1)
	// YearOfPlenty: 0-1 - cards gained from card
	// Monopoly: 0-3 - amountResourceLost
	// Point: 
	private void PlayDevelopmentCard (int dCard, int[] devArray)
	{
		Player currPlayer = Players[currentPlayer]; 
		currPlayer.DevelopmentCards[dCard]++; 
		switch (dCard) {
		case KNIGHT:
			Log.add(currPlayer.name + " played a Knight");
			Log.add("Robber was moved from " + (Robber + 1) + " to " + (devArray[0] + 1));
			Robber = devArray[0]; 
			
			if (devArray[1] != -1) 
			{
				currPlayer.Resources_Gain_K++; 
				Players[devArray[1]].Resources_Lost_K++; 
				Log.add(currPlayer.name + " stole from " + Players[devArray[1]].name);
			}
			currPlayer.numKnights++; 
			if (currPlayer.numKnights >= 3 && (largestArmy < 0 || currPlayer.numKnights > Players[largestArmy].numKnights))
				LargestArmy();
			break; 
		case ROAD_BUILDER: 
			Log.add(currPlayer.name + " played a Road Builder");
			currPlayer.Built[ROAD] += 2; 
			currPlayer.RoadLength = devArray[0];
			if (currPlayer.RoadLength >= 5 && (longestRoad < 0 || currPlayer.RoadLength > Players[longestRoad].RoadLength))
				LongestRoad();
			break; 
		case YEAR_OF_PLENTY: 
			Log.add(currPlayer.name + " played a Year of Plenty");
			currPlayer.Resources_Gain_D += 2; 
			break;
		case MONOPOLY: 
			Log.add(currPlayer.name + " played a Monopoly");
			for (int i = 0; i < devArray.length; i++) 
			{
				currPlayer.Resources_Gain_D += devArray[i];
				Players[i].Resources_Lost_D += devArray[i];
				if (i != currentPlayer)	
					Log.add(Players[i].name + " lost " + devArray[i] + " from Monopoly");
			}
			break; 
		case POINT: 
			Log.add(currPlayer.name + " revealed a Point");
			currPlayer.D_CardPoints++; 
			break; 
		}
	}
	
	private void Trade (int tradePartner, int[] cardsRecieved, int[] cardsSent)
	{
		Player currPlayer = Players[currentPlayer]; 
		currPlayer.numTrades++;
		currPlayer.TradeReceived += cardsRecieved.length;
		currPlayer.TradeSent += cardsSent.length;
		
		Players[tradePartner].numTrades++; 
		Players[tradePartner].TradeReceived += cardsSent.length;
		Players[tradePartner].TradeSent += cardsRecieved.length;

		for (int i = 0; i < 5; i++) {
			currPlayer.cardsReceivedFromTrades[i] += cardsRecieved[i];
			currPlayer.cardsSentFromTrades[i] += cardsSent[i];

			Players[tradePartner].cardsReceivedFromTrades[i] += cardsSent[i];
			Players[tradePartner].cardsSentFromTrades[i] += cardsRecieved[i];
		}
		
		Log.add(currPlayer.name + " traded " + cardsSent + " for " + cardsRecieved + " with " + Players[tradePartner].name);
	}
	
	private void Port (int res, int numCards)
	{
		PortResourcesTradedIn[res] += numCards;
		
		Player currPlayer = Players[currentPlayer];
		currPlayer.CardsTradedInPorts[numCards - 2] += 1;
		currPlayer.ResourcesTradedInPorts[res] += numCards; 
		
		Log.add(currPlayer.name + " traded " + numCards + " " + RESOURCES[res] + " in a port");
	}
	
	private void LongestRoad()
	{
		if (longestRoad >= 0) 
		{
			Log.add(Players[longestRoad].name + " lost Longest Road");
			Players[longestRoad].RoadPoints = 0; 
		}	
		longestRoad = currentPlayer; 
		Log.add(Players[longestRoad].name + " gained Longest Road");
		Players[longestRoad].RoadPoints = 2; 	
	}
	
	private void LargestArmy()
	{
		if (largestArmy >= 0) 
		{
			Log.add(Players[largestArmy].name + " lost Largest Army");
			Players[largestArmy].ArmyPoints = 0; 
		}
		
		largestArmy = currentPlayer; 
		Log.add(Players[largestArmy].name + " gained Largest Army");
		Players[largestArmy].ArmyPoints = 2; 	
	}
	
	private void SetUp(int player, int[] hexArray)
	{
		Player currPlayer = Players[player];
		currPlayer.SettlementPoints++; 
		for (int i: hexArray) 
		{
			if (gameBoard.Hex(i).Resource == DESERT)
			{
				continue; 
			}
			currPlayer.Dots [gameBoard.Hex(i).Resource] += gameBoard.Hex(i).dot;
			currPlayer.TotalDots += gameBoard.Hex(i).dot; 
			gameBoard.Hex(i).AddPlayer(currPlayer.pos);
			currPlayer.Built[SETTLEMENT]++;	
		}		
		if (gameBoard.isPort(hexArray))
		{
			System.out.println("*");
			int portType = gameBoard.getPortType(hexArray) + 1;
			currPlayer.Ports[portType] += 1; 
			String port = portType == 0 ? "3 for 1" : RESOURCES[portType - 1];
			
			Log.add(currPlayer.name + " built on a " + port + " port");
		}
		Log.add(currPlayer.name + " built a starting " + TITLES[SETTLEMENT]);
	}
	
	private void EndTurn () {
		Player currPlayer = Players[currentPlayer]; 
		currPlayer.Points = currPlayer.ArmyPoints + currPlayer.D_CardPoints + currPlayer.SettlementPoints + currPlayer.RoadPoints; 
		if (currPlayer.Points >= 10)
		{
			GameOver = true; 
			Log.add(currPlayer.name + " won the game with " + currPlayer.Points + " victory points!");
		}	
		currentPlayer++;
		currentPlayer = currentPlayer % numPlayers;
		Log.add(" ");
	}
	
	public static CatanScore parse(String gameString)
	{
		String[] gameArray = gameString.split("\n");
		
		String[] players = gameArray[0].split(",");
		Player[] playerArray = new Player[players.length];
		for (int i = 0; i < players.length; i++)
		{
			playerArray[i] = new Player(i, players[i]);
		}
		String board = gameArray[1] + "\n" + gameArray[2] + "\n" + gameArray[3];
		
		CatanScore score = new CatanScore(Board.parse(board), playerArray);
		for (int i = 4; i < gameArray.length; i++)
		{
			score.doMove(CatanMove.parseMove(gameArray[i]));
		}
		return score;
	}
	
	public static int DieRoll () {
		return new Random().nextInt(5) + 1;
	}
	
}
