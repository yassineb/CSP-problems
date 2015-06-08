import solver.Solver;
import solver.constraints.ICF;
import solver.variables.IntVar;
import solver.variables.VariableFactory;

/**
 * The N-Queens problem consists of placing n queens on a n×n chessboard
 * so that none of the queens can attack each other.
 */

public class NQueens {
    static int N = 8;

    public static void main(String[] args) {
        Solver solver = new Solver("N-Queens");
        IntVar[] X = new IntVar[N];

        for (int i = 0; i < N; i++)
            X[i] = VariableFactory.enumerated("Q" + (i + 1), 1, N, solver);


        IntVar[] diag1 = new IntVar[N];
        IntVar[] diag2 = new IntVar[N];

        for (int i = 0; i < N; i++) {
            diag1[i] = VariableFactory.offset(X[i], i);
            diag2[i] = VariableFactory.offset(X[i], -i);
        }

        solver.post(ICF.alldifferent(X));
        solver.post(ICF.alldifferent(diag1));
        solver.post(ICF.alldifferent(diag2));

        solver.findSolution();

        do {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (X[i].getValue() == (j + 1))
                        System.out.printf("%3s", "Q" + X[j].getValue() + " |");
                    else
                        System.out.print("___|");
                }
                System.out.println();
            }
            System.out.println("\n");
        } while (solver.nextSolution());

        System.out.println("\n\n" + solver.getMeasures().toString());
    }
}
