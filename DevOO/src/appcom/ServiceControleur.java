package appcom;

import controleur.Controleur;

public class ServiceControleur extends ServiceHandler {

	private Controleur controleur;
	
	public ServiceControleur(Controleur controleur, String nomService, ServeurLivraison serveur) {
		super("controleur/"+nomService, serveur);
		this.controleur = controleur;
	}
	
	protected Controleur getControleur(){
		return this.controleur;
	}

}
