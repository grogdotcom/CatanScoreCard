package score;

public enum TurnEventType {
	ROLL(0), ROLL_7(1), BUILD(2), PLAY_DCARD(3), TRADE(4), PORT_TRADE(5), SETUP(6);
	
	public final int num;
	
	TurnEventType(int num)
	{
		this.num = num;
	}
}
