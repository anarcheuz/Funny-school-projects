///////////////////////////////////////////////////
// Class Vue
function VueFeuilleDeRoute(appCom, vue){

    // attributs
    this.com = appCom;
    var com = this.com;

    this.vue = vue;
    var vue = this.vue;

    this.livraison = livraison;
    var livraison = this.livraison;

    //methodes
    this.chargerFeuille = function(doc) {
        console.log("chargerFeuille",doc);

        console.log("vue",vue);


        $('#FDR').html("<h1>Feuille de Route</h1>");

        var fdr = doc.getElementsByTagName("feuilleDeRoute")[0];
        var etapes = fdr.getElementsByTagName("etape");
        var id1 = etapes[0].getAttribute("idIntersection");

        for(var i = 1; i < etapes.length; ++i){
            var id2 = etapes[i].getAttribute("idIntersection");
            
            var route = this.vue.getRoute(id1, id2);
            if(route){
                var heure = etapes[i-1].getAttribute("heurePassage");
                //console.log("route", route);
                //console.log("plage", idPlage,this.couleurPlages[idPlage]);
                var attente = etapes[i-1].getAttribute("secondesAttente");
                if( attente > 0 ){
                    attente /= 1000*60;
                    attente = Math.round(attente);
                    $('#FDR').append("<p class='attente'>Attendre <tps>"+attente+"<tps> minute"+ (attente > 1 ?"s":"") +" avant de se présenter chez le client. </p>" );
                }

                var livraison = etapes[i-1].getElementsByTagName("livraison");

                if(livraison.length != 0 ){
                    for(var a = 0; a < livraison.length ; a++){
                        $('#FDR').append("<p class='livraison'><tps>"+heure+"</tps> : "+livraison[a].getAttribute("adresse")+" -> Livraison pour le client <client>"+ livraison[a].getAttribute("idClient") + "</client></p>");
                        $('#FDR').append("<p>Temps de livraison : 10 min</p>")
                        var start = heure.substring(0,heure.length - 2);
                        var nbr = heure.substring(heure.length-2,heure.length);
						start = parseInt(start);
						nbr = parseInt(nbr) + 10;
						if(nbr>60){
							nbr = nbr - 60;
							start = start + 1;
						}
						var heure = "";
                        heure = (heure.concat(start)+"h");
						if(nbr<10){
							heure = heure + "0";
						}
						heure=heure.concat(nbr);
                    }                  
                }
                $('#FDR').append("<p>");
                $('#FDR').append("<tps style=\"color: red\">"+heure+"</tps> : ");
                $('#FDR').append("<route>"+route.nom+ "</route> en direction de <inter>");
                $('#FDR').append(this.vue.getIntersection(id2).id +"</inter>");
                
                $('#FDR').append("</p>");
            }
            id1 = id2;
        }

        //var intersection = vue.getIntersection(id);
        //var route = vue.getRoute(id1,id2);
    };

    function checkLivraison(id){/*

        console.log("livraison", livraison);

        var plages = livraison.getElementsByTagName("plage");
        for(var i = 0; i < plages.length; ++i){
            var plageTxt = plages[i].getAttribute("debut")
                    + "-" + plages[i].getAttribute("fin") + "h";
            var livs = plages[i].getElementsByTagName("livraison");
            for(var j = 0; j < livs.length; ++j){
                var idIntersection = livs[j].getAttribute("idIntersection");
                console.log("livraison", id, idIntersection);

                if(id == idIntersection){
                    return true;
                } else {
                    return false;
                

                }
            }
        }*/
    };
}