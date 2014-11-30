import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class ServerBuilder {

	private CustomServer serveur;
	
	private ServerBuilder(CustomServer s){
		this.serveur = s;
	}
	
	public static ServerBuilder newServer(){
		CustomServer serveur = new CustomServer();
		ServerBuilder sb = new ServerBuilder(serveur);
		serveur.Demarrer();
		return sb;
	}
	
	public ServerBuilder deployMetadataService(String contexte){
		new ServiceHandler(contexte, serveur){
			protected Reponse getResponse(String in){
				System.err.println(in);
				return Reponse.success(MetaTag.getMetaTags(in));
			}
		};
		return this;
	}
	
	public ServerBuilder deployCmdService(String contexte, final String cmd, final StringFilter filter){
		
		/*
		 * Deploying search service
		 */
		new ServiceHandler(contexte, serveur){

			protected Reponse getResponse(String in){
				
				if(in.equals("")){
					return Reponse.error("No parameter found");
				}
				System.out.println("Execution de la commande : "+cmd + " " + in);
				String output = Cmd.getOutputString(cmd + " " + in);
				if( output == null){
					return Reponse.error("Failed to print the answer");
				} else {
					String resp = filter.filter(output);
					return Reponse.success(resp);
				}
			}
		};
		
		return this;
	}
	public ServerBuilder deployCmdService(String contexte, final String cmd){
		return deployCmdService(contexte, cmd,new StringFilter(){
			public String filter(String in){
				return in;
			}
		});
	}
	
}
