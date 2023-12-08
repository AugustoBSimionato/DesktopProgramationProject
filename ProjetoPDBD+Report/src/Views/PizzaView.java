package Views;

import DAOs.PizzaDAO;
import Model.Pizza;
import Utils.JDBCUtil;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
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
import static DAOs.PizzaDAO.pizzas_report;
import javax.swing.JFrame;

/**
 * @author augustosimionato
 */
public class PizzaView extends javax.swing.JFrame {

    PizzaDAO pizzaDAO = new PizzaDAO();

    /**
     * Creates new form PizzaView
     */
    public PizzaView() {
        initComponents();
    }

    public void loadPizza(Pizza pizza) {
        idApagar.setText(String.valueOf(pizza.getIdPizza()));
        nomePizza.setText(pizza.getNomePizza());
        descricaoPizza.setText(String.valueOf(pizza.getDescricao()));
    }

    public Pizza loadAllPizzas() {
        int idPizza = Integer.parseInt(idApagar.getText());
        String nome = nomePizza.getText();
        String descricao = descricaoPizza.getText();
        Pizza pizza = new Pizza(idPizza, nome, descricao);
        return pizza;
    }

    public void clearFields() {
        idApagar.setText("");
        nomePizza.setText("");
        descricaoPizza.setText("");
        idApagar.requestFocus();
    }

    public void savePizza() {
        Pizza pizza = loadAllPizzas();
        if (pizzaDAO.insert(pizza)) {
            loadData();
            loadFirstRow();
            JOptionPane.showMessageDialog(this, "Pizza salva com sucesso.");
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao inserir nova pizza.");
        }
    }

