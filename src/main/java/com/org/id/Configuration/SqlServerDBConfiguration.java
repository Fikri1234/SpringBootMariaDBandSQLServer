package com.org.id.Configuration;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Fikri
 *
 * Aug 2, 2019
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "sqlServerEntityManagerFactory", 
        transactionManagerRef = "sqlServerTransactionManager",
        basePackages = { "com.org.id.DAO.SqlServer" })
public class SqlServerDBConfiguration {
	
	private static final Logger logger = LoggerFactory.getLogger(SqlServerDBConfiguration.class);
	
	@Autowired
    JpaVendorAdapter jpaVendorAdapter;

    @Value("${sqlserver.datasource.url}")
    private String databaseUrl;

    @Value("${sqlserver.datasource.username}")
    private String username;

    @Value("${sqlserver.datasource.password}")
    private String password;

    @Value("${sqlserver.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${sqlserver.jpa.database-platform}")
    private String dialect;

    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(databaseUrl, username, password);
        dataSource.setDriverClassName(driverClassName);
        logger.info("create temp datasource");
        return dataSource;
    }

    @Bean(name = "sqlServerEntityManager")
    public EntityManager entityManager() {
    	logger.info("temp entity manager");
        return entityManagerFactory().createEntityManager();
    }

    @Bean(name = "sqlServerEntityManagerFactory")
    public EntityManagerFactory entityManagerFactory() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", dialect);

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setJpaVendorAdapter(jpaVendorAdapter);
        emf.setPackagesToScan("com.org.id.Entity.SqlServer");   // <- package for entities
        emf.setPersistenceUnitName("sqlServerPersistenceUnit");
        emf.setJpaProperties(properties);
        emf.afterPropertiesSet();
        logger.info("temp entity manager factory");
        return emf.getObject();
    }

    @Bean(name = "sqlServerTransactionManager")
    public PlatformTransactionManager transactionManager() {
    	logger.info("temp platform trx");
        return new JpaTransactionManager(entityManagerFactory());
    }

}
