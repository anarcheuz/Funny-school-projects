%-----------User interface gestion --------%
%--All needed procedure to create an xpce--%
%--interface are included in this file-----%

%--Declaration des faits dynamiques--------%
:- dynamic screen_w/1 .
:- dynamic screen_h/1 .
:- dynamic created/0 .
:- dynamic newWindow/0 .

%--Fait utilisé afin de gérer la fenêtre---%
%--déjà existante--------------------------%
:-assert(newWindow).

%width_board(5).
%height_board(5).

%--Procédure de gestion d'une fenêtre déjà-%
%--existante, avec ajout d'un nouvel-------%
%--element picture à la bonne taille-------%
%--parametres: Width=largeur de la fenêtre-%
%--------------Height=hauteur de la fenêtre%
create_Display(Width,Height):-
	created,
	assert(screen_w(Width)),
	assert(screen_h(Height)),
	send(@d, append, new(@p, picture('Dots and Boxes - H4104'))),
	PictWidth is Width+20, PictHeight is Height+20,
	send(@p,size, size(PictWidth,PictHeight)),
	send(@p, flush).

%--Procédure de création d'une fenêtre----%
%--avec assert et retract des faits-------%
%--de gestion de fenêtre-------------------%
%--parametres: Width=largeur de la fenêtre-%
%--------------Height=hauteur de la fenêtre%
create_Display(Width,Height):-
	newWindow,
	assert(screen_w(Width)),
	assert(screen_h(Height)),
	new(@d, dialog('Demo prolog')),
	send(@d, append, new(@p, picture('Dots and Boxes - H4104'))),
	WindowWidth is Width+20, WindowHeight is Height+20,
	send(@p,size, size(WindowWidth,WindowHeight)),
	send(@d, open),
	retract(newWindow),
	assert(created).

%--Procedures create_display---------------%
%--Procedures utilisees pour determiner la-%
%--taille de la future fenetre en prenant--%
%--en parametre la taille max d'un cote----%
%--Parametre: Size= taille max d'un cote---%	
create_display(Size):-
	width_board(W),
	height_board(H),
	W>H,
	Ratio is H/W,
	Height is Size*Ratio,
	create_Display(Size,Height).
	
create_display(Size):-
	width_board(W),
	height_board(H),
	W<H,
	Ratio is W/H,
	Width is Size*Ratio,
	create_Display(Width,Size).
	
create_display(Size):-
	width_board(W),
	height_board(H),
	W==H,
	create_Display(Size,Size).

%--Procedures screen-line------------------%
%--Procedures utilisées pour déterminer les%
%--coordonnees de la future ligne a tracer-%
%--Parametres: [X,Y]=coordonnees du centre-%
%--de la ligne-----------------------------%
%-------------FX,FY=coordonnees du premier-%
%--point formant la ligne------------------%
%-------------LX,LY=coordonnees du second--%
%--point formant la ligne------------------%
screen_line([X,Y],FX,FY,LX,LY):-
	horizontal([X,Y]),
	FX is X,
	FY is Y+0.5,
	LX is X+1.0,
	LY is Y+0.5.

screen_line([X,Y],FX,FY,LX,LY):-
	vertical([X,Y]),
	FX is X+0.5 ,
	FY is Y ,
	LX is X+0.5,
	LY is Y+1.0.

%--Vieille fonction plus utilisée----------%
draw_line(Pos):-
	screen_line(Pos,FirstX,FirstY,SecondX,SecondY),
	width_board(W),
	height_board(H),
	screen_w(WW),
	screen_h(HH),
	FactorX is WW/W,
	FactorY is HH/H,
	send(@p,display,new(Li,line(FirstX*FactorX,FirstY*FactorY,SecondX*FactorX,SecondY*FactorY,none))),
	send(Li,flush).

%--Procedure permettant de tracer les lignes%
%--en couleur (celles jouees par les joueurs%
%--Ici la ligne tracee est une 'box', ceci--%
%--afin de pouvoir les colorier et de-------%
%--pouvoir cliquer dessus-------------------%
%--Parametres: [X,Y]= coordonnees du point--%
%--haut-gauche du carre a dessiner----------%
%--------------Player= joueur actuel--------%
draw_line([X,Y],Player):-
	screen_line([X,Y],FirstX,FirstY,SecondX,SecondY),
	width_board(W),
	height_board(H),
	screen_w(WW),
	screen_h(HH),
	FactorX is WW/W,
	FactorY is HH/H,
	BW is  max(integer((SecondX-FirstX)*FactorX+1),5),
	BH is  max(integer((SecondY-FirstY)*FactorY+1),5),
	BX is (X+0.75)*FactorX - BW/2,
	BY is (Y+0.75)*FactorY - BH/2,
	%write(BX),write(' '),write(BY),not(true),
	send(@p,display,new(Sq,box(BW,BH)),point( BX,BY )),
	square_color(C,Player),
	send(Sq,fill_pattern,colour(C)),
	send(Sq,flush).

%--Procedure permettant de tracer les lignes%
%--grises de depart (les jouables)----------%
%--Ici la ligne tracee est une 'box', ceci--%
%--afin de pouvoir les colorier et de-------%
%--pouvoir cliquer dessus-------------------%
%--Parametre: [X,Y]= coordonnees du point--%
%--haut-gauche du carre a dessiner----------%
draft_line([X,Y]):-
	screen_line([X,Y],FirstX,FirstY,SecondX,SecondY),
	width_board(W),
	height_board(H),
	screen_w(WW),
	screen_h(HH),
	FactorX is WW/W,
	FactorY is HH/H,
	BW is  max(integer((SecondX-FirstX)*FactorX+1),5),
	BH is  max(integer((SecondY-FirstY)*FactorY+1),5),
	
	BX is (X+0.75)*FactorX - BW/2,
	BY is (Y+0.75)*FactorY - BH/2,
	%write(BX),write(' '),write(BY),not(true),
	send(@p,display,new(Sq,box(BW,BH)),point( BX,BY )),
	send(Sq,fill_pattern,colour(gray)),
	send(Sq,flush).
	
%%--Procedure permettant de dessiner un----%
%--carre dont la couleur sera recuperee----%
%--grace a la procedure square_color-------%
%--Parametres: [X,Y]= coordonnees du point-%
%--haut-gauche du carre a dessiner---------%
%--------------Player= joueur actuel-------%	
fill_square([X,Y],Player):-
	%write('\n square : '), write([X,Y]),write('\n'),
	NewX is X+0.25,
	NewY is Y+0.25,
	width_board(W),
	height_board(H),
	screen_w(WW),
	screen_h(HH),
	FactorX is WW/W,
	FactorY is HH/H,
	send(@p,display,new(Sq,box(FactorX,FactorY)),point(NewX*FactorX,NewY*FactorY)),
	square_color(C,Player),
	send(Sq,fill_pattern,colour(C)),
	send(Sq,flush).

%--Definissions des predicats donnant la---%
%--couleur d'un carre a remplir------------%	
square_color(red,ai).
square_color(blue,user).

%--Procedure permettant de liberer les-----%
%--donnees necessaires a la creation d'une-%
%--nouvelle 'picture':element de la fenetre%
close_ui:-
	free(@p),
	retract(screen_w(_)),
	retract(screen_h(_)).