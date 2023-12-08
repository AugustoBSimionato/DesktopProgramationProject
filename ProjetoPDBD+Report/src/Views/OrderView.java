package Views;

import DAOs.OrderDAO;
import static DAOs.OrderDAO.orders_report;
import Model.Order;
import Utils.JDBCUtil;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 * @author augustosimionato
 */
public class OrderView extends javax.swing.JFrame {

    OrderDAO orderDAO = new OrderDAO();

    /**
     * Creates new form OrderView
     */
    public OrderView() {
        initComponents();
    }

    public void loadOrder(Order order) {
        idApagar.setText(String.valueOf(order.getIdOrder()));
        nomeCompleto.setText(order.getNomeCompleto());
        endereco.setText(String.valueOf(order.getEndereco()));
        cep.setText(String.valueOf(order.getCep()));
        descricaoPedido.setText(String.valueOf(order.getDescricaoPedido()));
        tipoPedido.setText(String.valueOf(order.getTipoPedido()));
    }

    public Order loadAllOrders() {
        int idOrder = Integer.parseInt(idApagar.getText());
        String nome = nomeCompleto.getText();
        String end = endereco.getText();
        String cp = cep.getText();
        String desc = descricaoPedido.getText();
        String tipo = tipoPedido.getText();
        Order order = new Order(idOrder, nome, end, cp, desc, tipo);

        return order;
    }

    public void clearFields() {
        idApagar.setText("");
        nomeCompleto.setText("");
        endereco.setText("");
        cep.setText("");
        descricaoPedido.setText("");
        tipoPedido.setText("");
        idApagar.requestFocus();
    }

    public void saveOrder() {
        Order order = loadAllOrders();
        if (orderDAO.insert(order)) {
            loadData();
            loadFirstRow();
            JOptionPane.showMessageDialog(this, "Pedido salvo com sucesso.");
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao inserir novo pedido.");
        }
    }

    public void deleteOrder() {
        Order order = loadAllOrders();
        if (orderDAO.delete(order)) {
            JOptionPane.showMessageDialog(this, "Pedido excluído com sucesso!");
            if (JDBCUtil.catchFirst(orderDAO.getResults())) {
                idApagar.setText(String.valueOf(order.getIdOrder()));
                nomeCompleto.setText(order.getNomeCompleto());
                endereco.setText(order.getEndereco());
                cep.setText(order.getCep());
                descricaoPedido.setText(String.valueOf(order.getDescricaoPedido()));
                tipoPedido.setText(order.getTipoPedido());
                loadData();
                loadFirstRow();
            } else {
                JOptionPane.showMessageDialog(this, "O primeiro pedido já está selecionado.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir pedido.");
        }
    }

    public void updateOrder() {
        Order order = loadAllOrders();
        if (orderDAO.update(order)) {
            loadData();
            loadFirstRow();
            JOptionPane.showMessageDialog(this, "Pedido alterado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao alterar pedido.");
        }
    }

    public void loadData() {
        if (orderDAO.openConection()) {
            if (orderDAO.searchAll()) {
                DefaultTableModel model = (DefaultTableModel) Pedidos.getModel();
                model.setRowCount(0);

                try {
                    while (orderDAO.getResults().next()) {
                        int id = orderDAO.getResults().getInt("id");
                        String nomeCompleto = orderDAO.getResults().getString("nomeCompleto");
                        String endereco = orderDAO.getResults().getString("endereco");
                        String cep = orderDAO.getResults().getString("cep");
                        String descricao = orderDAO.getResults().getString("descricaoPedido");
                        String tipoPedido = orderDAO.getResults().getString("tipoPedido");
                        model.addRow(new Object[]{id, nomeCompleto, endereco, cep, descricao, tipoPedido});
                    }
                } catch (SQLException sql) {
                    sql.printStackTrace();
                }

                DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
                leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

                Pedidos.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
                Pedidos.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
                Pedidos.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
                Pedidos.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);
                Pedidos.getColumnModel().getColumn(4).setCellRenderer(leftRenderer);
                Pedidos.getColumnModel().getColumn(5).setCellRenderer(leftRenderer);
            }
        }
    }

    public void loadFirstRow() {
        if (orderDAO.openConection()) {
            if (orderDAO.searchAll()) {
                if (JDBCUtil.movNext(orderDAO.getResults())) {
                    loadOrder(orderDAO.getOrder());
                }
            }
        }
    }

