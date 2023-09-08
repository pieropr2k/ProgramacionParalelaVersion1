import java.awt.Font;
import java.awt.Color;

import javax.swing.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class DS_Thread_Parallel {
    private static int NumberOfInstances = 4; // Número de instancias de JFrames
    // Crear un arreglo de JFrames
    private static JFrame[] WDWList = new JFrame[NumberOfInstances];
    private static JScrollPane[] SPList = new JScrollPane[NumberOfInstances];
    private static JTextArea[] TAList = new JTextArea[NumberOfInstances];
    private static Font fntLABEL = new Font("Arial", Font.BOLD, 24);
    private static Font fntTEXT = new Font("Lucida Console", Font.BOLD, 18);
    private static JLabel[] LBLStarterList = new JLabel[NumberOfInstances];
    private static JLabel[] LBLFinishList = new JLabel[NumberOfInstances];
    private static final int N = 150000;
    private static int[][] VList = new int[NumberOfInstances][N];

    private static int[] ESTADO = new int[NumberOfInstances + 1];

    // ==============================================================================
    public static void ConfigurarControles(JFrame WDW,
            int WW,
            int HH,
            int LEFT,
            int TOP,
            JScrollPane SP,
            JTextArea TA,
            JLabel LBLStart,
            JLabel LBLFinish) {

        WDW.setSize(WW, HH);
        WDW.setLocation(LEFT, TOP);
        WDW.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        WDW.setVisible(true);

        LBLStart.setBounds(25, 20, 300, 40);
        LBLFinish.setBounds(5, 10, 300, 40);

        LBLStart.setFont(fntLABEL);
        LBLFinish.setFont(fntLABEL);

        // LBLStart.setBounds(spLeft,10,lblWidth,lblHeight);
        // LBLFinish.setBounds(spLeft,10+spHeight+10,lblWidth,lblHeight);

        TA.setEditable(false);
        TA.setBounds(25, 60, 300, 500);
        TA.setBackground(Color.WHITE);
        TA.setFont(fntTEXT);
        TA.setForeground(Color.GREEN);
        TA.setBackground(Color.BLACK);

        SP = new JScrollPane(TA);
        SP.setBounds(25, 50, 300, 500);

        WDW.add(LBLStart);
        WDW.add(SP);
        WDW.add(LBLFinish);
        WDW.setVisible(true);

    }

    // ==============================================================================
    private static void LoadVector() {
        ESTADO[1] = 0; // Libre
        ESTADO[2] = 0;
        ESTADO[3] = 0;
        ESTADO[4] = 0;
        for (int i = 0; i < NumberOfInstances; i++) {
            ESTADO[i] = 0;
        }
        Random r = new Random();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < NumberOfInstances; j++) {
                VList[j][i] = r.nextInt(100000);
            }
        }
    }

    // ==============================================================================
    public static void main(String[] args) throws InterruptedException {
        // Crear las instancias de JFrame y configurarlas
        for (int i = 0; i < NumberOfInstances; i++) {
            WDWList[i] = new JFrame(String.format("Program %s", i + 1));
            SPList[i] = new JScrollPane();
            TAList[i] = new JTextArea("");
            LBLStarterList[i] = new javax.swing.JLabel();
            LBLFinishList[i] = new javax.swing.JLabel();
        }
        AtomicInteger[] AIList = new AtomicInteger[N];
        for (int i = 0; i < NumberOfInstances; i++) {
            AIList[i] = new AtomicInteger(1); // AI1.get()==1
        }
        LoadVector();
        for (int i = 0; i < NumberOfInstances; i++) {
            ConfigurarControles(WDWList[i], 500, 800, 100 + 215 * i, 40, SPList[i], TAList[i], LBLStarterList[i],
                    LBLFinishList[i]);
        }
        // Version Paralela
        for (int i = 0; i < NumberOfInstances; i++) {
            // -------------------------------------------------
            final int i_iterator = i;
            new Thread(new Runnable() { // Thread.sleep(1000);
                public void run() {
                    long inicio = System.currentTimeMillis();
                    LBLStarterList[i_iterator].setText("Time Execution: " + inicio / 1000 + " segundos");
                    //BubbleSort(VList[i_iterator], TAList[i_iterator]);
                    //BubbleSort2(VList[i_iterator], TAList[i_iterator]);
                    ProcessFunction(i_iterator, VList[i_iterator], TAList[i_iterator], N);

                    AIList[i_iterator].set(0);
                    long fin = System.currentTimeMillis() - inicio;
                    LBLFinishList[i_iterator].setText("Time Execution: " + fin / 1000 + " segundos");
                    System.out.println("\n");
                }
            }).start();
        }
    }

    // ==============================================================================
    public static void ProcessFunction(int iteration_value, int V[], JTextArea TA, int B) {
        switch (iteration_value) {
            case 0:
                BubbleSort(V, TA);
                break;
            case 1:
                BubbleSort2(V, TA);
                break;
            case 2:
                //QuickSort(V, 1, B, TA);
                EvenNumbers(V, TA);
                break;
            case 3:
                FibonacciNumbers(V, TA);
                break;
            case 4:
                OddNumbers(V, TA);
                //BubbleSort2(V, TA);
                break;
            default:
                break;
        }
    }

    // ==============================================================================
    public static int Partition(int V[], int A, int B) {
        int E, P, TMP;
        P = A;
        E = V[B];
        for (int i = A; i < B; i++) {
            if (V[i] <= E) {
                TMP = V[i];
                V[i] = V[P];
                V[P] = TMP;
                P++;
            }
        }
        TMP = V[B];
        V[B] = V[P];
        V[P] = TMP;
        return P;
    }

    // ==============================================================================
    public static void QuickSort(int V[], int A, int B, JTextArea TA) {
        int E;
        if (A < B) {
            TA.append("Partition [" + A + "," + B + "]\n");
            E = Partition(V, A, B);
            QuickSort(V, A, E - 1, TA);
            QuickSort(V, E + 1, B, TA);
        }
    }

    // ==============================================================================
    public static void BubbleSort(int V[], JTextArea TA) {
        int n, tmp;
        n = V.length;
        TA.setText("");
        for (int k = 1; k <= n - 1; k++) {
            TA.append("Cycle " + k + "\n");
            for (int i = 0; i <= n - k - 1; i++) {
                if (V[i] > V[i + 1]) {
                    tmp = V[i];
                    V[i] = V[i + 1];
                    V[i + 1] = tmp;
                }
            }
        }
    }

    // ==============================================================================
    public static void BubbleSort2(int V[], JTextArea TA) {
        boolean Sw;
        int k, n, tmp;
        n = V.length;
        TA.setText("");
        Sw = true;
        k = 1;
        while ((k <= n - 1) && (Sw == true)) {
            TA.append("Cycle " + k + "\n");
            Sw = false;
            for (int i = 0; i <= n - k - 1; i++) {
                if (V[i] > V[i + 1]) {
                    Sw = true;
                    tmp = V[i];
                    V[i] = V[i + 1];
                    V[i + 1] = tmp;
                }
            }
            k++;
        }
    }

    // ==============================================================================
    public static void EvenNumbers(int V[], JTextArea TA) {
        int n = V.length;
        TA.setText("");
        for (int k = 1; k <= n - 1; k++) {
            if (k%2 == 0) {
                TA.append("Cycle " + k + "\n");    
            }
            //TA.append("Cycle " + k + "\n");
        }
    }

    // ==============================================================================
    public static void OddNumbers(int V[], JTextArea TA) {
        int n = V.length;
        TA.setText("");
        for (int k = 1; k <= n - 1; k++) {
            if (k%2 != 0) {
                TA.append("Cycle " + k + "\n");    
            }
            //TA.append("Cycle " + k + "\n");
        }
    }

    // ==============================================================================
    public static void FibonacciNumbers(int V[], JTextArea TA) {
        int n = V.length;
        TA.setText("");
        for (int k = 1; k <= n - 1; k++) {
            TA.append("Cycle " + FibR(k) + "\n");
        }
    }

    // ==============================================================================
    public static long FibR(int n) {
        if (n == 1) {
            return 0;
        } else if (n == 2) {
            return 1;
        } else {
            return FibR(n - 2) + FibR(n - 1);
        }
    }

    public void ImprimirSecuenciaFibonacci(int N) {
        for (int i = 1; i <= N; i++) {
            System.out.println(i + "\n" + FibR(i));
        }
    }

}
