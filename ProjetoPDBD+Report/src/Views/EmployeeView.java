package Views;

import DAOs.EmployeeDAO;
import static DAOs.EmployeeDAO.employees_report;
import Model.Employee;
import Utils.JDBCUtil;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
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
public class EmployeeView extends javax.swing.JFrame {

    EmployeeDAO employeeDAO = new EmployeeDAO();

    /**
     * Creates new form EmployeeView
     */
    public EmployeeView() {
        initComponents();
    }

    public void loadEmployee(Employee employee) {
        idApagar.setText(String.valueOf(employee.getIdEmployee()));
        nomeCompleto.setText(employee.getNomeCompleto());
        cpf.setText(employee.getCpf());
        endereco.setText(String.valueOf(employee.getEndereco()));
        cep.setText(String.valueOf(employee.getCep()));
        telefone.setText(employee.getTelefone());
        dataContratacao.setText(String.valueOf(employee.getContratacao()));
        dataDemissao.setText(String.valueOf(employee.getDemissao()));
    }

    public Employee loadAllEmployees() {
        int idEmployee = Integer.parseInt(idApagar.getText());
        String nome = nomeCompleto.getText();
        String cf = cpf.getText();
        String end = endereco.getText();
        String cp = cep.getText();
        String tel = telefone.getText();
        String contra = dataContratacao.getText();
        String demi = dataDemissao.getText();
        Employee employee = new Employee(idEmployee, nome, cf, end, cp, tel, contra, demi);

        return employee;
    }

    public void clearFields() {
        idApagar.setText("");
        nomeCompleto.setText("");
        cpf.setText("");
        endereco.setText("");
        cep.setText("");
        telefone.setText("");
        dataContratacao.setText("");
        dataDemissao.setText("");
        idApagar.requestFocus();
    }

    public void saveEmployee() {
        Employee employee = loadAllEmployees();
        if (employeeDAO.insert(employee)) {
            loadData();
            loadFirstRow();
            JOptionPane.showMessageDialog(this, "Funcionário salvo com sucesso.");
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao inserir novo funcionário.");
        }
    }

    public void deleteEmployee() {
        Employee employee = loadAllEmployees();
        if (employeeDAO.delete(employee)) {
            JOptionPane.showMessageDialog(this, "Funcionário excluído com sucesso!");
            if (JDBCUtil.catchFirst(employeeDAO.getResults())) {
                idApagar.setText(String.valueOf(employee.getIdEmployee()));
                nomeCompleto.setText(employee.getNomeCompleto());
                cpf.setText(employee.getCpf());
                endereco.setText(String.valueOf(employee.getEndereco()));
                cep.setText(String.valueOf(employee.getCep()));
                telefone.setText(employee.getTelefone());
                dataContratacao.setText(String.valueOf(employee.getContratacao()));
                dataDemissao.setText(String.valueOf(employee.getDemissao()));
                loadData();
                loadFirstRow();
            } else {
                JOptionPane.showMessageDialog(this, "O primeiro funcionário já está selecionado.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir funcionário.");
        }
    }

    public void updateEmployee() {
        Employee employee = loadAllEmployees();
        if (employeeDAO.update(employee)) {
            loadData();
            loadFirstRow();
            JOptionPane.showMessageDialog(this, "Funcionário alterado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao alterar funcionário.");
        }
    }

    public void loadData() {
        if (employeeDAO.openConection()) {
            if (employeeDAO.searchAll()) {
                DefaultTableModel model = (DefaultTableModel) Employees.getModel();
                model.setRowCount(0);

                try {
                    while (employeeDAO.getResults().next()) {
                        int id = employeeDAO.getResults().getInt("id");
                        String nome = employeeDAO.getResults().getString("nomeCompleto");
                        String cf = employeeDAO.getResults().getString("cpf");
                        String end = employeeDAO.getResults().getString("endereco");
                        String cp = employeeDAO.getResults().getString("cep");
                        String tel = employeeDAO.getResults().getString("telefone");
                        String contratacao = employeeDAO.getResults().getString("contratacao");
                        String demissao = employeeDAO.getResults().getString("demissao");
                        model.addRow(new Object[]{id, nome, cf, end, cp, tel, contratacao, demissao});
                    }
                } catch (SQLException sql) {
                    sql.printStackTrace();
                }

                DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
                leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

                Employees.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
                Employees.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
                Employees.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
                Employees.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);
                Employees.getColumnModel().getColumn(4).setCellRenderer(leftRenderer);
                Employees.getColumnModel().getColumn(5).setCellRenderer(leftRenderer);
                Employees.getColumnModel().getColumn(6).setCellRenderer(leftRenderer);
                Employees.getColumnModel().getColumn(7).setCellRenderer(leftRenderer);
            }
        }
    }

