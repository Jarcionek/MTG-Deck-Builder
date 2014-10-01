package mtgdeckbuilder.backend;

import mtgdeckbuilder.data.Url;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageDownloader {

    public void download(Url url, File file) {
        try {
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();

            InputStream inputStream = url.unwrap().openStream();
            OutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[2048];
            int length;

            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
