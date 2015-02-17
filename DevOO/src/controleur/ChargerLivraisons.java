package controleur;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import modele.Intersection;
import modele.Livraison;
import modele.PlageHoraire;
import modele.GrapheRoutier;
import modele.FeuilleDeRoute;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ChargerLivraisons {
	
	
	private static final DateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm:ss");
	private static FeuilleDeRoute fdr;
	
	/**
	 * G�n�ration de la FeuilleDeRoute � partir d'un document xml pass� en param�tre
	 * Le document est pars� 2 fois, une fois pour g�n�rer les PlagesHoraires,
	 * une fois pour g�n�rer les Livraisons et les lier � la PlageHoraire parente
	 * Si tout se passe bien, renvoie true
	 * Sinon renvoie false avec affichage en console du type de probl�me
	 * @param livDoc : document � parser
	 * @param grapheRoutier : graphe routier du controleur
	 * @return FeuilleDeRoute : feuille de route g�n�r�e (vide si erreur)
	 */
	public static FeuilleDeRoute chargerLivraisons(Document livDoc, GrapheRoutier grapheRoutier){
		fdr = new FeuilleDeRoute();
		livDoc.getDocumentElement().normalize();
		
		//check entrepot
		if(!genererEntrepot(livDoc,grapheRoutier)){
			fdr.clean();
			return fdr;
		}
		
		//premier passage, creation des plages horaires
		if(!genererPlageHoraire(livDoc)){
			fdr.clean();
			return fdr;
		}
		
		/* Deuxi�me passage avec cr�ation des Livraison et mise en relation avec la plage horaire 
		 * correspondante.
		 */
		
		if(!genererLivraisons(livDoc, grapheRoutier)){
			fdr.clean();
			return fdr;
		}
		
		
		return fdr;
	}
	
	/**
	 * Cr�ation de l'entrepot
	 * @param livDoc : document � parser
	 * @param grapheRoutier
	 * @return boolean
	 * 		true si l'entrep�t est correctement cr�� et ajout�
	 * 		false si il y une erreur
	 */
	public static boolean genererEntrepot(Document livDoc,GrapheRoutier grapheRoutier){
		Node entrepotNode = livDoc.getElementsByTagName("Entrepot").item(0);
		if(entrepotNode.getNodeType()==Node.ELEMENT_NODE){
			Element entreElement = (Element)entrepotNode;
			try{
				int idInter = Integer.parseInt(entreElement.getAttribute("adresse"));
				if(grapheRoutier.interExiste(idInter)){
					fdr.renseignerEntrepot(grapheRoutier.getIntersection(idInter));
					return true;
				}else{
					System.err.println("Entrepot invalide");
					return false;
				}
			}catch(Exception e){
				System.err.println("Entrepot invalide");
				return false;
			}
		}else{
			return false;
		}
		
	}

	/**
	 * 1er parsage
	 * Cr�ation des plages horaires de la liste de livraisons fournies
	 * @param livDoc : document � parser
	 * @return boolean
	 * 		true si les plages horaires sont correctements cr��es puis ajout�es � la feuille de route
	 * 		false si il y a une erreur
	 */
	@SuppressWarnings("deprecation")
	public static boolean genererPlageHoraire(Document livDoc){
		
		NodeList phNodeList = livDoc.getElementsByTagName("Plage");
		if(phNodeList.getLength()==0){
			System.err.println("Aucune plage horaire ");
			return false;
		}
		
		//premier passage, creation des plages horaires
		for(int i =0;i<phNodeList.getLength();i++){
			Node phNode = phNodeList.item(i);
			if(phNode.getNodeType() == Node.ELEMENT_NODE){
				Element phElement = (Element)phNode;
				if(phElement.hasAttributes()){
					NamedNodeMap nnm = phElement.getAttributes();
					/*
					 * V�rification de l'int�grit� des donn�es pour les PlageHoraire
					 */
					try{
						String heureDeb = nnm.getNamedItem("heureDebut").getTextContent();
						String heureF = nnm.getNamedItem("heureFin").getTextContent();
						if(heureDeb == null || heureF==null){
							System.err.println("Heure invalide");
							return false;
						}else{
							try{
								Date heureDebut = HOUR_FORMAT.parse(heureDeb);
								Date heureFin = HOUR_FORMAT.parse(heureF);
								// V�rification de la coh�rence des heures fournies
								if(heureDebut.getHours()>=0 && heureDebut.getHours()<=24
								&& heureFin.getHours()>=0 && heureFin.getHours()<=24
									&& heureDebut.before(heureFin)
									&& fdr.checkHB(heureDebut)){
									PlageHoraire ph = new PlageHoraire(heureDebut,heureFin);
									fdr.ajouterPlageHoraire(ph);
								}else{
									System.err.println("Format d'heure invalide.");
									return false;
								}
							}catch (Exception e){
								e.printStackTrace();
								System.err.println("Format d'heure invalide.");
								return false;
							}
						}
					}catch(Exception e){
						System.err.println("Format Plage horaire invalide.");
						return false;
					}
				}else{
					System.err.println("HasAttributes.");
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 2�me parsage
	 * Cr�ation des livraisons � partir de la liste de livraisons
	 * Le graphe routier permet de v�rifier l'existence des adresses
	 * Association avec la plage horaire correspondante
	 * @param livDoc : document � parser
	 * @param grapheRoutier : graphe routier du controleur
	 * @return boolean
	 * 		true si les livraisons sont correctements cr��es puis ajout�es � la feuille de route
	 * 		false si il y a une erreur
	 */
	public static boolean genererLivraisons(Document livDoc,GrapheRoutier grapheRoutier){
		NodeList livNodeList = livDoc.getElementsByTagName("Livraison");
		if(livNodeList.getLength() ==0){
			System.err.println("Aucune livraison");
			return false;
		}
		for(int j=0; j<livNodeList.getLength(); j++){
			Node livNode = livNodeList.item(j);
			if(livNode.getNodeType() == Node.ELEMENT_NODE){
				// R�cup�ration de la PlageHoraire parente
				Element livElement = (Element)livNode;
				Element phParent = (Element)livElement.getParentNode().getParentNode();
				String hDeb = phParent.getAttribute("heureDebut");
				PlageHoraire phParentObj;
				try{
					Date hDebDate = HOUR_FORMAT.parse(hDeb);
					phParentObj = fdr.rechercherPHParHD(hDebDate);

					if(phParentObj == null ){
						System.err.println("Plage Horaire Parente non trouv�e");
						return false;
					}
					
				}catch(Exception e){
					e.printStackTrace();
					System.err.println("Page horaire parente invalide");
					return false;
				}
				
				if(livElement.hasAttributes()){
					NamedNodeMap nnm = livElement.getAttributes();
					// V�rification de l'int�grit� des donn�es pour les Livraisons
					try{
						int id = Integer.parseInt(nnm.getNamedItem("id").getTextContent());
						int idClient = Integer.parseInt(nnm.getNamedItem("client").getTextContent());
						int idInter = Integer.parseInt(nnm.getNamedItem("adresse").getTextContent());
						
						
						if(grapheRoutier.interExiste(idInter)){
							Intersection inter = grapheRoutier.getIntersection(idInter);
							Livraison liv = new Livraison(inter,id, idClient);
							phParentObj.addLivraison(liv);
							liv.setPlageHoraire(phParentObj);
						}
					}catch(Exception e){
						System.err.println("Format de livraion invalide");
						return false;
					}
					
				}else{
					System.err.println("Has arrtibute");
					return false;
				}
			}
		}
		return true;
	}

}
