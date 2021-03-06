import java.util.*;

public class Chess extends Game<Chess.Board> {

    private static final int PAWN_VALUE = 100;
    private static final int KNIGHT_VALUE = 320;
    private static final int BISHOP_VALUE = 330;
    private static final int ROOK_VALUE = 500;
    private static final int QUEEN_VALUE = 900;
    private static final int KING_VALUE = 20000;
    private static final int BOARD_SIZE = 8;

    Chess() {
        super(7);
    }

    class Board implements Cloneable {
        Piece[][] arr;
        int oldX=-1, oldY=-1, newX=-1, newY=-1;

        Board() {
            arr = new Piece[BOARD_SIZE][BOARD_SIZE];
        }

        void init() {
            initNormal();
//            initRooks();
        }

        private void initRooks() {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    arr[i][j] = Piece.EMPTY;
                }
            }

            arr[0][0] = Piece.BLACK_KING;
            arr[BOARD_SIZE-1][BOARD_SIZE-1] = Piece.WHITE_KING;

            arr[0][1] = Piece.BLACK_ROOK;
            arr[1][0] = Piece.BLACK_ROOK;
        }

        private void initNormal() {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (i == 1) {
                        arr[i][j] = Piece.BLACK_PAWN;
                    } else if (i == 6) {
                        arr[i][j] = Piece.WHITE_PAWN;
                    } else {
                        arr[i][j] = Piece.EMPTY;
                    }
                }
            }

            arr[0][0] = Piece.BLACK_ROOK;
            arr[0][1] = Piece.BLACK_KNIGHT;
            arr[0][2] = Piece.BLACK_BISHOP;
            arr[0][3] = Piece.BLACK_QUEEN;
            arr[0][4] = Piece.BLACK_KING;
            arr[0][5] = Piece.BLACK_BISHOP;
            arr[0][6] = Piece.BLACK_KNIGHT;
            arr[0][7] = Piece.BLACK_ROOK;

            arr[7][0] = Piece.WHITE_ROOK;
            arr[7][1] = Piece.WHITE_KNIGHT;
            arr[7][2] = Piece.WHITE_BISHOP;
            arr[7][3] = Piece.WHITE_QUEEN;
            arr[7][4] = Piece.WHITE_KING;
            arr[7][5] = Piece.WHITE_BISHOP;
            arr[7][6] = Piece.WHITE_KNIGHT;
            arr[7][7] = Piece.WHITE_ROOK;
        }

        @Override
        public Board clone() {
            Board newBoard = new Board();
            for (int x = 0; x < BOARD_SIZE; x++) {
                newBoard.arr[x] = Arrays.copyOf(arr[x], BOARD_SIZE);
            }
            return newBoard;
        }

        @Override
        public boolean equals(Object obj) {
            try {
                Board that = (Board) obj;
                if (this.arr == null || that.arr == null) {
                    return this.arr == that.arr;
                }
                for (int i = 0; i < BOARD_SIZE; i++) {
                    for (int j = 0; j < BOARD_SIZE; j++) {
                        if (this.arr[i][j] != that.arr[i][j]) {
                            return false;
                        }
                    }
                }
                return true;
            } catch (ClassCastException e) {
                return false;
            }
        }
    }

    @Override
    public void initBoard() {
        currentBoard = new Board();
        currentBoard.init();
    }

    @Override
    public void printBoard() {
        if (currentBoard.newX != -1) {
            int oldRank = 8 - currentBoard.oldX;
            int newRank = 8 - currentBoard.newX;
            char oldFile = (char) ('A' + currentBoard.oldY);
            char newFile = (char) ('A' + currentBoard.newY);
            System.out.println("Moved " + oldFile + "" + oldRank + " to " + newFile + "" + newRank);
        }
        System.out.print("\t");
        char file = 'A';
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print("\t" + file + "\t");
            file++;
        }
        System.out.println();
        System.out.println("\t ________________________________________________________________");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print((8 - i) + "\t|");
            for (int j = 0; j < BOARD_SIZE; j++) {
                String c = " ";
                if (currentBoard.arr[i][j] == Piece.BLACK_KING) {
                    c = "BK";
                } else if (currentBoard.arr[i][j] == Piece.BLACK_ROOK) {
                    c = "BR";
                } else if (currentBoard.arr[i][j] == Piece.WHITE_KING) {
                    c = "WK";
                } else if (currentBoard.arr[i][j] == Piece.WHITE_ROOK) {
                    c = "WR";
                } else if (currentBoard.arr[i][j] == Piece.WHITE_BISHOP) {
                    c = "WB";
                } else if (currentBoard.arr[i][j] == Piece.BLACK_BISHOP) {
                    c = "BB";
                } else if (currentBoard.arr[i][j] == Piece.BLACK_QUEEN) {
                    c = "BQ";
                } else if (currentBoard.arr[i][j] == Piece.WHITE_QUEEN) {
                    c = "WQ";
                } else if (currentBoard.arr[i][j] == Piece.WHITE_KNIGHT) {
                    c = "WKn";
                } else if (currentBoard.arr[i][j] == Piece.BLACK_KNIGHT) {
                    c = "BKn";
                } else if (currentBoard.arr[i][j] == Piece.WHITE_PAWN) {
                    c = "WP";
                } else if (currentBoard.arr[i][j] == Piece.BLACK_PAWN) {
                    c = "BP";
                }
                System.out.print("\t" + c + "\t|");
            }
            System.out.println("\t" + (8 - i) + "");
            System.out.println("\t ________________________________________________________________");
        }
