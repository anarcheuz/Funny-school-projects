package controleur;

import modele.*;

/**
 * 
 */
public class CommandeAjout implements Commande {

	private FeuilleDeRoute feuilleDeRoute;
	private GrapheRoutier grapheRoutier;
	private Livraison livraison;
	private Livraison livraisonPrec;
	
    public CommandeAjout(Livraison l, Livraison lp, FeuilleDeRoute fdr, GrapheRoutier gR) {
    	this.feuilleDeRoute = fdr;
    	this.grapheRoutier = gR;
    	this.livraison = l;
    	this.livraisonPrec = lp;
    }
    

    @Override
    public void annuler() {
        this.feuilleDeRoute.supprimerLivraison(this.livraison, this.grapheRoutier);
    }

	@Override
	public void executer() {
		this.feuilleDeRoute.ajouterLivraison(this.livraison,this.livraisonPrec, this.grapheRoutier);
	}

}