import java.io.PrintStream;
//import java.net.MalformedURLException;
//import java.net.URL;

//import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;

public class XMLparser {

	public static void parseAndStream(Document doc, PrintStream stream){
		NodeList uris = doc.getElementsByTagName("a");
		for(int i = 0; i < uris.getLength(); ++i){
			Node a = uris.item(i);
			if( a.hasAttributes() ){
				NamedNodeMap nnm = a.getAttributes();
				Node href = nnm.getNamedItem("href");
				stream.println(href.getTextContent());
			}
		}
	}
}
