import solver.Solver;
import solver.constraints.IntConstraintFactory;
import solver.variables.IntVar;
import solver.variables.VariableFactory;


/**
 * "A Latin square is an n x n array filled with n different Latin letters,
 *  each occurring exactly once in each row and exactly once in each column"
 *
 * Model:
 *      X = {Xij : i, j ∈ n}
 *      Constraints:
 *          Crows = Alldifferent(Xij for all j ∈ n / i ∈ n)
 *          Ccolumns = Alldifferent(Xij for all i ∈ n / j ∈ n)
 *
 */

public class LatinSquare {
    static int n = 20;

    public static void main(String[] args)
    {
        Solver solver = new Solver("Latin Square");

        IntVar[][] matrix = new IntVar[n][n];
        IntVar[][] imatrix = new IntVar[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = VariableFactory.enumerated(i + "," + j, 1, n, solver);
                imatrix[j][i] = matrix[i][j];
            }
        }

        for (int i = 0; i < n; i++) {
            solver.post(IntConstraintFactory.alldifferent(matrix[i], "AC"));
            solver.post(IntConstraintFactory.alldifferent(imatrix[i], "AC"));
        }

        solver.findSolution();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                System.out.printf("%4s", matrix[i][j].getValue());

            System.out.println("");
        }

        System.out.println(solver.getMeasures().toString());
    }
}
