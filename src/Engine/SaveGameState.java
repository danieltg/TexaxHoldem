package Engine;

import java.io.*;

public class SaveGameState {

    public static void saveGameDataToFile(String path,GameManager gameManager ) throws IOException {
            File file = new File(path);
            FileOutputStream fileStream = new FileOutputStream(file);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);

            objectStream.writeObject(gameManager);

            objectStream.close();
            fileStream.close();
    }

}
