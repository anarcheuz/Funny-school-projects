var ctrl = new Controleur();
var map = ctrl.vue.map;

//////////////////////////////////////////////////////////////////////////////////
if (window.File && window.FileReader && window.FileList && window.Blob) {
    // Great success! All the File APIs are supported.
} else {
    alert('Les APIs pour l\'ouverture des fichiers ne sont pas pris en charge');
}

$.fn.modal.Constructor.prototype.enforceFocus = function () {};
///////////////////////////////////////////////////
// Class Controleur

function Controleur(){

    // attributes
    var com = new Com();
    var vue;

    // functions
    this.annuler = function() {
        console.log("undo");
        vue.afficherChargement("Annulation de l'action...");
        com.appelService("controleur/annuler","",this._annulerOk,this._annulerErr,true);
    }.bind(this);
    this._annulerOk = function(rep) {
        vue.nouvelItineraire(true);
    }.bind(this);
    this._annulerErr = function(msg) {
        vue.fermerChargement();
        vue.erreur(msg);
    }.bind(this);
    this.retablir = function(){
        console.log("redo");
        vue.afficherChargement("Retablissement de l'action...");
        com.appelService("controleur/retablir","",this._retablirOk,this._retablirErr,true);
    }.bind(this);
    this._retablirOk = function(rep) {
        vue.nouvelItineraire(true);
    }.bind(this);
    this._retablirErr = function(msg) {
        vue.fermerChargement();
        vue.erreur(msg);
    }.bind(this);
    this.clicChargerPlan = function(){
        document.getElementById('charger-plan').click();
    };
    this._chargerPlanOk = function(msg){
        vue.nouveauPlan();
    }.bind(this);
    this._chargerPlanErr = function(msg){
        vue.fermerChargement();
        vue.erreur(msg);
    }.bind(this);
    this.chargerPlan = function(evt){
        vue.afficherChargement("Création du réseau routier...\n"
                + "Merci de patienter quelques instants.");
        com.envoyerXml(evt,'controleur/charger-plan',this._chargerPlanOk,this._chargerLivraisonsErr, true);
        document.getElementById("charger-plan").value = null;
    }.bind(this);

    // déclenche le clic sur l'élément 'input' de la page html
    this.clicChargerLivraisons = function(){
        document.getElementById('charger-livraisons').click();
    };
    // Handler pour le retour du service charger-livraisons
    this._chargerLivraisonsOk = function(msg){
        vue.nouvellesLivraisons();
        // vue.creerPlan();
    };
    this._chargerLivraisonsErr = function(msg){
        vue.fermerChargement();
        vue.erreur(msg);
    }.bind(this);
    this.chargerLivraisons = function(evt){
        vue.afficherChargement("Chargement des données de livraisons...\n"
                + "Merci de patienter quelques instants.");
        com.envoyerXml(evt,'controleur/charger-livraisons',this._chargerLivraisonsOk,this._chargerLivraisonsErr,true);
        document.getElementById("charger-livraisons").value = null;
    }.bind(this);

    this.clicTelechargerInitineraire = function(){
        var pdf = new jsPDF();
        var elementHandler = {
            '#ignorePDF': function (element, renderer) {
                return true;
            }
        };
        var source = window.document.getElementById('FDR');
        pdf.fromHTML(
            source,
            15,
            15,
            {
              'width': 180,'elementHandlers': elementHandler
            });

        pdf.output("dataurlnewwindow");
        //... récupérer fichier
    };

    this._clicCalculOk = function(msg) {
        vue.nouvelItineraire();
    }.bind(this);
    this._clicCalculErr = function(msg) {
        vue.fermerChargement();
        vue.erreur(msg);
    }.bind(this);
    this._clicCalcul = function () {
        vue.afficherChargement("Calcul en cours, veuillez patienter...");
        com.appelService('controleur/calculer-itineraire','',
                        this._clicCalculOk,
                        this._clicCalculErr, true);
    }.bind(this);

    this.demandeDeSuppression = function(id) {
        vue.afficherChargement("Suppression en cours, veuillez patienter...");
        com.appelService("controleur/supprimer-livraison",""+id,this._suppressionOk, this._suppressionErr, true);
    };   
    this._suppressionOk = function(rep) {
        //vue.afficherChargement("Actualisation des données...");
        vue.nouvelItineraire(true);
    }; 
    this._suppressionErr = function(msg) {
        vue.fermerChargement();
        vue.erreur(msg);
    };

    this.ajouterLivraison = function(idIntersection, idLivraison, idClient){
        vue.afficherChargement("Ajout en cours, veuillez patienter...");
        var param = idIntersection+"\n"+idClient+"\n"+idLivraison;
        com.appelService("controleur/ajouter-livraison",param,this._ajouterLivraisonOk,this._ajouterLivraisonErr,true);
    }
    this._ajouterLivraisonOk = function(rep) {
        vue.nouvelItineraire(true);
    };
    this._ajouterLivraisonErr = function(msg) {
        vue.fermerChargement();
        vue.erreur(msg);
    };

    // init
    this.vue = vue = new Vue(this, com);

}



