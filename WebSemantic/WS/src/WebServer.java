import java.io.IOException;
import java.io.PrintWriter;


public class WebServer {

	
	public static void main(String[] args) throws InterruptedException {
		
		//final String cmd = "python search.py";
		//final String cmd = "dir";
		
		
		ServerBuilder.newServer()
		.deployCmdService("search","semanticSearch.bat", new StringFilter(){
			public String filter(String in){
				//todo : rien
				return in;
			}
		}).deployCmdService("rank", "ranking.bat", new StringFilter(){
			public String filter(String in){
				//todo: Rien à faire ici ?
				return in;
			}
		}).deployMetadataService("meta");
		

	    
	}

}

