package mtgdeckbuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class CardsInfoDownloader {

    private Url url;
    private File file;

    public void set(Url url, File file) {
        this.url = url;
        this.file = file;
    }

    public void download() {
        try {
            InputStream inputStream = url.unwrap().openStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            PrintWriter printWriter = new PrintWriter(file);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                printWriter.println(line);
            }

            bufferedReader.close();
            printWriter.close();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
