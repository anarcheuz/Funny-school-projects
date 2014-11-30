:- dynamic ai_play/1 .


/* Recherche le coup � jouer permettant de marquer autant de point que Score */
simple_ai(Line,Score) :-
	free_lines(Lines),
	element(Line,Lines,_),
	close_count(Line,Score).
	
/* L'IA cherche � remplir 2 carr�s */
simple_ai(Line) :-
	simple_ai(Line,2),!.
/*Si ce n'est pas possible, l'IA cherche � remplir 1 carr� */
simple_ai(Line) :-
	simple_ai(Line,1),!.
/*Si ce n'est pas possible, l'IA cherche � jouer un coup al�atoire qui ne permet pas � l'opposant de fermer un carr� */
simple_ai(Line) :-
	findall(L,(simple_ai(L,0),not(critical(L))),Lines),
	length(Lines,N),
	random(1,N,R),
	nth1(R,Lines,Line),!.
/* En dernier recours, l'IA joue tout simplement. */
simple_ai(Line) :-
	simple_ai(Line,0),!.

:-assert(p1_play(L):-simple_ai(L)).
:-assert(p2_play(L):-simple_ai(L)).
	
:- dynamic start/0 .
start :- consult('main_file.pl'), play.