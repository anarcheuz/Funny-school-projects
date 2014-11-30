import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class CustomServer {

	/**
	 * Serveur Http pour recevoir les requêtes de la partie JS
	 */
	private HttpServer serveurHttp;
	
	public CustomServer(){}
	
	/**
	 * 
	 * @return Le numéro du port sur lequel le serveur a été démarré
	 */
	public int Demarrer(){
		int port = 4500;
		boolean portLibre = false;
		do{
			try {
				serveurHttp = HttpServer.create(new InetSocketAddress(port), 0);
				portLibre = true;
				System.out.println("Connexion démarrée sur port "+port+".");
			} catch (IOException e) {
				e.printStackTrace();
				++port;
			}
		} while( !portLibre );
		
		serveurHttp.setExecutor(null); // creates a default executor
		serveurHttp.start();
		
		return port;
	}
	
	public void CreerContexte(String nomCtx, HttpHandler handler){
		System.out.println("Contexte créé : /"+nomCtx);
		serveurHttp.createContext("/"+nomCtx, handler);
	}
}
