package com.sample.libdbgenerator;


import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Edward on 9/11/2017.
 * <p>
 * The method of inserting data follows the findings of this blogpost: http://blog.quibb.org/2010/08/fast-bulk-inserts-into-sqlite/
 * <p>
 * In junction with the transaction tests from the jdbc library: https://github.com/xerial/sqlite-jdbc/blob/master/src/test/java/org/sqlite/TransactionTest.java
 */
public class SQLiteJDBCAssetHelper {

    public static final String JDBCSQLITE = "jdbc:sqlite:";

    public static final int BATCH_SIZE = 1000;
    public static final int CREATE_QUERY_TIMEOUT = 60;

    private String folderPath;

    public SQLiteJDBCAssetHelper(String folderPath) {
        this.folderPath = folderPath;
    }

    String getDBPath(String dbName) {
        return folderPath + File.separator + dbName + ".db";
    }

    String getDBConnectionPath(String dbName) {
        return JDBCSQLITE + getDBPath(dbName);
    }

    boolean cleanDatabaseFile(String file) {
        File f = new File(file);
        return !f.exists() | f.delete();
    }

    public void createDB(String dbName) throws IOException {
        if (!cleanDatabaseFile(getDBPath(dbName))) {
            throw new IOException("database file could not be cleared.");
        }

        try (Connection connection = DriverManager.getConnection(getDBConnectionPath(dbName))) {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(CREATE_QUERY_TIMEOUT);

            //android specific table
            statement.executeUpdate("CREATE TABLE android_metadata (locale TEXT)");

            //identity check
            statement.executeUpdate("CREATE TABLE \"Identity\" (`_id`	INTEGER NOT NULL, `MD5`	TEXT NOT NULL, `LastUpdatedAt`	REAL NOT NULL, PRIMARY KEY(`_id`))");

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            throw new IOException(e);
        }
    }

    public void setIdentity(String identity, String dbName) throws IOException {

        try (Connection connection = DriverManager.getConnection(getDBConnectionPath(dbName))) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO \"Identity\" (`MD5`, `LastUpdatedAt`"
                                                                              + ") VALUES ("
                                                                              + " ?, ? )");
            statement.setQueryTimeout(CREATE_QUERY_TIMEOUT);

            updateLine("Inserting Identity");

            //android specific table
            statement.setString(1, identity);
            statement.setDouble(2, System.currentTimeMillis());
            statement.addBatch();

            statement.executeBatch();
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            throw new IOException(e);
        }
    }

    public static void updateLine(String message) {
        System.out.print("\r" + message);
    }

}
