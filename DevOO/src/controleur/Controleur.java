package controleur;
import java.util.List;
import java.util.Stack;

import org.w3c.dom.*;

import modele.*;

public class Controleur {
	
	/**
	 * Attribut de classe DateFormat permettant le formatage de l'heure � partir d'une String
	 */
	
	
	private GrapheRoutier grapheRoutier;
	private FeuilleDeRoute feuilledeRoute;
	private Stack<Commande> listeFaits;
	private Stack<Commande> listeAnnules;
	/**
	 * Constructeur de Controleur
	 * Initialisation du graphe routier et de la feuille de route
	 */
	 public Controleur(){
		grapheRoutier = new GrapheRoutier();
		feuilledeRoute = new FeuilleDeRoute();
		listeFaits = new Stack<Commande>();
		listeAnnules = new Stack<Commande>();
	}
	
	
	/**
	 * Remise � z�ro de la feuille de route
	 * Appelle � la m�thode pour charger le document pass� en param�tre
	 * @param livDoc
	 * 		: document � parser
	 * @return boolean
	 * 		 true si la feuille de route est correctement g�n�r�e //
	 * 		 false si la feuille est incorrecte
	 */
	public boolean chargerLivraisons(Document livDoc){
		
		feuilledeRoute.clean();
		feuilledeRoute = ChargerLivraisons.chargerLivraisons(livDoc, grapheRoutier);
		if(feuilledeRoute.getPlagesHoraires().size() == 0){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Remise � z�ro du graphe routier
	 * Appelle � la m�thode pour charger le document pass� en param�tre
	 * @param plan
	 * 		: document � parser
	 * @return boolean
	 * 		 true si le graphe routier est correctement g�n�r� //
	 * 		 false si le graphe routier incorrecte
	 */
	public boolean chargerPlan(Document plan){
		grapheRoutier.clean();
		feuilledeRoute.clean();
		grapheRoutier = ChargerPlan.chargerPlan(plan);
		if(grapheRoutier.getListeIntersections().size() == 0){
			return false;
		}else{
			return true;
		}
	}

	
	
	/**
	 * getter sur grapheRoutier
	 * @return {@link GrapheRoutier} : graphe routier du controleur 
	 * 		
	 */
	public GrapheRoutier getGrapheRoutier() {
		return this.grapheRoutier;
		}
	
	
	
	/** getter de la feuille de route contenant les PlageHoraire de livraisons
	 * @return {@link FeuilleDeRoute} : feuille de route du controleur
	 */
	public FeuilleDeRoute getFeuilleDeRoute() {
		return this.feuilledeRoute;
		}
	
	
	
	/**
	 * Ajoute une livraison et l'enregistre dans la pile pour undo/redo
	 * @param idIntersection : id de l'intersection de l'adresse de livraison
	 * @param idClient : id du client de la livraison
	 * @param idLivraisonPrecedente : id de la livraison effectu� avant celle ajout�e
	 */
	public void ajouterLivraison(int idIntersection, int idClient, int idLivraisonPrecedente) {
		System.out.println("debut controleur");
		//Livraison precedente = this.getFeuilleDeRoute().getGrapheLivraison().getLivraison(idLivraisonPrecedente);
		Livraison precedente = null;
		for(PlageHoraire pH : this.feuilledeRoute.getPlagesHoraires()){
			for(Livraison l : pH.getListeLivraison()){
				System.err.println("livraison > "+l.getIdLiv());
				if(l.getIdLiv()==idLivraisonPrecedente){
					precedente=l;
				}
			}
		}
		if( precedente == null ){
			System.err.println("Precedente n'existe pas...");
			return;
		}
		System.err.println("precedente : "+precedente.getIdLiv());
		Intersection inter = this.grapheRoutier.getIntersection(idIntersection);
		
		List<Livraison> lI = precedente.getPlageHoraire().getListeLivraison();
		
		
		//creation de la commandeAjout
		Livraison nouvelle = new Livraison(inter, lI.size()+1 , idClient);
		nouvelle.setPlageHoraire(precedente.getPlageHoraire());
		CommandeAjout c = new CommandeAjout(nouvelle, precedente, this.getFeuilleDeRoute(), this.grapheRoutier);
		
		c.executer();
		this.listeFaits.push(c);
		this.listeAnnules.clear(); // supprime les actions annulees
	}
	
	/**
	 * Supprime la livraison concernee
	 * @param idLivraison : id de la livraison a supprimer
	 */
	public void supprimerLivraison(int idLivraison) {
		Livraison livraison = this.getFeuilleDeRoute().getGrapheLivraison().getLivraison(idLivraison);
		for(PlageHoraire pH : this.feuilledeRoute.getPlagesHoraires()){
			for(Livraison l : pH.getListeLivraison()){
				if(l.getIdLiv()==idLivraison){
					livraison=l;
				}
			}
		}
		CommandeSupression c = new CommandeSupression(livraison, this.grapheRoutier, this.feuilledeRoute);
		c.executer();
		this.listeFaits.push(c);
		this.listeAnnules.clear(); // supprime les actions annulees
	}
	
	/**
	 * annule l'ajout/suppression et met a jour les piles
	 */
	public boolean annuler() {
		if(listeFaits.isEmpty()){
			return false;
		}
		Commande c = this.listeFaits.pop();
		c.annuler();
		this.listeAnnules.push(c); // met sur la pile des actions annullees
		return true;
	}
	
	/**
	 * Retabli l'annulation et met a jour les piles
	 */
	public boolean retablir() {
		if(listeAnnules.isEmpty()){
			return false;
		}		
		Commande c = this.listeAnnules.pop();
		c.executer();
		this.listeFaits.push(c); // remet sur la pile des actions faites
		return true;
	}
}