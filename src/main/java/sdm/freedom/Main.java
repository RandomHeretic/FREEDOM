package sdm.freedom;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner s = new Scanner(System.in);

        System.out.println("Welcome to Freedom, state the size of the board please");

        int n = s.nextInt();

        Match CurrentMatch = new Match(n);

        for(int i=0;i<n*n;i++){
            System.out.println("Current Board State:");
            CurrentMatch.printBoardState();
            System.out.println("To exit write a -1 for either x or y");
            System.out.println("State your next move (first x, then y)");
            int x = s.nextInt();
            int y = s.nextInt();
            if (x<0 || y<0){
                break;
            }
            CurrentMatch.applyAMove(new Move(x,y));
        }
        System.out.println("The game ended with the following scores");
        int[] scores = CurrentMatch.evaluateBoard();
        System.out.println("White: " + scores[0]);
        System.out.println("Black: " + scores[1]);

    }
}
