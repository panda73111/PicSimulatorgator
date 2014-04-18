package pic.simulator.interrupts;

public class Interruption implements Comparable<Interruption>{

	private int priority = 0;
	
	public void getCause()
	{
		
	}
	public int getInterruptionCode()
	{
		return 0;	
	}
	public int getPriority()
	{
		return priority;
	}
	
	

	@Override
	public int compareTo(Interruption arg0) {
		
		if(priority < arg0.getPriority())
			return -1;
		else if(priority > arg0.getPriority())
			return 1;
		return 0;
	}
}
