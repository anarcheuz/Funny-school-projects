package modele;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class PlageHoraire {
	
	/**
	 * idPhs est l'id global des PlageHoraire.
	 * Il est attribu� puis incr�ment� � chaque cr�ation de plage horaire
	 * Il est remis � z�ro lors du chargement d'une nouvelle liste de livraison
	 */
	protected static int idPhs = 0;
	private int id;
	private Date heureDebut;
	private Date heureFin;
	private List<Livraison> listeLivraisons;
	
	/**
	 * Constructeur de PlageHoraire
	 * On prend les 2 heures de d�but et de fin en param�tre, l'id est attribu�, 
	 * listeLivraison, qui correspond � toutes les livraisons de cette plage horaire est initialis�e
	 * @param heureDeb
	 * 		 : heure de d�but
	 * @param heureFin
	 * 		 : heure de fin 
	 */
	public PlageHoraire(Date heureDeb, Date heureFin){
		this.id = idPhs++;
		this.listeLivraisons = new ArrayList<Livraison>();
		this.heureDebut = heureDeb;
		this.heureFin = heureFin;
				
	}
	
	
	/**
	 * getter sur heureDebut
	 * @return 
	 * 		Date : heure de d�but
	 */
	public Date getHeureDebut(){return this.heureDebut;}
	/** getter sur heureFin
	 * @return 
	 * 		Date : heure de fin
	 */
	public Date getHeureFin(){return this.heureFin;}
	/** getter sur listeLivraison
	 * @return 
	 * 		List <{@link Livraison}> : liste de livraison 
	 */
	public List<Livraison> getListeLivraison(){return this.listeLivraisons;}
	/** getter sur id
	 * @return 
	 * 		int : id de la plage horaire
	 */
	public int getIdPlageHoraire(){return this.id;}
	
	/**
	 * Ajoute la livraison pass�e en param�tre � listeLivraison
	 * On en profite pour renseigner l'attribut PlageHoraire de la livraison concern�e 
	 * @param liv 
	 * 		 : livraison � ajouter
	 */
	public void addLivraison(Livraison liv){
		this.listeLivraisons.add(liv);
        liv.setPlageHoraire(this);
	}
	
	/**
	 * Ajoute la livraison pass�e en param�tre � listeLivraison
	 * On en profite pour renseigner l'attribut PlageHoraire de la livraison concern�e 
	 * @param l 
	 * 		 : livraison � ajouter
	 * @param pre
	 * 		: livraison qui doit pr�c�der l
	 */
	public void addLivraison(Livraison pre, Livraison l){
		this.listeLivraisons.add(this.listeLivraisons.indexOf(pre), l);
	}
	
	/**
	 * Supprime la livraison pass�e en param�tre de listeLivraison
	 * @param liv
	 * 		 : livraison � supprimer
	 */
	public void deleteLivraison(Livraison liv){
		this.listeLivraisons.remove(liv);
	}

	public boolean display(PrintStream stream) {
		stream.print(heureDebut+ " "+ heureFin);
		for(int i = 0; i<listeLivraisons.size(); i++){
    		listeLivraisons.get(i).display(stream);
                }
		return true;
	}

    @Override
    public boolean equals(Object o){
    	PlageHoraire p = (PlageHoraire)o;    
    	return(this.heureDebut.equals(p.heureDebut)&&this.heureFin.equals(p.heureFin));    
    }
    
    /**
     * Renvoie les donn�es d'une plage horaire dans un String au format xml
     * @return  
     * 		String : string g�n�r�e
     */
    @SuppressWarnings("deprecation")
    public String toStringXML() {
    	String res = "";
    	
    	String minutes = String.format("%02d", this.heureDebut.getMinutes());
		String heureDebut = this.heureDebut.getHours() + "h" + minutes ;
		
		minutes = String.format("%02d", this.heureFin.getMinutes());
		String heureFin = this.heureFin.getHours() + "h" + minutes ;
    	
    	res += "<plage debut=\"" + heureDebut + "\" fin=\"" + heureFin + "\" >";
    	Iterator<Livraison> it = this.listeLivraisons.iterator();
    	while( it.hasNext() ){
			res += it.next().toStringXML();
		}
    	res += "</plage>";
    	return res;
    }
    
    public String getPlageXML() {
    	String res = "";
    	
    	String minutes = String.format("%02d", this.heureDebut.getMinutes());
		String heureDebut = this.heureDebut.getHours() + "h" + minutes ;
		
		minutes = String.format("%02d", this.heureFin.getMinutes());
		String heureFin = this.heureFin.getHours() + "h" + minutes ;
    	
    	res += "<plage id=\"" + this.getIdPlageHoraire() + "\" debut=\"" + heureDebut + 
    			"\" fin=\"" + heureFin + "\" />";
    	return res;
    }
        
}
