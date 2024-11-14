package petcrud;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PetCRUD extends JFrame {
    // Defina as constantes para conexão com o banco de dados
    private static final String DB_URL = "jdbc:mysql://localhost:3306/petcrud";  // Substitua "petcrud" pelo nome do seu banco de dados
    private static final String DB_USER = "root";  // Substitua "root" pelo seu usuário do banco de dados
    private static final String DB_PASS = "";  // Substitua pelo sua senha do banco de dados (se houver)
    
    private JComboBox<String> comboSexo;
    private JTextField txtBuscar, txtID, txtNome, txtEspecie, txtRaca, txtNascimento, txtCor;
    private JButton btnBuscar, btnNovo, btnInserir, btnPrimeiro, btnAnterior, btnProximo, btnUltimo;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public PetCRUD() {
        // Configurações da JFrame
        setTitle("PetCRUD");
        setSize(1120, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Layout nulo para posicionamento manual
        setLocationRelativeTo(null);  // Centralizar a janela

        // Label do título
        JLabel lblTitulo = new JLabel("PetCRUD");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setBounds(20, 10, 100, 30);
        add(lblTitulo);

        // Campo de busca
        txtBuscar = new JTextField("Pesquisar Itens");
        txtBuscar.setBounds(100, 15, 200, 25);
        add(txtBuscar);

        // Botão de busca
        
        // Carregar a imagem do ícone
        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/images/search_icon.png"));

        // Criar o botão com a imagem
        btnBuscar = new JButton(icon);

        // Definir o tamanho e a posição do botão
        btnBuscar.setBounds(310, 15, 40, 25);

        // Adicionar o botão ao layout
        add(btnBuscar);

        // Botões de ação
        btnNovo = new JButton("NOVO");
        btnNovo.setBounds(900, 15, 80, 25);
        add(btnNovo);

        btnInserir = new JButton("INSERIR");
        btnInserir.setBounds(990, 15, 80, 25);
        add(btnInserir);

        // Configuração da JTable com modelo
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Espécie", "Raça", "Nascimento", "Sexo", "Cor", "Excluir", "Alterar"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30); // Aumentando a altura das linhas para melhor legibilidade
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // Ajustar todas as colunas automaticamente
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 50, 860, 200); // Aumentando a largura da tabela
        
        // Alinhar os dados da tabela centralizados
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);  // Alinha ao centro horizontalmente
        renderer.setVerticalAlignment(SwingConstants.CENTER);    // Alinha ao centro verticalmente

        // Aplicando o renderer para todas as colunas
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        
        add(scrollPane);

        
        
        // Desabilitar a edição ao clicar ou dar dois cliques
        table.setDefaultEditor(Object.class, null); // Impede edição das células
        
        // Adicionar renderizadores e editores de botão nas colunas de "Excluir" e "Alterar"
        table.getColumn("Excluir").setCellRenderer(new ButtonRenderer("Excluir", table));
        table.getColumn("Excluir").setCellEditor(new ButtonEditor(table, "Excluir", this)); // 'this' passa a instância de PetCRUD
        table.getColumn("Alterar").setCellRenderer(new ButtonRenderer("Alterar", table));
        table.getColumn("Alterar").setCellEditor(new ButtonEditor(table, "Alterar", this)); // 'this' passa a instância de PetCRUD

        // Botões de navegação
        btnPrimeiro = new JButton("Primeiro");
        btnPrimeiro.setBounds(20, 270, 120, 30);
        add(btnPrimeiro);

        btnAnterior = new JButton("Anterior");
        btnAnterior.setBounds(150, 270, 120, 30);
        add(btnAnterior);

        btnProximo = new JButton("Próximo");
        btnProximo.setBounds(280, 270, 120, 30);
        add(btnProximo);

        btnUltimo = new JButton("Último");
        btnUltimo.setBounds(410, 270, 120, 30);
        add(btnUltimo);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(900, 55, 80, 25);  
        add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(980, 55, 100, 25);  
        add(txtNome);

        JLabel lblEspecie = new JLabel("Espécie:");
        lblEspecie.setBounds(900, 85, 80, 25);  // Novo valor Y = 85
        add(lblEspecie);

        txtEspecie = new JTextField();
        txtEspecie.setBounds(980, 85, 100, 25);  // Novo valor Y = 85
        add(txtEspecie);

        JLabel lblRaca = new JLabel("Raça:");
        lblRaca.setBounds(900, 115, 80, 25);  // Novo valor Y = 115
        add(lblRaca);

        txtRaca = new JTextField();
        txtRaca.setBounds(980, 115, 100, 25);  // Novo valor Y = 115
        add(txtRaca);

        JLabel lblNascimento = new JLabel("Nascimento:");
        lblNascimento.setBounds(900, 145, 80, 25);  // Novo valor Y = 145
        add(lblNascimento);

        txtNascimento = new JTextField();
        txtNascimento.setBounds(980, 145, 100, 25);  // Novo valor Y = 145
        add(txtNascimento);

        // Label para "Sexo"
        JLabel lblSexo = new JLabel("Sexo:");
        lblSexo.setBounds(900, 175, 80, 25);  // Novo valor Y = 175
        add(lblSexo);

        // ComboBox para selecionar o sexo
        comboSexo = new JComboBox<>(new String[]{"Masculino", "Feminino"});
        comboSexo.setBounds(980, 175, 100, 25);  // Novo valor Y = 175
        add(comboSexo);


        JLabel lblCor = new JLabel("Cor:");
        lblCor.setBounds(900, 205, 120, 25);  // Novo valor Y = 205
        add(lblCor);

        txtCor = new JTextField();
        txtCor.setBounds(980, 205, 100, 25);  // Novo valor Y = 205
        add(txtCor);




        
        setupListeners();
        updateTable();
    }
   
    
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        private String label;
        private JTable table;

        public ButtonRenderer(String label, JTable table) {
            this.label = label;
            this.table = table; // Passando a tabela
            setText(label);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }
    
    static class ButtonEditor extends DefaultCellEditor {
        private String label;
        private boolean isPushed;
        private JButton button;
        private JTable table;
        private PetCRUD petCRUD; // Referência à instância de PetCRUD

        public ButtonEditor(JTable table, String label, PetCRUD petCRUD) {
            super(new JCheckBox());
            this.table = table; // Passando a tabela
            this.label = label;
            this.petCRUD = petCRUD; // Armazenando a referência de PetCRUD
            button = new JButton(label);
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int row = table.getSelectedRow();  // Obtemos o índice da linha selecionada
                if (row >= 0) {  // Verifique se o índice da linha é válido
                    int petId = (Integer) table.getValueAt(row, 0);  // Obtém o ID do pet a partir da primeira coluna
                    if ("Excluir".equals(label)) {
                        petCRUD.deletePet(petId);  // Chama a função deletePet passando o ID do pet
                    } else if ("Alterar".equals(label)) {
                        petCRUD.editPet(petId);  // Chama a função editPet passando o ID do pet
                    }
                } else {
                    JOptionPane.showMessageDialog(petCRUD, "Nenhuma linha selecionada.");
                }
            }
            isPushed = false;
            return label;
        }

    }

    
    private void deletePet(int petId) {
        int response = JOptionPane.showConfirmDialog(this, "Tem certeza de que deseja excluir este pet?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM pet WHERE id_pet = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, petId);
                ps.executeUpdate();
                updateTable(); // Atualiza a tabela após exclusão
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir pet: " + e.getMessage());
            }
        }
    }

    private void editPet(int petId) {
        PetEditDialog editDialog = new PetEditDialog(petId, this);
        editDialog.setVisible(true);  // Tornando o diálogo visível e modal
    }

    
    private void setupListeners() {
        btnNovo.addActionListener(e -> clearForm());
        btnInserir.addActionListener(e -> insertPet());
        btnBuscar.addActionListener(e -> searchPets());
        btnPrimeiro.addActionListener(e -> navigateTo("first"));
        btnAnterior.addActionListener(e -> navigateTo("previous"));
        btnProximo.addActionListener(e -> navigateTo("next"));
        btnUltimo.addActionListener(e -> navigateTo("last"));
    }
    
    // Método para limpar o formulário
    private void clearForm() {
        txtNome.setText("");
        txtEspecie.setText("");
        txtRaca.setText("");
        txtNascimento.setText("");
        comboSexo.setSelectedIndex(0);
        txtCor.setText("");
    }    
    
    private void insertPet() {
        if (formIsValid()) {
            // Obter ou inserir as normalizações
            int especieId = getOrInsert("especie", txtEspecie.getText());
            int racaId = getOrInsert("raca", txtRaca.getText());
            int corId = getOrInsert("cor", txtCor.getText());
            int sexoId = getOrInsert("sexo", comboSexo.getSelectedItem().toString()); // Usar o ComboBox para pegar o sexo

            // Verificar se a data de nascimento está no formato correto
            String nascimento = txtNascimento.getText();
            if (!isValidDate(nascimento)) {
                JOptionPane.showMessageDialog(this, "A data de nascimento deve estar no formato AAAA-MM-DD.");
                return; // Interrompe a inserção, pois a data é inválida
            }

            // Inserir pet na tabela pet
            String sql = "INSERT INTO pet (nome, especie, raca, nascimento, sexo, cor) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, txtNome.getText());
                ps.setInt(2, especieId);
                ps.setInt(3, racaId);
                ps.setDate(4, Date.valueOf(nascimento)); // Convertendo a data (já validada)
                ps.setInt(5, sexoId);
                ps.setInt(6, corId);
                ps.executeUpdate();
                updateTable(); // Atualiza a tabela após inserção
                clearForm();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao inserir pet: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.");
        }
    }

    
    private boolean formIsValid() {
        // Verificar se os campos estão preenchidos e se o JComboBox tem um item selecionado
        return !txtNome.getText().isEmpty() && 
               !txtEspecie.getText().isEmpty() && 
               !txtRaca.getText().isEmpty() && 
               !txtNascimento.getText().isEmpty() && 
               comboSexo.getSelectedIndex() != -1 &&  // Verifica se o comboSexo tem um item selecionado
               !txtCor.getText().isEmpty();
    }
    
    // Método para validar a data no formato AAAA-MM-DD
    private boolean isValidDate(String dateStr) {
        try {
            // Tenta converter a string para uma data usando o formato AAAA-MM-DD
            LocalDate.parse(dateStr);
            return true;
        } catch (DateTimeParseException e) {
            // Se ocorrer uma exceção, significa que a data está no formato incorreto
            return false;
        }
    }    
    
    private int getOrInsert(String table, String value) {
        int id = -1;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            // Consultar se o valor já existe
            String sqlSelect = "SELECT id_" + table + " FROM " + table + " WHERE " + table + " = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlSelect)) {
                ps.setString(1, value);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    id = rs.getInt(1); // Retorna o ID existente
                } else {
                    // Se não existe, insere um novo valor
                    String sqlInsert = "INSERT INTO " + table + " (" + table + ") VALUES (?)";
                    try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                        psInsert.setString(1, value);
                        psInsert.executeUpdate();
                        ResultSet generatedKeys = psInsert.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            id = generatedKeys.getInt(1); // Retorna o novo ID
                        }
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao verificar/insert na tabela " + table + ": " + e.getMessage());
        }
        return id;
    }

    
    public void updateTable() {
        // Limpar e recarregar dados na JTable
        tableModel.setRowCount(0);
        String sql =    "SELECT pet.id_pet, pet.nome, especie.especie, raca.raca, pet.nascimento, sexo.sexo, cor.cor \n" +
                        "FROM pet \n" +
                        "JOIN especie ON pet.especie = especie.id_especie\n" +
                        "JOIN raca ON pet.raca = raca.id_raca\n" +
                        "JOIN sexo ON pet.sexo = sexo.id_sexo\n" +
                        "JOIN cor ON pet.cor = cor.id_cor;";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id_pet"),
                        rs.getString("nome"),
                        rs.getString("especie"),
                        rs.getString("raca"),
                        rs.getDate("nascimento"),
                        rs.getString("sexo"),
                        rs.getString("cor"),
                        "Excluir",  // Texto do botão "Excluir"
                        "Alterar"   // Texto do botão "Alterar"
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage());
        }
    }


    private void navigateTo(String position) {;;;
        int currentIndex = getCurrentIndexFromTable();
        switch (position) {
            case "first":
                currentIndex = 0;
                break;
            case "previous":
                if (currentIndex > 0) currentIndex--;
                break;
            case "next":
                if (currentIndex < tableModel.getRowCount() - 1) currentIndex++;
                break;
            case "last":
                currentIndex = tableModel.getRowCount() - 1;
                break;
        }
        selectRow(currentIndex);
    }
    
    private int getCurrentIndexFromTable() {
        return table.getSelectedRow();  // Retorna o índice da linha selecionada
    }
    
    private void selectRow(int index) {
        if (index >= 0 && index < tableModel.getRowCount()) {
            table.setRowSelectionInterval(index, index);  // Seleciona a linha especificada
        }
    }
    
    private void searchPets() {
        String especieFilter = txtBuscar.getText();
        String sql = "SELECT pet.id_pet, pet.nome, especie.especie, raca.raca, pet.nascimento, sexo.sexo, cor.cor " +
                     "FROM pet " +
                     "JOIN especie ON pet.especie = especie.id_especie " +
                     "JOIN raca ON pet.raca = raca.id_raca " +
                     "JOIN sexo ON pet.sexo = sexo.id_sexo " +
                     "JOIN cor ON pet.cor = cor.id_cor " +
                     "WHERE especie.especie LIKE ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + especieFilter + "%");
            ResultSet rs = ps.executeQuery();
            tableModel.setRowCount(0); // Limpar a tabela
            while (rs.next()) {
                tableModel.addRow(new Object[] {
                    rs.getInt("id_pet"),
                    rs.getString("nome"),
                    rs.getString("especie"),
                    rs.getString("raca"),
                    rs.getDate("nascimento"),
                    rs.getString("sexo"),
                    rs.getString("cor")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao pesquisar pets: " + e.getMessage());
        }
    }

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PetCRUD().setVisible(true);
        });
    }
}
