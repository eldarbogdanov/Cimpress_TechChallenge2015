**************************** Cimpress TechChallenge ****************************

******* Architecture overview *******
My solution is comprised of two parts:
1) The client that connects to techchallenge server, downloads the puzzle, feeds
it to the solver, gets the response back and sends it to the server. The client
is implemented in javascript, I was using node.js to run it locally.
2) The solver that, given a puzzle, tries to cover it optimally. The solver
receives the puzzle from stdin and prints the solution to stdout. The solver is
implemented in Java 7. It has multiple strategies (although one proved to be
substantially better than the others).

******* Best strategy overview *******
The strategy that gave me the best results is a variation of genetic algorithm.
It works iteratively and has a population of partially covered puzzles at each
iteration (the population contains only the initial puzzle before the process
begins).
For each member of the current population, the strategy finds the "first"
uncovered cell and tries to put squares of all applicable sizes starting at that
cell, which spawns the members of the next iteration's population.
Obviously, the growth is exponential so there is a limit imposed on the maximum
size of a population. To choose which members should survive, the partial
solution is completed in a greedy manner and we keep the members that have the
minimum number of squares in this greedy approximation. See the overview of
GreedySolverOptimized for details of this greedy strategy.
The "first" mentioned above refers to the first cell in some order of iteration:
we could traverse the horizontal or vertical direction first, and start from top
or bottom and from left or right.
This idea is implemented through GeneticSolver+ContourSquareChooser. I was
running it in 4 threads with different traversal directions. This logic is
implemented in MultiThreadDistributorContour.

******* Other strategies overview *******
My less successful strategies, in chronological order:
GreedySolver -- at every step, finds the first uncovered cell in the puzzle and
puts the greatest possible square starting there. The traversal order can be
imposed at construction time (horizontal/vertical first, from top to bottom or
vice versa, from left to right or vice versa).
GreedySolverOptimized -- chooses the best result from GreedySolver's traversing
in all 8 possible ways.
BruteForceSolver -- tries to cover one square, which it chooses from all
possible squares of all sizes. For each choice, calls another strategy that
will complete the puzzle. The strategy to be called is plugable, although
the only logical choices for it are the greedy ones; anything else would time
out.
GeneticSolver+RandomSquareChooser -- this is another take at genetic algorithm,
but this time, unlike with solution described above, for each member of current
population the squares that will augment this member are picked randomly. The
set of squares to pick from is defined as the squares rooted in cells that
neighbor covered cells in two orthogonal directions. The size of the squares
is maximum applicable for each position.

