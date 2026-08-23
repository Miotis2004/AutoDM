package com.autodm.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class DatabaseConfig {

    @Value("${autodm.data-dir:${user.home}/.autodm}")
    private String dataDir;

    @Value("${autodm.db-name:autodm.db}")
    private String dbName;

    @Bean
    public DataSource dataSource() {
        Path path = Paths.get(dataDir);
        File directory = path.toFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String dbPath = path.resolve(dbName).toString();
        // JDBC URL prefix for SQLite
        String url = "jdbc:sqlite:" + dbPath;

        return DataSourceBuilder.create()
                .driverClassName("org.sqlite.JDBC")
                .url(url)
                .build();
    }
}
