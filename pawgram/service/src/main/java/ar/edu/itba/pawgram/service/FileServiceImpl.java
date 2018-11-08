package ar.edu.itba.pawgram.service;

import ar.edu.itba.pawgram.interfaces.exception.FileException;
import ar.edu.itba.pawgram.interfaces.exception.FileUploadException;
import ar.edu.itba.pawgram.interfaces.persistence.FileDao;
import ar.edu.itba.pawgram.interfaces.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {

    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyz0123456789";
    @Autowired
    private FileDao fileDao;

    @Override
    @Transactional
    public String createFile(String path, byte[] raw_file) throws FileUploadException {
        return fileDao.createFile(path,randomAlphaNumeric(32),raw_file);
    }

    @Override
    public byte[] getFile(String path, String fileName) throws FileException {
        return fileDao.getFile(path,fileName);
    }

    /*
    //DEPRECATED USING FILE MANAGER
    @Override
    public String createFile(String path, byte[] raw_file) throws FileUploadException {
            String random_name = randomAlphaNumeric(32);
            //Path p = Paths.get(path + File.separator + random_name);
            // Files.write(p, raw_file);
            File dir = new File(path + File.separator);
            if(!dir.exists())
                throw new FileUploadException();

            File serverFile = new File(dir.getAbsolutePath() + File.separator + random_name);
            BufferedOutputStream stream = null;
            try {
                stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(raw_file);
                stream.close();
            } catch (FileNotFoundException e) {
                //e.printStackTrace(); DEBUG ONLY
                throw new FileUploadException();
            } catch (IOException e) {
                //e.printStackTrace(); DEBUG ONLY
                throw new FileUploadException();
            }
            return random_name;
    }

    @Override
    public byte[] getFile(String path, String fileName) throws FileException {
            File serverFile = new File(path + File.separator + fileName);
            byte[] bytes = new byte[0];
            try {
                bytes = Files.readAllBytes(serverFile.toPath());
            } catch (IOException e) {
                //e.printStackTrace(); DEBUG ONLY
                throw new FileException();
            }
            return bytes;
    }

    */
    private static String randomAlphaNumeric(int count) {
            StringBuilder builder = new StringBuilder();
            while (count-- != 0) {
                int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
                builder.append(ALPHA_NUMERIC_STRING.charAt(character));
            }
            return builder.toString();
    }

}
