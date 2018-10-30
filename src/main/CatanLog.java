package main;

import java.util.ArrayList;
import java.util.List;

public class CatanLog 
{
	public List<String> Log = new ArrayList();
	
	public void add(String text)
	{
		Log.add(text);
		//System.out.println(text);
	}

}
