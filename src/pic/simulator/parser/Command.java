package pic.simulator.parser;

public abstract class Command
{
	private static final short argumentCount = 0;

	public static Command newInstance(short command)
	{
		// TODO
		return null;
	}
	public static Command newInstance(String command)
	{
		return newInstance(parseStringToCommand(command));
	}
	
	private static short parseStringToCommand(String cmd)
	{
		// TODO
		return 0;
	}
	
	public static short getArgumentCount()
	{
		return argumentCount;
	}
	
}
