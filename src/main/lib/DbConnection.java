package main.lib;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class DbConnection {

    public DataSource dataSource(){
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUser("postgres");
        dataSource.setPassword("1234");
        dataSource.setDatabaseName("postgres");
        return dataSource;
    }
}
