package DAOs;

import Model.Order;
import Utils.JDBCUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 * @author augustosimionato
 */
public class OrderDAO {

    //MARK: Configuration of DB connection
    private Connection connection = null;
    private PreparedStatement pstdados = null;
    private ResultSet results = null;
    private static final String path = System.getProperty("user.dir");
    private static final File config_file = new File(path + "/src/Utils/configuracaobd.properties");

    //MARK: SQL script
    private static final String sqlLoadAllOrders = "SELECT * FROM orders order by id";
    private static final String sqlInsert = "INSERT INTO orders (id, nomeCompleto, endereco, cep, descricaoPedido, tipoPedido) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String sqlUpdate = "UPDATE orders SET nomeCompleto = ?, endereco = ?, cep = ?, descricaoPedido = ?, tipoPedido = ? WHERE id = ?";
    private static final String sqlDelete = "DELETE FROM orders WHERE id = ?";

    public static final String reports_archive = System.getProperty("user.dir") + "/src/relatorios/";
    public static final File orders_report = new File(reports_archive, "OrdersReport.jrxml");
    public static final File orders_type_report = new File(reports_archive, "OrderDeliveryReport.jrxml");

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

    public boolean insert(Order ord) {
        try {
            int type = ResultSet.TYPE_SCROLL_SENSITIVE;
            int concurrency = ResultSet.CONCUR_UPDATABLE;
            pstdados = connection.prepareStatement(sqlInsert, type, concurrency);
            pstdados.setInt(1, ord.getIdOrder());
            pstdados.setString(2, ord.getNomeCompleto());
            pstdados.setString(3, ord.getEndereco());
            pstdados.setString(4, ord.getCep());
            pstdados.setString(5, ord.getDescricaoPedido());
            pstdados.setString(6, ord.getTipoPedido());
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

    public boolean update(Order ord) {
        try {
            int type = ResultSet.TYPE_SCROLL_SENSITIVE;
            int concurrency = ResultSet.CONCUR_UPDATABLE;

            pstdados = connection.prepareStatement(sqlUpdate, type, concurrency);
            pstdados.setString(1, ord.getNomeCompleto());
            pstdados.setString(2, ord.getEndereco());
            pstdados.setString(3, ord.getCep());
            pstdados.setString(4, ord.getDescricaoPedido());
            pstdados.setString(5, ord.getTipoPedido());
            pstdados.setInt(6, ord.getIdOrder());

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

    public boolean delete(Order ord) {
        try {
            int type = ResultSet.TYPE_SCROLL_SENSITIVE;
            int concurrency = ResultSet.CONCUR_UPDATABLE;

            pstdados = connection.prepareStatement(sqlDelete, type, concurrency);
            pstdados.setInt(1, ord.getIdOrder());

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

            pstdados = connection.prepareStatement(sqlLoadAllOrders, type, concurrency);
            results = pstdados.executeQuery();

            return true;
        } catch (SQLException erro) {
            System.out.println("Search went wrong: " + erro);
        }
        return false;
    }

    public Order getOrder() {
        Order ord = null;

        if (results != null) {
            try {
                int id = results.getInt("id");
                String nomeCompleto = results.getString("nomeCompleto");
                String endereco = results.getString("endereco");
                String cep = results.getString("cep");
                String descricao = results.getString("descricaoPedido");
                String tipo = results.getString("tipoPedido");
                ord = new Order(id, nomeCompleto, endereco, cep, descricao, tipo);
            } catch (SQLException erro) {
                System.out.println(erro);
            }
        }

        return ord;
    }

    public Map createParamOrder() {
        Map params = new HashMap();

        var type = String.valueOf(JOptionPane.showInputDialog(
                null,
                "Digite o tipo de pedido para gerar o relatório (Delivery ou Retirada no local)",
                "Gerar relatório por tipo de pedido",
                JOptionPane.QUESTION_MESSAGE));

        params.put("SelectAllDelivery", type);
        return params;
    }

    public void geTypeReport() throws JRException {
        JasperPrint typeResponse;

        try {
            FileInputStream reportFile = new FileInputStream(orders_type_report);
            JasperReport report = JasperCompileManager.compileReport(reportFile);
            typeResponse = JasperFillManager.fillReport(report, createParamOrder(), connection);
            JasperViewer.viewReport(typeResponse);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Tipo não existe ou relatório não existe!", "Erro ao abrir relatório", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @return the results
     */
    public ResultSet getResults() {
        return results;
    }

}
