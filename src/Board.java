
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Board {
    int N;
    int M;
    String S;
    char[][] map;

    public static final String[] ANSI_COLORS = {"\u001b[38;2;0;0;255m", "\u001b[38;2;0;255;0m", "\u001b[38;2;255;128;0m", "\u001b[38;2;166;66;200m", "\u001b[38;2;101;55;0m",
                                                "\u001b[38;2;128;128;128m", "\u001b[38;2;255;0;0m", "\u001b[38;2;128;0;0m", "\u001b[38;2;143;0;255m", "\u001b[38;2;205;127;50m",
                                                "\u001b[38;2;255;255;204m", "\u001b[38;2;254;220;86m", "\u001b[38;2;0;0;128m", "\u001b[38;2;248;131;121m", "\u001b[38;2;224;176;255m",
                                                "\u001b[38;2;255;204;153m", "\u001b[38;2;255;215;0m", "\u001b[38;2;0;255;255m", "\u001b[38;2;0;127;255m", "\u001b[38;2;0;128;128m",
                                                "\u001b[38;2;112;130;56m", "\u001b[38;2;245;245;220m","\u001b[38;2;54;69;79m", "\u001b[38;2;0;0;255m", "\u001b[38;2;36;122;253m",
                                                "\u001b[38;2;255;209;220m"};

    public static final Color[] COLORS = {new Color(0, 0, 255), new Color(0, 255, 0), new Color(255, 128, 0), new Color(166,66,200), new Color(101,55,0),
                                        new Color(128, 128, 128), new Color(255, 0, 0), new Color(128,0,0), new Color(143, 0, 255), new Color(205, 127, 50),
                                        new Color(255, 255, 204), new Color(254, 220, 86), new Color(0, 0, 128), new Color(248, 131, 121), new Color(224, 176, 255),
                                        new Color(255, 204, 153), new Color(255, 215, 0), new Color(0, 255, 255), new Color(0, 127, 255), new Color(0, 128, 128),
                                        new Color(112, 130, 56), new Color(245, 245, 220), new Color(54, 69, 79), new Color(0, 0, 255), new Color(36, 112, 253),
                                        new Color(255, 209, 220)};
    
    public static final String ANSI_RESET = "\u001b[0m";

    Board(int N, int M, String S) {
        this.N = N;
        this.M = M;
        if (S.equals("DEFAULT") || S.equals("CUSTOM")) {
            this.map = new char[N][M];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    this.map[i][j] = ' ';
                }
            }
        }
    }

    public boolean isPieceFit(Piece piece, int x, int y) {
        if (x + piece.M > this.M || y + piece.N > this.N) {
            return false;
        }

        for (int i = 0; i < piece.N; i++) {
            for (int j = 0; j < piece.M; j++) {
                if (piece.shape[i][j] != ' ' && this.map[y + i][x + j] != ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public void placePiece(Piece piece, int x, int y) {
        for (int i = 0; i < piece.N; i++) {
            for (int j = 0; j < piece.M; j++) {
                if (piece.shape[i][j] != ' ') {
                    this.map[y + i][x + j] = piece.shape[i][j];
                }
            }
        }
    }

    public void takePiece(Piece piece, int x, int y) {
        for (int i = 0; i < piece.N; i++) {
            for (int j = 0; j < piece.M; j++) {
                if (piece.shape[i][j] != ' ') {
                    this.map[y + i][x + j] = ' ';
                }
            }
        }
    }

    public boolean isBoardFilled() {
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.M; j++) {
                if (this.map[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public void printBoard() {
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.M; j++) {
                if (this.map[i][j] == '.') {
                    System.out.print(' ');
                } else {
                    System.out.print(ANSI_COLORS[(int) this.map[i][j] - (int) 'A'] + this.map[i][j] + ANSI_RESET);
                }
            }
            System.out.println();
        }
    }

    public void writeBoard(BufferedWriter writer) throws IOException {
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.M; j++) {
                if (this.map[i][j] == '.') {
                    writer.write(' ');
                } else {
                    writer.write(this.map[i][j]);
                }
            }
            writer.write('\n');
        }
    }

    public void drawBoard(String output) throws IOException {
        int offset = 100;
        int radius = 50;
        int distance = 20;
        int connector = 10;

        BufferedImage image = new BufferedImage(2*offset + 2*radius*this.M + distance*(this.M-1), 2*offset + 2*radius*this.N + distance*(this.N-1), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.M; j++) {
                if (this.map[i][j] != '.') {
                    graphics.setColor(COLORS[(int) this.map[i][j] - (int) 'A']);
                    graphics.fillOval(offset + (2 * radius + distance) * j, offset + (2 * radius + distance) * i, 2*radius, 2*radius);
                    if (i < this.N - 1 && this.map[i][j] == this.map[i+1][j]) {
                        graphics.fillRect(offset + (2 * radius + distance) * j + radius - connector, offset + (2 * radius + distance) * i + radius, 2*connector, 2*radius + distance);
                    }
                    if (j < this.M - 1 && this.map[i][j] == this.map[i][j+1]) {
                        graphics.fillRect(offset + (2 * radius + distance) * j + radius, offset + (2 * radius + distance) * i + radius - connector, 2*radius + distance, 2*connector);
                    }
                }
            }
        }
        graphics.dispose();
        ImageIO.write(image, "png", new File(output));
    }
}
