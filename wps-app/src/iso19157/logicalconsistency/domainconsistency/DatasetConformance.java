package iso19157.logicalconsistency.domainconsistency;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DatasetConformance {
	
	private boolean conformance = true;
	private int numberOfConformingFeatures = 0;
	private int numberOfNonConformingFeatures = 0;
	private double nonConformanceRate = 0;
	private double conformanceRate = 0;
	
	public DatasetConformance(FeatureCollection fc, String attributeName, String codeListPath, String codeListName) {
		
		
		Document doc = null;
		try{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.parse(codeListPath);
			
			
		}
			
		catch (Exception e) {
			System.out.println(e);
		}
		
		Node node = readInCodeList(doc, "f_code");
		generateDomainConsistency(fc, attributeName, node);
			
	}
	
	
	private Node readInCodeList(Document doc, String codeListName) {
				
		Node codeListNodeToReturn = null;
		Node baseNode = (Node) doc.getElementsByTagName("CT_CodelistCatalogue").item(0);
		NodeList list = baseNode.getChildNodes();
		
		Node codeListNode = list.item(15);
		
		NodeList domainCodeLists = codeListNode.getChildNodes();
		
		//Node domainCodes = domainCodeLists.item(3);
		
		for (int i = 0; i < domainCodeLists.getLength(); i++) {
			Node domainCodes = domainCodeLists.item(i);
			
			if(domainCodes.getNodeName().equals("CodeListDictionary")) {
				
				String tempNodeString = domainCodes.getAttributes().item(0).getNodeValue();
		
				
				if(tempNodeString.equalsIgnoreCase(codeListName)) {
					
					codeListNodeToReturn = domainCodes;
				}
			
				//System.out.println(tempNode);
				
			}
	
		}
		
		//System.out.println(domainCodes.getAttributes().item(0).getNodeValue());
		
		return codeListNodeToReturn;
	}
	
	
	private void generateDomainConsistency(FeatureCollection fc, String attributeName, Node codeList) {
		
		ArrayList<String> domainList = new ArrayList<String>();
		//identify list of codes
		//identify attribute in feature collection
		//do some counting
		int numberOfConformant = 0;
		int numberOfNonConformant = 0;
	
		
		SimpleFeatureIterator fi = (SimpleFeatureIterator) fc.features();
		
		for (int i = 0; i < codeList.getChildNodes().item(5).getChildNodes().getLength(); i++) {
			
			Node innerNode = codeList.getChildNodes().item(5).getChildNodes().item(i);
			
			for (int j = 0; j < innerNode.getChildNodes().getLength(); j++) {
				
				Node innerinnerNode = innerNode.getChildNodes().item(j);
				
				String nodeName = innerinnerNode.getNodeName();
				
				
					if(nodeName.equals("gml:identifier")){
						
						System.out.println(innerinnerNode.getChildNodes().item(0).getNodeValue());
						
						String nodeValue = innerinnerNode.getChildNodes().item(0).getNodeValue();
						
						domainList.add(nodeValue);
						
					}
		
			}
			
			
			
		}
		//System.out.println(codeList.getChildNodes());
		
		while (fi.hasNext()) {
			
			SimpleFeature tempFeature = fi.next();
			
			String attributeValue = tempFeature.getProperty(attributeName).getValue().toString();
			
			Iterator listIterator = domainList.iterator();
			
			boolean match = false;
			
			while (listIterator.hasNext()) {
				
				String listValue = (String) listIterator.next();
				
				
				
				if(attributeValue.equals(listValue)) {
					
					match = true;
					
				}
				
				
			}
			
			if(match ==true) {
				numberOfConformant++;
			}
			if (match == false) {
				numberOfNonConformant++;
			}
			
		}
		
		numberOfConformingFeatures = numberOfConformant;
		numberOfNonConformingFeatures = numberOfNonConformant;
		
		if(numberOfNonConformant > 0) {
			conformance = true;
		}
		
		double totalSize = fc.size();
		conformanceRate = numberOfConformant/totalSize;
		nonConformanceRate = numberOfNonConformant/totalSize;
		
		
		
		
				
	}
	
	
	private double calculateRate(double featureCollectionSize, double featuresNumber) {
		return featureCollectionSize/featuresNumber;
		
	}
	
	public boolean getConformance() {
		return conformance;
	}
	
	public int getNumberOfConformingFeatures() {
		return numberOfConformingFeatures;
	}
	
	public int getNumberOfNonConformingFeatures() {
		return numberOfNonConformingFeatures;
	}
	
	public double getConformanceRate() {
		return conformanceRate;
	}
	
	public double getNonConformanceRate() {
		return nonConformanceRate;
	}

}
