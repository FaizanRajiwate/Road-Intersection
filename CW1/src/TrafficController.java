//this class should implement the observer pattern, when the state changes, 
//the phases should be informed that the color has changed.

public class TrafficController extends Thread {
	private TrafficState currentState;
	
	public TrafficController() {
		currentState = TrafficState.RED;
	}
	
	public enum TrafficState{
		RED,
		AMBER,
		GREEN
	}
	
	public void advanceState() {
		
		if (currentState == TrafficState.RED) {
			currentState = TrafficState.GREEN;
		}
		else if (currentState == TrafficState.GREEN) {
			currentState = TrafficState.AMBER;
		}
		else if (currentState == TrafficState.AMBER) {
			currentState = TrafficState.RED;
		}
	}
	
	public boolean isGreen() {
		if (currentState == TrafficState.GREEN) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public TrafficState getTafficState() {
		return currentState;
	}
	
	@Override
	public void run() {
		
	}
}