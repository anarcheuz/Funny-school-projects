package modele;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Livraison {
	
	/**
	 * Format d'heure correspondant à l'heure récupérer via le doc xml
	 */
	private static final DateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm:ss");
   
	/**
	 * idLivraison est l'id global de Livraison
	 * Il est attribué puis incrémenté à chaque création de nouvelle livraison
	 * Il est remis à zéro lors du chargement d'une nouvelle liste de livraisons
	 * 
	 */
	private static int idLivraison = 0;
	private Date heureDePassageEffective;
    private Etape etapePassagePrevue;
    private Intersection PointDeLivraison;
	private int id;
	private int idInPH,idClient;
	private PlageHoraire plageHoraire;
	
    /**
     * Constructeur de Livraison
     * idLivraison est atribué puis incrémenté
     * @param inter
     * 		 : point de livraison (pointDeLivraison)
     * @param id
     * 		 : idInPh, id de la livraison dans la PlageHoraire concernée
     * @param idClient
     * 		 : id du client
     */
    public Livraison(Intersection inter, int id, int idClient) {
    	this.id = idLivraison++;
    	this.PointDeLivraison = inter;
    	this.idInPH = id;
    	this.idClient = idClient;
    }
    
    /** Getter sur id
     * @return 
     *		int : id de la livraison
     */
    public int getIdLiv(){return this.id;}
    /** Getter sur idInPh
     * @return 
     * 		int : id de la livraison au sein de sa plage horaire
     */
    public int getIdInPH(){return this.idInPH;}
    /** Getter sur pointDeLivraison
     * @return 
     * 		Intersection : point de livraison
     */
    public Intersection getPointLivraison(){return this.PointDeLivraison;}
    /** Getter sur idClient
     * @return 
     * 		int : id du client
     */
    public int getIdClient(){return this.idClient;}
    /** Getter sur plageHoraire
     * @return 
     * 		PlageHoraire : plage horaire sur laquelle la livraison doit s'effectuer
     */
    public PlageHoraire getPlageHoraire(){return this.plageHoraire;}
    /** Getter sur heureDePassageEffective
     * @return 
     * 		Date : heure De Passage Effective
     */
    public Date getHeureDePassage(){return this.heureDePassageEffective;}
    /** getter sur Etape
     * @return 
     * 		Etape : étape de passage prévue
     */
    public Etape getEtape(){return this.etapePassagePrevue;}
    
   
    /**
     * Vérifie si après calcul du parcours la livraison est toujours réalisable dans sa plage horaire
     * @return boolean
     * 		true : réalisable
     * 		false : non réalisable dans cette plage horaire
     */
    public boolean isRealisable() {
        return etapePassagePrevue.getHeurePassagePrevue().before(plageHoraire.getHeureFin());
    }
  
    
    /** Renseigne le parmètre heureDePassageEffective
     * @param heure
     * 		 : heure fournie, à formater suivant le format définit.
     */
    public void setHeureDePassageEffective(String heure) {
    	try{
    		this.heureDePassageEffective = HOUR_FORMAT.parse(heure);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    /** Renseigne le paramètre plageHoraire
     * @param ph
     * 		 : plage horaire à renseigner
     */
    public void setPlageHoraire(PlageHoraire ph){
    	this.plageHoraire = ph;
    }
    
    /** Renseigne le paramètre etape
     * @param etape
     * 		 : étape à renseigner
     */
    public void setEtapePassagePrevue(Etape etape){
        etapePassagePrevue = etape;
        etape.setLivraison(this);
    }
    
    public boolean display(PrintStream stream){
    	PointDeLivraison.display(stream);
    	plageHoraire.display(stream);
    	stream.print(" Client: "+idClient);
    	return true;
    }
    
    @Override
    public boolean equals(Object o){
        return((Livraison)o).id==this.id;
    }
    
    /** 
     * Renvoie les données de la livraison dans un String au format xml
     * @return 
     * 		String : String générée
     */
    public String toStringXML() {
    	String res = "";
    	res += "<livraison id=\"" + this.id + "\" idClient=\"" + this.idClient + 
    			"\" idIntersection=\"" + this.PointDeLivraison.getId() + "\" />";
    	return res;
    }

}