    public void foward() {
        if (JDBCUtil.movNext(orderDAO.getResults())) {
            loadOrder(orderDAO.getOrder());
        } else {
            JOptionPane.showMessageDialog(this, "Chegou ao fim da lista");
        }
    }

    public void previous() {
        if (JDBCUtil.movPrev(orderDAO.getResults())) {
            loadOrder(orderDAO.getOrder());
        } else {
            JOptionPane.showMessageDialog(this, "Chegou ao começo da lista");
        }
    }

    public void selectTabRow() {
        String valLin = "";
        int posLin = Pedidos.getSelectedRow();

        for (int coluna = 0; coluna < Pedidos.getColumnCount(); coluna++) {
            valLin += Pedidos.getModel().getValueAt(posLin, coluna).toString();

            if (coluna + 1 < Pedidos.getColumnCount()) {
                valLin += " - ";
            }
        }
        JOptionPane.showMessageDialog(null, "Dados completos do pedido selecionado: \n" + valLin, "Mais informações sobre o pedido", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showOrderReport() throws SQLException {
        JasperPrint showReport;
        java.sql.Connection connection = null;

        try {
            connection = JDBCUtil.getConnection();
            FileInputStream file = new FileInputStream(orders_report);
            JasperReport report = JasperCompileManager.compileReport(file);
            showReport = JasperFillManager.fillReport(report, null, connection);
            JasperViewer viewer = new JasperViewer(showReport, false);
            viewer.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            viewer.setVisible(true);
        } catch (JRException | FileNotFoundException erro) {
            erro.printStackTrace();
            System.out.println("Can't open file report");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        idApagar = new javax.swing.JTextField();
        endereco = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        Pedidos = new javax.swing.JTable();
        nomeCompleto = new javax.swing.JTextField();
        btMovFoward = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        descricaoPedido = new javax.swing.JTextField();
        tipoPedido = new javax.swing.JTextField();
        cep = new javax.swing.JTextField();
        btMovPrev = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        MenuSalvar = new javax.swing.JMenuItem();
        MenuAlterar = new javax.swing.JMenuItem();
        MenuCancelar = new javax.swing.JMenuItem();
        MenuLimparCampos = new javax.swing.JMenuItem();
        MenuVoltar = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        MenuVisuRelaPedidos = new javax.swing.JMenuItem();
        MenuGeTipoPedido = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(null);

        idApagar.setBorder(javax.swing.BorderFactory.createTitledBorder("ID"));
        getContentPane().add(idApagar);
        idApagar.setBounds(410, 70, 40, 41);

        endereco.setBorder(javax.swing.BorderFactory.createTitledBorder("Endereço"));
        getContentPane().add(endereco);
        endereco.setBounds(120, 130, 370, 40);

        Pedidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID Pedido", "Nome Completo", "Endereço", "CEP", "Descrição", "Tipo de pedido"
            }
        ));
        Pedidos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PedidosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(Pedidos);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(20, 370, 760, 340);

        nomeCompleto.setBorder(javax.swing.BorderFactory.createTitledBorder("Nome completo"));
        getContentPane().add(nomeCompleto);
        nomeCompleto.setBounds(120, 70, 270, 40);

        btMovFoward.setText("->");
        btMovFoward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btMovFowardActionPerformed(evt);
            }
        });
        getContentPane().add(btMovFoward);
        btMovFoward.setBounds(510, 260, 50, 40);

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel5.setText("Área de Pedidos");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(330, 20, 150, 23);

        jLabel2.setText("Navegação entre pedidos");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(330, 270, 190, 17);

        descricaoPedido.setBorder(javax.swing.BorderFactory.createTitledBorder("Descrição"));
        getContentPane().add(descricaoPedido);
        descricaoPedido.setBounds(120, 190, 510, 40);

        tipoPedido.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo de pedido"));
        getContentPane().add(tipoPedido);
        tipoPedido.setBounds(520, 70, 160, 40);

        cep.setBorder(javax.swing.BorderFactory.createTitledBorder("CEP"));
        getContentPane().add(cep);
        cep.setBounds(520, 130, 90, 41);

        btMovPrev.setText("<-");
        btMovPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btMovPrevActionPerformed(evt);
            }
        });
        getContentPane().add(btMovPrev);
        btMovPrev.setBounds(250, 260, 50, 40);

        jLabel3.setText("Clique na linha da tabela para ver todas as informações");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(240, 330, 350, 20);

        jMenu1.setText("Menu");

        MenuSalvar.setText("Criar pedido");
        MenuSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuSalvarActionPerformed(evt);
            }
        });
        jMenu1.add(MenuSalvar);

        MenuAlterar.setText("Alterar pedido");
        MenuAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuAlterarActionPerformed(evt);
            }
        });
        jMenu1.add(MenuAlterar);

        MenuCancelar.setText("Cancelar pedido");
        MenuCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuCancelarActionPerformed(evt);
            }
        });
        jMenu1.add(MenuCancelar);

        MenuLimparCampos.setText("Limpar campos");
        MenuLimparCampos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuLimparCamposActionPerformed(evt);
            }
        });
        jMenu1.add(MenuLimparCampos);

        MenuVoltar.setText("Voltar");
        MenuVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuVoltarActionPerformed(evt);
            }
        });
        jMenu1.add(MenuVoltar);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Info");

        MenuVisuRelaPedidos.setText("Visualizar Relatório de Pedidos");
        MenuVisuRelaPedidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuVisuRelaPedidosActionPerformed(evt);
            }
        });
        jMenu2.add(MenuVisuRelaPedidos);

        MenuGeTipoPedido.setText("Gerar relatório por tipo de pedido");
        MenuGeTipoPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuGeTipoPedidoActionPerformed(evt);
            }
        });
        jMenu2.add(MenuGeTipoPedido);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        setSize(new java.awt.Dimension(799, 782));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void MenuVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuVoltarActionPerformed
        dispose();
    }//GEN-LAST:event_MenuVoltarActionPerformed

    private void MenuSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuSalvarActionPerformed
        saveOrder();
    }//GEN-LAST:event_MenuSalvarActionPerformed

    private void btMovFowardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMovFowardActionPerformed
        foward();
    }//GEN-LAST:event_btMovFowardActionPerformed

    private void MenuLimparCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuLimparCamposActionPerformed
        clearFields();
    }//GEN-LAST:event_MenuLimparCamposActionPerformed

    private void MenuAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuAlterarActionPerformed
        updateOrder();
    }//GEN-LAST:event_MenuAlterarActionPerformed

    private void MenuCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuCancelarActionPerformed
        deleteOrder();
    }//GEN-LAST:event_MenuCancelarActionPerformed

    private void btMovPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMovPrevActionPerformed
        previous();
    }//GEN-LAST:event_btMovPrevActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        orderDAO.closeConection();
    }//GEN-LAST:event_formWindowClosed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        loadData();
        loadFirstRow();
    }//GEN-LAST:event_formWindowOpened

    private void PedidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PedidosMouseClicked
        selectTabRow();
    }//GEN-LAST:event_PedidosMouseClicked

    private void MenuVisuRelaPedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuVisuRelaPedidosActionPerformed
        try {
            showOrderReport();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível abrir o relatório!", "Erro ao abrir relatório", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_MenuVisuRelaPedidosActionPerformed

    private void MenuGeTipoPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuGeTipoPedidoActionPerformed
        try {
            orderDAO.geTypeReport();
        } catch (JRException ex) {
            Logger.getLogger(OrderView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_MenuGeTipoPedidoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new OrderView().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem MenuAlterar;
    private javax.swing.JMenuItem MenuCancelar;
    private javax.swing.JMenuItem MenuGeTipoPedido;
    private javax.swing.JMenuItem MenuLimparCampos;
    private javax.swing.JMenuItem MenuSalvar;
    private javax.swing.JMenuItem MenuVisuRelaPedidos;
    private javax.swing.JMenuItem MenuVoltar;
    private javax.swing.JTable Pedidos;
    private javax.swing.JButton btMovFoward;
    private javax.swing.JButton btMovPrev;
    private javax.swing.JTextField cep;
    private javax.swing.JTextField descricaoPedido;
    private javax.swing.JTextField endereco;
    private javax.swing.JTextField idApagar;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField nomeCompleto;
    private javax.swing.JTextField tipoPedido;
    // End of variables declaration//GEN-END:variables
}
