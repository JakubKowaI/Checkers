package lib.test.DB;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    // Define a DataSource bean
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://chinesecheckerdatabase-chinesecheckers.e.aivencloud.com:23193/defaultdb");
        dataSource.setUsername("avnadmin");
        dataSource.setPassword("AVNS_2f5tJnKcXMOBSpN8dNk");
        return dataSource;
    }

    // Define a JdbcTemplate bean
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // Define a service or DAO bean
    @Bean
    public MyService myService(JdbcTemplate jdbcTemplate) {
        return new MyServiceImpl(jdbcTemplate);
    }
}
