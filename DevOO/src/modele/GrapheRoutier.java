package modele;

import java.io.PrintStream;
import java.util.*;

/**
 * 
 */
public class GrapheRoutier {
	
    private List<Intersection> listeIntersection;
    private List<Object[]> listePlusCourtsChemins;
    
    //
    private class Noeud implements Comparable{
        private double coutAcces;
        private Intersection intersection;
        private Noeud origine;
        
        public Noeud(Intersection pIntersection, Noeud pOrigine, double pCoutAcces){
            coutAcces=pCoutAcces;
            intersection = pIntersection;
            origine = pOrigine;
        }
        
        public void setOrigine(Noeud pOrigine){
            origine = pOrigine;
        }

        @Override
        public int compareTo(Object o) {
            Noeud n = (Noeud) o;
            if(this.equals(n)) {
                return 0;
            }
            if(this.coutAcces!=n.coutAcces) {
                return ((Double)this.coutAcces).compareTo(n.coutAcces);
            }
            else{
                return ((Integer)this.intersection.getId()).compareTo(n.intersection.getId());
            }
        }
        
        @Override
        public boolean equals(Object o){
            return this.intersection.equals(((Noeud)o).intersection);
        }
    }

    /**
     * Constructeur par défaut du graphe routier
     */
    public GrapheRoutier() {
    	this.listeIntersection = new ArrayList<Intersection>();
        listePlusCourtsChemins = new ArrayList<>();        
    }
    
    /**
     * Getter sur la liste des intersections
     * @return List Intersection : la liste des intersections du graphe routier
     */
    public List<Intersection> getListeIntersections(){
    	return this.listeIntersection;
    }
    
    /**
     * Vide le graphe routier de toutes ses intersections
     */
    public void clean(){
    	this.listeIntersection.clear();
    	Route.idRoutes = 0;
    }
    
    /**
     * Ajoute l'intersection donnée au graphe routier
     * @param inter : l'intersection à ajouter au graphe routier
     */
    public void ajouterIntersection(Intersection inter){
    	this.listeIntersection.add(inter.getId(),inter);
    }
    
    /**
     * Permet de savoir si une intersection avec un identifiant donné existe
     * @param idInter : l'identifiant de l'intersection pour laquelle on veut savoir si elle existe
     * @return boolean : vaut true si et seulement si l'intersection existe dans le graphe
     */
    public boolean interExiste(int idInter){
    	for(int i = 0; i<listeIntersection.size(); i++){
    		if(idInter == listeIntersection.get(i).getId()){
    			return true;
    		}
    	}
    	return false;
    }
    
    
    public boolean display(PrintStream stream){
    	for(int i = 0; i<listeIntersection.size(); i++){
    		listeIntersection.get(i).display(stream);
    	}
    	return true;
    }
    
    /**
     * 
     * @param depart : L'intersection dont on veut connaitre la route sortante
     * @param arrivee : L'intersection dont on veut connaitre la route entrante
     * @return Route : La route allant de départ à arrivée
     */
    public Route getRoute(Intersection depart, Intersection arrivee){
        for(Route r:depart.getTroncsSortants()){
            if(r.getInter().equals(arrivee)){
                return r;
            }
        }
        return null;
    } 
    
   /**
    * Calcul le plus court chemin entre le depart et l'arrivée donnés
    * @param depart Intersection : Intersection correspondant au point de départ du chemin calculé
    * @param arrivee : Intersection correspondant au point d'arrivée du chemin calculé
    * @return Object[] : Tableau contenant la liste ordonnée des routes à suivre pour atteindre l'arrivée depuis le départ avec un cout minimal et le cout de la solution, null si aucune solution
    */
     public Object[] calculerPlusCourtChemin(Intersection depart, Intersection arrivee) {

        Noeud noeudCourant = new Noeud(depart,null,0);
        Noeud successeurCourant;
        PriorityQueue<Noeud> frontiere = new PriorityQueue<>();
        frontiere.add(noeudCourant);
        List<Noeud> dejaExplores = new ArrayList<>();
        List<Intersection> solution = new ArrayList<>();
        Object[] retour = new Object[2];
        
        for(Object[] chemin : listePlusCourtsChemins){
            solution = (List) chemin[0];
            if(solution.get(0).equals(depart)&&solution.get(solution.size()-1).equals(arrivee)){
                return chemin;
            }
        }
        
        solution.clear();
        
        for(;;){
            if(frontiere.isEmpty()) {
                return null;
            }
            noeudCourant= frontiere.poll();
            if(noeudCourant.intersection.equals(arrivee)){
                retour[1]= noeudCourant.coutAcces;
                while(!noeudCourant.intersection.equals(depart)){
                    solution.add(noeudCourant.intersection);
                    noeudCourant=noeudCourant.origine;
                }
                solution.add(noeudCourant.intersection);
                Collections.reverse(solution);
                retour[0]=solution;
                listePlusCourtsChemins.add(retour);
                return retour;
            }
            dejaExplores.add(noeudCourant);
            for(Route r : noeudCourant.intersection.getTroncsSortants()){
                successeurCourant = new Noeud(r.getInter(),noeudCourant,noeudCourant.coutAcces+r.getTempsParcours());
                if(!dejaExplores.contains(successeurCourant)){
                    if(!frontiere.contains(successeurCourant)){
                        frontiere.add(successeurCourant);
                    }
                    else{
                        PriorityQueue<Noeud> frontiereParcours= new PriorityQueue<>(frontiere);
                        for(Noeud n:frontiereParcours){
                            if (n.equals(successeurCourant) && n.coutAcces>successeurCourant.coutAcces){
                                frontiere.remove(n);
                                frontiere.add(successeurCourant);
                            }
                        }
                    }
                }
            }
        }
        
    }
     
     /**
      * Permet d'obtenir la description XML du graphe
      * @return String : une chaîne de caractères contenant la description XML complète du graphe routier
      */
     public String getPlanXML(){
    	String res = "";
 		res += "<plan>";
 		Iterator<Intersection> it = this.listeIntersection.iterator();
 		while( it.hasNext() ){
 			res += it.next().toStringXML();
 		}
 		res += "</plan>";
 		return res;
     }
     
     /**
      * Permet d'obtenir l'intersection ayant l'id donné
      * @param idIntersection : identifiant de l'intersection à retrouver
      * @return Intersection : l'intersection correspondant à idIntersection
      */
     public Intersection getIntersection(int idIntersection) {
    	 Iterator<Intersection> it = this.listeIntersection.iterator();
    	 while (it.hasNext()) {
    		 Intersection inter = it.next();
    		 if (idIntersection == inter.getId()) {
    			 return inter;
    		 }
    	 }
    	 
    	 return null;
     }

}