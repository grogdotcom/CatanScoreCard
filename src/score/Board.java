package score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.Resources;

public class Board 
{
	public final static int BASIC_NUM_HEX = 19;
	public final static int BASIC_NUM_PORT = 9;
	
	public final static int BASIC_BOARD = 0; 
	public final static int RANDOM_BOARD = 1; 
	public final static int CUSTOM_BOARD = 2;
	
	public static final int[][] BASIC_PORT_PLACEMENT_SETUP = {{0}, 						// 0
															  {1}, {1, 2}, 				// 1, 1
															  {2, 3}, {3},				// 2, 2
															  {4},		 				// 3
															  {5}, {5, 6},			 	// 4, 4
															  {6, 7}, {7},				// 5, 5
															  {8},		 				// 6
															  {9, 10}, {10},		 	// 7, 7
															  {10, 11}, {11}};			// 8, 8
	
	public static final int[][] BASIC_HEX_NEIGHBORS = {{1, 11, 12}, 				// 0
													   {0, 12, 13, 2}, 				// 1	
													   {1, 13, 3},					// 2
													   {2, 13, 14, 4},				// 3
													   {3, 14, 5},					// 4
													   {4, 14, 15, 6},				// 5
													   {5, 15, 7},					// 6
													   {6, 15, 16, 8},				// 7
													   {7, 16, 9},					// 8
													   {8, 16, 17, 10},				// 9
													   {9, 17, 11},					// 10
													   {10, 17, 12, 0},				// 11
													   {0, 1, 13, 18, 17, 11},		// 12
													   {1, 2, 3, 14, 18, 12},		// 13
													   {13, 3, 4, 5, 15, 18},		// 14
													   {18, 14, 5, 6, 7, 16},		// 15
													   {17, 18, 15, 7, 8, 9},		// 16
													   {11, 12, 18, 16, 9, 10}, 	// 17
													   {12, 13, 14, 15, 16, 17}};	// 18
	
	public static final int[] BASIC_PORT_SETTLEMENT_MATCH = {0, 1, 1, 2, 2, 3, 4, 4, 5, 5, 6, 7, 7, 8, 8};
	
	public static final int[] BASIC_BOARD_RESOURCE_SETUP = {-1, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 4, 4, 4};
	public static final int[] BASIC_BOARD_NUM_SETUP = {2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12};
	public static final int[] BASIC_PORT_SETUP = {-1, -1, -1, -1, 0, 1, 2, 3, 4};
	
	public final int numHex;
	public final int numPort;
	
	public int desert;
	
	private String resString = "";
	private String rollString = "";
	private String portsString = "";
	
	int n_hex = 0;
	int n_port = 0;
	
	boolean hex_complete = false;
	boolean port_complete = false;
	
	private Hex[] Hexes;
	private int[] Ports;
	
	private int[] settlementPorts; 
	
	Map<Integer, Integer> portMap = new HashMap();

	private Board(int numHex, int numPort)
	{
		this.numHex = numHex;
		this.numPort = numPort;
		
		Hexes = new Hex[numHex];
		Ports = new int[numPort];
	}
	
	public boolean addHex(int res, int roll)
	{
		if (hex_complete)
		{
			return false;
		}
		
		if (res == CatanScore.DESERT)
		{
			this.desert = n_hex;
		}
		Hexes[n_hex] = new Hex(n_hex, res, roll);
		n_hex++;
		
		hex_complete = n_hex >= numHex;
		resString += res + " "; 
		rollString += (roll < 0 ? "" : roll) + " ";
		
		if (hex_complete)
		{
			setUpHexNeighbors();
		}
		
		return true; 
	}
	
	public boolean addPort(int res)
	{
		if (port_complete)
		{
			return false;
		}
		Ports[n_port++] = res;
		port_complete = n_port >= numPort;
		portsString += res + " ";
		
		if (port_complete)
		{
			setUpSettlementPorts();
		}
		
		return true; 
	}
	
	private void setUpSettlementPorts()
	{
		int size = BASIC_PORT_SETTLEMENT_MATCH.length;
		settlementPorts = new int[size];
		for (int i = 0; i < size; i++)
		{
			settlementPorts[i] = Ports[BASIC_PORT_SETTLEMENT_MATCH[i]];
			//System.out.println(printArray(BASIC_PORT_PLACEMENT_SETUP[i]) + " : " + settlementPorts[i]);
			portMap.put(getHash(BASIC_PORT_PLACEMENT_SETUP[i]), settlementPorts[i]);
		}
	}
	
