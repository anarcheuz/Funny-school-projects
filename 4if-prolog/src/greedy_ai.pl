:- dynamic ai_play/1 .
:- dynamic free_lines/1 .

:- use_module(library(random)).
/* Recherche le coup à jouer permettant de marquer autant de point que Score */
greedy_ai(Line,Score) :-
	free_lines(Lines),
	element(Line,Lines,_),
	close_count(Line,Score).

/* L'ai cherche à remplir 2 carrés */
greedy_ai(Line) :-
	greedy_ai(Line,2),!.
/*Si ce n'est pas possible, l'ai cherche à remplir 1 carré */
greedy_ai(Line) :-
	greedy_ai(Line,1),!.
/* autrement l'ai joue un coup aléatoire */
greedy_ai(Line):-
	free_lines(X),
	length(X,N),
	random(1,N,R),
	nth1(R,X,Line).
	

:-assert(p1_play(L):-greedy_ai(L)).
:-assert(p2_play(L):-greedy_ai(L)).
	
:- dynamic start/0 .
start :- consult('main_file.pl'), play.