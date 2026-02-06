package sdm.freedom;

public class Match {
    private final State CurrentState;
    private int CurrentPlayer;

    public Match(int n){
        Board b = new Board(n);
        CurrentState = new State(b);
        CurrentPlayer = 1;
    }

    public State giveCurrentState(){
        return CurrentState;
    }

    public void applyAMove(Move NewMove){
        if(checkValidMove(NewMove)) {
            CurrentState.applyMove(NewMove, CurrentPlayer);
            if (CurrentPlayer == 1) {
                CurrentPlayer = 2;
            } else {
                CurrentPlayer = 1;
            }
        }
    }

    public boolean checkValidMove(Move NewMove){
        if (CurrentState.giveBoard().isOutOfBounds(NewMove) || CurrentState.giveBoardPosition(NewMove) !=0){
            return false;
        }
        if (CurrentState.giveLastMove() == null){
            return true;
        }
        int[][] neighbours = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}};
        boolean flagFreedom=false; //checks for empty spaces next to the last move
        boolean flagNext=false;

        for(int[] nei: neighbours){

            int newx = CurrentState.giveLastMove().returnMove()[0]+nei[0];
            int newy = CurrentState.giveLastMove().returnMove()[1]+nei[1];
            Move LastMoveNeighbour = new Move(newx,newy);
            if(CurrentState.giveBoard().isOutOfBounds(new Move(newx,newy))){
                continue;
            }
            if(CurrentState.giveBoardPosition(LastMoveNeighbour)==0) {
                flagFreedom = true;
            }
            if(NewMove.compareMoves(LastMoveNeighbour)){
                flagNext = true;
            }
        }
        return !flagFreedom || flagNext;
    }

    public void printBoardState(){
        CurrentState.printState();
    }

    public int givePosition(Move m){
        return CurrentState.giveBoardPosition(m);
    }

    public int[] evaluateBoard(){
        return CurrentState.giveBoard().evaluateBoard();
    }
}
