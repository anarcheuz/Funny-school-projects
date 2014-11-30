package traitementgraphe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.Normalizer;



public class TraitementGraphe {

    public static void main(String[] args) throws FileNotFoundException {
        String nomRecherche=args[0];
        nomRecherche = Normalizer.normalize(nomRecherche.toLowerCase(),Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
        File f = new File("paires/"+nomRecherche+".txt");
        if(!f.exists()){
        ComparateurGraphes comp = new ComparateurGraphes();
        comp.parserGraphes(System.in);
        comp.creerPaires();
        FileOutputStream output = new FileOutputStream(f);
        comp.afficher(new PrintStream(output));
        }
        
    }
    
}
