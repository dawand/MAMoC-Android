package uk.ac.standrews.cs.mamoc.Benchmark;

import uk.ac.st_andrews.cs.mamoc_client.Annotation.Offloadable;

@Offloadable
public class Benchmark {

    private static final int fft_size= 1024;

    public double run() {

        Random R = new Random(Constants.RANDOM_SEED);

        double min_time = Constants.RESOLUTION_DEFAULT;

        return measureFFT(fft_size, min_time, R);
    }

    private double measureFFT(int N, double mintime, Random R)
    {
        // initialize FFT data as complex (N real/img pairs)

        double x[] = RandomVector(2*N, R);
        double oldx[] = NewVectorCopy(x);
        long cycles = 1;
        Stopwatch Q = new Stopwatch();

        while(true)
        {
            Q.start();
            for (int i=0; i<cycles; i++)
            {
                FFT.transform(x);	// forward transform
                FFT.inverse(x);		// backward transform
            }
            Q.stop();
            if (Q.read() >= mintime)
                break;

            cycles *= 2;
        }
        // approx Mflops

        final double EPS = 1.0e-10;
        if ( FFT.test(x) / N > EPS )
            return 0.0;

        return FFT.num_flops(N)*cycles/ Q.read() * 1.0e-6;
    }

    private static double[] NewVectorCopy(double x[])
    {
        int N = x.length;

        double y[] = new double[N];
        for (int i=0; i<N; i++)
            y[i] = x[i];

        return y;
    }

    private static void CopyVector(double B[], double A[])
    {
        int N = A.length;

        for (int i=0; i<N; i++)
            B[i] = A[i];
    }


    private static double normabs(double x[], double y[])
    {
        int N = x.length;
        double sum = 0.0;

        for (int i=0; i<N; i++)
            sum += Math.abs(x[i]-y[i]);

        return sum;
    }

    private static void CopyMatrix(double B[][], double A[][])
    {
        int M = A.length;
        int N = A[0].length;

        int remainder = N & 3;		 // N mod 4;

        for (int i=0; i<M; i++)
        {
            double Bi[] = B[i];
            double Ai[] = A[i];
            for (int j=0; j<remainder; j++)
                Bi[j] = Ai[j];
            for (int j=remainder; j<N; j+=4)
            {
                Bi[j] = Ai[j];
                Bi[j+1] = Ai[j+1];
                Bi[j+2] = Ai[j+2];
                Bi[j+3] = Ai[j+3];
            }
        }
    }

    private static double[][] RandomMatrix(int M, int N, Random R)
    {
        double A[][] = new double[M][N];

        for (int i=0; i<N; i++)
            for (int j=0; j<N; j++)
                A[i][j] = R.nextDouble();
        return A;
    }

    private static double[] RandomVector(int N, Random R)
    {
        double A[] = new double[N];

        for (int i=0; i<N; i++)
            A[i] = R.nextDouble();
        return A;
    }

    private static double[] matvec(double A[][], double x[])
    {
        int N = x.length;
        double y[] = new double[N];

        matvec(A, x, y);

        return y;
    }

    private static void matvec(double A[][], double x[], double y[])
    {
        int M = A.length;
        int N = A[0].length;

        for (int i=0; i<M; i++)
        {
            double sum = 0.0;
            double Ai[] = A[i];
            for (int j=0; j<N; j++)
                sum += Ai[j] * x[j];

            y[i] = sum;
        }
    }
}
