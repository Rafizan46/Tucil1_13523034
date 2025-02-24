public class Piece {
    int N;
    int M;
    char[][] shape;
    int x;
    int y;

    Piece(int N, int M) {
        this.N = N;
        this.M = M;
        this.shape = new char[N][M];
    }

    public void addShapeRow(char[] row) {
        int newN = this.N + 1;
        int newM;

        if (this.M < row.length) {
            newM = row.length;
        } else {
            newM = this.M;
        }

        char[][] newShape = new char[newN][newM];
        for (int i = 0; i < newN; i++) {
            for (int j = 0; j < newM; j++) {
                newShape[i][j] = ' ';
            }
        }

        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.M; j++) {
                newShape[i][j] = this.shape[i][j];
            }
        }

        for (int i = 0; i < row.length; i++) {
            newShape[newN-1][i] = row[i];
        }

        this.N = newN;
        this.M = newM;
        this.shape = newShape;
    }

    public Piece mirror() {
        Piece mirrored = new Piece(this.N, this.M);

        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.M; j++) {
                mirrored.shape[i][j] = this.shape[i][this.M-j-1];
            }
        }

        return mirrored;
    }

    public Piece rotate() {
        Piece rotated = new Piece(this.M, this.N);

        Piece mirrored = this.mirror();
        for (int i = 0; i < rotated.N; i++) {
            for (int j = 0; j < rotated.M; j++) {
                rotated.shape[i][j] = mirrored.shape[j][i];
            }
        }

        return rotated;
    }

    public Piece transform3D() {
        return this;
    }

    public Piece rotate3D() {
        return this;
    }
}