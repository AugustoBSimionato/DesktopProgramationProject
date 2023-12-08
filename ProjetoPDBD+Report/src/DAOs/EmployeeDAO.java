package DAOs;

import Model.Employee;
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
public class EmployeeDAO {

    //MARK: Configuration of DB connection
    private Connection connection = null;
    private PreparedStatement pstdados = null;
    private ResultSet results = null;
    private static final String path = System.getProperty("user.dir");
    private static final File config_file = new File(path + "/src/Utils/configuracaobd.properties");
    
    //MARK: SQL script
    private static final String sqlLoadAllEmployees = "SELECT * FROM employees order by id";
    private static final String sqlInsert = "INSERT INTO employees (id, nomeCompleto, cpf, endereco, cep, telefone, contratacao, demissao) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String sqlUpdate = "UPDATE employees SET nomeCompleto = ?, cpf = ?, endereco = ?, cep = ?, telefone = ?, contratacao = ?, demissao = ? WHERE id = ?";
    private static final String sqlDelete = "DELETE FROM employees WHERE id = ?";
    
    public static final String reports_archive = System.getProperty("user.dir") + "/src/relatorios/";
    public static final File employees_report = new File(reports_archive, "EmployeesReport.jrxml");

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

    public boolean insert(Employee emp) {
        try {
            int type = ResultSet.TYPE_SCROLL_SENSITIVE;
            int concurrency = ResultSet.CONCUR_UPDATABLE;
            pstdados = connection.prepareStatement(sqlInsert, type, concurrency);
            pstdados.setInt(1, emp.getIdEmployee());
            pstdados.setString(2, emp.getNomeCompleto());
            pstdados.setString(3, emp.getCpf());
            pstdados.setString(4, emp.getEndereco());
            pstdados.setString(5, emp.getCep());
            pstdados.setString(6, emp.getTelefone());
            pstdados.setString(7, emp.getContratacao());
            pstdados.setString(8, emp.getDemissao());
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

    public boolean update(Employee emp) {
       try {
            int type = ResultSet.TYPE_SCROLL_SENSITIVE;
            int concurrency = ResultSet.CONCUR_UPDATABLE;
            pstdados = connection.prepareStatement(sqlUpdate, type, concurrency);
            pstdados.setString(1, emp.getNomeCompleto());
            pstdados.setString(2, emp.getCpf());
            pstdados.setString(3, emp.getEndereco());
            pstdados.setString(4, emp.getCep());
            pstdados.setString(5, emp.getTelefone());
            pstdados.setString(6, emp.getContratacao());
            pstdados.setString(7, emp.getDemissao());
            pstdados.setInt(8, emp.getIdEmployee());
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

    public boolean delete(Employee emp) {
        try {
            int type = ResultSet.TYPE_SCROLL_SENSITIVE;
            int concurrency = ResultSet.CONCUR_UPDATABLE;
            
            pstdados = connection.prepareStatement(sqlDelete, type, concurrency);
            pstdados.setInt(1, emp.getIdEmployee());
            
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
            
            pstdados = connection.prepareStatement(sqlLoadAllEmployees, type, concurrency);
            results = pstdados.executeQuery();
            
            return true;
        } catch (SQLException erro) {
            System.out.println("Search went wrong: " + erro);
        }
        return false;
    }

    public Employee getEmployee() {
        Employee emp = null;
        
        if (results != null) {
            try {
                int id = results.getInt("id");
                String nomeCompleto = results.getString("nomeCompleto");
                String cpf = results.getString("cpf");
                String endereco = results.getString("endereco");
                String cep = results.getString("cep");
                String telefone = results.getString("telefone");
                String contratacao = results.getString("contratacao");
                String demissao = results.getString("demissao");
                emp = new Employee(id, nomeCompleto, cpf, endereco, cep, telefone, contratacao, demissao);
            } catch (SQLException erro) {
                System.out.println(erro);
            }
        }
        
        return emp;
    }

    /**
     * @return the results
     */
    public ResultSet getResults() {
        return results;
    }

}
