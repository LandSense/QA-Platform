package iso19157.thematicaccuracy.classificationcorrectness;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;


import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.request.model.PredictRequest;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.Model;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

public class CheckImageClassification {
	
	//do AI and ML stuff here, look for TensorFlow or clarify
	//Just does the imagery stuff at the moment, it'll have to be changed to parse feature collections with imagary associated with the output
	
	private boolean match = false;
	private int numberOfCorrectClassifications = 0;
	private int numberOfMisClassifications = 0;
	private double classificationRate = 0;
	private double misClassificationRate = 0;
	
	
	public CheckImageClassification(String imageLocation, String submittedConcept, double confidenceThreshold) {
		
		List<ClarifaiOutput<Concept>> list = runClarifai(imageLocation);
		
		ArrayList<Concept> identifiedConcepts = getIdentifiedConceptList(list);
		
		this.match = getImageMatchDecision(submittedConcept, identifiedConcepts, confidenceThreshold);
		
		
		
	}
	
	
	public CheckImageClassification (String[][] imageLocations, double confidenceThreshold) {
	
		boolean tempMatch = false;
		
		for (int i = 0; i < imageLocations.length; i++) {
			
			tempMatch = false;
			
			String imageLocation = imageLocations[i][0];
			
			List<ClarifaiOutput<Concept>> list = runClarifai(imageLocation);
			
			ArrayList<Concept> identifiedConcept = getIdentifiedConceptList(list);
			
			String submittedConcept = imageLocations[i][1];
			
			tempMatch = getImageMatchDecision(submittedConcept, identifiedConcept, confidenceThreshold);
			
			if(tempMatch == true) {
				
				numberOfCorrectClassifications++;
				
			}

		}
		
		numberOfMisClassifications = imageLocations.length - numberOfCorrectClassifications;
		
		classificationRate = ((double)numberOfCorrectClassifications/imageLocations.length);
		
		misClassificationRate = 1 - classificationRate;
		
		if (misClassificationRate > 0) {
			
			match = false;
		}
		
		else {
			
			match = true;
		}
		
		
	}
	
	private boolean getImageMatchDecision(String concept, ArrayList<Concept> concepts, double confidenceThreshold) {
		
		boolean decision = false;
		Iterator conceptIterator = concepts.iterator();
		
		while (conceptIterator.hasNext()) {
			
			Concept tempConcept = (Concept) conceptIterator.next();
			
			if (tempConcept.name().equalsIgnoreCase(concept)){
				
				if (tempConcept.value() >= confidenceThreshold)
				{
					decision = true;
				}
				
			}
			
			
		}
		
		
		return decision;
	}
	
	private List runClarifai(String imageLocation) {
		
		
		final String apiKey = "<insert clarify API key here>";
		
		final ClarifaiClient client = new ClarifaiBuilder(apiKey).buildSync();
		
		Model<Concept> generalModel = client.getDefaultModels().generalModel();

		PredictRequest<Concept> request = generalModel.predict().withInputs(
		        ClarifaiInput.forImage(imageLocation)
		    );
		List<ClarifaiOutput<Concept>> result = request.executeSync().get();
		
		//System.out.println(result.size() + " " + result);
		
		
		return result;
				
		
	}
	
	private ArrayList<Concept> getIdentifiedConceptList(List<ClarifaiOutput<Concept>> list) {
		
	//	System.out.println(list.get(0));
		
		ClarifaiOutput output = list.get(0);
		
		List outputList = output.data();
		
		ArrayList<Concept> returnConcept = new ArrayList<Concept>(); 
		
		for (int i = 0; i < outputList.size(); i++) {
			//System.out.println(outputList.get(i));
			
			Concept lowList = (Concept) outputList.get(i);
			
			returnConcept.add(lowList);
			
			System.out.println(lowList.name() + " " + lowList.value());
			
			
			
		}
				
	return returnConcept;	
	}
	
	public boolean getMatchDecision() {
		return match;
	}
	
	public double getClassificationRate() {
		return classificationRate;
	}
	
	public double getMisClassificationRate() {
		return misClassificationRate;
	}
	
	public int getNumberOfCorrectClassifications() {
		return numberOfCorrectClassifications;
	}
	
	public int getNumberOfMisClassifications() {
		return numberOfMisClassifications;
	}
	
	

	

}
