/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package traitementgraphe2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Normalizer;

/**
 *
 * @author Lilian
 */
public class TraitementGraphe2 {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String nomRecherche=args[0];
        nomRecherche = Normalizer.normalize(nomRecherche.toLowerCase(),Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
        File f = new File("paires/"+nomRecherche+".txt");
        FileInputStream input = new FileInputStream(f);
        ComparateurGraphes comp = new ComparateurGraphes();
        comp.creerPaires(input);
        comp.grouper(Double.parseDouble(args[1]));
        comp.afficher(System.out);
        input.close();
    }
}
