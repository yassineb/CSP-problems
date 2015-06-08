import solver.Solver;
import solver.constraints.ICF;
import solver.variables.IntVar;
import solver.variables.VariableFactory;

/**
 *  Model:
 *      X = {Xi ∈ D=[0-9] / i ∈ N}
 *      Constraints:
 *          S, M != 0
 *          Alldifferent(Xi / i ∈ N)
 *          SEND + MORE = MONEY
 */

public class SendMoreMoney {
    static int N = 8;

    static String[] letters = {"S","E","N","D","M","O","R","Y"};
    static IntVar[] X;


    public static void main(String[] args)
    {
        Solver solver = new Solver("Send + More = Money");
        IntVar[] L = new IntVar[N];

        for (int i = 0; i < N; i++) {
            L[i] = VariableFactory.enumerated(letters[i], 0, 9, solver);
        }


        solver.post(ICF.arithm(L[0], "!=", 0));
        solver.post(ICF.arithm(L[4], "!=", 0));

        solver.post(ICF.alldifferent(L, "BC"));

        X = new IntVar[]{
                L[0], L[1], L[2], L[3], // SEND
                L[4], L[5], L[6], L[1], // MORE
                L[4], L[5], L[2], L[1], L[7] // MONEY
        };

        int[] coeffs = new int[]{
                1000, 100, 10, 1,
                1000, 100, 10, 1,
                -10000, -1000, -100, -10, -1
        };

        solver.post(ICF.scalar(X, coeffs, VariableFactory.fixed(0, solver)));

        solver.findSolution();

        for (int i = 0; i < X.length; i++)
            System.out.printf("%2s", X[i].getName());

        System.out.println("\n\n" + solver.getMeasures().toString());
    }
}
