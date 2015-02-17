L.Rainbowline = L.FeatureGroup.extend({

    options:{
        weight: 0.002,
        fillOpacity:0.8
    },

    initialize: function(linepath, colors, options){
        L.FeatureGroup.prototype.initialize.call(this,[]);
        L.setOptions(this,options);
        this.linepath = linepath;

        var vect = [linepath[1][0]-linepath[0][0],linepath[1][1]-linepath[0][1]];
        vect = [-vect[1],vect[0]]; // perp
        var length = Math.sqrt(vect[1]*vect[1] + vect[0]*vect[0]);
        if(length > 0) vect = [vect[0]/length,vect[1]/length];
        var weight = this.options.weight;
        this.dpos = [vect[0]*weight,vect[1]*weight];

        this.lines= [];
        if(colors){
            for(var i = 0; i < colors.length; ++i){
                this.addColor(colors[i]);
            }
        }

    },

    addColor: function(color){
        var toggle = this.lines.length%2 == 0 ? 1 : -1;
        var d0 = Math.floor(this.lines.length/2);
        var d1 = (d0+1)*toggle;
        d0 *= toggle;
        var path = [[this.linepath[0][0]+this.dpos[0]*d0,
                    this.linepath[0][1]+this.dpos[1]*d0],
                    [this.linepath[1][0]+this.dpos[0]*d0,
                    this.linepath[1][1]+this.dpos[1]*d0],
                    [this.linepath[1][0]+this.dpos[0]*d1,
                    this.linepath[1][1]+this.dpos[1]*d1],
                    [this.linepath[0][0]+this.dpos[0]*d1,
                    this.linepath[0][1]+this.dpos[1]*d1]];

        var opt = this.options;
        opt.color = color;
        var line = L.polygon(path,opt);
        this.lines[this.lines.length] = line;
        this.addLayer(line);
    }

});
L.rainbowline = function(linepath, colors, options){
    return new L.Rainbowline(linepath,colors,options);
};

L.MultiRainbowline = L.FeatureGroup.extend({
    initialize: function(paths, colors, options){
        L.FeatureGroup.prototype.initialize.call(this,[]);
        L.setOptions(this,options);
        this._children = [];

        for(var i = 0; i < paths.length-1; ++i){
            this._children[i] = L.rainbowline([paths[i],path[i+1]],colors,options);
            //console.log(i);
            this.addLayer(this._children[i]);
        }
    },

    addColor: function(color){
        for(var i = 0; i < this._children; ++i ){
            this._children[i].addColor(color);
        }
    }
});
L.multiRainbowline = function(paths, colors, options){
    return new L.MultiRainbowline(paths, colors, options);
}

L.Arc = L.Polyline.extend({
    initialize: function(ab, offset){
        var radius = this._radius(ab[0], ab[1], offset);
        this._createPath(ab[0], ab[1], radius);
    },

    _radius: function(a, b, offset){
        var abpow2 = Math.pow(b[0]-a[0],2)+Math.pow(b[1]-a[1],2);
        return 0.5*offset + abpow2/(8*offset);
    },
    _createPath: function(a, b, r){

    }
});