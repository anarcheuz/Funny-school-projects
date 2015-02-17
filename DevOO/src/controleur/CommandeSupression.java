package controleur;

import java.util.*;

import modele.*;

/**
 * 
 */
public class CommandeSupression implements Commande {

	private FeuilleDeRoute feuilleDeRoute;
	private Livraison livraison;
	private Livraison livraisonPrecedente;
	private GrapheRoutier grapheRoutier;
    
    public CommandeSupression(Livraison l, GrapheRoutier gR, FeuilleDeRoute fdr) {
    	this.feuilleDeRoute = fdr;
    	this.livraison = l;
    	this.grapheRoutier = gR;
    	List<Integer>posListe = this.feuilleDeRoute.trouverPrecedent(livraison);
    	Etape etapePreced = this.feuilleDeRoute.getItineraire().get(posListe.get(0));
    	for(PlageHoraire pH : this.feuilleDeRoute.getPlagesHoraires()){
    		for(Livraison liv : pH.getListeLivraison()){
    			if(liv.getEtape()==etapePreced){
    				this.livraisonPrecedente = liv;
    			}
    		}
    	}
    }

    
    @Override
    public void annuler() {
        this.feuilleDeRoute.ajouterLivraison(this.livraison, this.livraisonPrecedente, this.grapheRoutier);
    }

	@Override
	public void executer() {
		this.feuilleDeRoute.supprimerLivraison(this.livraison, this.grapheRoutier);
	}

    

}