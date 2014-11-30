/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package traitementgraphe;

import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Lilian
 */
public class Graphe implements Comparable{
    
    private Set<Arete> graphe;
    private String nom;
    private int score;
    
    Graphe(){
        graphe = new TreeSet<>();
    }
    
    Graphe(String pNom){
        graphe = new TreeSet<>();
        nom=pNom;
        score=0;
    }
    
    public void ajouterArete(Arete pArete){
        graphe.add(pArete);
    }
    
    public boolean isEmpty(){
        return graphe.isEmpty();
    }
    
    public String getNom(){
        return nom;
    }
    
    public double coefficientJaccard(Graphe pGraphe){
        Set<Arete> union = new TreeSet<>();
        Set<Arete> intersection = new TreeSet<>();
        union.addAll(graphe);
        union.addAll(pGraphe.graphe);
        intersection.addAll(graphe);
        intersection.retainAll(pGraphe.graphe);
        return (double) intersection.size()/union.size();
    }

    public void setScore(int pScore){
        score=pScore;
    }
    
    @Override
    public int compareTo(Object o) {
        Graphe g = (Graphe)o;
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
