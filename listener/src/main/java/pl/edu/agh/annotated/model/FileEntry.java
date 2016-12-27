package pl.edu.agh.annotated.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.edu.agh.exceptions.TokenCouldNotBeParsedException;
import pl.edu.agh.util.FileHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Przemek on 27.12.2016.
 */
public class FileEntry {
    private String name;
    private String token;
    public List<FileEntry> children;
    private File file;


    public FileEntry(File file) {
        this.name = file.getName();
        this.file = file;

        if(this.directory()) {
            this.token = null;
            children = new ArrayList<>();
        } else {
            try {
                this.token = FileHelper.get().getToken(file.toPath());
            } catch (TokenCouldNotBeParsedException e) {
                this.token = null;
            }
            children = null;
        }
    }

    public void addSuccessor(FileEntry entry) {
        this.children.add(entry);
    }

    @Override
    public String toString() {
        try {
            ObjectMapper myObjectMapper = new ObjectMapper();
            return myObjectMapper.writeValueAsString(this)
                                 .replaceAll("\"token\":null,", "")
                                 .replaceAll("\"children\":null", "")
                                 .replaceAll(",}", "}");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Tree could not be created";
        }
    }

    public boolean directory() {
        return file.isDirectory();
    }

    public String getName() {
        return file.getName();
    }

    public String getToken() {
        return token;
    }

    public File[] listFiles() {
        return file.listFiles();
    }

    public File file() {
        return file;
    }
}
