import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class VehicleRNG {
		
		private Helper helper;
		private LinkedList<Phases> phaseList;
		private Random random;

		public VehicleRNG(Helper helper, LinkedList<Phases> phaseList){
			this.helper = helper;
			this.phaseList = phaseList;
			this.random = new Random();
		}
		
		public void rngVehicleCreator(Helper helper) throws PhaseException, NumberFormatException, InaccurateDataException, DuplicateIDException {
			
		}
		
			
		
}
