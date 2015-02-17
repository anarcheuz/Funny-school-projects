package modele;

import java.io.PrintStream;


public class Route{
	
	/**
	 * idRoutes est l'id global de Route
	 * Il est attribué puis incrémenté à chaque création de nouvelle route
	 * Il est remis à zéro lors du chargement d'un nouveau plan
	 */
	protected static int idRoutes =0;
	
	private String name;
	private double vitesse,longueur;
	private int id;
	private Intersection inter;
	
	/**
	 * Constructeur de Route
	 * idRoutes est attribué puis incrémenté.
	 * @param nom
	 * 		 : nom de la route
	 * @param vitesse
	 * 		 : vitesse sur cette route
	 * @param longueur
	 * 		 : longueur de la route
	 * @param inter
	 * 		 : destination de la route
	 */
	public Route(String nom, double vitesse, double longueur, Intersection inter){
		this.name = nom;
		this.vitesse = vitesse;
		this.longueur = longueur;
		this.id = idRoutes++;
		this.inter = inter;
	}
	
	
	
	/** Getter sur id
	 * @return 
	 * 		Int : id de la route
	 */
	public int getId(){return this.id;}
	
	/** Getter sur nom
	 * @return 
	 *		String : nom de la route
	 */
	public String getName(){return this.name;}
	
	/** Getter sur vitesse
	 * @return 
	 * 		double : vitesse de la route
	 */
	public double getVitesse(){return this.vitesse;}
	
	/** Getter sur longueur
	 * @return 
	 *  	double : longueur de la route
	 */
	public double getLongueur(){return this.longueur;}
        
    /** Getter et calcul du temps de parcours d'une route
     * @return 
     * 		double : résultat de longueur/vitesse
     */
    public double getTempsParcours(){
        return longueur/vitesse;
    }
	
	/** Getter sur inter
	 * @return 
	 * 		Intersection : destination de la route
	 */
	public Intersection getInter(){return this.inter;}


	public boolean display(PrintStream stream) {
		stream.println(name+" id : "+id +" vitesse : "+vitesse+" long : "+longueur+ " -> " + inter);
		return true;		
	}
	
	@Override
	public String toString(){
		return toStringXML();
	}
	
	/**
     * Renvoie les données de la route dans un String au format xml
     * @return  
     * 		String : string générée
     */
	public String toStringXML(){
		String res = "";
		res += "<route id=\"" + this.id + "\" idDestination=\"" + this.inter.getId() + "\" "
				+ "nom=\""+ this.name + "\" />";
		return res;
	}
}
