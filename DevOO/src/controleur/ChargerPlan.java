package controleur;


import modele.GrapheRoutier;
import modele.Intersection;
import modele.Route;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ChargerPlan {
	
	private static GrapheRoutier gr;
	
	/**
	 * Génération du GrapheRoutier à partir d'un document xml passé en paramètre
	 * Le document est parsé 2 fois, une fois pour générer les Intersections,
	 * une fois pour générer les Routes et les lier aux Intersections correspondantes
	 * Si le document est invalide, on vide le graphe et on le renvoie.
	 * Sinon le graph est envoyé avec toutes ses intersetions
	 * @param plan : document à parser
	 * @return GrapheRoutier : graphe généré à partir du document (vide si erreur)
	 */
	public static GrapheRoutier chargerPlan(Document plan){
		
		
		gr = new GrapheRoutier();
		plan.getDocumentElement().normalize();
		
		//premier passage avec création des intersections
		if(!genererIntersection(plan)){
			gr.clean();
			return gr;
		}

		//deuxième passage avec création des Routes
		if(!genererRoutes(plan)){
			gr.clean();
			return gr;
		}
		
		return gr;
	}
	
	/**
	 * 1er tour de parsage
	 * On tente de créer les intersections détectées dans le document passé en paramètre
	 * @param plan : document à parser
	 * @return boolean :
	 * 		true si les intersctions sont correctements ajoutées au graphe
	 * 		fasle si il y a un problème
	 */
	public static boolean genererIntersection(Document plan ){
		NodeList intersections = plan.getElementsByTagName("Noeud");
		
		if(intersections.getLength() == 0){
			System.err.println("Aucune intersections");
			return false;
		}
			
		
		for (int i = 0; i<intersections.getLength();i++){
			Node noeud = intersections.item(i);
			if(noeud.getNodeType() == Node.ELEMENT_NODE){
				
				Element elementNoeud = (Element) noeud;
				if(elementNoeud.hasAttributes()){
					NamedNodeMap attr= elementNoeud.getAttributes();
					/* Vérification de l'intégrité des données fournies par le document pour les 
					 * intersections
					 */
					try{
						int id = Integer.parseInt(attr.getNamedItem("id").getTextContent());
						if(gr.interExiste(id)){
							System.err.println("Intersection déjà existante");
							return false;
						}
						int x = Integer.parseInt(attr.getNamedItem("x").getTextContent());
						if(x<0){
							System.err.println("x < 0");
							return false;
						}
						int y = Integer.parseInt(attr.getNamedItem("y").getTextContent());
						if(y<0){
							System.err.println("y < 0");
							return false;
						}
						Intersection inter = new Intersection(id,x,y);
						gr.ajouterIntersection(inter);
						}
					catch(Exception e){
						return false;
					}
				}else{
					System.err.println("hasAttribute...");
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 2ème parsage
	 * Création des Routes, vérification de la destiniation (intersection correcte)
	 * Ajout de la route à l'intersection parente
	 * @param plan : document à parser
	 * @return boolean
	 * 		true si toutes les routes sont crées et ajoutées correctement
	 * 		false si il y un problème
	 */
	public static boolean genererRoutes(Document plan){
		NodeList routes = plan.getElementsByTagName("LeTronconSortant");
		if(routes.getLength()==0){
			return false;
		}
		for(int j = 0 ; j<routes.getLength() ; j++){
			Node routeNode = routes.item(j);
			if(routeNode.getNodeType() == Node.ELEMENT_NODE){
				Element elementRoute = (Element) routeNode;
				if(elementRoute.hasAttributes()){
					/* Vérification de l'intégrité des données fournies par le xml
					 */
					try{
						NamedNodeMap attr = elementRoute.getAttributes();
						String nom = attr.getNamedItem("nomRue").getTextContent();
						double vitesse = Double.parseDouble(attr.getNamedItem("vitesse").getTextContent().replace(",", "."));
						if(vitesse<0){
							System.err.println("Vitesse <0");
							return false;
						}
						double longueur = Double.parseDouble(attr.getNamedItem("longueur").getTextContent().replace(",", "."));
						if(longueur<0){
							System.err.println("longueur <0");
							return false;
						}
						/* Vérification de l'existence de la destination
						 * Récupération de l'intersection de provenance pour lui ajouter la nouvelle route
						 */
						int idInter = Integer.parseInt(attr.getNamedItem("idNoeudDestination").getTextContent());
						if(gr.interExiste(idInter)){
							Intersection inter = gr.getIntersection(idInter);
							Route routeObj = new Route(nom,vitesse,longueur,inter);
							int  idParent = Integer.parseInt(elementRoute.getParentNode().getAttributes().getNamedItem("id").getTextContent());
							Intersection parent = gr.getIntersection(idParent);
							parent.addTroncSortant(routeObj);
						}else{
							System.err.println("Erreur sur destination");
							return false;
						}
					}catch(Exception e){
						e.printStackTrace();
						return false;
					}
					
				}else{
					return false;
				}
			}
			
		}
		return true;
	}
	
}
