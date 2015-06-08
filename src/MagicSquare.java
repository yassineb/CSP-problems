import solver.Solver;
import solver.constraints.IntConstraintFactory;
import solver.variables.IntVar;
import solver.variables.VariableFactory;


/**
 * "An order n magic square is a n by n matrix containing the numbers 1 to n^2, with each row,
 * column and main diagonal equal the same sum.
 * As well as finding magic squares, we are interested in the number of a given size that exist."
 *
 * Model:
 *      X = {Xij : i, j ∈ n*n}
 *      M = n * (n*n + 1) / 2
 *      Constraints:
 *          Cvars = AllDifferent(Xij, i,j ∈ n, n);
 *          Crows = {Sum(Xij / j in n) = M / i ∈ n}
 *          Ccolumn = {Sum(Xij / i in n) = M / j ∈ n}
 *          Cdiagonals1 = {Sum(Xii) = M / i ∈ n}
 *          Cdiagonals2 = {Sum(X(n-1-i, i)) = M / i ∈ n}
 *
 */

public class MagicSquare {
    public static void main(String[] args) {
        int n = 3;
        Solver solver = new Solver("Magic Square");
        int M = n * (n * n + 1) / 2;

        IntVar[][] matrix = new IntVar[n][n];
        IntVar[][] imatrix = new IntVar[n][n]; // inverse matrix
        IntVar[] vars = new IntVar[n * n];

        int k = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++, k++) {
                matrix[i][j] = VariableFactory.enumerated("S" + i + "," + j, 1, n * n, solver);
                imatrix[j][i] = matrix[i][j];

                vars[k] = matrix[i][j];
            }
        }

        IntVar[] diag1 = new IntVar[n];
        IntVar[] diag2 = new IntVar[n];

        for (int i = 0; i < n; i++) {
            diag1[i] = matrix[i][i];
            diag2[i] = matrix[n - 1 - i][i];
        }

        solver.post(IntConstraintFactory.alldifferent(vars, "BC"));

        IntVar Mv = VariableFactory.fixed(M, solver);
        for (int i = 0; i < n; i++) {
            solver.post(IntConstraintFactory.sum(matrix[i], "=", Mv));
            solver.post(IntConstraintFactory.sum(imatrix[i], "=", Mv));
        }

        solver.post(IntConstraintFactory.sum(diag1, "=", Mv));
        solver.post(IntConstraintFactory.sum(diag2, "=", Mv));

        // symmetry breaking
        solver.post(IntConstraintFactory.arithm(matrix[0][n - 1], "<", matrix[n - 1][0]));
        solver.post(IntConstraintFactory.arithm(matrix[0][0], "<", matrix[n - 1][n - 1]));
        solver.post(IntConstraintFactory.arithm(matrix[0][0], "<", matrix[n - 1][0]));
        solver.findSolution();

        do {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++)
                    System.out.printf("%3s %s", matrix[i][j].getValue(), "|");

                System.out.print("\n");
            }
            System.out.print("\n\n");
        } while (solver.nextSolution());

        System.out.println(solver.getMeasures().toString());
    }
}