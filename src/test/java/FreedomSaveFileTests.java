
import sdm.freedom.*;

import org.junit.Test;
import sdm.freedom.agents.AbstractAgent;
import sdm.freedom.agents.AgentFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class FreedomSaveFileTests {

    @Test
    public void verifySavesToGivenFile() throws Exception{
        Path tempDir = Files.createTempDirectory("test-save");
        AbstractAgent[] agents = new AbstractAgent[]{
                AgentFactory.create("Player", 1),
                AgentFactory.create("Random", 2)
        };
        GameController game = GameController.getInstance();
        game.initialize(4,UIController.getInstance(),agents);

        Path saveFile = tempDir.resolve("test.txt");

        assertTrue(game.saveState(saveFile.toString()));

        assertTrue(Files.exists(saveFile));
    }


    @Test
    public void verifySavesDataCorrectly() throws Exception{

        AbstractAgent[] agents = new AbstractAgent[]{
                AgentFactory.create("Player", 1),
                AgentFactory.create("Player", 2)
        };
        GameController game = GameController.getInstance();
        game.initialize(4,UIController.getInstance(),agents);
        game.applyMove(new Move(2,2));
        Path tempDir = Files.createTempDirectory("test-save");
        Path saveFile = tempDir.resolve("test.txt");
        assertTrue(game.saveState(saveFile.toString()));

        Scanner scanner = new Scanner(new File(saveFile.toString()));
        String agent1Name = scanner.next();
        String agent2Name = scanner.next();
        AgentFactory.create(agent1Name, 1); // this will throw an exception if the file did not save correctly
        AgentFactory.create(agent2Name, 2); // if an exception is thrown the test will fail

        int currentTurn = scanner.nextInt();
        assertEquals(game.getPlayerTurn(),currentTurn);

        int boardSize = scanner.nextInt();
        assertEquals(game.getBoard().length,boardSize);
        int x = scanner.nextInt();
        Move LastMove;
        if (x>=0){
            int y = scanner.nextInt();
            LastMove = new Move(x,y);
        }else{
            LastMove = new Move(true);
        }
        assertEquals(game.getLastMove(),LastMove);

        int boardPosition;
        int[][] gameBoard = game.getBoard();

        for(int i=0;i<boardSize;i++){
            for (int j=0;j<boardSize;j++){
                boardPosition= scanner.nextInt();
                assertEquals(gameBoard[i][j],boardPosition);
            }
        }
        scanner.close();

    }
    @Test
    public void verifyReadsDataCorrectly() throws Exception{
        AbstractAgent[] agents = new AbstractAgent[]{
                AgentFactory.create("Player", 1),
                AgentFactory.create("Player", 2)
        };
        GameController game = GameController.getInstance();
        game.initialize(4,UIController.getInstance(),agents);
        game.applyMove(new Move(2,2));
        Path tempDir = Files.createTempDirectory("test-save");
        Path saveFile = tempDir.resolve("test.txt");
        assertTrue(game.saveState(saveFile.toString()));

        game.reset();

        assertEquals(0,game.loadState(saveFile.toString()));

        Scanner scanner = new Scanner(new File(saveFile.toString()));
        String agent1Name = scanner.next();
        String agent2Name = scanner.next();
        AgentFactory.create(agent1Name, 1); // this will throw an exception if the file did not save correctly
        AgentFactory.create(agent2Name, 2); // if an exception is thrown the test will fail

        int currentTurn = scanner.nextInt();
        assertEquals(game.getPlayerTurn(),currentTurn);

        int boardSize = scanner.nextInt();
        assertEquals(game.getBoard().length,boardSize);
        int x = scanner.nextInt();
        Move LastMove;
        if (x>=0){
            int y = scanner.nextInt();
            LastMove = new Move(x,y);
        }else{
            LastMove = new Move(true);
        }
        assertEquals(game.getLastMove(),LastMove);

        int boardPosition;
        int[][] gameBoard = game.getBoard();

        for(int i=0;i<boardSize;i++){
            for (int j=0;j<boardSize;j++){
                boardPosition= scanner.nextInt();
                assertEquals(gameBoard[i][j],boardPosition);

            }
        }
        scanner.close();
    }

}
