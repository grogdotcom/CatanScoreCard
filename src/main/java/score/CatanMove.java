package score;

import java.util.ArrayList;
import java.util.List;

public class CatanMove 
{	
	public static final int numPlayers = 4; 
	public static final int numHexes = 19; 
	
	public static final TurnEventType[] types = {TurnEventType.ROLL, TurnEventType.ROLL_7, 
			TurnEventType.BUILD, TurnEventType.PLAY_DCARD, TurnEventType.TRADE, 
			TurnEventType.PORT_TRADE, TurnEventType.SETUP};
	
	public boolean diceRolled = false;	
	public boolean canPlayDCard = true; 
	
	List<TurnEvent> moveLog = new ArrayList();
		
	private int pos = 0; 
	
	public static CatanMove parseMove(String moveString)
	{
		CatanMove move = new CatanMove();
		
		String[] turnArray = moveString.split(" "); 
		for (int index = 0; index < turnArray.length; index++)
		{
			int header = new Integer(turnArray[index].charAt(0) + "");
			String message = turnArray[index].substring(1, turnArray[index].length());
			if (header >= types.length)
			{
				System.out.println(moveString);
			}
			
			move.addTurnEvent(new TurnEvent(types[header], message));	
		}
		return move;
	}
	
	public TurnEvent next() 
	{
		return moveLog.get(pos++);
	}
	
	public boolean hasNext()
	{
		return pos < moveLog.size(); 
	}
	
	public void undoLastEvent()
	{
		moveLog.remove(moveLog.size() - 1);
	}
	
	void addTurnEvent(TurnEvent event)
	{
		moveLog.add(event);
	}
	
	public boolean buildStartingSettlement(int player, int[] hexes)
	{
		TurnEvent buildEvent = new TurnEvent(TurnEventType.SETUP, new int[] {player}, hexes); 
		moveLog.add(buildEvent);
		return true; 
	}
	
	//N: dice roll
	//roll
	public boolean diceRoll(int roll)
	{
		if (diceRolled)
			return false; 
		
		if (roll <= 0 || roll > 12)
			return false; 
			
		diceRolled = true; 
		
		TurnEvent rollEvent = new TurnEvent(TurnEventType.ROLL, new int[] {roll}, null);
		moveLog.add(rollEvent);
		return true;
	}
	
	// N: dice roll
	//7,cL1_cL2_cL..._robberPlacement_playerStole
	public boolean sevenRoll(int robberPlacement, int playerStole, int[] cardsLost)
	{
		if (cardsLost == null || cardsLost.length < numPlayers)
			return false; 
		
		if (robberPlacement < 0 || robberPlacement > numHexes)
			return false; 
		
		TurnEvent sevenRollEvent = new TurnEvent(TurnEventType.ROLL_7, new int[] {robberPlacement, playerStole}, cardsLost);
		diceRolled = true; 
		moveLog.add(sevenRollEvent);
		return true;
	}
	
	//T: Trading 
	//tradePartner_NumcardsRecieved_NumcardsTraded
	public boolean Trade (int tradePartner, int[] cardsRecieved, int[] cardsSent)
	{
		if (!diceRolled)
			return false; 
		
		if (tradePartner < 0 || tradePartner >= numPlayers)
			return false; 
		
		if (cardsRecieved.length <= 0)
			return false; 
		
		if (cardsSent.length <= 0 )
			return false;

		for (int i = 0; i < 5; i++) {
			if (cardsRecieved[i] > 0 && cardsSent[i] > 0)
				return false;
		}

		int[] payloadArray = new int[] {cardsRecieved[0], cardsRecieved[1], cardsRecieved[2], cardsRecieved[3], cardsRecieved[4],
				cardsSent[0], cardsSent[1], cardsSent[2], cardsSent[3], cardsSent[4]};
		
		TurnEvent tradeEvent = new TurnEvent(TurnEventType.TRADE, new int[] {tradePartner}, payloadArray);
		moveLog.add(tradeEvent);
		return true;
	}
	
	public boolean PortTrade(int res, int numCards)
	{
		if (res < 0 || res >= CatanScore.NUM_RESOURCES)
		{
			return false;
		}
		
		if (numCards < 2 || numCards > 4)
		{
			return false; 
		}
		
		TurnEvent tradeEvent = new TurnEvent(TurnEventType.PORT_TRADE, new int[] {res, numCards}, null);
		moveLog.add(tradeEvent);
		return true;
	}
	
	public boolean build(int buildType, int[] hexes)
	{
		if (!diceRolled)
			return false; 
		
		if (buildType == CatanScore.SETTLEMENT || buildType == CatanScore.CITY)
		{
			if (hexes == null || (hexes.length < 1 && hexes.length > 3))
				return false; 			
		}
		else if (buildType == CatanScore.ROAD)
		{
			if (hexes == null || hexes.length > 1)
				return false;
		}
		
		TurnEvent buildEvent = new TurnEvent(TurnEventType.BUILD, new int[] {buildType}, hexes); 
		moveLog.add(buildEvent);
		return true; 
	}
	
	//D: Played Development Card 
	//	R_longestRoad - road Building
	//	P - Point
	//	Y - Year of Plenty 
	//	K_RobberLocation_PlayerStole - Knight 
	//	M_cardsLost1_cardsLost2_cardsLost3_cardsLost4 - Monopoly  
	public boolean PlayDevelopmentCard (int dCard, int[] devResult) 
	{
		if (!canPlayDCard)
			return false; 
		
		if (!(diceRolled || dCard == CatanScore.KNIGHT || dCard == CatanScore.POINT))
			return false;
		
		if (dCard == CatanScore.KNIGHT)
		{
			if (devResult == null || devResult.length < 2)
				return false; 
			
			if (devResult[0] < 0 || devResult[0] > numHexes)
				return false; 

			if (devResult[1] < -1 || devResult[1] > numPlayers)
				return false; 
		}
		else if (dCard == CatanScore.MONOPOLY)
		{
			if (devResult == null || devResult.length < numPlayers)
				return false; 
		}
		else if (dCard == CatanScore.ROAD_BUILDER)
		{
			if (devResult == null || devResult.length < 1)
				return false; 
		}

		TurnEvent dCardEvent = new TurnEvent(TurnEventType.PLAY_DCARD, new int[] {dCard}, devResult); 
		moveLog.add(dCardEvent);
		
		canPlayDCard = dCard == CatanScore.POINT; 
		return true; 
	}
	
	public String toString()
	{
		String s = "";
		for (TurnEvent m: moveLog)
			s += m + " ";
		return s; 
	}

	public static void main(String args[])
	{
		CatanMove move = new CatanMove();
		move.diceRoll(5);
		move.build(CatanScore.SETTLEMENT, new int[] {3, 5, 7});
		move.build(CatanScore.DEVELOPMENT_CARD, null);
		move.PlayDevelopmentCard(CatanScore.ROAD_BUILDER, new int[] {4});
		
		System.out.println(move);
		CatanMove move2 = CatanMove.parseMove(move.toString());
		System.out.println(move2);
		
		move = new CatanMove();
		move.PlayDevelopmentCard(CatanScore.POINT, null);
		System.out.println(move);	
	}
}
