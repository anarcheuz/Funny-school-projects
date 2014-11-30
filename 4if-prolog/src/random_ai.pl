:- dynamic ai_play/1 .
:- dynamic free_lines/1 .

:- use_module(library(random)).

random_ai(Line):-
	free_lines(X),
	length(X,N),
	random(1,N,R),
	nth1(R,X,Line).
	

:-assert(p1_play(L):-random_ai(L)).
:-assert(p2_play(L):-random_ai(L)).
	
:- dynamic start/0 .
start :- consult('main_file.pl'), play.