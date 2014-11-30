import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Cmd {

	private static Cmd instance = new Cmd();
	
	//private PrintWriter cmdwrtr; 
	private Process process;
	private ByteArrayOutputStream ostrm;
	
	protected Cmd(){
		ostrm = new ByteArrayOutputStream();
	}
	
	private void openInstance() throws IOException{
		String[] command = 
		{
	        "cmd",
	    };
		process = Runtime.getRuntime().exec(command);
		new Thread(new SyncPipe(process.getErrorStream(), System.err)).start();
	    new Thread(new SyncPipe(process.getInputStream(), ostrm)).start();
	}
	public static void open() throws IOException {
		instance.openInstance();
	}
	
	private void execInstance(String cmdstrg){
		/*try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ostrm.reset();*/
		PrintWriter cmdwrtr = new PrintWriter(process.getOutputStream());
		cmdwrtr.println(cmdstrg);
		cmdwrtr.close();
	}
	public static void exec(String cmdstrg){
		instance.execInstance(cmdstrg);
	}
	
	private String closeInstance() throws InterruptedException {
	    int returnCode = -1;
		try {
			returnCode = process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    System.out.println("Cmd closed with return code = " + returnCode);
	    
	    String rep;
	    try {
			rep =  ostrm.toString("Cp850");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		    rep =  ostrm.toString();
		}
	    
	    ostrm.reset();
	    //System.out.println(rep);
	    return rep;
	}
	public static String close() throws InterruptedException {
		return instance.closeInstance();
	}
	
	public static String getOutputString(String cmd){
		try {
			Cmd.open();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		Cmd.exec(cmd);
		try {
			String rep = Cmd.close();
			rep = rep.substring(rep.indexOf("#DEBUT")+6,rep.lastIndexOf("#FIN"));
			return rep;		
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	/*
	String[] command =
	    {
	        "cmd",
	    };
	    Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			
			new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
		    new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
		    PrintWriter stdin = new PrintWriter(p.getOutputStream());
		    stdin.println("dir /A /Q");
		    // write any other commands you want here
		    stdin.close();
		    int returnCode = -1;
			try {
				returnCode = p.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    System.out.println("Return code = " + returnCode);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
}