    public void deletePizza() {
        Pizza pizza = loadAllPizzas();
        if (pizzaDAO.delete(pizza)) {
            JOptionPane.showMessageDialog(this, "Pizza excluída com sucesso!");
            if (JDBCUtil.catchFirst(pizzaDAO.getResults())) {
                idApagar.setText(String.valueOf(pizza.getIdPizza()));
                nomePizza.setText(pizza.getNomePizza());
                descricaoPizza.setText(String.valueOf(pizza.getDescricao()));
                loadData();
                loadFirstRow();
            } else {
                JOptionPane.showMessageDialog(this, "O primeiro registro ja esta selecionado.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir pizza.");
        }
    }

    public void updatePizza() {
        Pizza pizza = loadAllPizzas();
        if (pizzaDAO.update(pizza)) {
            loadData();
            loadFirstRow();
            JOptionPane.showMessageDialog(this, "Pizza alterada com sucesso!");
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao alterar pizza.");
        }
    }

    public void loadData() {
        if (pizzaDAO.openConection()) {
            if (pizzaDAO.searchAll()) {
                DefaultTableModel model = (DefaultTableModel) Cardapio.getModel();
                model.setRowCount(0);

                try {
                    while (pizzaDAO.getResults().next()) {
                        int id = pizzaDAO.getResults().getInt("id");
                        String nomePizza = pizzaDAO.getResults().getString("nomePizza");
                        String descricao = pizzaDAO.getResults().getString("descricao");
                        model.addRow(new Object[]{id, nomePizza, descricao});
                    }
                } catch (SQLException sql) {
                    sql.printStackTrace();
                }

                DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
                leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

                Cardapio.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
                Cardapio.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
                Cardapio.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
            }
        }
    }

    public void loadFirstRow() {
        if (pizzaDAO.openConection()) {
            if (pizzaDAO.searchAll()) {
                if (JDBCUtil.movNext(pizzaDAO.getResults())) {
                    loadPizza(pizzaDAO.getPizza());
                }
            }
        }
    }

    public void foward() {
        if (JDBCUtil.movNext(pizzaDAO.getResults())) {
            loadPizza(pizzaDAO.getPizza());
        } else {
            JOptionPane.showMessageDialog(this, "Chegou ao fim da lista");
        }
    }

    public void previous() {
        if (JDBCUtil.movPrev(pizzaDAO.getResults())) {
            loadPizza(pizzaDAO.getPizza());
        } else {
            JOptionPane.showMessageDialog(this, "Chegou ao começo da lista");
        }
    }

    public void selectTabRow() {
        String valLin = "";
        int posLin = Cardapio.getSelectedRow();

        for (int coluna = 0; coluna < Cardapio.getColumnCount(); coluna++) {
            valLin += Cardapio.getModel().getValueAt(posLin, coluna).toString();

            if (coluna + 1 < Cardapio.getColumnCount()) {
                valLin += " - ";
            }
        }
        JOptionPane.showMessageDialog(null, "Dados completos da pizza selecionada: \n" + valLin, "Mais informações sobre a pizza", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showPizzaReport() throws SQLException {
        JasperPrint showReport;
        java.sql.Connection connection = null;

        try {
            connection = JDBCUtil.getConnection();
            FileInputStream file = new FileInputStream(pizzas_report);
            JasperReport report = JasperCompileManager.compileReport(file);
            showReport = JasperFillManager.fillReport(report, null, connection);
            JasperViewer viewer = new JasperViewer(showReport, false);
            viewer.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            viewer.setVisible(true);
        } catch (JRException | FileNotFoundException erro) {
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
        descricaoPizza = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        Cardapio = new javax.swing.JTable();
        nomePizza = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btMovFoward = new javax.swing.JButton();
        btMovPrev = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        MenuSalvar = new javax.swing.JMenuItem();
        MenuAtualizar = new javax.swing.JMenuItem();
        MenuApagar = new javax.swing.JMenuItem();
        MenuLimparCampos = new javax.swing.JMenuItem();
        MenuVoltar = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        MenuVisuRelaPizzas = new javax.swing.JMenuItem();

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
        idApagar.setBounds(20, 90, 40, 41);

        descricaoPizza.setBorder(javax.swing.BorderFactory.createTitledBorder("Descrição"));
        getContentPane().add(descricaoPizza);
        descricaoPizza.setBounds(20, 210, 760, 41);

        Cardapio.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID Pizza", "Nome da Pizza", "Descrição"
            }
        ));
        Cardapio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CardapioMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(Cardapio);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(20, 410, 760, 350);

        nomePizza.setBorder(javax.swing.BorderFactory.createTitledBorder("Nome da pizza"));
        getContentPane().add(nomePizza);
        nomePizza.setBounds(20, 150, 270, 40);

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel5.setText("Cardápio");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(370, 30, 80, 23);

        jLabel6.setText("Navegação entre pizzas");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(340, 300, 180, 17);

        btMovFoward.setText("->");
        btMovFoward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btMovFowardActionPerformed(evt);
            }
        });
        getContentPane().add(btMovFoward);
        btMovFoward.setBounds(510, 290, 50, 40);

        btMovPrev.setText("<-");
        btMovPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btMovPrevActionPerformed(evt);
            }
        });
        getContentPane().add(btMovPrev);
        btMovPrev.setBounds(270, 290, 50, 40);

        jLabel2.setText("Clique na linha da tabela para ver todas as informações");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(250, 360, 350, 20);

        jMenu1.setText("Menu");

        MenuSalvar.setText("Adicionar pizza");
        MenuSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuSalvarActionPerformed(evt);
            }
        });
        jMenu1.add(MenuSalvar);

        MenuAtualizar.setText("Atualizar pizza");
        MenuAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuAtualizarActionPerformed(evt);
            }
        });
        jMenu1.add(MenuAtualizar);

        MenuApagar.setText("Apagar pizza");
        MenuApagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuApagarActionPerformed(evt);
            }
        });
        jMenu1.add(MenuApagar);

        MenuLimparCampos.setText("Limpar Campos");
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

        MenuVisuRelaPizzas.setText("Visualizar Relatório de Pizzas");
        MenuVisuRelaPizzas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuVisuRelaPizzasActionPerformed(evt);
            }
        });
        jMenu2.add(MenuVisuRelaPizzas);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        setSize(new java.awt.Dimension(799, 831));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void MenuVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuVoltarActionPerformed
        dispose();
    }//GEN-LAST:event_MenuVoltarActionPerformed

    private void MenuSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuSalvarActionPerformed
        savePizza();
    }//GEN-LAST:event_MenuSalvarActionPerformed

    private void MenuLimparCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuLimparCamposActionPerformed
        clearFields();
    }//GEN-LAST:event_MenuLimparCamposActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        loadData();
        loadFirstRow();
    }//GEN-LAST:event_formWindowOpened

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        pizzaDAO.closeConection();
    }//GEN-LAST:event_formWindowClosed

    private void MenuAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuAtualizarActionPerformed
        updatePizza();
    }//GEN-LAST:event_MenuAtualizarActionPerformed

    private void MenuApagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuApagarActionPerformed
        deletePizza();
    }//GEN-LAST:event_MenuApagarActionPerformed

    private void btMovFowardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMovFowardActionPerformed
        foward();
    }//GEN-LAST:event_btMovFowardActionPerformed

    private void btMovPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMovPrevActionPerformed
        previous();
    }//GEN-LAST:event_btMovPrevActionPerformed

    private void CardapioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CardapioMouseClicked
        selectTabRow();
    }//GEN-LAST:event_CardapioMouseClicked

    private void MenuVisuRelaPizzasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuVisuRelaPizzasActionPerformed
        try {
            showPizzaReport();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível abrir o relatório!", "Erro ao abrir relatório", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_MenuVisuRelaPizzasActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new PizzaView().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Cardapio;
    private javax.swing.JMenuItem MenuApagar;
    private javax.swing.JMenuItem MenuAtualizar;
    private javax.swing.JMenuItem MenuLimparCampos;
    private javax.swing.JMenuItem MenuSalvar;
    private javax.swing.JMenuItem MenuVisuRelaPizzas;
    private javax.swing.JMenuItem MenuVoltar;
    private javax.swing.JButton btMovFoward;
    private javax.swing.JButton btMovPrev;
    private javax.swing.JTextField descricaoPizza;
    private javax.swing.JTextField idApagar;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField nomePizza;
    // End of variables declaration//GEN-END:variables
}
