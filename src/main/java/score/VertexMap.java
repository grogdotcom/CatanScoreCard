package score;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VertexMap {

	private Map<Point, Vertex> vertexMap = new HashMap();
	
	public void addPoint(int hex, int c, Point p)
	{
		if (!vertexMap.containsKey(p))
		{
			vertexMap.put(p, new Vertex());
		}
		Vertex ver = vertexMap.get(p);
		if (!ver.contains(hex))
		{
			ver.add(hex, c);
		}
	}
	
	private class Vertex
	{
		List<Integer> hexes = new ArrayList();
		List<Integer> corners = new ArrayList();
		
		public boolean contains(int hex)
		{
			return hexes.contains(hex);
		}
		
		public void add(int hex, int c)
		{
			 hexes.add(hex);
			 corners.add(c);
			
		}
	}
	
}
