package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Resources {

	private static Map<String, BufferedImage> imgMap;  
	
	public static BufferedImage loadImage (String path) throws IOException {
		if (imgMap == null) {
			imgMap = new HashMap();
		}
		
		if (imgMap.containsKey(path)) {
			BufferedImage image = imgMap.get(path);
			return image;
		}
		
		String pathHeader = "resources/";
		BufferedImage image = ImageIO.read(new File(pathHeader + path)); 
		
		imgMap.put(path, image);
		return image;
	}
	
	public static BufferedImage resizeImage(BufferedImage originalImage, int width, int height){
		BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();

		return resizedImage;
	}
	
	public static BufferedImage resizeImage(BufferedImage originalImage, float scale) {
		int width = (int)(originalImage.getWidth() * scale);
		int height = (int)(originalImage.getHeight() * scale);
		return resizeImage(originalImage, width, height);
	}
	
	public static String[] parseFile(String filePath, String toFind) throws FileNotFoundException
	{
		toFind = "_" + toFind; 
		Scanner s = new Scanner(new File(filePath));
		int numLines = -1;
		while (s.hasNextLine()) 
		{
			String[] s_split = s.nextLine().split(" ");
			if (s_split[0].equals(toFind))
			{
				numLines = new Integer(s_split[1]);
				break;
			}
		}
		if (numLines < 0)
			return null;
		
		
		String[] fileLines = new String[numLines];
		for (int i = 0; i < numLines; i++) 
		{
			String line =  s.nextLine();
			if (line.equals("#"))
			{
				return fileLines;
			}
			fileLines[i] = line;
			
		}
		return fileLines;
	}
	
	public static List<String> loadFromFile(String filePath) throws FileNotFoundException
	{
		List<String> loadList = new ArrayList();
		Scanner s = new Scanner(new File(filePath));
		while (s.hasNextLine()) 
		{
			loadList.add(s.nextLine());
		}
		return loadList;
	}
	
	public static void writeToFile(String filePath, String line, boolean append) throws IOException
	{
	    FileWriter fw = new FileWriter(filePath, append); //the true will append the new data
	    fw.write(line);//appends the string to the file
	    fw.close();
	}
	
	public static int[] stringArrayToIntArray(String[] stringArray)
	{
		int[] array = new int[stringArray.length];
		for (int i = 0; i < stringArray.length; i++)
		{
			//System.out.println("*" + stringArray[i] + "*");
			array[i] = new Integer(stringArray[i]);
		}
		return array;
	}
	
	public static void shuffleArray(int[] ar)
	{
		Random rnd = new Random();
		for (int i = 0; i < ar.length; i++)
		{
			int index = rnd.nextInt(i + 1);
			int a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}
	
	
	
	private static class ImageMap {
		
		private Map<String, BufferedImage> map = new HashMap();
		
		public boolean containsKey(String key) {
			return this.map.containsKey(key);
		}
		
		public BufferedImage get(String key) {
			return this.map.get(key);
		}
		
		public BufferedImage put(String key, BufferedImage image) {
			return this.map.put(key, image);
		}
		
	}
	
}
