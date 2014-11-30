import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ServiceHandler implements HttpHandler {

	private String nomService;
	
	public ServiceHandler(String nomService, CustomServer serveur){
		this.nomService = nomService;
		
		serveur.CreerContexte(nomService, this);
	}
	
	/**
	 * Génère une réponse au service pour le javascript
	 * @param in Buffer du contenu de la requête si besoin
	 * @return la reponse à donner au JS
	 */
	protected Reponse getResponse(BufferedReader in){
		return null;
	}
	
	/**
	 * Génère une réponse au service pour le javascript
	 * @param in Stream du contenu de la requête si besoin
	 * @return la reponse à donner au JS
	 */
	protected Reponse getResponse(InputStream in){
		return null;
	}
	
	/**
	 * Génère une réponse au service pour le javascript
	 * @param in Stream du contenu de la requête si besoin
	 * @return la reponse à donner au JS
	 */
	protected Reponse getResponse(String in){
		return null;
	}
	
	/**
	 * Gère une requete : récupère le contenu, crée une reponse puis la renvoie
	 * @param t Echange http à gérer
	 * @throws IOException
	 * @see getReponse
	 */
	@Override
	public void handle(HttpExchange t) throws IOException {
        
        Headers responseHeaders= t.getResponseHeaders();
        responseHeaders.set("Content-Type","text/html; charset=cp850");
        responseHeaders.set("Access-Control-Allow-Origin", "*");
        
        Reponse rep = getResponse(t.getRequestBody());
        if( rep == null ){
	        BufferedReader in = new BufferedReader (new InputStreamReader (t.getRequestBody()));
			rep = getResponse(in);
			if( rep == null ){
				String param = "";
				String buf;
				while( (buf = in.readLine()) != null){
					param += buf;
				}
				rep =  getResponse(param);
				if( rep == null ) throw new IOException(); // no method implemented
			}
        }
        t.sendResponseHeaders(rep.code, rep.message.length());
        
        OutputStream os = t.getResponseBody();
        os.write(rep.message.getBytes());
        os.close();

	}

	
	/**
	 * 
	 * @return Nom du service géré par le handler
	 */
	public String getNomService(){
		return this.nomService;
	}
	
	/**
	 * Cette classe a pour but de contenir les réponse aux requêtes http
	 * @author rnicolet
	 *
	 */
	protected static class Reponse{
		 public int code;
		 public String message;
		 private Reponse(int code, String message){
			 this.code = code;
			 this.message = message;
		 }
		 public static Reponse error(String msg){
			 return new Reponse(500,msg);
		 }
		 public static Reponse success(String msg){
			 return new Reponse(200,msg);
		 }
	}

}
