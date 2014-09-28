package mtgdeckbuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class UrlDownloader {

    public String download(Url url) {
        try {
            InputStream inputStream = url.unwrap().openStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();

            String line = bufferedReader.readLine();
            while (true) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
                if (line == null) {
                    break;
                } else {
                    stringBuilder.append(System.getProperty("line.separator"));
                }
            }

            bufferedReader.close();

            return stringBuilder.toString();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
