package modele;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Livraison {
	
	/**
	 * Format d'heure correspondant � l'heure r�cup�rer via le doc xml
	 */
	private static final DateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm:ss");
   
	/**
	 * idLivraison est l'id global de Livraison
	 * Il est attribu� puis incr�ment� � chaque cr�ation de nouvelle livraison
	 * Il est remis � z�ro lors du chargement d'une nouvelle liste de livraisons
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
     * idLivraison est atribu� puis incr�ment�
     * @param inter
     * 		 : point de livraison (pointDeLivraison)
     * @param id
     * 		 : idInPh, id de la livraison dans la PlageHoraire concern�e
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
     * 		Etape : �tape de passage pr�vue
     */
    public Etape getEtape(){return this.etapePassagePrevue;}
    
   
    /**
     * V�rifie si apr�s calcul du parcours la livraison est toujours r�alisable dans sa plage horaire
     * @return boolean
     * 		true : r�alisable
     * 		false : non r�alisable dans cette plage horaire
     */
    public boolean isRealisable() {
        return etapePassagePrevue.getHeurePassagePrevue().before(plageHoraire.getHeureFin());
    }
  
    
    /** Renseigne le parm�tre heureDePassageEffective
     * @param heure
     * 		 : heure fournie, � formater suivant le format d�finit.
     */
    public void setHeureDePassageEffective(String heure) {
    	try{
    		this.heureDePassageEffective = HOUR_FORMAT.parse(heure);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    /** Renseigne le param�tre plageHoraire
     * @param ph
     * 		 : plage horaire � renseigner
     */
    public void setPlageHoraire(PlageHoraire ph){
    	this.plageHoraire = ph;
    }
    
    /** Renseigne le param�tre etape
     * @param etape
     * 		 : �tape � renseigner
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
     * Renvoie les donn�es de la livraison dans un String au format xml
     * @return 
     * 		String : String g�n�r�e
     */
    public String toStringXML() {
    	String res = "";
    	res += "<livraison id=\"" + this.id + "\" idClient=\"" + this.idClient + 
    			"\" idIntersection=\"" + this.PointDeLivraison.getId() + "\" />";
    	return res;
    }

}