import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import nu.validator.htmlparser.dom.HtmlDocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class MetaTag {
	
	private static Map<String,String> urlmap = new HashMap<String,String>();
	
	public static String getMetaTags(String url) {
		String json = urlmap.get(url);
		if(json != null){
			return json;
		} else {
			String title = "No title available";
			
			Document doc = getDoc(url);
	
			try{
				title = ((Element)doc.getElementsByTagName("title").item(0)).getTextContent();
				title = title.replaceAll("[\n]", "");
			} catch(Exception e){
				e.printStackTrace();
				System.out.println("Exception handled");
			}
			
			String desc = getMeta(doc,"description");
			if(desc == null) desc = "No description available";
			String title2 = getMeta(doc,"title");
			if(title2 != null) title = title2;
			String image= getMeta(doc,"image");
			image = image == null ? "" : ",image:\""+image+"\"";
			String keywords = getMeta(doc,"keywords");
			keywords = keywords == null? "": ",keywords:\""+keywords+"\"";
			json = "{title:\""+title+"\",description:\""+desc+"\""+image+keywords+"}";
			urlmap.put(url, json);
		    return json;
		}
	}
	
	private static String getMeta(Document doc, String name){
		if( doc != null ){
			NodeList list = doc.getDocumentElement().getElementsByTagName("meta");
			for(int i = 0; i < list.getLength(); ++i){
				if( list.item(i).getNodeType() == Node.ELEMENT_NODE ){
					Element e = (Element)list.item(i);
					if( e.hasAttribute("name") && e.getAttribute("name").contains(name) ){
						if( e.hasAttribute("content") ){
							return e.getAttribute("content").replaceAll("[\n]", "").replaceAll("\"", "''");
						}
					} else if (e.hasAttribute("property") && e.getAttribute("property").contains(name) ){
						if( e.hasAttribute("content") ){
							return e.getAttribute("content").replaceAll("[\n]", "").replaceAll("\"", "''");
						}
					}
				}
			}
		}
		return null;
	}
	
	private static Document getDoc(String urlstr){
		//création de la connection
		URL url;
		try {
			url = new URL(urlstr);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB;     rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");
		    conn.setRequestProperty("Content-Type", "text/xml");
		    conn.setConnectTimeout(1000);
		    conn.setDoOutput(false);
			conn.setDoOutput(true);
			
			/*BufferedReader isr = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String buf;
			while( (buf = isr.readLine()) != null){
				System.out.println(buf);
			}*/
			
			HtmlDocumentBuilder db = new HtmlDocumentBuilder();
			Document doc = db.parse(new InputSource(conn.getInputStream()));
			return doc;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		} catch (SAXException e){
			e.printStackTrace();
		}
		return null;
		
	}
}
