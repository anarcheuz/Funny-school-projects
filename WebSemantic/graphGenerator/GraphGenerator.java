import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

public abstract class GraphGenerator {
	private static final String URL = "#H4104 url";
	private static final String URI = "#H4104 uri";
	private static final String END = "#H4104 end";
	//début de la requête SPARQL
	private static final String BEGGIN = "SELECT * FROM <http://dbpedia.org> WHERE{?s ?p ?o. FILTER(?s in ("; 
	//private static PrintWriter outt;
	private static Scanner scan;
	
	public static void main(String[] args){
		scan = new Scanner(System.in);
		readIn();
	}
	
	public static void readIn()  {
		/*try{
			outt = new PrintWriter("filename.txt");
			
		}catch(Exception e){
			e.printStackTrace();
		}*/
		String request = "";
		String tmp = "";
		String token = "";
		tmp = scan.nextLine();
		while(!tmp.isEmpty()){
			if(tmp.equals(URL) ){
				token = "url";
				if(request.isEmpty()){
					request = BEGGIN;
				}else{
					if ( request.charAt(request.length()-1)==',') {
					  request = request.substring(0, request.length()-1);
					}
					request += ")) }"; 
					// requête finie !
					sendRequest(request);
					request = BEGGIN;
				}
			}else if(tmp.equals(URI)){
				token = "uri";
			}else if(tmp.equals(END)){
				if ( request.charAt(request.length()-1)==',') {
					  request = request.substring(0, request.length()-1);
					}
					request += ")) }";
					// requête finie !
					sendRequest(request);
					scan.close();
					
					return;
			}
			
			if(token.equals("url") && !tmp.equals(URL)){
				
				
				//renvoie à la ligne pour la prochaine url
				//outt.println();
				System.out.println();
				//Renvoie de l'url sur la console
				//outt.println(tmp);
				System.out.println(tmp);
			}else if(token.equals("uri") && !tmp.equals(URI)){
				request += "<"+tmp+">,";
			}
			tmp = scan.nextLine();
		}
		
	}
	
	public static void sendRequest(String request){
		String ur = "http://live.dbpedia.org/sparql/";
		final String urlEnd = "+&format=xml&timeout=30000";
		final String urlParam = "default-graph-uri=http%3A%2F%2Fdbpedia.org&query=";
		String line;
		String result ="";
		// http://live.dbpedia.org/sparql/?default-graph-uri=http%3A%2F%2Fdbpedia.org&query=SELECT+*+WHERE%7B%3Fs+%3Fp+%3Fo.+FILTER%28%3Fs+in+%28%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2FTerraforming%3E%2C%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2FEarth%3E%2C%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2FPeter_Griffin%3E%2C%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2FTerraforming%3E%2C%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2FEarth%3E%2C%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2FPeter_Griffin%3E%2C%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2FIrony%3E%2C%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2FTorture%3E%2C%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2FCharacter_%2528arts%2529%3E%2C%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2FCharacter_%2528arts%2529%3E%2C%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2FTorture%3E%2C%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2FProstitution%3E%2C%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2FEvil%3E%2C%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2FThe_Observer%3E%2C%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2FPeter_Griffin%3E%2C%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2FPeter_Griffin%3E%2C%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2FFilm%3E%2C%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2FBlog%3E%29%29+%7D+&format=xml&timeout=30000

		BufferedReader reader;
		try{
			//création de la requête
			String requestURL = URLEncoder.encode(request, "UTF-8");
			String finalRequest = urlParam+requestURL+urlEnd;
			//ouverture de la connexion
			//ur = ur+finalRequest;					
			URL url = new URL(ur);
			
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			DataOutputStream os = new DataOutputStream(con.getOutputStream());
			os.writeBytes(finalRequest);
			os.flush();
			os.close();
			
	
	         /*reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
	         while ((line = reader.readLine()) != null) {
	            result += line+"\n";
	         }
	         
	         DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	         InputSource is = new InputSource();
	         is.setCharacterStream(new StringReader(result));*/
			 DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	         Document doc = db.parse(con.getInputStream());
	         
	         parseAndPrint(doc);
	         
		      
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Parse an XML document and print a RDF graph of it in the standard output
	 * @param doc Document to be parsed
	 */
	public static void parseAndPrint(Document doc){
		
		//First, we're looking for results elements
		NodeList results = doc.getElementsByTagName("result");
		for(int i = 0 ; i< results.getLength();++i){
			Node resultNode = results.item(i);
			
			// We need to check that it is actually an element before casting,
			// as the document could contain some errors
			if( resultNode.getNodeType() == Node.ELEMENT_NODE){
				Element result = (Element)resultNode;
				
				// forward declaration of our result variables
				String s = null, p = null, o = null;
				
				// Again, looking for binding nodes
				NodeList nodes = result.getElementsByTagName("binding");
				/* note : getLength() should be 3
				 * if there are more than 3 children, the first occurrences of s,p,o will be
				 * overwritten.
				 */
				for(int j = 0; j < nodes.getLength(); ++j){ 
					Node n = nodes.item(j);
					if(n.getNodeType() == Node.ELEMENT_NODE && n.hasAttributes()){
						Element binding = (Element)n;
						Node nameNode = binding.getAttributes().getNamedItem("name");
						if( nameNode.getNodeType() == Node.ATTRIBUTE_NODE ){
							
							Attr name = (Attr)nameNode;
							// we're only taking the first item, as there should be only one
							Node datanode = binding.getElementsByTagName("*").item(0);
							
							// we still need to check if there was at least one
							if( datanode != null ){
								String rdfValue = null;
								
								switch(datanode.getNodeName()){
								case "uri":
									{
										if(datanode.getNodeType() == Node.ELEMENT_NODE){
											rdfValue = ((Element)datanode).getTextContent();
										}
									}
									break;
								case "literal":
									{
										if(datanode.getNodeType() == Node.ELEMENT_NODE){
											rdfValue = ((Element)datanode).getTextContent().replace("\n","");
										}
									}
									break;
								default:
									System.err.println("unhandled tagname : "+datanode.getNodeName());
								}
								
								switch(name.getValue()){
								case "s":
									s = rdfValue;
									break;
								case "p":
									p = rdfValue;
									break;
								case "o":
									o = rdfValue;
									break;
								}
							}
							
						}
					}
				}
				
				if( s != null && p != null && o != null){
					
					//outt.println(s+"\n"+p+"\n"+o);
					System.out.println(s+"\n"+p+"\n"+o);
				}
				
			} else {
				System.err.println("TagName 'result' should be an element !");
			}
		}
	}
	
	
	
}