//        System.out.println();
        System.out.print("\t");
        file = 'A';
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print("\t" + file + "\t");
            file++;
        }
        System.out.println();
    }

    @Override
    public boolean gameOver(Board board) {
        return doesHeWin(board, Player.MAXIMIZER) || doesHeWin(board, Player.MINIMIZER);
    }

    @Override
    public int evaluateBoard(Board board, Player player) {
        if (gameOver(board)) {
            if (doesHeWin(board, Player.MAXIMIZER)) {
                return Integer.MAX_VALUE;
            }
            if (doesHeWin(board, Player.MINIMIZER)) {
                return Integer.MIN_VALUE;
            }
            return 0;
        }
        return evalMaterial(board) + 10*evalDomination(board) + evalMobility(board);
//        return evalMaterial(board) + 20*evalMobility(board) + evalKingAttackers(board)*80;
//        return evalMaterial(board);
    }

    private int evalKingAttackers(Board board) {
        int score = 0;
        List<Board> blackMoves = getChildren(board, Player.MINIMIZER);//get all black moves
        List<Board> whiteMoves = getChildren(board, Player.MAXIMIZER);//get all white moves
        for (Board b : blackMoves) {
            boolean hasWhiteKing = false;
            outerloop:
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    Piece piece = b.arr[i][j];
                    if (piece == Piece.WHITE_KING) {
                        hasWhiteKing = true;
                        break outerloop;
                    }
                }
            }
            if (!hasWhiteKing) {
                score--;
            }
        }
        for (Board b : whiteMoves) {
            boolean hasBlackKing = false;
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    Piece piece = b.arr[i][j];
                    if (piece == Piece.BLACK_KING) {
                        hasBlackKing = true;
                        break;
                    }
                }
            }
            if (!hasBlackKing) {
                score++;
            }
        }
        return score;
    }

    private int evalMobility(Board board) {
        return getChildren(board, Player.MAXIMIZER).size() - getChildren(board, Player.MINIMIZER).size();
    }


    private int evalDomination(Board board) {
        int whiteValue = 0;
        int blackValue = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Piece piece = board.arr[i][j];
                if (piece == Piece.WHITE_PAWN) {
                    whiteValue += BOARD_SIZE - i - 1;
                } else if (piece == Piece.BLACK_PAWN) {
                    blackValue += i;
                }
            }
        }
        return whiteValue - blackValue;
    }

    private int evalMaterial(Board board) {
        //white material - black material
        int whiteValue = 0;
        int blackValue = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Piece piece = board.arr[i][j];
                if (piece == Piece.WHITE_KING) {
                    whiteValue += KING_VALUE;
                } else if (piece == Piece.BLACK_KING) {
                    blackValue += KING_VALUE;
                } else if (piece == Piece.WHITE_ROOK) {
                    whiteValue += ROOK_VALUE;
                } else if (piece == Piece.BLACK_ROOK) {
                    blackValue += ROOK_VALUE;
                } else if (piece == Piece.WHITE_BISHOP) {
                    whiteValue += BISHOP_VALUE;
                } else if (piece == Piece.BLACK_BISHOP) {
                    blackValue += BISHOP_VALUE;
                } else if (piece == Piece.BLACK_QUEEN) {
                    blackValue += QUEEN_VALUE;
                } else if (piece == Piece.WHITE_QUEEN) {
                    whiteValue += QUEEN_VALUE;
                } else if (piece == Piece.BLACK_KNIGHT) {
                    blackValue += KNIGHT_VALUE;
                } else if (piece == Piece.WHITE_KNIGHT) {
                    whiteValue += KNIGHT_VALUE;
                } else if (piece == Piece.BLACK_PAWN) {
                    blackValue += PAWN_VALUE;
                } else if (piece == Piece.WHITE_PAWN) {
                    whiteValue += PAWN_VALUE;
                }
            }
        }
        return whiteValue - blackValue;
    }

    @Override
    public void playHumanMove(Player humanPlayer) {
        //ask user for input
        String start, end;
        int startX, startY, endX, endY;
        Piece piece;
        System.out.println("board value: " + evaluateBoard(currentBoard, humanPlayer));
        Scanner s = new Scanner(System.in);
        whileloop:
        while (true) {
            try {
                start = s.next();
                end = s.next();
                char startFile = start.charAt(0);
                int startRank = Integer.parseInt(start.substring(1, 2));
                startY = startFile - 'A';
                startX = 8 - startRank;

                char endFile = end.charAt(0);
                int endRank = Integer.parseInt(end.substring(1, 2));
                endY = endFile - 'A';
                endX = 8 - endRank;
            } catch (Exception e) {
                System.out.println("invalid input");
                continue;
            }
            if (startX < 0 || startX > BOARD_SIZE-1 || startY < 0 || startY > BOARD_SIZE-1 || endX < 0 || endX > BOARD_SIZE-1 || endY < 0 || endY > BOARD_SIZE-1) {
                System.out.println("Out of bounds, try again.");
                continue;
            }
            piece = currentBoard.arr[startX][startY];
            if (piece == Piece.EMPTY || !currentBoard.arr[endX][endY].canBeReplacedBy(piece)) {
                System.out.println("No piece available, try again.");
                continue;
            }
            Board newBoard = currentBoard.clone();
            newBoard.arr[startX][startY] = Piece.EMPTY;
            newBoard.arr[endX][endY] = piece;
            List<Board> children = getChildren(currentBoard, humanPlayer);
            if (!children.contains(newBoard)) {
                System.out.println("Illegal move.");
                continue;
            }
            //newBoard is valid, so get children of newBoard
            List<Board> oppositeChildren = getChildren(newBoard, humanPlayer.opposite());
            for (Board b : oppositeChildren) {
                if (doesHeWin(b, humanPlayer.opposite())) {
                    System.out.println("Illegal. Must get out of check.");
                    continue whileloop;
                }
            }
            break;
        }
        currentBoard.arr[startX][startY] = Piece.EMPTY;
        currentBoard.arr[endX][endY] = piece;
    }

    @Override
    public boolean doesHeWin(Board board, Player player) {
        boolean hasBlackKing = false;
        boolean hasWhiteKing = false;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board.arr[i][j] == Piece.WHITE_KING) {
                    hasWhiteKing = true;
                } else if (board.arr[i][j] == Piece.BLACK_KING) {
                    hasBlackKing = true;
                }
            }
        }

        if (player == Player.MAXIMIZER) {
            return !hasBlackKing;
        } else {
            return !hasWhiteKing;
        }
    }

    @Override
    public List<Board> getChildren(Board board, Player player) {
        List<Board> children = new ArrayList<>();

        Piece kingPiece;
        Piece rookPiece;
        Piece bishopPiece;
        Piece queenPiece;
        Piece knightPiece;
        Piece pawnPiece;
        if (player == Player.MAXIMIZER) {
            kingPiece = Piece.WHITE_KING;
            rookPiece = Piece.WHITE_ROOK;
            bishopPiece = Piece.WHITE_BISHOP;
            queenPiece = Piece.WHITE_QUEEN;
            knightPiece = Piece.WHITE_KNIGHT;
            pawnPiece = Piece.WHITE_PAWN;
        } else {
            kingPiece = Piece.BLACK_KING;
            rookPiece = Piece.BLACK_ROOK;
            bishopPiece = Piece.BLACK_BISHOP;
            queenPiece = Piece.BLACK_QUEEN;
            knightPiece = Piece.BLACK_KNIGHT;
            pawnPiece = Piece.BLACK_PAWN;
        }
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board.arr[i][j] == kingPiece) {
                    moveKingAround(kingPiece, i, j, board, children);
                } else if (board.arr[i][j] == rookPiece) {
                    moveRookAround(rookPiece, i, j, board, children);
                } else if (board.arr[i][j] == bishopPiece) {
                    moveBishopAround(bishopPiece, i, j, board, children);
                } else if (board.arr[i][j] == queenPiece) {
                    moveQueenAround(queenPiece, i, j, board, children);
                } else if (board.arr[i][j] == knightPiece) {
                    moveKnightAround(knightPiece, i, j, board, children);
                } else if (board.arr[i][j] == pawnPiece) {
                    movePawnAround(pawnPiece, i, j, board, children);
                }
            }
        }
        return children;
    }

    private void movePawnAround(Piece pawnPiece, int i, int j, Board board, List<Board> children) {
        if (pawnPiece.isBlack() && i == 1 && board.arr[2][j] == Piece.EMPTY && board.arr[3][j] == Piece.EMPTY) {
            Board newBoard = board.clone();
            newBoard.arr[1][j] = Piece.EMPTY;
            newBoard.arr[3][j] = pawnPiece;
            newBoard.oldX = 1;
            newBoard.oldY = j;
            newBoard.newX = 3;
            newBoard.newY = j;
            children.add(newBoard);
        }
        if (pawnPiece.isWhite() && i == 6 && board.arr[5][j] == Piece.EMPTY && board.arr[4][j] == Piece.EMPTY) {
            Board newBoard = board.clone();
            newBoard.arr[6][j] = Piece.EMPTY;
            newBoard.arr[4][j] = pawnPiece;
            newBoard.oldX = 6;
            newBoard.oldY = j;
            newBoard.newX = 4;
            newBoard.newY = j;
            children.add(newBoard);
        }
        if (pawnPiece.isBlack() && i < 7 && board.arr[i+1][j] == Piece.EMPTY) {
            Board newBoard = board.clone();
            newBoard.arr[i][j] = Piece.EMPTY;
            if (i+1 == BOARD_SIZE-1) {
                newBoard.arr[i+1][j] = Piece.BLACK_QUEEN;
            } else {
                newBoard.arr[i+1][j] = pawnPiece;
            }
            newBoard.oldX = i;
            newBoard.oldY = j;
            newBoard.newX = i+1;
            newBoard.newY = j;
            children.add(newBoard);
        }
        if (pawnPiece.isWhite() && i > 0 && board.arr[i-1][j] == Piece.EMPTY) {
            Board newBoard = board.clone();
            newBoard.arr[i][j] = Piece.EMPTY;
            if (i-1 == 0) {
                newBoard.arr[i-1][j] = Piece.WHITE_QUEEN;
            } else {
                newBoard.arr[i-1][j] = pawnPiece;
            }
            newBoard.oldX = i;
            newBoard.oldY = j;
            newBoard.newX = i-1;
            newBoard.newY = j;
            children.add(newBoard);
        }
        if (pawnPiece.isBlack() && i < 7 && j > 0 && board.arr[i+1][j-1].isWhite()) {
            Board newBoard = board.clone();
            newBoard.arr[i][j] = Piece.EMPTY;
            if (i+1 == BOARD_SIZE-1) {
                newBoard.arr[i+1][j-1] = Piece.BLACK_QUEEN;
            } else {
                newBoard.arr[i+1][j-1] = pawnPiece;
            }
            newBoard.oldX = i;
            newBoard.oldY = j;
            newBoard.newX = i+1;
            newBoard.newY = j-1;
            children.add(newBoard);
        }
        if (pawnPiece.isBlack() && i < 7 && j < 7 && board.arr[i+1][j+1].isWhite()) {
            Board newBoard = board.clone();
            newBoard.arr[i][j] = Piece.EMPTY;
            if (i+1 == BOARD_SIZE-1) {
                newBoard.arr[i+1][j+1] = Piece.BLACK_QUEEN;
            } else {
                newBoard.arr[i+1][j+1] = pawnPiece;
            }
            newBoard.oldX = i;
            newBoard.oldY = j;
            newBoard.newX = i+1;
            newBoard.newY = j+1;
            children.add(newBoard);
        }
        if (pawnPiece.isWhite() && i > 0 && j > 0 && board.arr[i-1][j-1].isBlack()) {
            Board newBoard = board.clone();
            newBoard.arr[i][j] = Piece.EMPTY;
            if (i-1 == 0) {
                newBoard.arr[i-1][j-1] = Piece.WHITE_QUEEN;
            } else {
                newBoard.arr[i-1][j-1] = pawnPiece;
            }
            newBoard.oldX = i;
            newBoard.oldY = j;
            newBoard.newX = i-1;
            newBoard.newY = j-1;
            children.add(newBoard);
        }
        if (pawnPiece.isWhite() && i > 0 && j < 7 && board.arr[i-1][j+1].isBlack()) {
            Board newBoard = board.clone();
            newBoard.arr[i][j] = Piece.EMPTY;
            if (i-1 == 0) {
                newBoard.arr[i-1][j+1] = Piece.WHITE_QUEEN;
            } else {
                newBoard.arr[i-1][j+1] = pawnPiece;
            }
            newBoard.oldX = i;
            newBoard.oldY = j;
            newBoard.newX = i-1;
            newBoard.newY = j+1;
            children.add(newBoard);
        }
    }

    private void moveQueenAround(Piece queenPiece, int i, int j, Board board, List<Board> children) {
        moveRookAround(queenPiece, i, j, board, children);
        moveBishopAround(queenPiece, i, j, board, children);
    }

    private void moveBishopAround(Piece bishopPiece, int i, int j, Board board, List<Board> children) {
        for (int newI = i, newJ = j; newI>0 && newJ>0;) {
            if (board.arr[newI-1][newJ-1].canBeReplacedBy(bishopPiece)) {
                Board newBoard = board.clone();
                newBoard.arr[i][j] = Piece.EMPTY;
                newBoard.arr[newI-1][newJ-1] = bishopPiece;
                newBoard.oldX = i;
                newBoard.oldY = j;
                newBoard.newX = newI-1;
                newBoard.newY = newJ-1;
                children.add(newBoard);
            }
            if (board.arr[newI-1][newJ-1] != Piece.EMPTY) {
                //attack here, and then stop trying
                break;
            }
            newI--;newJ--;
        }
        for (int newI = i, newJ = j; newI<BOARD_SIZE-1 && newJ<BOARD_SIZE-1;) {
            if (board.arr[newI+1][newJ+1].canBeReplacedBy(bishopPiece)) {
                Board newBoard = board.clone();
                newBoard.arr[i][j] = Piece.EMPTY;
                newBoard.arr[newI+1][newJ+1] = bishopPiece;
                newBoard.oldX = i;
                newBoard.oldY = j;
                newBoard.newX = newI+1;
                newBoard.newY = newJ+1;
                children.add(newBoard);
            }
            if (board.arr[newI+1][newJ+1] != Piece.EMPTY) {
                //attack here, and then stop trying
                break;
            }
            newI++;newJ++;
        }
        for (int newI = i, newJ = j; newI>0 && newJ<BOARD_SIZE-1;) {
            if (board.arr[newI-1][newJ+1].canBeReplacedBy(bishopPiece)) {
                Board newBoard = board.clone();
                newBoard.arr[i][j] = Piece.EMPTY;
                newBoard.arr[newI-1][newJ+1] = bishopPiece;
                newBoard.oldX = i;
                newBoard.oldY = j;
                newBoard.newX = newI-1;
                newBoard.newY = newJ+1;
                children.add(newBoard);
            }
            if (board.arr[newI-1][newJ+1] != Piece.EMPTY) {
                //attack here, and then stop trying
                break;
            }
            newI--;newJ++;
        }
        for (int newI = i, newJ = j; newI<BOARD_SIZE-1 && newJ>0;) {
            if (board.arr[newI+1][newJ-1].canBeReplacedBy(bishopPiece)) {
                Board newBoard = board.clone();
                newBoard.arr[i][j] = Piece.EMPTY;
                newBoard.arr[newI+1][newJ-1] = bishopPiece;
                newBoard.oldX = i;
                newBoard.oldY = j;
                newBoard.newX = newI+1;
                newBoard.newY = newJ-1;
                children.add(newBoard);
            }
            if (board.arr[newI+1][newJ-1] != Piece.EMPTY) {
                //attack here, and then stop trying
                break;
            }
            newI++;newJ--;
        }
    }

    private void moveRookAround(Piece rookPiece, int i, int j, Board board, List<Board> children) {
        //move it left (i--)
        for (int newI = i; newI>0; newI--) {
            if (board.arr[newI-1][j].canBeReplacedBy(rookPiece)) {
                Board newBoard = board.clone();
                newBoard.arr[i][j] = Piece.EMPTY;
                newBoard.arr[newI-1][j] = rookPiece;
                newBoard.oldX = i;
                newBoard.oldY = j;
                newBoard.newX = newI-1;
                newBoard.newY = j;
                children.add(newBoard);
            }
            if (board.arr[newI-1][j] != Piece.EMPTY) {
                //attack here, and then stop trying
                break;
            }
        }
        for (int newI = i; newI<BOARD_SIZE-1; newI++) {
            if (board.arr[newI+1][j].canBeReplacedBy(rookPiece)) {
                Board newBoard = board.clone();
                newBoard.arr[i][j] = Piece.EMPTY;
                newBoard.arr[newI+1][j] = rookPiece;
                newBoard.oldX = i;
                newBoard.oldY = j;
                newBoard.newX = newI+1;
                newBoard.newY = j;
                children.add(newBoard);
            }
            if (board.arr[newI+1][j] != Piece.EMPTY) {
                //attack here, and then stop trying
                break;
            }
        }
        for (int newJ = j; newJ>0; newJ--) {
            if (board.arr[i][newJ-1].canBeReplacedBy(rookPiece)) {
                Board newBoard = board.clone();
                newBoard.arr[i][j] = Piece.EMPTY;
                newBoard.arr[i][newJ-1] = rookPiece;
                newBoard.oldX = i;
                newBoard.oldY = j;
                newBoard.newX = i;
                newBoard.newY = newJ-1;
                children.add(newBoard);
            }
            if (board.arr[i][newJ-1] != Piece.EMPTY) {
                //attack here, and then stop trying
                break;
            }
        }
        for (int newJ = j; newJ<BOARD_SIZE-1; newJ++) {
            if (board.arr[i][newJ+1].canBeReplacedBy(rookPiece)) {
                Board newBoard = board.clone();
                newBoard.arr[i][j] = Piece.EMPTY;
                newBoard.arr[i][newJ+1] = rookPiece;
                newBoard.oldX = i;
                newBoard.oldY = j;
                newBoard.newX = i;
                newBoard.newY = newJ+1;
                children.add(newBoard);
            }
            if (board.arr[i][newJ+1] != Piece.EMPTY) {
                //attack here, and then stop trying
                break;
            }
        }
    }

    private void moveKingAround(Piece kingPiece, int i, int j, Board board, List<Board> children) {
        movePieceAround(kingPiece, i, j, i +1, j, board, children);
        movePieceAround(kingPiece, i, j, i -1, j, board, children);
        movePieceAround(kingPiece, i, j, i, j +1, board, children);
        movePieceAround(kingPiece, i, j, i, j -1, board, children);
        movePieceAround(kingPiece, i, j, i -1, j -1, board, children);
        movePieceAround(kingPiece, i, j, i -1, j +1, board, children);
        movePieceAround(kingPiece, i, j, i +1, j -1, board, children);
        movePieceAround(kingPiece, i, j, i +1, j +1, board, children);
    }

    private void moveKnightAround(Piece knightPiece, int i, int j, Board board, List<Board> children) {
        movePieceAround(knightPiece, i, j, i+2, j+1, board, children);
        movePieceAround(knightPiece, i, j, i+2, j-1, board, children);
        movePieceAround(knightPiece, i, j, i-2, j+1, board, children);
        movePieceAround(knightPiece, i, j, i-2, j-1, board, children);
        movePieceAround(knightPiece, i, j, i-1, j+2, board, children);
        movePieceAround(knightPiece, i, j, i+1, j+2, board, children);
        movePieceAround(knightPiece, i, j, i-1, j-2, board, children);
        movePieceAround(knightPiece, i, j, i+1, j-2, board, children);
    }

    private void movePieceAround(Piece piece, int oldX, int oldY, int newX, int newY, Board board, List<Board> children) {
        if (newX <= BOARD_SIZE-1 && newX >= 0 && newY <= BOARD_SIZE-1 && newY >= 0) {
            if (board.arr[newX][newY] == Piece.EMPTY || board.arr[newX][newY].canBeReplacedBy(piece)) {
                Board newBoard = board.clone();
                newBoard.arr[oldX][oldY] = Piece.EMPTY;
                newBoard.arr[newX][newY] = piece;
                newBoard.oldX = oldX;
                newBoard.oldY = oldY;
                newBoard.newX = newX;
                newBoard.newY = newY;
                children.add(newBoard);
            }
        }
    }

    enum Piece {
        WHITE_ROOK, WHITE_BISHOP, WHITE_KING, WHITE_QUEEN, WHITE_KNIGHT, WHITE_PAWN, BLACK_ROOK, BLACK_BISHOP, BLACK_KING, BLACK_QUEEN, BLACK_KNIGHT, BLACK_PAWN, EMPTY;

        public boolean isBlack() {
            return this == BLACK_KING || this == BLACK_ROOK || this == BLACK_BISHOP || this == BLACK_QUEEN || this == BLACK_KNIGHT || this == BLACK_PAWN;
        }

        public boolean isWhite() {
            return this == WHITE_KING || this == WHITE_ROOK || this == WHITE_BISHOP || this == WHITE_QUEEN || this == WHITE_KNIGHT || this == WHITE_PAWN;
        }

        public boolean canBeReplacedBy(Piece replacer) {
            return this == EMPTY || this.isWhite() && replacer.isBlack() || this.isBlack() && replacer.isWhite();
        }
    }
}
