package test;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;
import java.nio.file.Path;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.*;

import modele.*;
import controleur.*;

public class Test {
	public static void main(String[] args){
		Intersection inter = new Intersection(1,0,2);
		if(ConstrucIntersection(1,0,2)){
			System.out.println("V - Création d'une intersection");
			if(ConstrucRoute("h1",12,12,inter)){
				System.out.println("V - Route à partir de Intersec marche");
				Route route = new Route("h1",12,12,inter);
				if(addTroncIntersection(inter,route)){
					System.out.println("V - Ajout d'un tronc sortant");
				} else {
					System.out.println("X - Ajout d'un tronc sortant");
				}
			} else {
				System.out.println("X - Route à partir de Intersec marche");
			}
			 DateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm:ss");
			 Date dateBis = new Date();
			 Date date = new Date();
			 try{
				 date = HOUR_FORMAT.parse("9:0:0");
				 dateBis = HOUR_FORMAT.parse("8:0:0");
			 }catch(Exception e){
				 e.printStackTrace();
			 }
			 
			
			if(ConstrucEtape(dateBis,inter)){
				System.out.println("V - Création étape");
				Etape etape = new Etape(dateBis, inter);
				etape.setHeurePassagePrevue(date);
				
				
				if(etape.getHeurePassagePrevue()==date){
					System.out.println("V - Etape - Modification heure");
				} else {
					System.out.println("X - Etape - Modification heure");
				}
			} else {
				System.out.println("X - Création étape");
			}
			if(ConstrucLivraison(inter,1,2)){
				System.out.println("V - Livraison - Construc");
			} else {
				System.out.println("X - Livraison - Construc");
			}
			System.out.println("==============================");
			String path = new File("Tests/Plan-Minimal.xml").getAbsolutePath();
			System.out.println("Vérifiez que vous disposez du Fichier à :"+path);
			File file = new File(path);
			//ChargerPlan plan = new ChargerPlan(stringToDom(loadFile(file)));
			
			
		} else {
			System.out.println("X - Création d'une intersection");
		}
		
	}

	private static boolean ConstrucLivraison(Intersection inter, int i, int j) {
		Livraison liv = new Livraison(inter, i, j);
		if( (inter == liv.getPointLivraison()) &&
				(i == liv.getIdInPH()) &&
				(j == liv.getIdClient())
				){
			return true;
		} else {
			return false;
		}
	}

	public static boolean ConstrucIntersection(int id, int x, int y){
		Intersection inter = new Intersection(id,x,y);
		if( (x==inter.getX()) && (y==inter.getY()) && (id==inter.getId())){
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean ConstrucRoute(String nom, double vitesse, double longueur, Intersection inter1  ){
		Route route = new Route(nom, vitesse, longueur, inter1);
		if ( (nom == route.getName()) &&
				(vitesse== route.getVitesse()) &&
				(longueur==route.getLongueur()) &&
				(inter1 == route.getInter())
				){
			return true;
		} else {
			return false;
		}
	}
	
	
	public static boolean addTroncIntersection(Intersection inter, Route route){
		inter.addTroncSortant(route);
		if( (inter.getTroncsSortants().size() == 1) &&
				(inter.getTroncsSortants().get(0) == route)
				){
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean ConstrucEtape(Date heurePassage, Intersection adresse){
		Etape etape = new Etape(heurePassage, adresse);
		if( (heurePassage == etape.getHeurePassagePrevue()) &&
				(adresse == etape.getAdresse())
				){
			return true;
		} else {
			return false;
		}
	}
	
	
	
	
	public static void displayRoute(Route item){
		System.out.println(item);
		
	}
	
	public static void displayLivraison(Livraison item){
		
	}
	
	public static Document stringToDom(String xmlSource) 
            throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xmlSource)));
    }
	
	 public static String loadFile(File f) {
		    try {
		       BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
		       StringWriter out = new StringWriter();
		       int b;
		       while ((b=in.read()) != -1)
		           out.write(b);
		       out.flush();
		       out.close();
		       in.close();
		       return out.toString();
		    }
		    catch (IOException ie)
		    {
		         ie.printStackTrace(); 
		         return "";
		    }
		}

}
