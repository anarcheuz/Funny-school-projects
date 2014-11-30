
%util
element(X,[X|_]).
element(X,[_|L]) :- element(X,L).

element(X,[X|R],R).
element(X2,[T|Q],[T|R2]):-
	element(X2,Q,R2).

direction([X,Y],[U,V],west):-
	U is X-1.0,
	V is Y.
direction([X,Y],[U,V],east):-
	U is X+1.0,
	V is Y.
direction([X,Y],[U,V],north):-
	U is X,
	V is Y-1.0.
direction([X,Y],[U,V],south):-
	U is X,
	V is Y+1.0.
direction([X,Y],[U,V],north_west):-
	U is X-0.5,
	V is Y-0.5.
direction([X,Y],[U,V],north_east):-
	U is X+0.5,
	V is Y-0.5.
direction([X,Y],[U,V],south_east):-
	U is X+0.5,
	V is Y+0.5.
direction([X,Y],[U,V],south_west):-
	U is X-0.5,
	V is Y+0.5.
	
horizontal([X,_]) :- Y is (round((X+1)*2) mod 2), Y == 0.
vertical(X) :- not(horizontal(X)).

/* Tests sur la fermeture du carré  */
close_square( [X,Y] , bottom):-
	horizontal([X,Y]),
	V is Y - 0.5, U is X - 0.5, played([U,V]),
	T is X + 0.5, played([T,V]),
	W is Y-1, played([X,W]).
close_square( [X,Y] , top):-
	horizontal([X,Y]),
	V is Y + 0.5, U is X - 0.5, played([U,V]),
	T is X + 0.5, played([T,V]),
	W is Y+1, played([X,W]).
close_square( [X,Y] , right):-
	vertical([X,Y]),
	V is Y - 0.5, U is X - 0.5, played([U,V]),
	T is Y + 0.5, played([U,T]),
	W is X-1, played([W,Y]).
close_square( [X,Y] , left):-
	vertical([X,Y]),
	V is Y + 0.5, U is X + 0.5, played([U,V]),
	T is Y - 0.5, played([U,T]),
	W is X+1, played([W,Y]).
	
close_square( L ) :- close_square( L, _ ).
	
close_count( L, N) :- findall(D, close_square(L,D), Xs), length(Xs,N).

/* Test pour savoir si une cellule est critique*/

neighbours(Pos,bottom,N):-
	horizontal(Pos),neighbours_count(Pos,[south_west, south, south_east],N).
neighbours(Pos,bottom,N):-
	horizontal(Pos),neighbours_count(Pos,[north_west, north, north_east],N).
neighbours(Pos,bottom,N):-
	vertical(Pos),neighbours_count(Pos,[south_west, west, north_west],N).
neighbours(Pos,bottom,N):-
	vertical(Pos),neighbours_count(Pos,[south_east, east, north_east],N).
	
neighbours_count(Pos,D,N):-
	D = [_|_],
	findall( Dir, (direction(Pos,PosDir,Dir),element(Dir,D),played(PosDir)), Dirs ),
	length(Dirs,N).
	
critical(Pos) :- neighbours(Pos,_,2).
	
	


update_player(TurnScore) :-
	TurnScore == 0,
	player(ai),
	print_update_player(TurnScore),
	retract(player(ai)),
	assert(player(user)),!.
	
update_player(TurnScore) :-
	TurnScore == 0,
	player(user),
	print_update_player(TurnScore),
	retract(player(user)),
	assert(player(ai)),!.
	
update_player(TurnScore):-
	print_update_player(TurnScore).

print_update_player(TurnScore) :-
	player(X)/*,
	write(X),
	write(' filled '),
	write(TurnScore),
	write(' box(es).\n')*/.

/*
	Artificial intelligence
*/
%
/*
 UI handler
*/

draw_cases(_,_,0).
draw_cases(Line,Player,1):-
	draw_cases_dir(Line,Player,_).
draw_cases(Line,Player,2):-
	draw_cases_dir(Line,Player,X),
	draw_cases_dir(Line,Player,Y),
	X \= Y.

draw_cases_dir([X,Y],Player, top):-
	close_square([X,Y],bottom),
	YY is Y-0.5,
	fill_square([X,YY],Player).
draw_cases_dir([X,Y],Player, bottom):-
	close_square([X,Y],top),
	YY is Y+0.5,
	fill_square([X,YY],Player).
draw_cases_dir([X,Y],Player,right):-
	close_square([X,Y],left),
	XX is X+0.5,
	fill_square([XX,Y],Player).
draw_cases_dir([X,Y],Player, left):-
	close_square([X,Y],right),
	XX is X-0.5,
	fill_square([XX,Y],Player).

/*
	Plays handler
*/


play(ai,L) :-
	p1_play(L).%,write('Computer : '),write(L),write('\n').
	
play(user,L) :-
	p2_play(L).%,write('User : '),write(L),write('\n').

play :- 
	player(Player),
	play(Player, Line),
	close_count(Line,Count),
	%write('Count : '),write(Count),write('\n'),
	%write('Coups joués : '),findall(X,played(X),Xs),write(Xs),write('\n'),
	update_score(Player,Count),
	free_lines(F),
	element(Line, F, R),
	retract(free_lines(_)),
	assert(free_lines(R)),
	assert(played(Line)),
	update_player(Count),
	draw_line(Line,Player),
	draw_cases(Line,Player,Count),
	sleep(0.01),
	%write('----------------------\n'),
	play,
	!.
play :-
	free_lines(F),
	F == [],
	write('Game Over\n'),
	score(ai,AI),
	score(user,US),
	analytics_stream(Stream),
	write(Stream,AI), write(Stream,'\t'),write(Stream,US),write(Stream,'\n'),
	write('AI : '),write(AI),
	write('\nYou : '),write(US),write('\n'),
	print_end(AI,US),!.

print_end(AI,US) :-
	AI < US,
	write('You won, maybe you\'re not so dumb afterall...\n').

print_end(AI,US) :-
	AI > US,
	write('lol, buy a brain and come back !\n').

print_end(AI,US) :-
	AI == US,
	write('Just because you didn\'t loose, doesn\'t mean you won !\n').


update_score(Player,Score):- score(Player,N), M is N + Score, retract(score(Player,_)), assert(score(Player,M)).


/* dynamic variables declaration */
:- dynamic free_lines/1 .
:- dynamic played/1 .
:- dynamic score/2 .
:- dynamic p1_play/1 .
:- dynamic p2_play/1 .
:- dynamic player/1 .
:- dynamic width_board/1 .
:- dynamic height_board/1 .
:- dynamic analytics_stream/1 .

/* INIT */
init_game:-
	assert(player(ai)),
	assert(score(ai,0)),
	assert(score(user,0)),
	consult('plateau.pl'),
	create_display(400),
	free_lines(X),
	draft_lines(X),
	open('analytics/analytics.txt',append,Stream),
	assert(analytics_stream(Stream)).
	
draft_lines([]).
draft_lines([H|T]):- draft_line(H), draft_lines(T).
	
clear_game:-
	retract(player(_)),
	retract(score(ai,_)),
	retract(score(user,_)),
	retract(free_lines(_)),
	retractall(played(_)),
	retract(width_board(_)),
	retract(height_board(_)),
	analytics_stream(Stream),
	close(Stream),
	retract(analytics_stream(_)),
	close_ui.

restart_game:- clear_game, init_game, play.

:-
	consult('ui.pl').
:- init_game.
	
/*
:-
	play,play,play,play,play,play,play.
*/