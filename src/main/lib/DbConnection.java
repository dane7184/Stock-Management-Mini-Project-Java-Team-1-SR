package main.lib;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class DbConnection {

    public DataSource dataSource(){
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUser("postgres");
        dataSource.setPassword("admin");
        dataSource.setDatabaseName("stock_management_system");
        return dataSource;
    }
}
