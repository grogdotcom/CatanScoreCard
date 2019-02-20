package score;

import java.util.ArrayList;
import java.util.List;

public class Hex {

	public int[] PlayerOnHex = new int[4];
	
	public final int pos; 
	public final int Resource; 
	public final int roll; 
	public final int dot; 
	
	private List<Hex> neighbors = new ArrayList(); 
	
	public Hex(int pos, int res, int num) {
		this.pos = pos;
		this.Resource = res; 
		this.roll = num; 
		
		this.dot = 6 - Math.abs(7 - roll);
	}
	
	public void AddNeighbor(Hex hex) {
		if (!isNeighbor(hex)) 
		{
			neighbors.add(hex);
		}
		
		if (!hex.isNeighbor(this))
		{
			hex.AddNeighbor(this);
		}
	}
	
	public boolean isNeighbor(Hex hex) {
		return neighbors.contains(hex);
	}
	
	public void AddPlayer (int pos) {
		PlayerOnHex[pos]++; 
	}
	
	public boolean equals(Object obj)
	{
		Hex hex = (Hex)obj;
		if (this.pos != hex.pos)
		{
			return false;
		}
		if (this.Resource != hex.Resource)
		{
			return false;
		}
		if (this.roll != hex.roll)
		{
			return false;
		}
		return true;
	}
	
	public String toString()
	{
		return pos + " " + Resource + " " + roll;
	}
	
	
	
	

}
