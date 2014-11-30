/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package traitementgraphe;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Lilian
 */
public class ComparateurGraphes {

    private List<Graphe> listeGraphes;
    private Set<PaireGraphe> setGraphes;
    //Groupement: utiliser une map de graphes sur la liste des graphes avec lequel il groupe.
    //Classement: pour chaque Graphe du classement de base, virer ceux qui sont de son groupe du classement.

    ComparateurGraphes(){
        listeGraphes = new ArrayList<>();
        setGraphes = new TreeSet<>();
    }

    public void parserGraphes(InputStream entree){
        Graphe grapheCourant = new Graphe();
        Arete areteCourante;
        Noeud sourceCourante = null;
        Noeud destinationCourante = null;
        int partieCourante=-1;

        Scanner s = new Scanner(entree);
        String chaine;
        while(s.hasNextLine()){
            chaine = s.nextLine();
            if(chaine.isEmpty()||partieCourante==-1){
                partieCourante=0;
            }
            
            if(!chaine.isEmpty()){
                if(partieCourante==0){
                    if(!grapheCourant.isEmpty()){
                        listeGraphes.add(grapheCourant);
                    }
                    grapheCourant=new Graphe(chaine);
                    partieCourante++;
                }
                else if(partieCourante==1){
                    sourceCourante = new Noeud(chaine);
                    partieCourante++;
                }
                else if(partieCourante==2){
                    destinationCourante = new Noeud(chaine);
                    partieCourante++;
                }
                else if(partieCourante==3){
                    areteCourante = new Arete(sourceCourante,destinationCourante,chaine);
                    partieCourante=1;
                    grapheCourant.ajouterArete(areteCourante);
                }
            }
        }
        if(!grapheCourant.isEmpty()){
            listeGraphes.add(grapheCourant);
        }
    }

    public void creerPaires(){
        for(int i =0; i<listeGraphes.size()-1;i++){
            for(int j=i+1; j<listeGraphes.size();j++){
                setGraphes.add(new PaireGraphe(listeGraphes.get(i).coefficientJaccard(listeGraphes.get(j)),
                                                listeGraphes.get(i),listeGraphes.get(j)));
            }
        }
    }

    public void afficher(PrintStream sortie){
            for(PaireGraphe p: setGraphes){
                sortie.println(p.getGraphe1().getNom()+" "+p.getJaccard()+" "+p.getGraphe2().getNom());
            }
        }

}
