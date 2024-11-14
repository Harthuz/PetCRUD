package petcrud;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    private JButton btnBuscar, btnNovo, btnInserir, btnPrimeiro, btnAnterior, btnProximo, btnUltimo, btnAlterar, btnExcluir;
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

        btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(540, 270, 120, 30); // Novo botão Excluir
        add(btnExcluir);

        btnAlterar = new JButton("Alterar");
        btnAlterar.setBounds(670, 270, 120, 30); // Novo botão Alterar
        add(btnAlterar);


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
        String sql = "SELECT * FROM pet WHERE id_pet = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, petId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                txtNome.setText(rs.getString("nome"));
                txtEspecie.setText(rs.getString("especie"));
                txtRaca.setText(rs.getString("raca"));
                txtNascimento.setText(rs.getString("nascimento"));
                txtCor.setText(rs.getString("cor"));
                comboSexo.setSelectedItem(rs.getString("sexo"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados para edição: " + e.getMessage());
    }
}

    
    private void setupListeners() {
        btnNovo.addActionListener(e -> clearForm());
        btnInserir.addActionListener(e -> insertPet());
        btnBuscar.addActionListener(e -> searchPets());
        btnPrimeiro.addActionListener(e -> navigateTo("first"));
        btnAnterior.addActionListener(e -> navigateTo("previous"));
        btnProximo.addActionListener(e -> navigateTo("next"));
        btnUltimo.addActionListener(e -> navigateTo("last"));
        
        // Adicionar os novos listeners para os botões Excluir e Alterar
        btnExcluir.addActionListener(e -> deleteSelectedPet());
        btnAlterar.addActionListener(e -> editSelectedPet());
    }

    private void deleteSelectedPet() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int petId = (Integer) table.getValueAt(row, 0);  // Obtemos o ID da linha selecionada
            deletePet(petId);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma linha para excluir.");
        }
    }

    private void editSelectedPet() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int petId = (Integer) table.getValueAt(row, 0);  // Obtemos o ID da linha selecionada
            editPet(petId);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma linha para alterar.");
        }
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
        String nome = txtNome.getText();
        String especie = txtEspecie.getText();
        String raca = txtRaca.getText();
        String nascimento = txtNascimento.getText();
        String sexo = (String) comboSexo.getSelectedItem();
        String cor = txtCor.getText();
    
        if (nome.isEmpty() || especie.isEmpty() || raca.isEmpty() || nascimento.isEmpty() || cor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.");
            return;
        }
    
        String sql = "INSERT INTO pets (nome, especie, raca, nascimento, sexo, cor) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, especie);
            stmt.setString(3, raca);
            stmt.setString(4, nascimento);
            stmt.setString(5, sexo);
            stmt.setString(6, cor);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Pet inserido com sucesso!");
            updateTable();
            clearForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao inserir o pet: " + e.getMessage());
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
        String sql = "SELECT * FROM pet";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            tableModel.setRowCount(0);  // Limpar a tabela antes de atualizar
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id_pet"),
                    rs.getString("nome"),
                    rs.getString("especie"),
                    rs.getString("raca"),
                    rs.getString("nascimento"),
                    rs.getString("sexo"),
                    rs.getString("cor")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar a tabela: " + e.getMessage());
        }
    }


    private void navigateTo(String direction) {
        int rowCount = table.getRowCount();
        int currentRow = table.getSelectedRow();
        if (rowCount > 0) {
            switch (direction) {
                case "first":
                    table.setRowSelectionInterval(0, 0);
                    break;
                case "previous":
                    if (currentRow > 0) {
                        table.setRowSelectionInterval(currentRow - 1, currentRow - 1);
                    }
                    break;
                case "next":
                    if (currentRow < rowCount - 1) {
                        table.setRowSelectionInterval(currentRow + 1, currentRow + 1);
                    }
                    break;
                case "last":
                    table.setRowSelectionInterval(rowCount - 1, rowCount - 1);
                    break;
            }
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