var path2 = [[0.4,0.4],[0.6,0.5]];
//var fg = L.rainbowLine(path,["#fff","#7a6bd9","#fe6a6d","#67e860","#ffe06a","#de252a"]).bindLabel("HEY").on("click",function(){alert("hey");}).addTo(map);
//var fg2 = L.rainbowLine([[0.6,0.5],[0.7,0.4]],["#fff","#7a6bd9","#fe6a6d","#67e860","#ffe06a","#de252a"]).bindLabel("HEY").on("click",function(){alert("hey");}).addTo(map);
var colors = ["#fff","#7a6bd9","#fe6a6d","#67e860","#ffe06a","#de252a"];

/*
var inter = [];
ctrl.vue.ajouterIntersection([0.2,0.2],1).activerClic();
ctrl.vue.ajouterIntersection([0.4,0.2],2).activerClic();
ctrl.vue.ajouterIntersection([0.5,0.3],3).activerClic();
ctrl.vue.ajouterIntersection([0.4,0.4],4).activerClic();
ctrl.vue.ajouterIntersection([0.2,0.5],5).activerClic();
ctrl.vue.ajouterIntersection([0.3,0.5],6).activerClic();
ctrl.vue.ajouterIntersection([0.3,0.6],7).activerClic();
ctrl.vue.ajouterIntersection([0.5,0.6],8).activerClic();
ctrl.vue.ajouterIntersection([0.6,0.5],9).activerClic();
ctrl.vue.getIntersection(4).setLivraison(0,45,'8h45');
ctrl.vue.getIntersection(5).setLivraison(1,65,'9h12');
ctrl.vue.getIntersection(6).setLivraison(2,12,'9h37');

ctrl.vue.ajouterRoute(1,2).setNom("Rue de la paix")
    .ajouterPassage(0,colors[0]);
ctrl.vue.ajouterRoute(2,1).setNom("route");
ctrl.vue.ajouterRoute(2,3).setNom("route")
    .ajouterPassage(0,colors[0]);
ctrl.vue.ajouterRoute(3,9).setNom("route")
    .ajouterPassage(0,colors[0])
    .ajouterPassage(0,colors[1]);
ctrl.vue.ajouterRoute(9,4).setNom("route")
    .ajouterPassage(0,colors[0])
    .ajouterPassage(0,colors[1]);
ctrl.vue.ajouterRoute(4,3).setNom("route")
    .ajouterPassage(0,colors[1]);
ctrl.vue.ajouterRoute(3,8).setNom("route");
ctrl.vue.ajouterRoute(8,7).setNom("route")
    .ajouterPassage(0,colors[3]);
ctrl.vue.ajouterRoute(7,6).setNom("route")
    .ajouterPassage(0,colors[3]);
ctrl.vue.ajouterRoute(6,7).setNom("route")
    .ajouterPassage(0,colors[1]);
ctrl.vue.ajouterRoute(6,8).setNom("route")
    .ajouterPassage(0,colors[2]);
ctrl.vue.ajouterRoute(6,4).setNom("route");
ctrl.vue.ajouterRoute(4,6).setNom("route")
    .ajouterPassage(0,colors[1])
    .ajouterPassage(0,colors[3]);
ctrl.vue.ajouterRoute(4,2).setNom("route");
ctrl.vue.ajouterRoute(4,1).setNom("route")
    .ajouterPassage(0,colors[3]);
ctrl.vue.ajouterRoute(7,5).setNom("route")
    .ajouterPassage(0,colors[2]);
ctrl.vue.ajouterRoute(5,6).setNom("route")
    .ajouterPassage(0,colors[2]);


ctrl.vue.afficher();
*/
/*for( var i = 0; i < routes.length; ++i){
    routes[i].afficher();
}
for( var i = 0; i < inter.length; ++i){
    inter[i].afficher();
}*/

