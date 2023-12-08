package Utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * @author augustosimionato
 */
public class JDBCUtil {

    private static String url;
    private static String username;
    private static String password;

    /**
     * Initializes the data source.
     *
     * @param fileName the name of the property file that contains the database
     * driver, URL, username, and password
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public static void init(File fileName) throws IOException, ClassNotFoundException {
        Properties props = new Properties();
        FileInputStream in = new FileInputStream(fileName);
        
        props.load(in);
        String driver = props.getProperty("jdbc.driver");
        url = props.getProperty("jdbc.url");
        username = props.getProperty("jdbc.username");
        
        if (username == null) {
            username = "";
        }
        
        password = props.getProperty("jdbc.password");
        
        if (password == null) {
            password = "";
        }
        
        if (driver != null) {
            Class.forName(driver);
        }
    }

    /**
     * Gets a connection to the database.
     *
     * @return the database connection
     *
     * @throws java.sql.SQLException
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static boolean movNext(ResultSet rsdados) {
        try {
            if (rsdados != null) {
                if (!rsdados.isLast()) {
                    rsdados.next();
                    return true;
                }               
            }
        } catch (SQLException erro) {
            System.out.println(erro);
            return false;
        }
        return false;
    }

    public static boolean movPrev(ResultSet rsdados) {
        try {
            if (rsdados != null) {
                if (!rsdados.isFirst()) {
                    rsdados.previous();
                    return true;
                }
            }
        } catch (SQLException erro) {
            System.out.println(erro);
            return false;
        }
        return false;
    }

    public static boolean catchFirst(ResultSet rsdados) {
        try {
            if (rsdados != null) {
                if (!rsdados.isFirst()) {
                    rsdados.first();
                    return true;
                }
            }
        } catch (SQLException erro) {
            System.out.println(erro);
            return false;
        }
        return false;
    }
    
}
