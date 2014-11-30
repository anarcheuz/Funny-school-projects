/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package traitementgraphe2;

/**
 *
 * @author Lilian
 */
public class PaireGraphe implements Comparable{
    
    private double jaccard;
    private Graphe graphe1;
    private Graphe graphe2;
    
    PaireGraphe(double pJaccard, Graphe pGraphe1, Graphe pGraphe2){
        jaccard=pJaccard;
        graphe1=pGraphe1;
        graphe2=pGraphe2;
    }

    @Override
    public int compareTo(Object o) {
        PaireGraphe paireComp = (PaireGraphe) o;
        if(this.jaccard<paireComp.jaccard){
            return 1;
        }
        else if (this.jaccard>paireComp.jaccard){
            return -1;
        }
        else{
            if(this.graphe1.getNom().equals(paireComp.graphe2.getNom())
               && this.graphe2.getNom().equals(paireComp.graphe1.getNom())){
                return 0;
            }
            if (this.graphe1.getNom().compareTo(paireComp.graphe1.getNom())!=0){
                return this.graphe1.getNom().compareTo(paireComp.graphe1.getNom());
            }
            else{
                return this.graphe2.getNom().compareTo(paireComp.graphe2.getNom());
            }
        }
    }
    
    @Override
    public String toString(){
        return graphe1.getNom()+" "+graphe2.getNom()+":"+jaccard;
    }
    
    public double getJaccard(){
        return jaccard;
    }
    
    public Graphe getGraphe1(){
        return graphe1;
    }
    
    public Graphe getGraphe2(){
        return graphe2;
    }
}
