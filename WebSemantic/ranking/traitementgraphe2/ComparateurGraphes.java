/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package traitementgraphe2;

import java.io.InputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author Lilian
 */
public class ComparateurGraphes {

    private Set<PaireGraphe> setGraphes;
    private Map<Graphe,List<Graphe>> mapGraphes;
    private Set<Graphe> scoresGraphes;
    private double similariteMax;

    ComparateurGraphes(){
        setGraphes = new TreeSet<>();
        mapGraphes = new TreeMap<>();
        scoresGraphes = new TreeSet<>();
        similariteMax = Double.NEGATIVE_INFINITY;
    }
    
    public void creerPaires(InputStream entree)
    {
        Scanner s = new Scanner(entree);
        String chaine;
        String[] decoupe;
        while(s.hasNextLine()){
            chaine = s.nextLine();
            decoupe = chaine.split(" ");
            if(decoupe.length>0){
                setGraphes.add(new PaireGraphe(Double.parseDouble(decoupe[1]),new Graphe(decoupe[0]),new Graphe(decoupe[2])));
            }
        }
    }
    
    public void grouper(double seuil){
        List<Graphe> listeCourante;
        
        for(PaireGraphe paire:setGraphes){
            if(paire.getJaccard()>similariteMax){
                similariteMax = paire.getJaccard();
            }
        }
        
        for(PaireGraphe paire : setGraphes){
            listeCourante = mapGraphes.get(paire.getGraphe1());
            if(listeCourante == null){
                listeCourante = new ArrayList<>();
            }
            if(paire.getJaccard()>=seuil){
                listeCourante.add(paire.getGraphe2());
            }
            mapGraphes.put(paire.getGraphe1(), listeCourante);
                
            listeCourante = mapGraphes.get(paire.getGraphe2());
            if(listeCourante == null){
                listeCourante = new ArrayList<>();
            }
            if(paire.getJaccard()>=seuil){
                listeCourante.add(paire.getGraphe1());
            }
            mapGraphes.put(paire.getGraphe2(), listeCourante);
        }
        for(Entry<Graphe,List<Graphe>> lEntry : mapGraphes.entrySet()){
            lEntry.getKey().setScore(lEntry.getValue().size());
            scoresGraphes.add(lEntry.getKey());
        }
        
    }
    
    public List<Graphe> getClassement(){
        List<Graphe> classement = new ArrayList<>();
        Set<Graphe> ensembleTaboo = new TreeSet<>();
        Set<Graphe> dejaTraites = new TreeSet<>();
        Set<Graphe> aTraiter = scoresGraphes;
        
        while(!(aTraiter.isEmpty())){
            aTraiter.removeAll(dejaTraites);
            ensembleTaboo.clear();
            for(Graphe g : aTraiter){
                if(!ensembleTaboo.contains(g))
                {
                    classement.add(g);
                    dejaTraites.add(g);
                    if(mapGraphes.get(g)!=null){
                        ensembleTaboo.addAll(mapGraphes.get(g));
                    }
                }
            }
        }        
        return classement;
    }

    public void afficher(PrintStream sortie){
        sortie.println("#DEBUT");
        DecimalFormat format = new DecimalFormat("#0.00");
        sortie.println(format.format(similariteMax));
        for(Graphe g: getClassement()){
            sortie.println(g.getNom());
        }
        sortie.println("#FIN");
    }

}
