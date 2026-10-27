import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class DataSourceReader {

    private DataSourceReader() {
    }

    public static Reader open(String source) throws IOException {
        if (source.startsWith("https://") || source.startsWith("http://")) {
            URLConnection connection = URI.create(source).toURL().openConnection();
            connection.setConnectTimeout(10_000);
            connection.setReadTimeout(10_000);

            return new InputStreamReader(
                    connection.getInputStream(),
                    StandardCharsets.UTF_8
            );
        }

        return Files.newBufferedReader(
                Path.of(source),
                StandardCharsets.UTF_8
        );
    }
}