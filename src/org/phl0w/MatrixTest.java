package org.phl0w;

/**
 * Multiplies two specified matrices.
 *
 * @author _phl0w
 */
public class MatrixTest {

    private static final double[][] MATRIX_1_ARRAY = {
            {0, 1, 1},
            {2, 3, 5},
            {8, 13, 21}
    };
    private static final double[][] MATRIX_2_ARRAY = {
            {2, 3, 5,},
            {7, 11, 13},
            {17, 19, 23}
    };

    public static void main(String[] args) {
        Matrix m1 = new Matrix(MATRIX_1_ARRAY);
        Matrix m2 = new Matrix(MATRIX_2_ARRAY);
        m1.multiplyWith(m2).show();
    }

    /**
     * Allows creation of LogEntry objects.
     *
     * @author _phl0w
     */
    public static class Matrix {
        private final int rows;
        private final int columns;
        private final double[][] array;

        /**
         * Creates a new Matrix object using the specified rows and columns.
         *
         * @param rows    the amount of rows for the Matrix object.
         * @param columns the amount of columns for the Matrix object.
         */
        public Matrix(int rows, int columns) {
            this.rows = rows;
            this.columns = columns;
            array = new double[rows][columns];
        }

        /**
         * Creates a new Matrix object using the specified data.
         *
         * @param data
         */
        public Matrix(double[][] data) {
            rows = data.length;
            columns = data[0].length;
            this.array = new double[rows][columns];
            for (int i = 0; i < rows; i++)
                System.arraycopy(data[i], 0, this.array[i], 0, columns);
        }

        /**
         * Calculates the Matrix of two multiplied matrices.
         *
         * @param b the Matrix to multiply with.
         * @return the multiplied Matrix.
         */
        public Matrix multiplyWith(Matrix b) {
            Matrix a = this;
            if (a.columns != b.rows) {
                System.err.println("Matrix A and Matrix B can not be multiplied");
            }
            Matrix c = new Matrix(a.rows, b.columns);
            for (int i = 0; i < c.rows; i++) {
                for (int j = 0; j < c.columns; j++) {
                    for (int k = 0; k < a.columns; k++) {
                        c.array[i][j] += (a.array[i][k] * b.array[k][j]);
                    }
                }
            }
            return c;
        }

        /**
         * 'Shows' the matrix.
         */
        public void show() {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    System.out.printf("%9.4f ", array[i][j]);
                }
            }
        }
    }
}
