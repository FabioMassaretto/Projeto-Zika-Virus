/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import com.mysql.jdbc.PreparedStatement;
import components.DocumentSizeFilter;
import selecionador.SelecionadorResultados;
import dbconnector.DBconnector;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import validadores.ValidaCPF;
import validadores.ValidaCampos;

/**
 *
 * @author Fabio
 */
public class EditarPaciente extends javax.swing.JInternalFrame {
    private static boolean podeProsseguir = false;
    private ResultSet resultSet = null;
    private Statement statement = null;
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private DefaultStyledDocument doc;
    private boolean isAdmin = false;
    private String recebeNrCaso;
    private String recebeNomeCompleto;
    private String recebeCPF;
    private String recebeObservacoes;
    private String recebeResultAmostra1;
    private String recebeResultAmostra2;
    private String recebeResultAmostra3;
    private String recebeResultAmostra4;
    private String recebeResultFinalMae;
    private String recebeResultFinalRN;
    private String resDataEntrada;
    private int recebeAmostraSoro;
    private int recebeAmostraPlasma;
    private int recebeAmostraSaliva;
    private int recebeAmostraUrina;
    private int recebeAmostraSemen;
    private int recebeAmostraLiquor;
    private int recebeAmostraLeite;
    private int recebeAmostraPlacenta;
    private int recebeAmostraCordao;
    private String resDataColeta1;
    private String resDataColeta2;
    private String resDataColeta3;
    private String resDataColeta4;
    private String resDataColeta5;
    
    ValidaCampos validaCampos = new ValidaCampos();
    DBconnector conexao = new DBconnector();
    BuscarPaciente buscarPaciente = new BuscarPaciente();
    DateFormat format = new SimpleDateFormat("dd/MM/yy");

    /**
     * Creates new form editarPaciente
     */
    public EditarPaciente() throws SQLException, ParseException {
        initComponents();
        if (!isAdmin) {
            txtNrCaso.setEditable(false);
            txtNomeCompleto.setEditable(false);
            txtCPF.setEditable(false);
            
            txtNrCaso.setFocusable(false);
            txtNomeCompleto.setFocusable(false);
            txtCPF.setFocusable(false);
        }
        preencherCamposQuery();
        contPalavras();
    }

    public void contPalavras(){
        doc = new DefaultStyledDocument();
        doc.setDocumentFilter(new DocumentSizeFilter(500));
            doc.addDocumentListener(new DocumentListener(){
                @Override
                public void changedUpdate(DocumentEvent e) { updateCount();}
                @Override
                public void insertUpdate(DocumentEvent e) { updateCount();}
                @Override
                public void removeUpdate(DocumentEvent e) { updateCount();}          
            });
            txtAreaObs.setDocument(doc);
            txtAreaObs.setText(recebeObservacoes);
            updateCount();
    }
    private void updateCount() {
           lblContPalavras1.setText(((500) -doc.getLength()) + " letras restando!");                      
    }
    
