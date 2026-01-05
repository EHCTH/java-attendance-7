package attendance.infrastrucutre;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvWriter {
    private final Path resourceDir;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public CsvWriter() {
        this.resourceDir = Paths.get("src", "main", "resources");
    }

    public void writeNewFile(String fileName, String header, List<String> lines) {
        try {

            Files.createDirectories(resourceDir);
            Path target = resourceDir.resolve(fileName);
            List<String> writeLine = new ArrayList<>(lines);
            writeLine.addFirst(header);
            Files.write(target, writeLine, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] resources에 파일을 저장할 수 없습니다: " + fileName, e);
        }
    }


    public void appendWithHeaderIfNew(String fileName, String header, List<String> lines) {
        try {
            Files.createDirectories(resourceDir);

            Path target = resourceDir.resolve(fileName);
            System.out.println(target.getFileName());
            boolean needHeader = Files.notExists(target) || Files.size(target) == 0;

            List<String> toWrite = new ArrayList<>();
            if (needHeader) {
                toWrite.add(header);
            }
            toWrite.addAll(lines);

            Files.write(target, toWrite, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);

        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] resources에 파일을 저장할 수 없습니다: " + fileName, e);
        }
    }
}
