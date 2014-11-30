:- dynamic ai_play/1 .
:- dynamic free_lines/1 .

click_line(P, Position) :-
	get(Position, x, X1),
	screen_w(W),
	width_board(WB),
	X is X1 * WB / W - 0.75,
	%write('X : '),write(X),write('\n'),
	get(Position, y ,Y1) ,
	screen_h(H),
	height_board(HB),
	Y is Y1 * HB / H - 0.75,
	%write('Y : '),write(Y),write('\n'),
	%send(P, display, new(C, circle(25)), Position),
	free_lines(Lines),
	closest_line([X,Y],Lines,L,D),
	D < 0.5,
	tell(out),
	write(L),write('.'),
	told.

closest_line([X,Y],[[U,V]|[]],[U,V],D):- D is (X - U)*(X - U) + (Y - V)*(Y - V).
closest_line([X,Y],[[U,V]|T],[A,B],D):-
	closest_line([X,Y],T,[A2,B2],D2),
	D1 is ((X - U)*(X - U) + (Y - V)*(Y - V)),
	( D1 < D2 -> (A = U, B = V, D = D1) ; (A = A2, B = B2, D = D2) ). 
	
/*
player_ai(L) :- sleep(1), 
	(						%if-then-else statement
		played() ->
		getLine(L), retract(played(True))
		;player_ai(L)
	).
*/

player_ai(L):-
	see(out),
	read(R),
	%write('read : '),write(L),write('\n'),
	seen,
	tell(out),
	write('0.'),
	told,
	R \= 0 ->
	L = R.
player_ai(L):-
	sleep(0.2),
	player_ai(L).

player_clicked([0.0,0.0]).

:-assert(p1_play(L):-player_ai(L)).
:-assert(p2_play(L):-player_ai(L)).
	
:- dynamic start/0 .
start :- consult('main_file.pl'),
send(@p, recogniser, click_gesture(left, '', single, 
	message(@prolog, click_line, @p, @event?position))), 
play.