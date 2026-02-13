package sdm.freedom;

public record Move(int x, int y) {

    public int[] returnMove(){
        return new int[] {x,y};
    }

    public void printAsLastMove(){
        System.out.println("Last Move: (" + x + "," + y + ")");
    }
}
