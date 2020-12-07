package helyx.dataquality.metadata;

import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class GenerateISO19115QualityMetadata {
	
	
	Logger LOG = Logger.getLogger(GenerateISO19115QualityMetadata.class);
	
	private String metadataDocumentPath;
	private String qualityElementName;
	private String[] elements;
	private static Document document;
	private static Node newDataQualityNode;
	private static Document qualityNodeDocument;
	private Node qualityNode;
	
	public GenerateISO19115QualityMetadata(String metadataDocumentPath, String qualityElementName, String[] elements){
		
		//elements = new String[7];
		
		this.elements = elements;
		this.qualityElementName = qualityElementName;
		
		Node node = null;
		if (metadataDocumentPath != null){
			node = getBaseNode(metadataDocumentPath, qualityElementName);
		
		}
		else{
			
			try{
				InputStream is = this.getClass().getClassLoader().getResourceAsStream("src/helyx/dataquality/metadata/iso19115_example.xml");
				LOG.warn("+++++ " + this.getClass().getClassLoader().getResourceAsStream("src/helyx/dataquality/metadata/iso19115_example.xml"));
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(is);
		
				node = getBaseNodeFromResource(doc, qualityElementName);
			}
			catch (Exception e){
				
			}
		}
		
		Node emptyResultNode = getResultsNode(node);
		
		//
		String nameOfMeasure = elements[0];
		String measureDescription = elements[1];
		String evaluationMethodType = elements[2];

		//quality data node variables
		String title = elements[3];
		String date = elements[4];
		String dateType = elements[5];
		String explanation = elements[6];
		int passFail = Integer.parseInt(elements[7]);
		
		System.out.println(" Generate " + nameOfMeasure + " " + measureDescription);
		
				
		newDataQualityNode = generateNewQualityNode(emptyResultNode,title, date, dateType, 
				explanation, passFail );
		
		Node nodeToUpdate = updateDataQualityElement(node, nameOfMeasure, measureDescription, 
				evaluationMethodType, newDataQualityNode);
		
		document = outputUpdatedMetadataDocument(metadataDocumentPath, qualityElementName, nodeToUpdate);
		
		qualityNodeDocument = outputQualityNodeAsDocument(nodeToUpdate);
		
		this.qualityNode = nodeToUpdate;
		
			
		
	}
	
	private static Node getBaseNodeFromDocument(Document doc, String elementToUpdate){
		Node staff = doc.getElementsByTagName("gmd:MD_Metadata").item(0);
		Node returnNode = null;
		NodeList list = staff.getChildNodes();

		for (int i = 0; i < list.getLength(); i++) {
			
                   Node node = list.item(i);
                   checkDoc(node);
                  // System.out.println(node.getNodeName());

		   // get the salary element, and update the value
		   if ("gmd:dataQualityInfo".equals(node.getNodeName())) {
			   
			   NodeList list2 = node.getChildNodes();
			   
			   for (int j = 0; j < list2.getLength(); j++){
				   Node node2 = list2.item(j);
				 //  System.out.println(node2);
			   
			   
			   if ("gmd:DQ_DataQuality".equals(node2.getNodeName())) {
				   
				   NodeList list3 = node2.getChildNodes();
				   
				   for (int k = 0; k < list3.getLength(); k++){
					   Node node3 = list3.item(k);
					  // System.out.println(node3);
				   

					   if ("gmd:report".equals(node3.getNodeName())) {
						   
						   NodeList list4 = node3.getChildNodes();
						   
						   for (int l = 0; l < list4.getLength(); l++){
							   Node node4 = list4.item(l);
						//	   System.out.println(node4);
							   if (elementToUpdate.equals(node4.getNodeName())){
								   returnNode = node4;
							   }
						   }
					   }
			   
			   
				   }
			   
			   	}
			   }
		   }
		
		}
		
		return returnNode;
		
	}
	
	private static Node getBaseNode(String path, String elementToUpdate){
		
		Node returnNode = null;
		try{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(path);
		
		Node staff = doc.getElementsByTagName("gmd:MD_Metadata").item(0);

		NodeList list = staff.getChildNodes();

		for (int i = 0; i < list.getLength(); i++) {
			
                   Node node = list.item(i);
                   
                 //  System.out.println(node.getNodeName());

		   // get the salary element, and update the value
		   if ("gmd:dataQualityInfo".equals(node.getNodeName())) {
			   
			   NodeList list2 = node.getChildNodes();
			   
			   for (int j = 0; j < list2.getLength(); j++){
				   Node node2 = list2.item(j);
				  // System.out.println(node2);
			   
			   
			   if ("gmd:DQ_DataQuality".equals(node2.getNodeName())) {
				   
				   NodeList list3 = node2.getChildNodes();
				   
				   for (int k = 0; k < list3.getLength(); k++){
					   Node node3 = list3.item(k);
					//   System.out.println(node3);
				   

					   if ("gmd:report".equals(node3.getNodeName())) {
						   
						   NodeList list4 = node3.getChildNodes();
						   
						   for (int l = 0; l < list4.getLength(); l++){
							   Node node4 = list4.item(l);
							//   System.out.println(node4);
							   if (elementToUpdate.equals(node4.getNodeName())){
								   returnNode = node4;
							   }
						   }
					   }
			   
			   
				   }
			   
			   	}
			   }
		   }
		}
		}
		catch (Exception e){
			
		}
		return returnNode;

		
	}
private static Node getBaseNodeFromResource(Document doc, String elementToUpdate){
		
		Node returnNode = null;
		
		
		Node staff = doc.getElementsByTagName("gmd:MD_Metadata").item(0);

		NodeList list = staff.getChildNodes();

		for (int i = 0; i < list.getLength(); i++) {
			
                   Node node = list.item(i);
                   
                 //  System.out.println(node.getNodeName());

		   // get the salary element, and update the value
		   if ("gmd:dataQualityInfo".equals(node.getNodeName())) {
			   
			   NodeList list2 = node.getChildNodes();
			   
			   for (int j = 0; j < list2.getLength(); j++){
				   Node node2 = list2.item(j);
				  // System.out.println(node2);
			   
			   
			   if ("gmd:DQ_DataQuality".equals(node2.getNodeName())) {
				   
				   NodeList list3 = node2.getChildNodes();
				   
				   for (int k = 0; k < list3.getLength(); k++){
					   Node node3 = list3.item(k);
					//   System.out.println(node3);
				   

					   if ("gmd:report".equals(node3.getNodeName())) {
						   
						   NodeList list4 = node3.getChildNodes();
						   
						   for (int l = 0; l < list4.getLength(); l++){
							   Node node4 = list4.item(l);
							//   System.out.println(node4);
							   if (elementToUpdate.equals(node4.getNodeName())){
								   returnNode = node4;
							   }
						   }
					   }
			   
			   
				   }
			   
			   	}
			   }
		   }
		}
		
	
		return returnNode;

		
	}
	
	private static Node updateDataQualityElement(Node node, String nameOfMeasure, String measureDescription,
			String evaluationMethodType, Node resultsNode){
		
		Node oldNode = node;
		NodeList list = oldNode.getChildNodes();
		
		for(int i = 0; i < list.getLength(); i++ ){
			Node tempNode = list.item(i);
		
			if ("gmd:nameOfMeasure".equalsIgnoreCase(tempNode.getNodeName())){
					
					Node conNode = tempNode.getChildNodes().item(1);
					conNode.getChildNodes().item(0).setNodeValue(nameOfMeasure);
					//System.out.println("Child node 33 " + conNode.getNodeName() + " " + 
							//conNode.getChildNodes().item(0).getNodeValue());
				}
			if ("gmd:measureDescription".equalsIgnoreCase(tempNode.getNodeName())){
					Node conNode = tempNode.getChildNodes().item(1);
					conNode.getChildNodes().item(0).setNodeValue(measureDescription);
					//System.out.println("Child node 33 " + conNode.getNodeName() + " " + 
							//conNode.getChildNodes().item(0).getNodeValue());
					
			}
			if ("gmd:evaluationMethodType".equalsIgnoreCase(tempNode.getNodeName())){
				Node conNode = tempNode.getChildNodes().item(1);
				conNode.getChildNodes().item(0).setNodeValue(evaluationMethodType);
				//System.out.println("Child node 33 " + conNode.getNodeName() + " " + 
						//conNode.getChildNodes().item(0).getNodeValue());
				
				if(evaluationMethodType.equalsIgnoreCase("direct internal")){
					conNode.getAttributes().getNamedItem("codeListValue").setNodeValue("directInternal");
					//System.out.println("++++++++++++++++++node Value 1 " + conNode.getAttributes().getNamedItem("codeListValue"));
				}
				if(evaluationMethodType.equalsIgnoreCase("direct external")){
					conNode.getAttributes().getNamedItem("codeListValue").setNodeValue("directExternal");
					//System.out.println("++++++++++++++++++node Value 2 " + conNode.getAttributes().getNamedItem("codeListValue"));

				}
				
			}
			if ("gmd:result".equalsIgnoreCase(tempNode.getNodeName())){
				node.replaceChild(resultsNode, tempNode);
			}
		}
		
		Node newNode = oldNode;
		return newNode;
	}
	
	private static Node getResultsNode(Node baseNode){
		
		Node oldNode = baseNode;
		NodeList list = oldNode.getChildNodes();
		Node newNode = null;
		for(int i = 0; i < list.getLength(); i++ ){
			Node tempNode = list.item(i);
			
		
			if ("gmd:result".equalsIgnoreCase(tempNode.getNodeName())){
				newNode = tempNode;
				//System.out.println("result node name " + tempNode.getNodeName());
			}
		}
			
		
		
		return newNode;
		
	}
	
	private static Node generateNewQualityNode(Node qualityBaseNode, String title, String date, String datetype, String explanation, int pass){
		
		Node oldNode = qualityBaseNode;
		NodeList list = oldNode.getChildNodes();
		
		for(int i = 0; i < list.getLength(); i++ ){
			Node tempNode = list.item(i);
			System.out.println(tempNode.getNodeName());
			if ("gmd:DQ_ConformanceResult".equalsIgnoreCase(tempNode.getNodeName())){
					//set title
					NodeList conList= tempNode.getChildNodes();
					Node conNode1 = conList.item(1).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(1);
					conNode1.getChildNodes().item(0).setNodeValue(title);
					System.out.println("Result child node " + conNode1.getNodeName() + " title " + conNode1.getChildNodes().item(0).getNodeValue());
					
					//set date
					NodeList dateList = tempNode.getChildNodes();
					Node conNode2 = dateList.item(1).getChildNodes().item(1).getChildNodes()
							.item(3).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(1);
					conNode2.getChildNodes().item(0).setNodeValue(date);
					System.out.println("Result child node " + conNode2.getNodeName() + " " + conNode2.getChildNodes().item(0).getNodeValue());
					
					//set datetype
					Node conNode3 = dateList.item(1).getChildNodes().item(1).getChildNodes()
							.item(3).getChildNodes().item(1).getChildNodes().item(3).getChildNodes().item(1);
					conNode3.getChildNodes().item(0).setNodeValue(datetype);
					System.out.println("Result Child Node " + conNode3.getNodeName() 
					+ " " + conNode3.getChildNodes().item(0).getNodeValue());
					
					if(datetype.equalsIgnoreCase("creation")){
						conNode3.getAttributes().getNamedItem("codeListValue").setNodeValue("creation");
					}
					if(datetype.equalsIgnoreCase("revision")){
						conNode3.getAttributes().getNamedItem("codeListValue").setNodeValue("revision");
					}
					
					//set explanation
					Node conNode4 = dateList.item(3).getChildNodes().item(1);
					conNode4.getChildNodes().item(0).setNodeValue(explanation);
					System.out.println("Result Child Node " + conNode4.getNodeName() + " " +
					conNode4.getChildNodes().item(0).getNodeValue());
					
					//set pass/fail boolean
					Node conNode5 = dateList.item(5).getChildNodes().item(1);
					conNode5.getChildNodes().item(0).setNodeValue(pass + "");
					System.out.println("Result Child Node " + conNode5.getNodeName() + " " +
					conNode5.getChildNodes().item(0).getNodeValue());
					
			}
		}
		
		Node newNode = oldNode;
		
		return newNode;
			
	}
	
	private static Document outputUpdatedMetadataDocument(String inputPath,
			String elementToUpdate, Node updatedResult){
		Document doc = null;
		Document newDoc = null;
		try {
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.parse(inputPath);
			
			Node node = getBaseNodeFromDocument(doc, elementToUpdate);
			Node parentNode = node.getParentNode();
			
			Node importedNode = doc.importNode(updatedResult, true);
			
			parentNode.removeChild(node);
			parentNode.appendChild(importedNode);
			//newDoc = doc;
			//parentNode.replaceChild(updatedResult, node);
			
			//Node importNode = doc.importNode(updatedResult, true);
			
		//	node.appendChild(importNode);
			
			/**TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(outputPath).getPath());
			transformer.transform(source, result);**/

			System.out.println("Done");
			
			
			
			
		}
		
		catch (Exception e){
			System.out.println("Exception " + e);
		}
		
		//System.out.println(newDoc.getBaseURI());
		
		return doc;
		
	}
private static Document outputQualityNodeAsDocument( Node updatedResult){
		
	Document doc = null;
	
		try {
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
			Node importedNode = doc.importNode(updatedResult, true);
			doc.appendChild(importedNode);
			//Node node = getBaseNodeFromDocument(doc, elementToUpdate);
			//Node parentNode = node.getParentNode();
			//
		//	Node importedNode = doc.importNode(updatedResult, true);
			
			//parentNode.removeChild(node);
		//	parentNode.appendChild(importedNode);
			
			//parentNode.replaceChild(updatedResult, node);
			
			//Node importNode = doc.importNode(updatedResult, true);
			
		//	node.appendChild(importNode);
			
		/**	TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(outputPath).getPath());
			transformer.transform(source, result);

			System.out.println("Done");**/
		
			
			
		}
		
		catch (Exception e){
			System.out.println("Exception " + e);
		}
		
		return doc;
		
	}

public Node getQualityNode(){
	return qualityNode;
}
	
	
	
	
	public Document getUpdatedDocument(){
		return document;
	}
	public Document getUpdatedNodeAsDocument(){
		return qualityNodeDocument;
	}
	
	public static void checkDoc(Node n) {
		  if (n instanceof Text) {
		    if (((Text) n).getData() == null) {
		      System.err.println("null data!!!!");
		    }
		  }

		  NodeList l = n.getChildNodes();
		  for (int i = 0; i < l.getLength(); ++i) {
		    checkDoc(l.item(i));
		  }
		}
}
