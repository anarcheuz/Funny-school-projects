:- dynamic ai_play/1 .
:- dynamic minmax_ai/1 .


init_minmax_ai:-
	assert((

/* Recherche le coup à jouer permettant de marquer autant de point que Score */
minmax_ai(Line,Score) :-
	free_lines(Lines),
	element(Line,Lines,_),
	close_count(Line,Score)
	
	)),assert((
	
/* L'IA cherche à remplir 2 carrés */
minmax_ai(Line) :-
	minmax_ai(Line,2),!
	
	)),assert((
/*Si ce n'est pas possible, l'IA cherche à remplir 1 carré */
minmax_ai(Line) :-
	minmax_ai(Line,1),!
	
	)),assert((
/*Si ce n'est pas possible, l'IA cherche à jouer un coup aléatoire qui ne permet pas à l'opposant de fermer un carré */
minmax_ai(Line) :-
	findall(L,(minmax_ai(L,0),not(critical(L))),Lines),
	length(Lines,N),
	random(1,N,R),
	nth1(R,Lines,Line),!
	
	)),assert((
/* En dernier recours, l'IA joue tout simplement. */
minmax_ai(Line) :-
	write('retracteur\n'),
	retractall(minmax_ai(_)),
	write('retracte\n'),
	assert((
		minmax_ai(Move) :-
			%read(_),
			free_lines([X|Xs]),
			assert(best_value(-100)),
			assert(best_move(X)),
			get_time(T),
			T1 is T + 0.1,
			( minmax_ai([], [X|Xs], 6, 1, _, 0, T1) -> true ; true),
			best_move(Move),
			retract(best_move(_)),
			retract(best_value(_)),
			%write('\nBest move : '),write(Move),write('\n'),
			!
		)),
	%write('asserte\n'),
	minmax_ai(Line)
	
	)).
	
clear_minmax_ai:- retractall(minmax_ai(_)).

/*-------------------------*/

:- dynamic best_value/1 .

minmax_ai(_,[],_,_,_,_,_).
minmax_ai(_,_,-1,_,FirstMove,Value,_):-
	maj_best(FirstMove,Value).
	

maj_best(FirstMove, Value):-	
	best_value(B),
	( Value > B ->
		retract(best_value(_)),
		retract(best_move(_)),
		assert(best_value(Value)),
		assert(best_move(FirstMove))
		;
		true
	).

minmax_ai(Before,[Move|After], D, Player, FirstMove, Value, Tmax):-
	get_time(T),
	( T > Tmax -> true ;
		D >= 0,
		
		%write('Move : '),write(Move),write('\n'),
		
		append(Before,[Move],Before2),
		close_count(Move,Count),
		
		Value2 is (Value + (Count * Player)),
		
		( FirstMove is 0 -> FM = Move ; FM = FirstMove ),
		maj_best(FM, Value2),
		minmax_ai(Before2, After, D, Player, FirstMove, Value, Tmax),
		
		append(Before,After,Moves),
		D1 is D - 1,
		Player1 is -Player * 1,
		
			%write('\nFM : '),write(FM),write('\n'),
		
		( Count > 0 ->
			minmax_ai([],After, D1, Player , FM,Value2, Tmax)
		;
			minmax_ai([],After, D1, Player1, FM, Value, Tmax)
		)
	).
	
	
/* minmax_ai(+Moves,+Position,+Depth,+Player,+Alpha,+Beta,+Move0,
            -BestValue,-BestMove)
      Chooses the Best move from the list of Moves from the current Position
      using the alpha beta algorithm searching Depth ply ahead.
      Player indicates if the next move is by player (1) or opponent (-1).
      Move0 records the best move found so far and Alpha its value.
      If a value >= Beta is found, then this position is too good to be true:
      the opponent will not move us into this position.
*/

/*
minmax_ai(_,_,0,_,_,_,_,_,_). %Depth atteint
minmax_ai(_,[],_,_,_,_,_,_,_). %Plus dautre coup à tester
minmax_ai(Before,[Move|After],D, Player, Alpha, Beta, Move0, BestValue, BestMove):-
  D > 0,
  append(Before,After,Moves),
  D1 is (D - 1),
% gestion du prochain jouer %
  ( close_square( move ) -> 
    Value1 is BestValue + Player,
    minmax_ai([], Moves, D1, Player, Alpha, Beta, Move, Value1, BestMove), %rejouter %On entre dans le noeud plus profond
    Value is Value1
  ; Opponent is -Player,
    OppAlpha is -Beta,
    OppBeta is -Alpha,
    minmax_ai([], Moves, D1, Opponent, OppAlpha, OppBeta, Move, BestValue, _OppMove), %On entre dans le noeud plus profond
    Value is -BestValue
  ),
  append(Before,[Move],Before1), %On passe move (le coup testé) dans la liste des coups testés pour essayer depuis le meme noeud avec les autres coups restant
% suite en fonction de la valeur %
  ( Value >= Beta *->
    BestValue = Value, BestMove = Move % abort: too good to be true
  ; Value > Alpha *->
    minmax_ai(Before1, After, D, Player, Value, Beta, Move, BestValue, Move), BestMove = Move
  ; minmax_ai(Before1, After, D, Player, Alpha, Beta, Move0,BestValue, Move0), BestMove = Move0
  ).
*/
:-assert(p1_play(L):-
	minmax_ai(L)).
:-assert(p2_play(L):-
	minmax_ai(L)).
	
:- dynamic start/0 .
start :- consult('main_file.pl'),
play.