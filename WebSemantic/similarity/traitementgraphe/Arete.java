/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package traitementgraphe;

/**
 *
 * @author Lilian
 */
public class Arete implements Comparable {
    
    private Noeud source;
    private Noeud destination;
    private String relation;
    
    Arete(Noeud pSource, Noeud pDestination, String pRelation){  
        source=pSource;
        destination=pDestination;
        relation=pRelation;
    }

    @Override
    public int compareTo(Object o) {
        Arete a = (Arete) o;
        int comparaison = this.source.compareTo(a.source);
        if(comparaison == 0) {
            comparaison = this.destination.compareTo(a.destination);
        }
        if(comparaison == 0){
            comparaison = this.relation.compareTo(a.relation);
        }
        return comparaison;
    }
}
