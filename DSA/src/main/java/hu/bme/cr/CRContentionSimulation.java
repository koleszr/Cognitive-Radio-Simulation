package hu.bme.cr;


import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import hu.bme.cr.entity.CognitiveRadio;
import hu.bme.cr.utilities.ListUtility;

public class CRContentionSimulation extends CRSystem {
	
	private String docName;
	
	private List<List<List<Integer>>> competingUsers;
	
	private List<List<Integer>> accessDecisions;
	
	public CRContentionSimulation() {
		super();
	}
	
	public CRContentionSimulation(String simulationParams) {
		super(simulationParams);
	}

	/*
	 * Getter and setter
	 */
	
	public List<List<List<Integer>>> getCompetingUsers() {
		return competingUsers;
	}

	public void setCompetingUsers(List<List<List<Integer>>> competingUsers) {
		this.competingUsers = competingUsers;
	}

	/**
	 * Initialize the cognitive radio system.
	 */
	@Override
	public void init() {
		initSystem();
	}
	
	/**
	 * Ends the game by persisting important 
	 * data to MongoDB database ("thesis" database, 
	 * "simulations" collection) and by closing output.
	 */
	@Override
	public void endGame() {
		//List<Document> collisionsList = collisions.entrySet().stream().map(c -> new Document().append("name", c.getKey()).append("number", c.getValue())).collect(Collectors.toList());
		persist(NORMAL_PHASE + "_" + (Integer.valueOf(props.getProperty("ROUNDS")) - 1));
		
		ds.getClient().close();
		
		System.out.println("End of Simulation");
		
		out.println();
		out.println("***********************");
		out.println("** End of Simulation **");
		out.println("***********************");
		out.close();	
	}
	
	/**
	 * Steps: 
	 * <ol>
	 * <li>Channel access in each subslot by generating a random backoff time.</li>
	 * <li>Calculate channel access probability by backoff time and add it to
	 * CognitiveRadios captureProbabilities list.</li>
	 * <li>Check if there was a collision on the channel at the given subslot 
	 * according to the backoff times.</li>
	 * <li>Calculate user estimate for each CognitiveRadio.</li>
	 * <li>Calculate channel capture probability for each CognitiveRadio using user estimate.</li>
	 * <li>Calculate channel collision probability for each CognitiveRadio using user estimate.</li>
	 * <li>Calculate utility for each CognitiveRadio.</li>
	 * </ol>
	 * 
	 * @param key - key to store the number of collisions
	 */
	@Override
	protected void play(String key) {
		super.play(key);
		
		accessDecisions = new ArrayList<>();
		for (CognitiveRadio r : radios) {
			accessDecisions.add(r.getAccessDecisions());
		}
		
		setCompetingUserNumber();
	}
	
	@Override
	protected void persist(String phase) {
		Document doc = new Document();
		List<Document> radioDocs = new ArrayList<>();
		for (CognitiveRadio r : radios) {
			Document radioDoc = new Document();
			radioDoc
				.append("radio", radios.indexOf(r))
				.append("accessDecisions", accessDecisions.get(radios.indexOf(r)))
				.append("utilities", r.getUtilities())
				.append("regrets", r.getRegrets())
				.append("competingUsers", competingUsers.get(radios.indexOf(r)))
				.append("demand", r.getDemand())
				.append("contentions", r.getContentions())
				.append("utilityFunction", r.getUtilityFunction().getType())
				.append("rates", r.getUtilityFunction().getRates())
				.append("strategy", r.getStrategy().toString());
			
			radioDocs.add(radioDoc);
		}

		doc.append("subslots", Integer.valueOf(props.getProperty("SUBSLOTS")))
			.append("radioNumber", radios.size())
			.append("channels", channels.size())
			.append("strategySpaceSize", strategySpaceSize)
			.append("radios", radioDocs)
			.append("phase", phase)
			.append("name", docName);
		
		ds.getDocuments().insertOne(doc);
	}
	
	public void setCompetingUserNumber() {
		competingUsers = new ArrayList<>(radios.size());
		
		for (int i = 0; i < radios.size(); i++) {
			List<List<Integer>> tmp = new ArrayList<>(strategySpaceSize);
			
			for (int j = 0; j < strategySpaceSize; j++) {
				tmp.add(ListUtility.fillListWithNValues(Integer.valueOf(0), channels.size()));			
			}
			
			competingUsers.add(tmp);
		}
		
		for (int i = 0; i < radios.size(); i++) {
			List<Integer> adi = radios.get(i).getAccessDecisions();
			
			for (int j = 0; j < adi.size(); j++) {
				int aj = adi.get(j);
				List<Integer> contentions = ListUtility.fillListWithNValues(Integer.valueOf(0), channels.size());
				List<Boolean> ssi = CognitiveRadio.getStrategySpace().get(aj);
				
				for (int k = 0; k < radios.size(); k++) {
					if (i != k) {
						List<Boolean> ssk = CognitiveRadio.getStrategySpace().get(radios.get(k).getAccessDecisions().get(j));
						
						for (int l = 0; l < ssi.size(); l++) {
							if (ssi.get(l) && ssk.get(l)) {
								contentions.set(l, Math.incrementExact(contentions.get(l)));
							}
						}
					}
				}
				
				competingUsers.get(i).set(aj, contentions);
			}
		}
		
	}
}

