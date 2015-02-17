package appcom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import controleur.Controleur;

public class MainClass {

	public static void main(String[] args) {
		Controleur c = new Controleur();
		
		ServeurBuilder.nouveauServeur()
			.deployerServicesControleur(c).deployerServicesVue(c);
		
	}

}