	private void setUpHexNeighbors()
	{
		for (int i = 0; i < numHex; i++)
		{
			for (int j = 0; j < BASIC_HEX_NEIGHBORS[i].length; j++)
			{
				Hexes[i].AddNeighbor(Hexes[BASIC_HEX_NEIGHBORS[i][j]]);
			}
		}
	}
	
	private String printArray(int[] array)
	{
		String s = "";
		for (int i = 0; i < array.length; i++)
		{
			s += array[i] + ",";
		}
		return s;
			
	}
	
	public Hex Hex(int i)
	{
		return Hexes[i];
	}
	
	private int getHash(int[] hexes)
	{
		int hash = 0; 
		for (int i = 0; i < hexes.length; i++)
		{
			hash *= 10;
			hash += (hexes[i] + 1);
		}
		return hash;
	}
	
	public boolean isPort(int[] hexes)
	{
		return portMap.containsKey(getHash(hexes));
	}
	
	public int getPortType(int[] hexes)
	{
		return portMap.get(getHash(hexes));
	}
	
	public List<Integer> getPlayersOnHex(int hex)
	{
		List<Integer> players = new ArrayList(8);
		
		for (int i = 0; i < 4; i ++)
		{
			if (Hexes[hex].PlayerOnHex[i] > 0)
			{
				players.add(i);
			}
		}
		return players;
	}
	
	public String toString()
	{
		return resString + "\n" + rollString + "\n" + portsString;
	}
	
	public static Board parse(String boardString)
	{
		String[] boardArray = boardString.split("\n");
		
		int[] res = Resources.stringArrayToIntArray(boardArray[0].split(" "));
		int[] rolls = Resources.stringArrayToIntArray(boardArray[1].split(" "));
		int[] ports = Resources.stringArrayToIntArray(boardArray[2].split(" "));
		
		Board board = new Board(res.length, ports.length);
		Hex[] hexes = resRollArrayToHexArray(res, rolls);
		for (int i = 0; i < hexes.length; i++)
		{
			board.addHex(hexes[i].Resource, hexes[i].roll);
		}
		
		for (int i = 0; i < ports.length; i++)
		{
			board.addPort(ports[i]);
		}
		
		return board; 
		
	}
	
	public static Board BasicBoardBuilder(int buildType)
	{
		Board board = new Board(BASIC_NUM_HEX, BASIC_NUM_PORT);
		
		int[] res = new int[BASIC_NUM_HEX]; 
		int[] roll = new int[BASIC_NUM_HEX - 1];
		int[] port = new int[BASIC_NUM_PORT];
		
		if (buildType == BASIC_BOARD)
		{
			res = BASIC_BOARD_RESOURCE_SETUP;
			roll = BASIC_BOARD_NUM_SETUP;
			port = BASIC_PORT_SETUP;
		}
		else if (buildType == RANDOM_BOARD)
		{
			res = BASIC_BOARD_RESOURCE_SETUP.clone();
			roll = BASIC_BOARD_NUM_SETUP.clone();
			port = BASIC_PORT_SETUP.clone();
			Resources.shuffleArray(res);
			Resources.shuffleArray(roll);
			Resources.shuffleArray(port);
		}
		
		if (buildType == BASIC_BOARD || buildType == RANDOM_BOARD)
		{
			Hex[] hexes = resRollArrayToHexArray(res, roll);
			for (int i = 0; i < hexes.length; i++)
			{
				board.addHex(hexes[i].Resource, hexes[i].roll);
			}
			
			for (int i = 0; i < BASIC_NUM_PORT; i++)
			{
				board.addPort(port[i]);
			}
			
		}
		
		return board;
	}
	
	private static Hex[] resRollArrayToHexArray(int[] res, int[] roll)
	{
		Hex[] hexes = new Hex[res.length];
		int k = 0;
		for (int i = 0; i < BASIC_NUM_HEX; i++)
		{
			if (res[i] == CatanScore.DESERT)
			{
				hexes[i] = new Hex(i, res[i], -1);
				k--;
			}
			else
			{
				hexes[i] = new Hex(i, res[i], roll[k]);
			}
			k++;	
		}
		return hexes;
	}
	

	
}
