/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package traitementgraphe2;

/**
 *
 * @author Lilian
 */
public class Graphe implements Comparable{
    
    private String nom;
    public int score;
    
    Graphe(String pNom){
        nom=pNom;
        score=0;
    }
  
    public String getNom(){
        return nom;
    }

    public void setScore(int pScore){
        score=pScore;
    }
    
    @Override
    public int compareTo(Object o) {
        Graphe g = (Graphe)o;

        if(this.nom.equals(g.nom)) {
            return 0;
        }
        if(this.score<g.score){
            return 1;
        }
        else if(this.score>g.score){
            return -1;
        }
        else{
            return this.nom.compareTo(g.nom);
        }
    }
    
    @Override
    public boolean equals(Object o){
        Graphe g = (Graphe) o;
        return g.nom.equals(this.nom);
    }
}
