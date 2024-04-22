import api.HiraganaKatakanaRecognitionApi;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class NeuroBridge {

    public static String recognizeSymbol(BufferedImage bufferedImage, boolean remotely){
        byte [] imageBytes;
        try {
            imageBytes = ImageProcessor.processImage(bufferedImage);
        } catch (ImageProcessor.EmptyImageException e) {
            return "Empty image";
        }
        if(remotely){
            return launchNeuroRemotely(imageBytes);
        }
        else {
            return launchNeuroLocally(imageBytes);
        }
    }
    private static String launchNeuroLocally(byte [] image_bytes) {
        try (FileOutputStream fileOutputStream =new FileOutputStream("src/main/java/saves/neuroinput")){
            fileOutputStream.write(image_bytes);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        StringBuilder output;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder( "pipenv","run","python","ML/modelLaunch.py","src/main/java/saves/neuroinput"
            );
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return output.toString();
    }


    private static String launchNeuroRemotely(byte [] image_bytes) {
        HiraganaKatakanaRecognitionApi hiraganaKatakanaRecognitionApi = new HiraganaKatakanaRecognitionApi();
        return hiraganaKatakanaRecognitionApi.getSymbolAsync(image_bytes,0);
    }
}
