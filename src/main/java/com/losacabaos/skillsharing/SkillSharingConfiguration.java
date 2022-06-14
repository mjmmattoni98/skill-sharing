package com.losacabaos.skillsharing;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SkillSharingConfiguration {
    @Value("${PASS}")
    private String password;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    /*    @Bean
        public ConnectionFactory connectionFactory(R2dbcProperties properties) {
            ConnectionFactoryOptions baseOptions = ConnectionFactoryOptions.parse(properties.getUrl());
            ConnectionFactoryOptions.Builder ob = ConnectionFactoryOptions.builder().from(baseOptions);
            if (!StringUtil.isNullOrEmpty(properties.getUsername())) {
                ob = ob.option(USER, properties.getUsername());
            }
            if (!StringUtil.isNullOrEmpty(properties.getPassword())) {
                ob = ob.option(PASSWORD, properties.getPassword());
            }
            return ConnectionFactories.get(ob.build());
        }
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host("localhost")
                        .port(5432)
                        .username("postgres")
                        .password(password)
                        .database("skill-sharing")
                        .build());
    }

/*    @Bean
    public ConnectionFactory connectionFactory() {
        //return ConnectionFactories.get("r2dbc:postgresql://localhost:5432/skillsharing");
        ConnectionFactoryOptions baseOptions = ConnectionFactoryOptions.parse("r2dbc:postgresql://localhost:5432/skillsharing");
        ConnectionFactoryOptions.Builder ob = ConnectionFactoryOptions.builder().from(baseOptions);
        ob = ob.option(USER, "postgres");
        ob = ob.option(PASSWORD, "marc0s98");
        return ConnectionFactories.get(ob.build());
    }

 */

/*    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("schema.sql"));
        initializer.setDatabasePopulator(populator);
        return initializer;
    }

 */

/*    @Bean
    ConnectionFactoryInitializer initializer(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        ResourceDatabasePopulator resource =
                new ResourceDatabasePopulator(new ClassPathResource("schema.sql"));
        initializer.setDatabasePopulator(resource);
        return initializer;
    }
 */
}
