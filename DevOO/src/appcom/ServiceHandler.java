package appcom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * 
 * @author Robin Nicolet - 2081 on GitHub
 *
 */
public class ServiceHandler implements HttpHandler {

	private String nomService;
	
	public ServiceHandler(String nomService, ServeurLivraison serveur){
		this.nomService = nomService;
		
		serveur.CreerContexte(nomService, this);
	}
	
	/**
	 * Génère une réponse au service pour le javascript
	 * @param in Buffer du contenu de la requête si besoin
	 * @return la reponse à donner au JS
	 */
	protected Reponse getReponse(BufferedReader in){
		return null;
	}
	
	/**
	 * Génère une réponse au service pour le javascript
	 * @param in Stream du contenu de la requête si besoin
	 * @return la reponse à donner au JS
	 */
	protected Reponse getReponse(InputStream in){
		return null;
	}
	/**
	 * Génère une réponse au service pour le javascript
	 * @param in Stream du contenu de la requête si besoin
	 * @return la reponse à donner au JS
	 */
	protected Reponse getReponse(String in){
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
        responseHeaders.set("Content-Type","text/plain;  charset=cp850");
        responseHeaders.set("Access-Control-Allow-Origin", "*");
        
        Reponse rep = getReponse(t.getRequestBody());
        if( rep == null ){
	        BufferedReader in = new BufferedReader (new InputStreamReader (t.getRequestBody()));
			rep = getReponse(in);
			if( rep == null ){
				String instr = "";
				String buf;
				while( (buf = in.readLine()) != null){
					instr += buf;
				}
				rep =  getReponse(instr);
				if( rep == null ) throw new IOException();
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
		 public static Reponse erreur(String msg){
			 return new Reponse(500,msg);
		 }
		 public static Reponse succes(String msg){
			 return new Reponse(200,msg);
		 }
	}

}
