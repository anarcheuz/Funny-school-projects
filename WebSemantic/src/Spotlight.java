//import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;
//import javax.xml.parsers.DocumentBuilderFactory;

import nu.validator.htmlparser.dom.HtmlDocumentBuilder;

import org.w3c.dom.*;
import org.xml.sax.InputSource;


public class Spotlight {
	public static void main (String[] Args) throws MalformedURLException, IOException{
		Scanner sc = new Scanner(System.in);
		boolean empty = false;
		while(sc.hasNextLine() /*&& !empty*/){
			if(!(sc.hasNextLine() && !empty)){
				System.out.println("erreur");
			}
			String url = sc.nextLine();
			if(url.equals("#H4104 end")){
				break;
			} else {
				System.out.println("#H4104 url");
				System.out.println(url);
				System.out.println("#H4104 uri");
				String texte = sc.nextLine();
				doPost(texte);
			}
		}
		sc.close();
		System.out.println("#H4104 end");

	}
	

public static void doPost(String texte){
   OutputStreamWriter writer = null;
   //BufferedReader reader = null;
   try {
     //encodage des paramètres de la requête
      String donnees = URLEncoder.encode("text", "UTF-8")+
                        "="+(texte);
 
      //création de la connection
      URL url = new URL("http://spotlight.dbpedia.org/rest/annotate?confidence=0.2&support=20");
      URLConnection conn = url.openConnection();
      conn.setDoOutput(true);
 
      //envoi de la requête
      writer = new OutputStreamWriter(conn.getOutputStream());
      writer.write(donnees);
      writer.flush();
 
      //lecture de la réponse
      HtmlDocumentBuilder db = new HtmlDocumentBuilder();
      //Document doc = db.parse(new URL(url).openStream());
      Document doc = db.parse(new InputSource(conn.getInputStream()));
      
      XMLparser.parseAndStream(doc,System.out);
      /*reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String ligne;
      while ((ligne = reader.readLine()) != null) {
         System.out.println(ligne);
      }*/
   }catch (Exception e) {
      e.printStackTrace();
   }finally{
      try{writer.close();}catch(Exception e){}
   }
}


}