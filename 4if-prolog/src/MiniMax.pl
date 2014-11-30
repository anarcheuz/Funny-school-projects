:- dynamic ai_play/1 .



ai_play(Move) :- free_lines(X),
    ai_play([], X, 3, 1, -999, +999, _, 0, Move),!.

/* ai_play(+Moves,+Position,+Depth,+Player,+Alpha,+Beta,+Move0,
            -BestValue,-BestMove)
      Chooses the Best move from the list of Moves from the current Position
      using the alpha beta algorithm searching Depth ply ahead.
      Player indicates if the next move is by player (1) or opponent (-1).
      Move0 records the best move found so far and Alpha its value.
      If a value >= Beta is found, then this position is too good to be true:
      the opponent will not move us into this position.
*/


ai_play(_,_,0,_,_,_,_,_,_). %Depth atteint
ai_play(_,[],_,_,_,_,_,_,_). %Plus dautre coup à tester
ai_play(Before,[Move|After],D, Player, Alpha, Beta, Move0, BestValue, BestMove):-
  D > 0,
  append(Before,After,Moves),
  D1 is (D - 1),
/* gestion du prochain jouer */
  ( close_square( move ) -> 
    Value1 is BestValue + Player,
    ai_play([], Moves, D1, Player, Alpha, Beta, Move, Value1, _BestMove), %rejouter %On entre dans le noeud plus profond
    Value is Value1
  ; Opponent is -Player,
    OppAlpha is -Beta,
    OppBeta is -Alpha,
    ai_play([], Moves, D1, Opponent, OppAlpha, OppBeta, Move, BestValue, _OppMove), %On entre dans le noeud plus profond
    Value is -BestValue
  ),
  append(Before,[Move],Before1), %On passe move (le coup testé) dans la liste des coups testés pour essayer depuis le meme noeud avec les autres coups restant
/* suite en fonction de la valeur */
  ( Value >= Beta *->
    BestValue = Value, BestMove = Move % abort: too good to be true
  ; Value > Alpha *->
    ai_play(Before1, After, D, Player, Value, Beta, Move, BestValue, Move), BestMove = Move
  ; ai_play(Before1, After, D, Player, Alpha, Beta, Move0,BestValue, Move0), BestMove = Move0
  ).

:- consult('main_file.pl').