import java.io.File;
import java.io.IOException;

import com.codingame.gameengine.runner.MultiplayerGameRunner;
import com.google.common.io.Files;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
        //        gameRunner.setSeed(-8289918975308209200l);

        gameRunner.addAgent(
            compileJava("starterAIs/Starter.java"),
            "Player one"
        );
        gameRunner.addAgent(
            "python3 starterAIs/starter.py",
            "Player two"
        );

        gameRunner.setLeagueLevel(1);

        gameRunner.start();
    }

    private static String compileJava(String botFile) throws IOException, InterruptedException {

        File outFolder = Files.createTempDir();
        
        System.out.println("Compiling Boss.java... " + botFile);
        Process compileProcess = Runtime.getRuntime().exec(
            new String[] {
                "bash", "-c", "javac " + botFile + " -d " + outFolder.getAbsolutePath()
            }
        );
        compileProcess.waitFor();
        return "java -cp " + outFolder + " Player";
    }
}
