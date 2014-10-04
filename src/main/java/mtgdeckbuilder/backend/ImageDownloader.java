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

            boolean downloadedAnything = false;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
                downloadedAnything = true;
            }

            inputStream.close();
            outputStream.close();

            if (!downloadedAnything) {
                //TODO Jarek: any more robust solution...?
                throw new RuntimeException("did not download any image! url=" + url + ", file=" + file.getAbsolutePath());
            }
        } catch (IOException exception) {
            throw new RuntimeException("url=" + url + ", file=" + file, exception);
        }
    }

}
