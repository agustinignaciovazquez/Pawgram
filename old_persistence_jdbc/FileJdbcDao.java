package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.exception.FileException;
import ar.edu.itba.pawgram.interfaces.exception.FileUploadException;
import ar.edu.itba.pawgram.interfaces.persistence.FileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Repository
public class FileJdbcDao implements FileDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static ResultSetExtractor<byte[]> ROW_MAPPER = new ResultSetExtractor<byte[]> () {

        @Override
        public byte[] extractData(ResultSet rs) throws SQLException, DataAccessException {
            return rs.next() ? rs.getBytes("data") : null;
        }
    };

    @Autowired
    public FileJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("fileDump");
    }

    @Override
    public String createFile(String path, String fileName, byte[] raw_file) throws FileUploadException {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("pathId", path);
        args.put("imageId", fileName);
        args.put("data", raw_file);
        try {
            jdbcInsert.execute(args);
            return fileName;
        }catch (DuplicateKeyException e) {
            throw new FileUploadException();
        }
    }

    @Override
    public byte[] getFile(String path, String fileName) throws FileException {

        byte[] bytes = jdbcTemplate.query("SELECT data FROM fileDump WHERE pathId = ? AND imageId = ? ",
                ROW_MAPPER, path, fileName);

        if (bytes == null)
            throw new FileException();

        return bytes;
    }
}
