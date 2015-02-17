package appcom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import controleur.Controleur;

public class ServeurBuilder {

	private ServeurLivraison serveur;
	
	private ServeurBuilder(ServeurLivraison s){
		this.serveur = s;
	}
	
	public static ServeurBuilder nouveauServeur(){
		ServeurLivraison serveur = new ServeurLivraison();
		ServeurBuilder sb = new ServeurBuilder(serveur);
		serveur.Demarrer();
		return sb;
	}
	
	/**
	 * methodes pas ï¿½ jour car format de sortie non specifie
	 * @param c
	 * @return
	 */
	public ServeurBuilder deployerServicesControleur(Controleur c){
		
		new ServiceControleur(c,"charger-plan",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						
						if (this.getControleur().chargerPlan(doc)) {
							return Reponse.succes("Le plan a bien ï¿½tï¿½ chargï¿½.");
						} else {
							return Reponse.erreur("Le service de chargement du plan a ï¿½chouï¿½.");
						}
						
					} catch (SAXException e) {
						e.printStackTrace();
						return Reponse.erreur("Woops,\nnous n'avons pas pu interprï¿½ter le fichier transmis.\n"
								+ "Veuillez vous assurer qu'il ne contient aucune erreur.");
					} catch (IOException e) {
						e.printStackTrace();
						return Reponse.erreur("Erreur lors de la lecture du fichier.");
					}
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					return Reponse.erreur("Problï¿½me d'initialisation dans la gestion du service");
				}
			}
		};
		
		new ServiceControleur(c,"charger-livraisons",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
			
						if (this.getControleur().chargerLivraisons(doc)) {
							return Reponse.succes("Les livraisons ont ï¿½tï¿½ chargï¿½es.");
						} else {
							return Reponse.erreur("Le service de chargement des livraisons a échoué. Peut être n'avez vous pas chargé de plan ?");	
						}
						
					} catch (SAXException e) {
						e.printStackTrace();
						return Reponse.erreur("Woops,\nnous n'avons pas pu interprï¿½ter le fichier transmis.\n"
								+ "Veuillez vous assurer qu'il ne contient aucune erreur.");
					} catch (IOException e) {
						e.printStackTrace();
						return Reponse.erreur("Erreur lors de la lecture du fichier.");
					}
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					return Reponse.erreur("Problï¿½me d'initialisation dans la gestion du service");
				}
			}
		};
		
		new ServiceControleur(c,"ajouter-livraison",this.serveur){
			protected Reponse getReponse(BufferedReader in){
				try {
					int idIntersection = Integer.parseInt(in.readLine());
					int idClient = Integer.parseInt(in.readLine());
					int idLivraisonPrecedente = Integer.parseInt(in.readLine());
					System.err.println(idIntersection+" "+idClient+" "+idLivraisonPrecedente);
					this.getControleur().ajouterLivraison(idIntersection, idClient, idLivraisonPrecedente);
					return Reponse.succes("Ajout livraison terminée.");
				} catch (IOException e){
					e.printStackTrace();
					return Reponse.erreur("La lecture des paramètres à échoué.");
				}
			}
		};
		
		new ServiceControleur(c,"supprimer-livraison",this.serveur){
			protected Reponse getReponse(String in){
				int idLivraison = Integer.parseInt(in);
				this.getControleur().supprimerLivraison(idLivraison);
				return Reponse.succes("Suppression terminée.");
			}
		};
		
		new ServiceControleur(c,"annuler",this.serveur){
			protected Reponse getReponse(InputStream in){
				if(this.getControleur().annuler()){
					return Reponse.succes("Annulation terminï¿½e.");
				} else {
					return Reponse.erreur("Il n'y a rien à annuler !");
				}
			}
		};
		
		new ServiceControleur(c,"retablir",this.serveur){
			protected Reponse getReponse(InputStream in){
				if(this.getControleur().retablir()){
					return Reponse.succes("Retablir rï¿½tablie");
				} else {
					return Reponse.erreur("Il n'y a rien à rétablir !");
				}
			}
		};
		
		new ServiceControleur(c,"calculer-itineraire",this.serveur){
			protected Reponse getReponse(InputStream in){
				if(this.getControleur().getFeuilleDeRoute().calculerParcours(this.getControleur().getGrapheRoutier())){
					return Reponse.succes("Itineraire calculé !");
				} else {
					return Reponse.erreur("Impossible de calculer l'itinéraire :(...\nN'auriez vous pas oublié de charger des livraisons ?");
				}		
			}
		};
		
		return this;
	}
	
	
	/**
	 * 
	 * @param c
	 * @return
	 */
	public ServeurBuilder deployerServicesVue(Controleur c){
		
		new ServiceModele(c,"itineraire",this.serveur){
			protected Reponse getReponse(InputStream in){
				String itineraire = this.getControleur().getFeuilleDeRoute().getItineraireXML();
				return Reponse.succes(itineraire);
			}
		};
		
		new ServiceModele(c,"plan",this.serveur){
			protected Reponse getReponse(InputStream in){
				String plan = this.getControleur().getGrapheRoutier().getPlanXML();
				return Reponse.succes(plan);
			}  
		};
		
		new ServiceModele(c,"livraisons",this.serveur){
			protected Reponse getReponse(InputStream in){
				String livraisons = this.getControleur().getFeuilleDeRoute().getLivraisonsXML();
				return Reponse.succes(livraisons);
			}  
		};
		
		new ServiceModele(c,"plagesHoraires",this.serveur){
			protected Reponse getReponse(InputStream in){
				String plagesHoraires = this.getControleur().getFeuilleDeRoute().getPlagesHorairesToXML();
				return Reponse.succes(plagesHoraires);
			}  
		};
		
		return this;
	}
}
