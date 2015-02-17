package appcom;


import controleur.Controleur;

public class ServiceModele extends ServiceHandler {
	private Controleur controleur;
	
	public ServiceModele(Controleur controleur, String nomService, ServeurLivraison serveur) {
		super("modele/"+nomService, serveur);
		this.controleur = controleur;
	}
	
	protected Controleur getControleur(){
		return this.controleur;
	}

}