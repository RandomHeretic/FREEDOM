package sdm.freedom;

public class Match {
    private final State CurrentState;
    private int CurrentPlayer;

    public Match(int n){
        Board b = new Board(n);
        CurrentState = new State(b);
        CurrentPlayer = 1;
    }

    public State getCurrentState(){
        return CurrentState;
    }

    public int getCurrentPlayer(){
        return CurrentPlayer;
    }

    public int getCurrentPlayerIdx(){
        return getCurrentPlayer()-1;
    }

    public void checkAndApplyMove(Move NewMove){
        if(!checkValidMove(NewMove)) return;
        CurrentState.applyMove(NewMove, CurrentPlayer);
        CurrentPlayer = 3-CurrentPlayer; //swap between 1 and 2
    }

    public void applyMove(Move NewMove){
        CurrentState.applyMove(NewMove, CurrentPlayer);
        CurrentPlayer = 3-CurrentPlayer; //swap between 1 and 2
    }

    public boolean checkValidMove(Move NewMove){
        if (NewMove.skipMove() || CurrentState.getLastMove() == null) {
            return true;
        }
        if (CurrentState.getBoard().isOutOfBounds(NewMove) || CurrentState.getBoardPosition(NewMove) !=0){
            return false;
        }
        int[][] neighbours = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}};
        boolean flagFreedom=true; //checks for empty spaces next to the last move


        for(int[] nei: neighbours){

            int newx = CurrentState.getLastMove().returnMove()[0]+nei[0];
            int newy = CurrentState.getLastMove().returnMove()[1]+nei[1];
            Move LastMoveNeighbour = new Move(newx,newy);
            if(CurrentState.getBoard().isOutOfBounds(LastMoveNeighbour)){
                continue;
            }
            if(CurrentState.getBoardPosition(LastMoveNeighbour)==0) {
                flagFreedom = false;
            }
            if(NewMove.equals(LastMoveNeighbour)){
                return true;
            }
        }
        return flagFreedom;
    }

    public int getPosition(Move m){
        return CurrentState.getBoardPosition(m);
    }

    public int[] evaluateBoard(){
        return CurrentState.getBoard().evaluateBoard();
    }
}
