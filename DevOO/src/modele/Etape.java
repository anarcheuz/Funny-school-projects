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
	 * @param heurePassage : l'heure de passage qui doit �tre associ�e � l'�tape
	 * @param adresse : l'intersection o� se trouve l'�tape
	 */
    public Etape(Date heurePassage, Intersection adresse){
    	this.adresse = adresse;
    	this.heureDePassage = heurePassage;
    	DateFormat formatHeure = new SimpleDateFormat("HH:mm:ss");        
        secondesAttenteAvantPassage=0;
        livraison = null;
    }

    

    /**
     * Getter sur l'heure de passage pr�vue � l'�tape
     * @return Date : l'heure � laquelle le passage � l'�tape est pr�vu
     */
    public Date getHeurePassagePrevue() {
        // TODO implement here
        return this.heureDePassage;
    }

    /**
     * Getter sur l'adresse de la livraison
     * @return Intersection : l'intersection � laquelle se trouve l'�tape
     */
    public Intersection getAdresse(){return this.adresse;}
    
	/**
	 * Setter de l'heure de passage pr�vue � l'�tape
	 * @param nouvelleHeure : la nouvelle heure de passage pr�vue � l'�tape
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
     * Getter sur le temps � attendre avant de pouvoir r�aliser l'�tape
     * @return int : le temps d'attente avant de r�aliser l'�tape
     */
    public int getAttenteAvantPassage() {
        return secondesAttenteAvantPassage;
    }

    /**
     * Setter du temps d'attente avant de pouvoir r�aliser l'�tape
     * @param attenteAvantPassage : nouveau temps � attendre avant de passer � l'�tape
     */
    public void setAttenteAvantPassage(int attenteAvantPassage) {
        this.secondesAttenteAvantPassage = attenteAvantPassage;
    }
    
    public boolean display(PrintStream stream){
    	stream.println(heureDePassage.toString());
    	return true;
    }
    
    /**
     * Permet d'obtenir une description XML de l'�tape
     * @return String : une cha�ne de caract�res contenant la description XML de l'�tape
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
	 * Permet d'obtenir l'identifiant de la plage horaire � laquelle l'�tape est associ�e
	 * @param plages : la liste des plages parmi lesquels l'�tape est recherch�e
	 * @return int : l'identifiant de la plage horaire associ� � l'�tape, -1 si il n'y en a aucune
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