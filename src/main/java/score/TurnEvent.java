package score;

import main.Resources;

public class TurnEvent 
{
	public final TurnEventType type; 
	public final String eventString; 
	
	private int[] payload; 
	private int[] payload_array; 
	
	//protected TurnEvent next; 
	
	public TurnEvent(TurnEventType type, int[] payload, int[] payloadArray)
	{
		this.type = type; 
		
		this.payload = payload; 
		this.payload_array = payloadArray; 
		this.eventString = toString();
	}
	
	public TurnEvent(TurnEventType type, String eventString)
	{
		this.type = type; 
		this.eventString = eventString;
		parse(eventString);
		
	}
	
	private void parse(String message)
	{
		String[] splitMessage = message.split(",");
		
		String[] payloadMessage = splitMessage[0].split("_");
		this.payload = Resources.stringArrayToIntArray(payloadMessage);
		
		if (splitMessage.length > 1)
		{
			String[] payloadArrayMessage = splitMessage[1].split("_");
			this.payload_array = Resources.stringArrayToIntArray(payloadArrayMessage);
		}
	}
		
	public int[] getPayload()
	{
		return payload; 
	}
	
	public int[] getPayloadArray()
	{
		return payload_array;
	}
	
	public String toString()
	{
		String s = type.num + "";
		for (int i = 0; i < payload.length; i++){
			s += payload[i] + (i < payload.length - 1 ? "_" : "");
		}
		
		s += ",";
		
		if (payload_array != null)
		{
			for (int i = 0; i < payload_array.length; i++){
				s += payload_array[i] + (i < payload_array.length - 1 ? "_" : "");
			}
		}
		
		return s; 
	}
}
