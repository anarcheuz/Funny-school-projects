package modele;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 */
public class Etape {
	
	private Date heureDePassage;
    private int secondesAttenteAvantPassage;
	private Intersection adresse;
	private Livraison livraison;

	/**
	 * Constructeur d'Etape    
	 * @param heurePassage : l'heure de passage qui doit être associée à l'étape
	 * @param adresse : l'intersection où se trouve l'étape
	 */
    public Etape(Date heurePassage, Intersection adresse){
    	this.adresse = adresse;
    	this.heureDePassage = heurePassage;
    	DateFormat formatHeure = new SimpleDateFormat("HH:mm:ss");        
        secondesAttenteAvantPassage=0;
        livraison = null;
    }

    

    /**
     * Getter sur l'heure de passage prévue à l'étape
     * @return Date : l'heure à laquelle le passage à l'étape est prévu
     */
    public Date getHeurePassagePrevue() {
        // TODO implement here
        return this.heureDePassage;
    }

    /**
     * Getter sur l'adresse de la livraison
     * @return Intersection : l'intersection à laquelle se trouve l'étape
     */
    public Intersection getAdresse(){return this.adresse;}
    
	/**
	 * Setter de l'heure de passage prévue à l'étape
	 * @param nouvelleHeure : la nouvelle heure de passage prévue à l'étape
	 */
    public void setHeurePassagePrevue(Date nouvelleHeure) {
    	this.heureDePassage = nouvelleHeure;
    }
    
    public Livraison getLivraison() {
		return livraison;
	}



	public void setLivraison(Livraison livraison) {
		this.livraison = livraison;
	}
	
	public boolean hasLivraison(){
		return livraison != null;
	}


	/**
     * Getter sur le temps à attendre avant de pouvoir réaliser l'étape
     * @return int : le temps d'attente avant de réaliser l'étape
     */
    public int getAttenteAvantPassage() {
        return secondesAttenteAvantPassage;
    }

    /**
     * Setter du temps d'attente avant de pouvoir réaliser l'étape
     * @param attenteAvantPassage : nouveau temps à attendre avant de passer à l'étape
     */
    public void setAttenteAvantPassage(int attenteAvantPassage) {
        this.secondesAttenteAvantPassage = attenteAvantPassage;
    }
    
    public boolean display(PrintStream stream){
    	stream.println(heureDePassage.toString());
    	return true;
    }
    
    /**
     * Permet d'obtenir une description XML de l'étape
     * @return String : une chaîne de caractères contenant la description XML de l'étape
     */
	public String toStringXML(List<PlageHoraire> plages){
		String res = "";
		String minutes = String.format("%02d", this.heureDePassage.getMinutes());
		String heure = heureDePassage.getHours() + "h" + minutes ;
		
		res += "<etape heurePassage=\""+heure+"\" secondesAttente=\""+ 
				this.secondesAttenteAvantPassage +"\" idIntersection=\"" +
				adresse.getId() + "\" idPlageHoraire=\"" + 
				getPlage(plages) + "\">";
		if(livraison != null){
			res+="<livraison idClient=\""+livraison.getIdClient() +"\" adresse=\""+adresse.getId() +"\" />";
		}		
		res+= "</etape>";
		return res;
	}
	
	/**
	 * Permet d'obtenir l'identifiant de la plage horaire à laquelle l'étape est associée
	 * @param plages : la liste des plages parmi lesquels l'étape est recherchée
	 * @return int : l'identifiant de la plage horaire associé à l'étape, -1 si il n'y en a aucune
	 */
	public int getPlage(List<PlageHoraire> plages) {
		for (PlageHoraire p: plages) {
                    if((heureDePassage.equals(p.getHeureDebut())||heureDePassage.after(p.getHeureDebut()))
                            &&heureDePassage.before(p.getHeureFin())){
                        return p.getIdPlageHoraire();
                    }
                        
		}
		return -1;
	}
	
	@Override
	public boolean equals(Object o){
		Etape e = (Etape) o;
		return e.adresse.equals(this.adresse) && e.heureDePassage.equals(this.heureDePassage);
	}
}