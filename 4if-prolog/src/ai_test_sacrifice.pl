:-
	dynamic ai_play/1.

:- dynamic max/1.
:- dynamic potential_free_list/1.
:- dynamic potential_played_chain/1.

ai_play(Line,Score) :-
	free_lines(Lines),
	element(Line,Lines,_),
	close_count(Line,Score),
	(Score =:= 1  *->
	/* gérer l'enchainement de coups */
	free_lines(F),
	assert(potential_free_list(F)),
	element(Line,F,R),
	retract(potential_free_list(_)),
	assert(potential_free_list(R)),
	assert(potential_played_chain(Line))
	;
	).
ai_play(Line) :-
	ai_play(Line,1),
	 
	
	!. /*améliorer avec sacrifice si plus de 2 */
ai_play(Line) :-
	ai_play(Line,0),
	not(critical(Line)), !.	
ai_play(Line) :-
	ai_play(Line,0),!. 


	

	
	
consult('main_file.pl').