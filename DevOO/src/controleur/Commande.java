package controleur;

import java.util.*;
import modele.*;

/**
 * 
 */
public interface Commande {

    /**
     * 
     */
    public void executer();

    /**
     * 
     */
    public void annuler();
    
    ArrayList<String> arguments = new ArrayList<String>();
    

}