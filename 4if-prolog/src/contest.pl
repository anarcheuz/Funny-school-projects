:- dynamic ai_play/1 .
			
				
:-	consult('minmax_ai.pl'),consult('simple_ai.pl').
:-	retractall(p1_play(_)),
	assert(p1_play(L):- simple_ai(L)),
	retractall(p2_play(_)),
	assert(p2_play(L):- minmax_ai(L)).

contest(N):-
	N > 0,
	init_minmax_ai,
	start,
	contest_score,
	M is N-1,
	recontest(M),
	write('Player 1 : '),cscore(p1,S1),write(S1),write('\n'),
	write('Player 2 : '),cscore(p2,S2),write(S2),write('\n'),
	write('Null     : '),S0 is N - S1 - S2, write(S0), write('\n'),!.

recontest(0).
recontest(N):-
	N > 0,
	clear_minmax_ai,
	init_minmax_ai,
	restart_game,
	contest_score,
	M is N-1,
	write(M),write('\n'),
	recontest(M),!.

:- dynamic cscore/2 .	
cscore(p1,0).
cscore(p2,0).

contest_score:-
	cscore(p1,N),
	cscore(p2,M),
	retractall(cscore(_,_)),
	score(ai,A),
	score(user,B),
	contest_score(A,B,N,M,X,Y),
	assert(cscore(p1,X)),
	assert(cscore(p2,Y)).

contest_score(A,B,N,M,X,Y):-
	A == B,
	X is N,
	Y is M.
contest_score(A,B,N,M,X,Y):-
	A > B,
	X is N+1,
	Y is M.
contest_score(A,B,N,M,X,Y):-
	A < B,
	X is N,
	Y is M+1.