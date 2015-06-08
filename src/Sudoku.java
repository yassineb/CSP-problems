import solver.Solver;
import solver.constraints.ICF;
import solver.variables.IntVar;
import solver.variables.VariableFactory;

/**
 *
 * The objective is to fill a 9×9 grid with digits so that each column, each row, and each of the nine 3×3 sub-grids
 * that compose the grid (also called "boxes", "blocks", "regions", or "sub-squares")
 * contains all of the digits from 1 to 9
 *
 * Model:
 *      X = {Xi / i, j in [0, 8]}
 *      D = {D(i,j) = [1, 9] / i, j in [0, 8]}
 *      Crows = { Alldiff(Xij / for all j in [0, 8]) / i in [0, 8]}
 *      Ccolumns = {Alldiff(Xij for all i in [0, 8]) / j in [0, 8]}
 *      Csubgrids = {AllDiff(Xkl / i,j in [0, 8] }
 *      k = j % 3 + (i % 3) * 3
 *      l = j % 3 + (i/3) * 3
 */

public class Sudoku {
    static int N = 9;

    public static void main(String[] args) {
        Solver solver = new Solver("Sudoku");
        /*int[][] grid = {
            {3, 0, 0,  0, 5, 0,  0, 0, 2},
            {0, 0, 0,  9, 0, 2,  0, 0, 8},
            {5, 0, 0,  0, 0, 4,  0, 9, 0},

            {6, 3, 0,  0, 7, 0,  8, 0, 0},
            {0, 0, 0,  0, 0, 0,  3, 0, 0},
            {0, 1, 5,  6, 0, 9,  0, 4, 0},

            {0, 7, 0,  0, 0, 1,  0, 2, 0},
            {0, 0, 0,  0, 0, 6,  0, 0, 3},
            {0, 0, 0,  2, 0, 0,  0, 0, 0}
        };*/

        int[][] grid = {
            {4, 0, 0,  0, 9, 2,  0, 0, 0},
            {0, 0, 0,  0, 0, 0,  9, 0, 0},
            {3, 0, 0,  0, 6, 0,  0, 0, 4},
            {0, 3, 0,  0, 0, 0,  1, 0, 0},
            {6, 0, 0,  0, 0, 0,  0, 0, 0},
            {8, 7, 2,  1, 0, 0,  0, 0, 0},
            {1, 0, 0,  7, 0, 0,  0, 3, 0},
            {0, 0, 0,  0, 0, 0,  0, 7, 6},
            {2, 0, 0,  0, 5, 9,  0, 0, 0}
        };

        IntVar[][] X = new IntVar[N][N];
        IntVar[][] iX = new IntVar[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                X[i][j] = (grid[i][j] > 0)
                            ? VariableFactory.fixed(grid[i][j], solver)
                            : VariableFactory.enumerated("S" + i + "," + j, 1, N, solver);
                iX[j][i] = X[i][j];
            }

        }

        IntVar[] subgrid = new IntVar[N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                subgrid[j] = X[j % 3 + (i % 3) * 3][j/3 + i/3 * 3];

            solver.post(ICF.alldifferent(X[i], "AC"));
            solver.post(ICF.alldifferent(iX[i], "AC"));
            solver.post(ICF.alldifferent(subgrid, "AC"));
        }

        solver.findSolution();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                System.out.printf("%2s", X[i][j].getValue());

            System.out.println("");
        }

        System.out.println("\n\n" + solver.getMeasures().toString());
    }
}
