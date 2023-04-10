//this class should implement the observer pattern, when the state changes, 
//the phases should be informed that the color has changed.

public class TrafficController {
	private TrafficState currentState;

	public TrafficController() {
		currentState = TrafficState.RED;
	}

	public enum TrafficState {
		RED, AMBER, GREEN
	}

	public synchronized void advanceState() {

		if (currentState == TrafficState.RED) {
			currentState = TrafficState.GREEN;
		} else if (currentState == TrafficState.GREEN) {
			currentState = TrafficState.AMBER;
		} else if (currentState == TrafficState.AMBER) {
			currentState = TrafficState.RED;
		}
	}

	public boolean isGreen() {
		if (currentState == TrafficState.GREEN) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isAmber() {
		if (currentState == TrafficState.AMBER) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isRed() {
		if (currentState == TrafficState.RED) {
			return true;
		} else {
			return false;
		}
	}

	public synchronized TrafficState getTrafficState() {
		return currentState;
	}

}