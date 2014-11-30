/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package traitementgraphe;

/**
 *
 * @author Lilian
 */
public class Noeud implements Comparable{
    
    private String nom;
    
    Noeud(String pNom){
        nom=pNom;
    }

    @Override
    public int compareTo(Object o) {
        Noeud n = (Noeud) o;
        if(this.nom.compareTo(n.nom)<0){
            return -1;
        }
        else if (this.nom.compareTo(n.nom)>0){
            return 1;
        }
        return 0;
            
    }
    
}
