package api;

import api.responsebody.SymbolRecognitionResponse;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HiraganaKatakanaRecognitionApi {
    private final String API_URI = "http://127.0.0.1:8000/items";
    private String apiKey;
    public String getSymbolAsync(byte [] image, int mode) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(API_URI).openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("image", image);
            jsonObject.put("mode", mode);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                return bufferedReader.readLine();
            }
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
