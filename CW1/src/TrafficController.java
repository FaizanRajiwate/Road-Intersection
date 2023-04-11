//this class should implement the observer pattern, when the state changes, 
//the phases should be informed that the color has changed.

public class TrafficController {
	//traffic Controller 
	private TrafficState currentState;

	public TrafficController() {
		currentState = TrafficState.RED;
	}

	public enum TrafficState {
		//list of states Traffic can be in
		RED, AMBER, GREEN
	}

	public synchronized void advanceState() {
		//rules for advancing traffic state
		if (currentState == TrafficState.RED) {
			currentState = TrafficState.GREEN;
		} else if (currentState == TrafficState.GREEN) {
			currentState = TrafficState.AMBER;
		} else if (currentState == TrafficState.AMBER) {
			currentState = TrafficState.RED;
		}
	}

	public boolean isGreen() {
		//checks if light is green
		if (currentState == TrafficState.GREEN) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isAmber() {
		//checks if light is amber
		if (currentState == TrafficState.AMBER) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isRed() {
		//checks if light is red
		if (currentState == TrafficState.RED) {
			return true;
		} else {
			return false;
		}
	}

	public synchronized TrafficState getTrafficState() {
		//get's current traffic state
		return currentState;
	}

}