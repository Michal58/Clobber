# Clobber
Artificial intelligence for Clobber strategy game. 
There are used two algorithms for game tree search: 
MinMax and Alpha Beta Pruning. 

### Structure overview
- Components involved in game states representation and generation are 
located at [src/StateComponents](src/StateComponents).
- Game state is assessed by set of heuristics which are defined at [src/Evaluations](src/Evaluations).
- Heuristics for player strategy adaptation, algorithms and decisions components are located at [src/Players](src/Players).

### Running convention
There are three possible sets of arguments to run program in different modes:
1) `java -jar Clobber.jar` - then you will get comparison of different heuristics used for game states evaluation.
2) `java -jar Clobber.jar <arg>` - then you will see comparison of performance of algorithms MinMax and Alpha Beta - 'arg' can be any single argument.
3) `java -jar Clobber.jar <n> <m> <h> <d> <c> <ua> <ma> <up> <mp>` - enables game between two program instances.

| Argument | Explanation                                                                                                                                                                                                                                 |
|----------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| n        | Width of board                                                                                                                                                                                                                              |
| m        | Height of board                                                                                                                                                                                                                             |
| h        | Choice of heuristic - in range from 0 to 2 there are heuristics to take from [TreePlayer](src/Players/TreePlayer.java) enumerated strategies. Any other number selects [adaptable heuristic](src/Players/StrategyAdaptationHeuristic.java). |
| d        | Max depth for tree evaluation                                                                                                                                                                                                               |
| c        | Color of active player: `1` for white, `2` for black                                                                                                                                                                                        |
| ua       | Update channel path (file) for active player – used to write game state updates that the passive player reads                                                                                                                               |
| ma       | Move channel path (file) for active player – used to read opponent's (passive player's) responses and moves                                                                                                                                 |
| up       | Update channel path (file) for passive player – used to write game state updates that the active player reads                                                                                                                               |
| mp       | Move channel path (file) for passive player – used to read opponent's (active player's) moves                                                                                                                                               |

> Both instances must use reversed pairs of `ua/ma` and `up/mp` to ensure synchronized communication.
