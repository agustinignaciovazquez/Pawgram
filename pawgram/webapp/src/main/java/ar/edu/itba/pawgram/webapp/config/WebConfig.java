package ar.edu.itba.pawgram.webapp.config;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;



@EnableWebMvc
@EnableTransactionManagement
@ComponentScan({ "ar.edu.itba.pawgram.webapp.validators", "ar.edu.itba.pawgram.webapp.rest", "ar.edu.itba.pawgram.webapp.utils",
		"ar.edu.itba.pawgram.persistence", "ar.edu.itba.pawgram.service" })
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	@Value("classpath:schema.sql")
	private Resource schemaSql;

	@Value("classpath:haversine.sql")
	private Resource haversineSql;

	private int maxUploadSizeInMb = 5 * 1024 * 1024; // 5 MB

	@Bean
	public DataSource dataSource() {
		final SimpleDriverDataSource ds = new SimpleDriverDataSource();
		ds.setDriverClass(org.postgresql.Driver.class);


		/*//PRODUCCION
		ds.setUrl("jdbc:postgresql://10.16.1.110/paw-2018b-11");
		ds.setUsername("paw-2018b-11");
		ds.setPassword("29bvHDhpm");*/

		//LOCAL
		ds.setUrl("jdbc:postgresql://localhost/pawgram");
		ds.setUsername("pawgram");
		ds.setPassword("123456aa");

		return ds;
	}
	
	@Bean
	public MessageSource messageSource() {
		final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:i18n/messages");
		messageSource.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
		messageSource.setCacheSeconds(5);
		return messageSource;
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	@Bean
	public DataSourceInitializer dataSourceInitializer(final DataSource ds) {
		final DataSourceInitializer dsi = new DataSourceInitializer();
		dsi.setDataSource(ds);
		dsi.setDatabasePopulator(databasePopulator());
		return dsi;
	}

	@Bean
	public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}

	@Bean
	public Validator validator() {
		return Validation.buildDefaultValidatorFactory().getValidator();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setPackagesToScan("ar.edu.itba.pawgram.model");
		factoryBean.setDataSource(dataSource());

		final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		factoryBean.setJpaVendorAdapter(vendorAdapter);

		final Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", "update");
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL92Dialect");

		// ONLY FOR DEBUG, COMMENT THIS FOR DEPLOY PRODUCTION
		//properties.setProperty("hibernate.show_sql", "true");
		//properties.setProperty("format_sql", "true");

		factoryBean.setJpaProperties(properties);
		return factoryBean;
	}

	@Bean
	public CommonsMultipartResolver multipartResolver() {

		/*CommonsMultipartResolver cmr = new CommonsMultipartResolver();
		cmr.setMaxUploadSize(maxUploadSizeInMb * 2);
		cmr.setMaxUploadSizePerFile(maxUploadSizeInMb); //bytes
		return cmr;*/
		return new CommonsMultipartResolver();
	}

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);

		mailSender.setUsername("pawgram.noreply@gmail.com");
		mailSender.setPassword("tomysoracco_papota");

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		//props.put("mail.debug", "true");

		return mailSender;
	}


	private DatabasePopulator databasePopulator() {
		final ResourceDatabasePopulator dbp = new ResourceDatabasePopulator();
		dbp.addScript(haversineSql);
		return dbp;
	}

}