//var path = ArcMaker.arcPath([0.5,0.5],[0.2,0.2],0.05+0.02,10);
/*for(var i = 0; i < 5; ++i){
    var path = ArcMaker.arcPath([0.2,0.2],[0.5,0.5],0.05+0.02*i,10);
    var pl = L.polyline(path,{weight: 3,color: colors[i]}).addTo(map);
}*/


///////////////////////////////////////////////////
// Class Com

function Com(){

    this.appelService = function(nomService, params, fctOk, fctErr, async){
        //console.log("async",async);
        var asynchronous = async == null ? false : async;
        console.log(nomService, (asynchronous? "async" : "sync"));
        var xmlhttp=new XMLHttpRequest();
        xmlhttp.open("POST","http://localhost:4500/"+nomService,asynchronous);
        //xmlhttp.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
        xmlhttp.overrideMimeType('text/xml');

        var msg = "";
        if(params){
            for( var i = 0; i < params.length; ++i){
                msg += params[i];
            }
        }
        if( fctErr == null)fctErr = ctrl.vue.erreur;
        if( !asynchronous ){
            xmlhttp.send(msg); // bloquant
            if(fctOk){
                fctOk(xmlhttp.responseText);
            }
            return xmlhttp.responseText;
        } else {
            xmlhttp.onload = function (e) {
                if (xmlhttp.readyState === 4) {
                    if (xmlhttp.status === 200) {
                        fctOk(xmlhttp.responseText);
                    } else {
                        fctErr(xmlhttp.responseText);
                    }
                }
            };
            xmlhttp.onerror = function (e) {
                fctErr("Connexion impossible avec le serveur...");
            };
            xmlhttp.send(msg);
        }
        
    }

    this.envoyerXml = function(fileEvt, nomService, fctOk, fctErr, async){
        var f = fileEvt.target.files[0];
        if(f){
            var extension = f.name.split('.').pop();
            if(extension === "xml" || extension === "XML"){
                var reader = new FileReader();
                
                reader.onload = function(e){
                    this.appelService(nomService,[e.target.result],fctOk,fctErr, async);
                }.bind(this);
                reader.readAsText(f);
            } else {
                vue.erreur("Le fichier sélectionné (."+extension+") n'a pas la bonne extension (.xml) !");
            }
        }
    }.bind(this);
}

/*
function addLine( A, B, label){
    L.polyline([A,B],{weight:5,color:'#fff',opacity:0.8})
        .addTo(map)
        .bindLabel(label)
        .on("mouseover", function (){
            this.setStyle({color:"#0f0"});
        })
        .on("mouseout", function (){
            this.setStyle({color:'#fff'});
        });
}
*/