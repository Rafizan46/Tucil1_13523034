import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        int N;
        int M;
        int P;
        String S;
        Piece[][] pieces;
        int[][] positions;
        int[] types;
        int cases;

        //#region Input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan nama file input: ");

        BufferedReader reader = new BufferedReader(new FileReader(scanner.nextLine()));

        String[] inputNums = reader.readLine().trim().split("\\s+");

        N = Integer.parseInt(inputNums[0]);
        M = Integer.parseInt(inputNums[1]);
        P = Integer.parseInt(inputNums[2]);

        S = reader.readLine();

        Board board = new Board(N, M, S);

        if (S.equals("CUSTOM")) {
            for (int i = 0; i < N; i++) {
                char[] mapRow = reader.readLine().toCharArray();
                for (int j = 0; j < M; j++) {
                    if (mapRow[j] == 'X') {
                        board.map[i][j] = ' ';
                    } else {
                        board.map[i][j] = '.';
                    }
                }
            }
        }

        pieces = new Piece[P][8];
        
        String line = reader.readLine();
        char[] row = line.toCharArray();
        int symbolIdx = 0;
        while (row[symbolIdx] == ' ') {
            symbolIdx++;
        }
        char currentSymbol = row[symbolIdx];
        int currentPiece = 0;
        pieces[currentPiece][0] = new Piece(0, 0);
        pieces[currentPiece][0].addShapeRow(row);
        
        while ((line = reader.readLine()) != null) {
            row = line.toCharArray();
            if (CustomUtils.arrayContains(row, currentSymbol)) {
                pieces[currentPiece][0].addShapeRow(row);
            } else {
                symbolIdx = 0;
                while (row[symbolIdx] == ' ') {
                    symbolIdx++;
                }
                currentSymbol = row[symbolIdx];
                currentPiece++;
                pieces[currentPiece][0] = new Piece(0, 0);
                pieces[currentPiece][0].addShapeRow(row);
            }
        }

        reader.close();
        //#endregion

        //#region Test Input
        // System.out.println(N);
        // System.out.println(M);
        // System.out.println(P);
        // System.out.println(S);
        
        // for (int k = 0; k < 8; k++) {
        //     for (i = 0; i < pieces[k][0].N; i++) {
        //         for (int j = 0; j < pieces[k][0].M; j++) {
        //             System.out.print(pieces[k][0].shape[i][j]);
        //         }
        //         System.out.println();
        //     }
        // }
        //#endregion

        //#region Pre-Process
        for (int i = 0; i < P; i++) {
            for (int j = 1; j < 8; j++) {
                if (j % 4 == 0) {
                    pieces[i][j] = pieces[i][j - 1].mirror();
                } else {
                    pieces[i][j] = pieces[i][j - 1].rotate();
                }
            }
        }

        positions = new int[P][2];
        types = new int[P];
        for (int i = 0; i < P; i++) {
            positions[i][0] = 0;
            positions[i][1] = 0;
            types[i] = 0;
        }

        cases = 0;
        //#endregion

        long start = System.currentTimeMillis();
        //#region Solve
        int piecesPlaced = 0;
        while (piecesPlaced < P) {
            cases++;
            // check if the current piece will fit at the current position
            if (board.isPieceFit(pieces[piecesPlaced][types[piecesPlaced]], positions[piecesPlaced][0], positions[piecesPlaced][1])) {
                // place the current piece
                board.placePiece(pieces[piecesPlaced][types[piecesPlaced]], positions[piecesPlaced][0], positions[piecesPlaced][1]);
                piecesPlaced++;
            // else rotate, mirror, or slide the current piece
            } else {
                // next iteration
                types[piecesPlaced] = (types[piecesPlaced] + 1) % 8;
                if (types[piecesPlaced] == 0) {
                    positions[piecesPlaced][0] = (positions[piecesPlaced][0] + 1) % board.M;
                    if (positions[piecesPlaced][0] == 0) {
                        positions[piecesPlaced][1] = (positions[piecesPlaced][1] + 1);
                    }
                }

                while (positions[piecesPlaced][1] == board.N) { 
                    // go back to the previous piece
                    positions[piecesPlaced][1] = 0;
                    piecesPlaced--;

                    // check if it went past the first piece
                    if (piecesPlaced == -1) {
                        break;
                    }

                    board.takePiece(pieces[piecesPlaced][types[piecesPlaced]], positions[piecesPlaced][0], positions[piecesPlaced][1]);

                    // next iteration
                    types[piecesPlaced] = (types[piecesPlaced] + 1) % 8;
                    if (types[piecesPlaced] == 0) {
                        positions[piecesPlaced][0] = (positions[piecesPlaced][0] + 1) % board.M;
                        if (positions[piecesPlaced][0] == 0) {
                            positions[piecesPlaced][1] = (positions[piecesPlaced][1] + 1);
                        }
                    }
                }

                if (piecesPlaced == -1) {
                    break;
                }
            }
        }
        //#endregion
        long end = System.currentTimeMillis();

        if (board.isBoardFilled()) {
            System.out.println();
            board.printBoard();
            System.out.println("\nWaktu pencarian: " + (end - start) + " ms");
            System.out.println("Banyak kasus yang ditinjau: " + cases + "");

            System.out.print("Apakah anda ingin menyimpan solusi dalam .txt file? (ya/tidak) ");
            if (scanner.nextLine().equals("ya")) {
                BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
                board.writeBoard(writer);
                writer.close();
            }

            System.out.print("Apakah anda ingin menyimpan solusi dalam .png file? (ya/tidak) ");
            if (scanner.nextLine().equals("ya")) {
                board.drawBoard("output.png");
            }
        } else {
            System.out.println("\nTidak ada solusi yang ditemukan :(");
            System.out.println("Waktu pencarian: " + (end - start) + " ms");
            System.out.println("Banyak kasus yang ditinjau: " + cases);
        } 
    }
}