    public void loadFirstRow() {
        if (employeeDAO.openConection()) {
            if (employeeDAO.searchAll()) {
                if (JDBCUtil.movNext(employeeDAO.getResults())) {
                    loadEmployee(employeeDAO.getEmployee());
                }
            }
        }
    }

    public void foward() {
        if (JDBCUtil.movNext(employeeDAO.getResults())) {
            loadEmployee(employeeDAO.getEmployee());
        } else {
            JOptionPane.showMessageDialog(this, "Chegou ao fim da lista");
        }
    }

    public void previous() {
        if (JDBCUtil.movPrev(employeeDAO.getResults())) {
            loadEmployee(employeeDAO.getEmployee());
        } else {
            JOptionPane.showMessageDialog(this, "Chegou ao começo da lista");
        }
    }

    public void selectTabRow() {
        String valLin = "";
        int posLin = Employees.getSelectedRow();

        for (int coluna = 0; coluna < Employees.getColumnCount(); coluna++) {
            valLin += Employees.getModel().getValueAt(posLin, coluna).toString();

            if (coluna + 1 < Employees.getColumnCount()) {
                valLin += " - ";
            }
        }
        JOptionPane.showMessageDialog(null, "Dados completos do funcionário selecionado: \n" + valLin, "Mais informações sobre o funcionário", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showEmployeeReport() throws SQLException {
        JasperPrint showReport;
        java.sql.Connection connection = null;

        try {
            connection = JDBCUtil.getConnection();
            FileInputStream file = new FileInputStream(employees_report);
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
        endereco = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        Employees = new javax.swing.JTable();
        nomeCompleto = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        dataContratacao = new javax.swing.JTextField();
        telefone = new javax.swing.JTextField();
        cep = new javax.swing.JTextField();
        cpf = new javax.swing.JTextField();
        dataDemissao = new javax.swing.JTextField();
        btMovFoward = new javax.swing.JButton();
        btMovPrev = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        MenuSalvar = new javax.swing.JMenuItem();
        MenuAlterar = new javax.swing.JMenuItem();
        MenuApagarFuncionario = new javax.swing.JMenuItem();
        MenuLimparCampos = new javax.swing.JMenuItem();
        MenuVoltar = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        MenuVisuRelaFuncionarios = new javax.swing.JMenuItem();

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
        idApagar.setBounds(460, 180, 40, 41);

        endereco.setBorder(javax.swing.BorderFactory.createTitledBorder("Endereço"));
        getContentPane().add(endereco);
        endereco.setBounds(30, 120, 400, 40);

        Employees.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Funcionário", "Nome Completo", "CPF", "Endereço", "CEP", "Telefone", "Data Contratação", "Data Demissão"
            }
        ));
        Employees.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EmployeesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(Employees);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(20, 310, 1010, 330);

        nomeCompleto.setBorder(javax.swing.BorderFactory.createTitledBorder("Nome completo"));
        getContentPane().add(nomeCompleto);
        nomeCompleto.setBounds(30, 60, 400, 40);

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel5.setText("Gerenciar funcionários");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(420, 10, 210, 23);

        jLabel2.setText("Clique na linha da tabela para ver todas as informações");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(450, 280, 350, 20);

        dataContratacao.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Contratação"));
        getContentPane().add(dataContratacao);
        dataContratacao.setBounds(30, 180, 400, 40);

        telefone.setBorder(javax.swing.BorderFactory.createTitledBorder("Telefone"));
        getContentPane().add(telefone);
        telefone.setBounds(460, 60, 210, 40);

        cep.setBorder(javax.swing.BorderFactory.createTitledBorder("CEP"));
        getContentPane().add(cep);
        cep.setBounds(460, 120, 90, 41);

        cpf.setBorder(javax.swing.BorderFactory.createTitledBorder("CPF"));
        getContentPane().add(cpf);
        cpf.setBounds(570, 120, 90, 41);

        dataDemissao.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Demissão"));
        getContentPane().add(dataDemissao);
        dataDemissao.setBounds(30, 240, 400, 40);

        btMovFoward.setText("->");
        btMovFoward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btMovFowardActionPerformed(evt);
            }
        });
        getContentPane().add(btMovFoward);
        btMovFoward.setBounds(910, 220, 50, 40);

        btMovPrev.setText("<-");
        btMovPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btMovPrevActionPerformed(evt);
            }
        });
        getContentPane().add(btMovPrev);
        btMovPrev.setBounds(630, 220, 50, 40);

        jLabel3.setText("Navegação entre funcionários");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(710, 230, 210, 20);

        jMenu1.setText("Menu");

        MenuSalvar.setText("Adicionar Funcionário");
        MenuSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuSalvarActionPerformed(evt);
            }
        });
        jMenu1.add(MenuSalvar);

        MenuAlterar.setText("Alterar Funcionário");
        MenuAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuAlterarActionPerformed(evt);
            }
        });
        jMenu1.add(MenuAlterar);

        MenuApagarFuncionario.setText("Apagar Funcionário");
        MenuApagarFuncionario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuApagarFuncionarioActionPerformed(evt);
            }
        });
        jMenu1.add(MenuApagarFuncionario);

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

        MenuVisuRelaFuncionarios.setText("Visualizar Relatório de Funcionários");
        MenuVisuRelaFuncionarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuVisuRelaFuncionariosActionPerformed(evt);
            }
        });
        jMenu2.add(MenuVisuRelaFuncionarios);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        setSize(new java.awt.Dimension(1048, 705));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void MenuVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuVoltarActionPerformed
        dispose();
    }//GEN-LAST:event_MenuVoltarActionPerformed

    private void MenuSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuSalvarActionPerformed
        saveEmployee();
    }//GEN-LAST:event_MenuSalvarActionPerformed

    private void MenuLimparCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuLimparCamposActionPerformed
        clearFields();
    }//GEN-LAST:event_MenuLimparCamposActionPerformed

    private void MenuAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuAlterarActionPerformed
        updateEmployee();
    }//GEN-LAST:event_MenuAlterarActionPerformed

    private void MenuApagarFuncionarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuApagarFuncionarioActionPerformed
        deleteEmployee();
    }//GEN-LAST:event_MenuApagarFuncionarioActionPerformed

    private void btMovFowardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMovFowardActionPerformed
        foward();
    }//GEN-LAST:event_btMovFowardActionPerformed

    private void btMovPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMovPrevActionPerformed
        previous();
    }//GEN-LAST:event_btMovPrevActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        employeeDAO.closeConection();
    }//GEN-LAST:event_formWindowClosed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        loadData();
        loadFirstRow();
    }//GEN-LAST:event_formWindowOpened

    private void EmployeesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EmployeesMouseClicked
        selectTabRow();
    }//GEN-LAST:event_EmployeesMouseClicked

    private void MenuVisuRelaFuncionariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuVisuRelaFuncionariosActionPerformed
        try {
            showEmployeeReport();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível abrir o relatório!", "Erro ao abrir relatório", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_MenuVisuRelaFuncionariosActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new EmployeeView().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Employees;
    private javax.swing.JMenuItem MenuAlterar;
    private javax.swing.JMenuItem MenuApagarFuncionario;
    private javax.swing.JMenuItem MenuLimparCampos;
    private javax.swing.JMenuItem MenuSalvar;
    private javax.swing.JMenuItem MenuVisuRelaFuncionarios;
    private javax.swing.JMenuItem MenuVoltar;
    private javax.swing.JButton btMovFoward;
    private javax.swing.JButton btMovPrev;
    private javax.swing.JTextField cep;
    private javax.swing.JTextField cpf;
    private javax.swing.JTextField dataContratacao;
    private javax.swing.JTextField dataDemissao;
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
    private javax.swing.JTextField telefone;
    // End of variables declaration//GEN-END:variables
}
