package ar.edu.itba.pawgram.interfaces.service;

import java.io.IOException;
import java.util.List;

public interface FileService {
    /**
     * Creates a new file
     * @param path - path where the file will be stored
     * @param raw_file - file raw bytes
     * @return file name (randomly generated)
     */
    public String createFile(final String path, final byte[] raw_file) throws IOException;

    /**
     * Get raw bytes of a file
     * @param path - path where the file is stored
     * @return raw bytes of file
     */
    public byte[] getFile(final String path) throws IOException;
}