    private void preencherCamposQuery() throws SQLException, ParseException{
        conexao.conexao();
        String id = buscarPaciente.getSelectedID();
        System.out.println("id: " + id);
//        String nome = buscarPaciente.getSelectedNome();
//        String cpf = buscarPaciente.getSelectedCPF();
        
//        String sql = "SELECT * FROM pacientes p INNER JOIN resultados r ON r.ID_Paciente = '" + id + "' INNER JOIN coletas c ON c.ID_Paciente = '" + id + "'";
        String sql = "SELECT * FROM pacientes p INNER JOIN resultados r ON r.ID_Paciente = p.ID_Paciente INNER JOIN coletas c ON c.ID_Paciente = p.ID_Paciente INNER JOIN amostras a ON a.ID_Paciente = p.ID_Paciente WHERE p.ID_Paciente = '" + id + "'";
        PreparedStatement  preparedStatement = (PreparedStatement) conexao.connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        
        while(resultSet.next()){
            recebeNrCaso = resultSet.getString("NumeroCaso");
            recebeNomeCompleto = resultSet.getString("NomeCompleto");
            recebeCPF = resultSet.getString("CPF");
            resDataEntrada = resultSet.getString("DataEntrada");
            recebeObservacoes = resultSet.getString("Observacoes");
            recebeResultAmostra1 = resultSet.getString("ResultadoAmostra1");
            recebeResultAmostra2 = resultSet.getString("ResultadoAmostra2");
            recebeResultAmostra3 = resultSet.getString("ResultadoAmostra3");
            recebeResultAmostra4 = resultSet.getString("ResultadoAmostra4");
            recebeResultFinalMae = resultSet.getString("ResultadoFinalMAE");
            recebeResultFinalRN = resultSet.getString("ResultadoFinalRN");
            resDataColeta1 = resultSet.getString("DataPrimeiraC");
            resDataColeta2 = resultSet.getString("DataSegundaC");
            resDataColeta3 = resultSet.getString("DataTerceiraC");
            resDataColeta4 = resultSet.getString("DataQuartaC");
            resDataColeta5 = resultSet.getString("DataQuintaC");
            recebeAmostraSoro = resultSet.getInt("Soro");
            recebeAmostraPlasma = resultSet.getInt("Plasma");
            recebeAmostraSaliva = resultSet.getInt("Saliva");
            recebeAmostraUrina = resultSet.getInt("Urina");
            recebeAmostraSemen = resultSet.getInt("Semen");
            recebeAmostraLiquor = resultSet.getInt("Liquor");
            recebeAmostraLeite = resultSet.getInt("LeiteMaterno");
            recebeAmostraPlacenta = resultSet.getInt("Placenta");
            recebeAmostraCordao = resultSet.getInt("CordaoUmbilical");
        }
        
        if (!resDataEntrada.equals("") && resDataEntrada != null) {
            String stringDataEntrada = resDataEntrada;
            System.out.println("data: " + resDataEntrada);
            Date date = format.parse(stringDataEntrada);
            dtEntrada.setDate(date);
            System.out.println(date); 
        }
        if (!resDataColeta1.equals("") && resDataColeta1 != null) {
            String stringDataEntrada = resDataColeta1;
            System.out.println("data: " + resDataColeta1);
            Date date = format.parse(stringDataEntrada);
            dtColeta1.setDate(date);
            System.out.println(date); 
        }
        if (!resDataColeta2.equals("") && resDataColeta2 != null) {
            String stringDataEntrada = resDataColeta2;
            System.out.println("data: " + resDataColeta2);
            Date date = format.parse(stringDataEntrada);
            dtColeta2.setDate(date);
            System.out.println(date); 
        }
        if (!resDataColeta3.equals("") && resDataColeta3 != null) {
            String stringDataEntrada = resDataColeta3;
            System.out.println("data: " + resDataColeta3);
            Date date = format.parse(stringDataEntrada);
            dtColeta3.setDate(date);
            System.out.println(date); 
        }
        if (!resDataColeta4.equals("") && resDataColeta4 != null) {
            String stringDataEntrada = resDataColeta4;
            System.out.println("data: " + resDataColeta4);
            Date date = format.parse(stringDataEntrada);
            dtColeta4.setDate(date);
            System.out.println(date); 
        }
        if (!resDataColeta5.equals("") && resDataColeta5 != null) {
            String stringDataEntrada = resDataColeta5;
            System.out.println("data: " + resDataColeta5);
            Date date = format.parse(stringDataEntrada);
            dtColeta5.setDate(date);
            System.out.println(date); 
        }
        
        txtNrCaso.setText(recebeNrCaso);
        txtNomeCompleto.setText(recebeNomeCompleto);
        txtCPF.setText(recebeCPF);
        txtAreaObs.setText(txtAreaObs.getText() + recebeObservacoes);
        lblQtdeSoro.setText(String.valueOf(recebeAmostraSoro));
        lblQtdeUrina.setText(String.valueOf(recebeAmostraUrina));
        lblQtdeSemen.setText(String.valueOf(recebeAmostraSemen));
        lblQtdeSaliva.setText(String.valueOf(recebeAmostraSaliva));
        lblQtdePlasma.setText(String.valueOf(recebeAmostraPlasma));
        lblQtdePlacenta.setText(String.valueOf(recebeAmostraPlacenta));
        lblQtdeLiquor.setText(String.valueOf(recebeAmostraLiquor));
        lblQtdeLeiteMaterno.setText(String.valueOf(recebeAmostraLeite));
        lblQtdeCordao.setText(String.valueOf(recebeAmostraCordao));
        
        SelecionadorResultados resultadoAmostra1 = new SelecionadorResultados(recebeResultAmostra1);
        cmbAmostra1.setSelectedIndex(resultadoAmostra1.getIndeReturnado());
        
        SelecionadorResultados resultadoAmostra2 = new SelecionadorResultados(recebeResultAmostra2);
        cmbAmostra2.setSelectedIndex(resultadoAmostra2.getIndeReturnado());
        
        SelecionadorResultados resultadoAmostra3 = new SelecionadorResultados(recebeResultAmostra3);
        cmbAmostra3.setSelectedIndex(resultadoAmostra3.getIndeReturnado());
        
        SelecionadorResultados resultadoAmostra4 = new SelecionadorResultados(recebeResultAmostra4);
        cmbAmostra4.setSelectedIndex(resultadoAmostra4.getIndeReturnado());
        
        SelecionadorResultados resultadoFinalMae = new SelecionadorResultados(recebeResultFinalMae);
        cmbFinalMae.setSelectedIndex(resultadoFinalMae.getIndeReturnado());
        
        SelecionadorResultados resultadoFinalRN = new SelecionadorResultados(recebeResultFinalRN);
        cmbFinalRN.setSelectedIndex(resultadoFinalRN.getIndeReturnado());
        

    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dtColeta4 = new org.jdesktop.swingx.JXDatePicker();
        dtColeta5 = new org.jdesktop.swingx.JXDatePicker();
        lblDtEntrada = new javax.swing.JLabel();
        lblDtCol1 = new javax.swing.JLabel();
        lblDtCol2 = new javax.swing.JLabel();
        lblDtCol3 = new javax.swing.JLabel();
        lblDtCol4 = new javax.swing.JLabel();
        lblDtCol5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblNrCaso = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblCPF = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblNome = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtNomeCompleto = new javax.swing.JTextField();
        txtNrCaso = new javax.swing.JFormattedTextField();
        txtCPF = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        dtEntrada = new org.jdesktop.swingx.JXDatePicker();
        dtColeta1 = new org.jdesktop.swingx.JXDatePicker();
        btnAlterar = new javax.swing.JButton();
        dtColeta2 = new org.jdesktop.swingx.JXDatePicker();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        cmbAmostra1 = new javax.swing.JComboBox<>();
        cmbAmostra2 = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        cmbAmostra3 = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        cmbAmostra4 = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        cmbFinalMae = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        cmbFinalRN = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        lblQtdeSoro = new javax.swing.JLabel();
        lblQtdePlasma = new javax.swing.JLabel();
        lblQtdeSaliva = new javax.swing.JLabel();
        lblQtdeUrina = new javax.swing.JLabel();
        lblQtdeSemen = new javax.swing.JLabel();
        lblQtdeLeiteMaterno = new javax.swing.JLabel();
        lblQtdeLiquor = new javax.swing.JLabel();
        lblQtdePlacenta = new javax.swing.JLabel();
        lblQtdeCordao = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        lblContPalavras1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaObs = new javax.swing.JTextPane();
        dtColeta3 = new org.jdesktop.swingx.JXDatePicker();

        setClosable(true);
        setTitle("Editar Paciente");
        setPreferredSize(new java.awt.Dimension(780, 560));

        lblDtEntrada.setText("entrada");

        lblDtCol1.setText("col1");

        lblDtCol2.setText("col2");

        lblDtCol3.setText("col3");

        lblDtCol4.setText("col4");

        lblDtCol5.setText("col5");

        jLabel2.setText("CPF:");

        jLabel3.setText("Numero do Caso:");

        jLabel4.setText("Data da 1ª Coleta:");

        jLabel5.setText("Data da 2ª Coleta:");

        lblNrCaso.setText("Nr Caso");

        jLabel6.setText("Data da 3ª Coleta:");

        lblCPF.setText("CPF");

        jLabel7.setText("Data da 4ª Coleta:");

        lblNome.setText("Nome");

        jLabel8.setText("Data da 5ª Coleta:");

        jLabel16.setText("\\16");

        txtNomeCompleto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNomeCompletoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNomeCompletoFocusLost(evt);
            }
        });
        txtNomeCompleto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeCompletoActionPerformed(evt);
            }
        });

        txtNrCaso.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNrCasoFocusLost(evt);
            }
        });

        txtCPF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCPFFocusLost(evt);
            }
        });

        jLabel9.setText("Data da Entrada: ");

        jLabel1.setText("Nome Completo:");

        btnAlterar.setText("Alterar Paciente");
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        jLabel10.setText("1ª Amostra:");

        cmbAmostra1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "Negativo", "Positivo" }));
        cmbAmostra1.setMinimumSize(new java.awt.Dimension(90, 22));
        cmbAmostra1.setName(""); // NOI18N
        cmbAmostra1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAmostra1ActionPerformed(evt);
            }
        });

        cmbAmostra2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "Negativo", "Positivo" }));
        cmbAmostra2.setMinimumSize(new java.awt.Dimension(90, 22));
        cmbAmostra2.setName(""); // NOI18N
        cmbAmostra2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAmostra2ActionPerformed(evt);
            }
        });

        jLabel11.setText("2ª Amostra:");

        cmbAmostra3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "Negativo", "Positivo" }));
        cmbAmostra3.setMinimumSize(new java.awt.Dimension(90, 22));
        cmbAmostra3.setName(""); // NOI18N
        cmbAmostra3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAmostra3ActionPerformed(evt);
            }
        });

        jLabel12.setText("3ª Amostra:");

        cmbAmostra4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "Negativo", "Positivo" }));
        cmbAmostra4.setMinimumSize(new java.awt.Dimension(90, 22));
        cmbAmostra4.setName(""); // NOI18N
        cmbAmostra4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAmostra4ActionPerformed(evt);
            }
        });

        jLabel13.setText("4ª Amostra:");

        cmbFinalMae.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "Negativo", "Positivo" }));
        cmbFinalMae.setMinimumSize(new java.awt.Dimension(90, 22));
        cmbFinalMae.setName(""); // NOI18N
        cmbFinalMae.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbFinalMaeActionPerformed(evt);
            }
        });

        jLabel14.setText("Final da Mãe:");

        cmbFinalRN.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "Negativo", "Positivo" }));
        cmbFinalRN.setMinimumSize(new java.awt.Dimension(90, 22));
        cmbFinalRN.setName(""); // NOI18N
        cmbFinalRN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbFinalRNActionPerformed(evt);
            }
        });

        jLabel15.setText("Final do RN:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(cmbAmostra1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(cmbAmostra2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(cmbAmostra3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(cmbAmostra4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(cmbFinalMae, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(cmbFinalRN, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbFinalRN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbFinalMae, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbAmostra4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbAmostra3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbAmostra2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbAmostra1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Resultados", jPanel1);

        jLabel18.setText("Informar a quantidade total das seguites amostras:");

        jLabel19.setText("Soro:");

        jLabel20.setText("Plasma:");

        jLabel21.setText("Saliva:");

        jLabel22.setText("Urina:");

        jLabel23.setText("Sêmen:");

        jLabel24.setText("Leite Materno:");

        jLabel25.setText("Liquor:");

        jLabel26.setText("Placenta:");

        jLabel27.setText("Cordão Umbilical:");

        lblQtdeSoro.setText("0");

        lblQtdePlasma.setText("0");

        lblQtdeSaliva.setText("0");

        lblQtdeUrina.setText("0");

        lblQtdeSemen.setText("0");

        lblQtdeLeiteMaterno.setText("0");

        lblQtdeLiquor.setText("0");

        lblQtdePlacenta.setText("0");

        lblQtdeCordao.setText("0");

        jButton1.setText("Aterar Qtde.");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Aterar Qtde.");

        jButton3.setText("Aterar Qtde.");

        jButton4.setText("Aterar Qtde.");

        jButton5.setText("Aterar Qtde.");

        jButton6.setText("Aterar Qtde.");

        jButton7.setText("Aterar Qtde.");

        jButton8.setText("Aterar Qtde.");

        jButton9.setText("Aterar Qtde.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel18))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel20)
                                .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel21)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblQtdeSoro)
                            .addComponent(lblQtdePlasma)
                            .addComponent(lblQtdeSaliva))
                        .addGap(63, 63, 63)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel22)
                            .addComponent(jLabel23)
                            .addComponent(jLabel24))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblQtdeUrina)
                            .addComponent(lblQtdeSemen, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblQtdeLeiteMaterno, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblQtdePlacenta)
                            .addComponent(lblQtdeCordao)
                            .addComponent(lblQtdeLiquor))
                        .addGap(35, 35, 35)))
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton9, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel22)
                    .addComponent(jLabel25)
                    .addComponent(lblQtdeSoro)
                    .addComponent(lblQtdeUrina)
                    .addComponent(lblQtdeLiquor)
                    .addComponent(jButton1)
                    .addComponent(jButton6)
                    .addComponent(jButton7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel23)
                    .addComponent(jLabel26)
                    .addComponent(lblQtdePlasma)
                    .addComponent(lblQtdeSemen)
                    .addComponent(lblQtdePlacenta)
                    .addComponent(jButton2)
                    .addComponent(jButton5)
                    .addComponent(jButton8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel24)
                    .addComponent(jLabel27)
                    .addComponent(lblQtdeSaliva)
                    .addComponent(lblQtdeLeiteMaterno)
                    .addComponent(lblQtdeCordao)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton9))
                .addGap(0, 23, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Amostras", jPanel2);

        jScrollPane2.setViewportView(txtAreaObs);
        txtAreaObs.getAccessibleContext().setAccessibleName("");
        txtAreaObs.getAccessibleContext().setAccessibleParent(this);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 649, Short.MAX_VALUE)
                .addComponent(lblContPalavras1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblContPalavras1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Observações", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAlterar))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(jLabel2)
                                .addComponent(jLabel6)
                                .addComponent(jLabel5)
                                .addComponent(jLabel4))
                            .addGap(12, 12, 12)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtNomeCompleto, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(txtCPF, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(lblCPF))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(dtColeta1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(lblDtCol1))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(dtColeta2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(lblDtCol2))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(dtColeta3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(lblDtCol3))))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addGap(18, 18, 18)
                            .addComponent(txtNrCaso, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel16)
                            .addGap(32, 32, 32)
                            .addComponent(lblNrCaso)
                            .addGap(25, 25, 25)
                            .addComponent(jLabel9)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(dtEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(26, 26, 26)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblNome)
                                .addComponent(lblDtEntrada))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dtColeta4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblDtCol4))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dtColeta5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblDtCol5)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel9)
                    .addComponent(jLabel16)
                    .addComponent(txtNrCaso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dtEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNrCaso)
                    .addComponent(lblDtEntrada))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNomeCompleto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNome))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCPF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCPF))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dtColeta1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(lblDtCol1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dtColeta2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(lblDtCol2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dtColeta3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(lblDtCol3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(dtColeta4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDtCol4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(dtColeta5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDtCol5))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAlterar)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNomeCompletoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNomeCompletoFocusGained
        if (txtNomeCompleto.getText().equals("Informe o Nome Completo")) {
            txtNomeCompleto.setText("");
        }
    }//GEN-LAST:event_txtNomeCompletoFocusGained

    private void txtNomeCompletoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNomeCompletoFocusLost
        lblNome.setText(txtNomeCompleto.getText());
    }//GEN-LAST:event_txtNomeCompletoFocusLost

    private void txtNomeCompletoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeCompletoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeCompletoActionPerformed

    private void txtNrCasoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNrCasoFocusLost
        lblNrCaso.setText(txtNrCaso.getText());
    }//GEN-LAST:event_txtNrCasoFocusLost

    private void txtCPFFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCPFFocusLost
        String auxCPF1 = txtCPF.getText();
        String auxCPF2 = auxCPF1.replace(".", "");
        String auxCPF3 = auxCPF2.replace("-", "");
        String nrCPF = auxCPF3.replace(" ", "");

        lblCPF.setText(nrCPF);

        if (!nrCPF.equals("")) {
            if (!ValidaCPF.isCPF(lblCPF.getText())) {
                JOptionPane.showMessageDialog(rootPane, "CPF inválido!");
                podeProsseguir = false;
            }

        }
    }//GEN-LAST:event_txtCPFFocusLost

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        //datePickerSetup();

        // implementar se for obrigatorio todos os pacientes de terem CPF e retirar codigo do txtCPFFocusLost

        //        if (!ValidaCPF.isCPF(lblCPF.getText())) {
            //            JOptionPane.showMessageDialog(rootPane, "CPF inválido!");
            //            podeProsseguir = false;
            //        }else{ podeProsseguir = true; }
        if (validaCampos.validaNomeCompleto(lblNome.getText())) {
            JOptionPane.showMessageDialog(rootPane, "O campo NOME COMPLETO está em branco!");
            podeProsseguir = false;
        }else{ podeProsseguir = true; }
        try{
            if (validaCampos.validaCampoNrCaso(Integer.parseInt(txtNrCaso.getText()))) {
                JOptionPane.showMessageDialog(rootPane, "O campo NUMERO DO CASO está em inválido!");
                podeProsseguir = false;
            }else{ podeProsseguir = true; }
        }catch (NumberFormatException nFE){
            JOptionPane.showMessageDialog(rootPane, "O campo NUMERO DO CASO está no formato inválido! \n"
                + "Por favor digite no seguinte formato com tres numeros, por exemplo: 003");
            podeProsseguir = false;
        }

        if (podeProsseguir) {

            try {

                conexao.conexao();
                //                    System.out.println("-------- MySQL JDBC Connection Testing ------------");
                //
                //                    try {
                    //                            Class.forName("com.mysql.jdbc.Driver");
                    //                    } catch (ClassNotFoundException e) {
                    //                            System.out.println("Where is your MySQL JDBC Driver?");
                    //                            e.printStackTrace();
                    //                            return;
                    //                    }
                //
                //                    System.out.println("MySQL JDBC Driver Registered!");
                //                    Connection connection = null;
                //
                //                    try {
                    //                            connection = DriverManager
                    //                            .getConnection("jdbc:mysql://localhost:3306/projetozikadb","root", "lmi56n");
                    //
                    //                    } catch (SQLException e) {
                    //                            System.out.println("Connection Failed! Check output console");
                    //                            e.printStackTrace();
                    //                            return;
                    //                    }
                //
                //                    if (connection != null) {
                    //                            System.out.println("You made it, take control your database now!");
                    //                    } else {
                    //                            System.out.println("Failed to make connection!");
                    //                    }
                //parte que insere dados do paciente na tabela pacientes
                preparedStatement = (PreparedStatement) conexao.connection.prepareStatement("INSERT INTO pacientes VALUES(default, ?, ?, ?, ?)");
                preparedStatement.setString(1, lblNome.getText());
                preparedStatement.setString(2, lblCPF.getText());
                preparedStatement.setString(3, lblDtEntrada.getText());
                preparedStatement.setString(4, txtNrCaso.getText());
                preparedStatement.executeUpdate();

                //parte que insere os resultados do paciente na tabela resultados
                preparedStatement = (PreparedStatement) conexao.connection.prepareStatement("INSERT INTO resultados VALUES(default,(SELECT ID_Paciente FROM pacientes WHERE NomeCompleto = '"+ txtNomeCompleto.getText() +"'), ?, ?, ?, ?, ?, ?)");
                preparedStatement.setString(1, cmbAmostra1.getSelectedItem().toString());
                preparedStatement.setString(2, cmbAmostra2.getSelectedItem().toString());
                preparedStatement.setString(3, cmbAmostra3.getSelectedItem().toString());
                preparedStatement.setString(4, cmbAmostra4.getSelectedItem().toString());
                preparedStatement.setString(5, cmbFinalMae.getSelectedItem().toString());
                preparedStatement.setString(6, cmbFinalRN.getSelectedItem().toString());
                preparedStatement.executeUpdate();

                //parte que insere as datas das coletas na tabela coletas
                preparedStatement = (PreparedStatement) conexao.connection.prepareStatement("INSERT INTO coletas VALUES(default,(SELECT ID_Paciente FROM pacientes WHERE NomeCompleto = '"+ txtNomeCompleto.getText() +"'), ?, ?, ?, ?, ?)");
                preparedStatement.setString(1, lblDtCol1.getText());
                preparedStatement.setString(2, lblDtCol2.getText());
                preparedStatement.setString(3, lblDtCol3.getText());
                preparedStatement.setString(4, lblDtCol4.getText());
                preparedStatement.setString(5, lblDtCol5.getText());
                preparedStatement.executeUpdate();
                conexao.connection.close();
                //
                //            stmt = connection.createStatement();
                //            rs = stmt.executeQuery("INSERT INTO pacientes ");
                //
                //            // or alternatively, if you don't know ahead of time that
                //            // the query will be a SELECT...
                //
                //            if (stmt.execute("SELECT foo FROM bar")) {
                    //                rs = stmt.getResultSet();
                    //            }

                // Now do something with the ResultSet ....
            }catch (SQLException ex){
                // handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void cmbAmostra1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAmostra1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbAmostra1ActionPerformed

    private void cmbAmostra2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAmostra2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbAmostra2ActionPerformed

    private void cmbAmostra3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAmostra3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbAmostra3ActionPerformed

    private void cmbAmostra4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAmostra4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbAmostra4ActionPerformed

    private void cmbFinalMaeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFinalMaeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbFinalMaeActionPerformed

    private void cmbFinalRNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFinalRNActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbFinalRNActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JComboBox<String> cmbAmostra1;
    private javax.swing.JComboBox<String> cmbAmostra2;
    private javax.swing.JComboBox<String> cmbAmostra3;
    private javax.swing.JComboBox<String> cmbAmostra4;
    private javax.swing.JComboBox<String> cmbFinalMae;
    private javax.swing.JComboBox<String> cmbFinalRN;
    private org.jdesktop.swingx.JXDatePicker dtColeta1;
    private org.jdesktop.swingx.JXDatePicker dtColeta2;
    private org.jdesktop.swingx.JXDatePicker dtColeta3;
    private org.jdesktop.swingx.JXDatePicker dtColeta4;
    private org.jdesktop.swingx.JXDatePicker dtColeta5;
    private org.jdesktop.swingx.JXDatePicker dtEntrada;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblCPF;
    private javax.swing.JLabel lblContPalavras1;
    private javax.swing.JLabel lblDtCol1;
    private javax.swing.JLabel lblDtCol2;
    private javax.swing.JLabel lblDtCol3;
    private javax.swing.JLabel lblDtCol4;
    private javax.swing.JLabel lblDtCol5;
    private javax.swing.JLabel lblDtEntrada;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblNrCaso;
    private javax.swing.JLabel lblQtdeCordao;
    private javax.swing.JLabel lblQtdeLeiteMaterno;
    private javax.swing.JLabel lblQtdeLiquor;
    private javax.swing.JLabel lblQtdePlacenta;
    private javax.swing.JLabel lblQtdePlasma;
    private javax.swing.JLabel lblQtdeSaliva;
    private javax.swing.JLabel lblQtdeSemen;
    private javax.swing.JLabel lblQtdeSoro;
    private javax.swing.JLabel lblQtdeUrina;
    private javax.swing.JTextPane txtAreaObs;
    private javax.swing.JFormattedTextField txtCPF;
    private javax.swing.JTextField txtNomeCompleto;
    private javax.swing.JFormattedTextField txtNrCaso;
    // End of variables declaration//GEN-END:variables
}
