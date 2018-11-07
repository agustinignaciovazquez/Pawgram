package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.exception.FileException;
import ar.edu.itba.pawgram.interfaces.exception.FileUploadException;
import ar.edu.itba.pawgram.interfaces.persistence.FileDao;
import ar.edu.itba.pawgram.model.FileDump;
import ar.edu.itba.pawgram.model.Post;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class FileHibernateDao implements FileDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public String createFile(String path, String fileName, byte[] raw_file) throws FileUploadException {
        final FileDump fd = new FileDump(path,fileName,raw_file);
        try{
            //Try to insert your entity by calling persist method
            em.persist(fd);
            em.flush(); //to catch exception
        }
        catch(EntityExistsException e){
            //Entity you are trying to insert already exist, then call merge method
            throw new FileUploadException();
        }
        return fileName;
    }

    @Override
    public byte[] getFile(String path, String fileName) throws FileException {
        final TypedQuery<FileDump> query = em.createQuery("select f from FileDump as f WHERE f.pathId = :pathId AND f.imageId = :imageId", FileDump.class);
        query.setParameter("pathId", path);
        query.setParameter("imageId", fileName);

        List<FileDump> result = query.getResultList();
        if(result.isEmpty())
            throw new FileException();

        final byte[] data = result.get(0).getData();
        return data;
    }
}
