package ar.edu.itba.pawgram.interfaces.persistence;

import ar.edu.itba.pawgram.interfaces.exception.FileException;
import ar.edu.itba.pawgram.interfaces.exception.FileUploadException;

import java.io.IOException;
import java.util.List;

public interface FileDao {
    /**
     * Creates a new file
     * @param path - path where the file will be stored
     * @param raw_file - file raw bytes
     * @return file name (randomly generated)
     */
    public String createFile(final String path, final String fileName, final byte[] raw_file) throws FileUploadException;

    /**
     * Get raw bytes of a file
     * @param path - path where the file is stored
     * @return raw bytes of file
     */
    public byte[] getFile(final String path, final String fileName) throws FileException;
}

