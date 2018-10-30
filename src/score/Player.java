package score;

public class Player {
		
	public final int pos;
	public final String name;

	public int Points = 0;
	public int SettlementPoints = 0;
	public int ArmyPoints = 0;
	public int RoadPoints = 0; 
	public int D_CardPoints = 0;
	
	public int[] ResourcesCollected = new int[CatanScore.NUM_RESOURCES];
	public int Resources_Gain_K = 0; 
	public int Resources_Gain_D = 0; 
	
	public int Resources_Lost_7 = 0; 
	public int Resources_Lost_K = 0;
	public int Resources_Lost_D = 0;
	
	public int[] Dots = new int[CatanScore.NUM_HEX];
	public int TotalDots = 0; 
	
	public int[] DevelopmentCards = new int[CatanScore.NUM_D_CARDS]; 
	public int[] Built = new int[CatanScore.NUM_BUILD];
	
	public int[] Ports = new int[CatanScore.NUM_RESOURCES + 1];
	public int[] CardsTradedInPorts = new int[3];
	public int[] ResourcesTradedInPorts = new int[CatanScore.NUM_RESOURCES];
	
	public int RoadLength = 0; 
	public int numKnights = 0; 
	
	public int numTrades = 0; 
	public int TradeRecieved = 0; 
	public int TradeSent = 0; 
	
	public Player(int pos, String name) {
		this.pos = pos;
		this.name = name;
	}
	
	public boolean hasPort(int portType)
	{
		return Ports[portType] > 0;
	}
}
