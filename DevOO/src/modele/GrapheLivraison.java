package modele;

import java.io.PrintStream;
import java.util.*;
import modele.GrapheRoutier.*;

import tsp.TSP;

/**
 * 
 */
public class GrapheLivraison implements Graph {

	int maxArcCost;
	int minArcCost;
	int nbSommets;
	int[][] matriceAdjacence;
	List<Livraison> livraisons;
	
	/**
	 * Constructeur de GrapheLivraison
	 */
	public GrapheLivraison(){
		livraisons = new ArrayList<Livraison>();
	}
	
	/**
	 * Constructeur de GrapheLivraison
	 * @param matriceAdjacence : Matrice contenant les couts des plus courts chemins entre chaque livraison. Le depet doit etre a l'indice 0.
	 * @param lLivraisons : Liste des livraisons pour la tournee a calculer, ordonnee selon le meme ordre que les colonnes de la matrice d'adjacence.
	 */
	public GrapheLivraison(int[][] matriceAdjacence, List<Livraison> lLivraisons){
		this.nbSommets = matriceAdjacence[0].length;
		this.matriceAdjacence=matriceAdjacence;
		
		int coutMinimum = Integer.MAX_VALUE;
		int coutMaximum = Integer.MIN_VALUE;
		int coutCourant;
		
		for(int i=0; i<nbSommets;i++){
			for(int j=0;j<nbSommets;j++){
				coutCourant=matriceAdjacence[i][j];
				if(coutCourant<coutMinimum && coutCourant!= -1 ){
					coutMinimum=coutCourant;
				}
				if(coutCourant>coutMaximum){
					coutMaximum=coutCourant;
				}
			}
		}
		maxArcCost = coutMaximum;
		minArcCost = coutMinimum;
		livraisons = lLivraisons;
	}
    
	/**
	 * Mï¿½thode de calcul de l'ordre des livraisons en fonction des chemins entre elles
	 * @return List Livraison : La liste ordonnï¿½e des livraisons si la tournee specifiee est realisable, null sinon
	 */
    public List<Livraison> calculerOrdreLivraisons() {
    	List<Livraison> lListe = new ArrayList<>();
    	int[] solution;
    	TSP resolveur = new TSP(this);
    	resolveur.solve(Integer.MAX_VALUE, Integer.MAX_VALUE);
    	try{
    		solution = resolveur.getNext();
    	}
    	catch (NullPointerException e){
    		return null;
    	}
    	
    	int livraisonCourante = solution[0];
    	while(livraisonCourante != 0){
    		lListe.add(livraisons.get(livraisonCourante-1));
    		livraisonCourante=solution[livraisonCourante];
    	}

    	return lListe;
    }
    
	@Override
	public int getMaxArcCost() {
		return maxArcCost;
	}

	@Override
	public int getMinArcCost() {
		return minArcCost;
	}

	@Override
	public int getNbVertices() {
		return nbSommets;
	}

	@Override
	public int[][] getCost() {
		int[][] retour = matriceAdjacence;
		for(int i=0;i<nbSommets;i++){
			for(int j=0;j<nbSommets;j++){
				if(retour[i][j]==-1) retour[i][j]=maxArcCost+1;
			}
		}
		return retour;
	}

	@Override
	public int[] getSucc(int i) throws ArrayIndexOutOfBoundsException {
		List<Integer> liste = new ArrayList<>();
		for(int j=0;j<nbSommets;j++){
			if(matriceAdjacence[i][j]!=-1) liste.add(j);
		}
		int[] retour = new int[liste.size()];
		int k=0;
		for(Integer entier : liste){
			retour[k++]=entier;
		}
		return retour;
	}

	@Override
	public int getNbSucc(int i) throws ArrayIndexOutOfBoundsException {
		List<Integer> liste = new ArrayList<>();
		for(int j=0;j<nbSommets;j++){
			if(matriceAdjacence[i][j]!=-1) {liste.add(j);}
		}
		return liste.size();
	}
	
	public boolean display(PrintStream stream){
		/*
			int maxArcCost;
			int minArcCost;
			int nbSommets;
			int[][] matriceAdjacence;
			List<Livraison> livraisons;
		 */
		
		stream.print("Nb Sommets :" + nbSommets + " MinArcCost : "+ minArcCost+ " MaxArcCost : "+maxArcCost);
		stream.println("Livraisons :");
		
		for(int i=0; i<livraisons.size();i++){
			livraisons.get(i).display(stream);
		}
		
		stream.println("Matric Adjacence :");
		
		for(int i=0; i<matriceAdjacence.length;i++){
			for(int j = 0; j < matriceAdjacence[i].length;j++){
				stream.print(matriceAdjacence[i][j]+ " ");
			}
			stream.println("");
		}
		
		return true;
	}

	/**
	 * Permet d'obtenir une livraison par son identifiant
	 * @param idLivraison : identifiant de la livraison à retrouver
	 * @return Livraison : la livraison correspondant à idLivraison
	 */
	public Livraison getLivraison(int idLivraison) {
		System.err.println("getLivraison :"+idLivraison+ " | livraisons" +this.livraisons );
		Livraison next = null;
		Iterator<Livraison> it = this.livraisons.iterator();
		while (it.hasNext()) {
			next = it.next();
			System.err.println("next > "+next);
			if (next.getIdLiv() == idLivraison) {
				return next;
			}
		}
		return null;
	}
	
	public List<Livraison> getLivraisons(){
		return this.livraisons;
	}
    
}