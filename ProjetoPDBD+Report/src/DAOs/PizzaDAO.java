package DAOs;

import Model.Pizza;
import Utils.JDBCUtil;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author augustosimionato
 */
public class PizzaDAO {

    //MARK: Configuration of DB connection
    private Connection connection = null;
    private PreparedStatement pstdados = null;
    private ResultSet results = null;
    private static final String path = System.getProperty("user.dir");
    private static final File config_file = new File(path + "/src/Utils/configuracaobd.properties");
    
    //MARK: SQL script
    private static final String sqlLoadAllPizzas = "SELECT * FROM pizzas order by id";
    private static final String sqlInsert = "INSERT INTO pizzas (id, nomePizza, descricao) VALUES (?, ?, ?)";
    private static final String sqlUpdate = "UPDATE pizzas SET nomePizza = ?, descricao = ? WHERE id = ?";
    private static final String sqlDelete = "DELETE FROM pizzas WHERE id = ?";
    
    public static final String reports_archive = System.getProperty("user.dir") + "/src/relatorios/";
    public static final File pizzas_report = new File(reports_archive, "PizzasReport.jrxml");

    public boolean openConection() {
        try {
            JDBCUtil.init(config_file);
            connection = JDBCUtil.getConnection();
            connection.setAutoCommit(false);
            System.out.println("Conection OK!");

            return true;
        } catch (ClassNotFoundException erro) {
            System.out.println("Can't load driver JDBC: " + erro);
        } catch (IOException erro) {
            System.out.println("Can't load configuration file: " + erro);
        } catch (SQLException erro) {
            System.out.println("Can't connect to DB, sql command = " + erro);
        }
        return false;
    }

    public boolean closeConection() {
        if (connection != null) {
            try {
                connection.close();
                return true;
            } catch (SQLException erro) {
                System.err.println("Can't close the connection: " + erro);
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean insert(Pizza piz) {
        try {
            int tipo = ResultSet.TYPE_SCROLL_SENSITIVE;
            int concorrencia = ResultSet.CONCUR_UPDATABLE;
            pstdados = connection.prepareStatement(sqlInsert, tipo, concorrencia);
            pstdados.setInt(1, piz.getIdPizza());
            pstdados.setString(2, piz.getNomePizza());
            pstdados.setString(3, piz.getDescricao());
            int check = pstdados.executeUpdate();
            pstdados.close();
            
            if (check == 0) {
                connection.rollback();
                System.out.println("Can't insert");
                return false;
            }
            
            System.out.println("Inserted");
            connection.commit();
            
            return true;
            
        } catch (SQLException erro) {
            System.out.println("Insert went wrong: " + erro);
        }
        
        return false;
    }

    public boolean update(Pizza piz) {
       try {
            int type = ResultSet.TYPE_SCROLL_SENSITIVE;
            int concurrency = ResultSet.CONCUR_UPDATABLE;
            
            pstdados = connection.prepareStatement(sqlUpdate, type, concurrency);
            pstdados.setString(1, piz.getNomePizza());
            pstdados.setString(2, piz.getDescricao());
            pstdados.setInt(3, piz.getIdPizza());
            
            int resposta = pstdados.executeUpdate();
            pstdados.close();
            
            if (resposta == 1) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
            
        } catch (SQLException erro) {
            System.out.println("Update went wrong: " + erro);
        }
        return false;
    }

    public boolean delete(Pizza piz) {
        try {
            int tipo = ResultSet.TYPE_SCROLL_SENSITIVE;
            int concorrencia = ResultSet.CONCUR_UPDATABLE;
            
            pstdados = connection.prepareStatement(sqlDelete, tipo, concorrencia);
            pstdados.setInt(1, piz.getIdPizza());
            
            int resposta = pstdados.executeUpdate();
            pstdados.close();
            
            if (resposta == 1) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException erro) {
            System.out.println("Delete went wrong: " + erro);
        }
        return false;
    }

    public boolean searchAll() {
        try {
            int type = ResultSet.TYPE_SCROLL_SENSITIVE;
            int concurrency = ResultSet.CONCUR_UPDATABLE;
            
            pstdados = connection.prepareStatement(sqlLoadAllPizzas, type, concurrency);
            results = pstdados.executeQuery();
            
            return true;
        } catch (SQLException erro) {
            System.out.println("Search went wrong: " + erro);
        }
        return false;
    }

    public Pizza getPizza() {
        Pizza piz = null;
        
        if (results != null) {
            try {
                int id = results.getInt("id");
                String nomePizza = results.getString("nomePizza");
                String descricao = results.getString("descricao");
                piz = new Pizza(id, nomePizza, descricao);
            } catch (SQLException erro) {
                System.out.println(erro);
            }
        }
        
        return piz;
    }

    /**
     * @return the results
     */
    public ResultSet getResults() {
        return results;
    }

}
