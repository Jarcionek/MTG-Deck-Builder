package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestingSearchSpeed {

    private static final int X = 10000;
    public static final String N = System.getProperty("line.separator");

    public static void main(String[] args) throws IOException {
        File desktop = new File("C:\\Users\\Jarcionek\\Desktop\\testujemy");
        desktop.mkdir();
        for (int i = 0; i < X; i++) {
            write(new File(desktop, i + ".txt"),
                    "Outlast {W} ({W}, {T}: Put a +1/+1 counter on this creature. Outlast only as a sorcery.)" + N +
                    N +
                    "Each creature you control with a +1/+1 counter on it has lifelink."
            );
        }
        write(new File(desktop, X + ".txt"),
                "basdgs This is my search phrase sadasgsg"
        );
        System.out.println("files created");

        long start = System.currentTimeMillis();

        for (File file : desktop.listFiles()) {
            byte[] blah = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            String blah2 = new String(blah, Charset.forName("Unicode"));
            if (blah2.contains("This is my search phrase")) {
                System.out.println("found!");
            }
        }

        long end = System.currentTimeMillis();

        System.out.println("done, took: " + (end - start) + "ms");
    }

    private static void write(File file, String str) throws IOException {
        Writer bf = new OutputStreamWriter(new FileOutputStream(file), "Unicode");
        bf.write(str);
        bf.close();
    }

}
