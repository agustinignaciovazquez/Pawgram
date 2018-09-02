package ar.edu.itba.pawgram.persistence;

import javax.sql.DataSource;

import org.hsqldb.jdbc.JDBCDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ComponentScan({ "ar.edu.itba.pawgram.persistence", })
@Configuration
public class TestConfig {
	@Bean
	public DataSource dataSource() {
	final SimpleDriverDataSource ds = new SimpleDriverDataSource();
	ds.setDriverClass(JDBCDriver.class);
	ds.setUrl("jdbc:hsqldb:mem:paw");
	ds.setUsername("ha");
	ds.setPassword("");
	return ds;
	}
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}
}
