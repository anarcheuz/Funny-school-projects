package appcom;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ServeurLivraison {

	/**
	 * Serveur Http pour recevoir les requ�tes de la partie JS
	 */
	private HttpServer serveurHttp;
	
	public ServeurLivraison(){}
	
	/**
	 * 
	 * @return Le num�ro du port sur lequel le serveur a �t� d�marr�
	 */
	public int Demarrer(){
		int port = 4500;
		boolean portLibre = false;
		do{
			try {
				serveurHttp = HttpServer.create(new InetSocketAddress(port), 0);
				portLibre = true;
				System.out.println("Connexion d�marr�e sur port "+port+".");
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
		serveurHttp.createContext("/"+nomCtx, handler);
	}
}
