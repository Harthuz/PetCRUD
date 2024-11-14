package petcrud;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class PetEditDialog extends JDialog {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/petcrud";  
    private static final String DB_USER = "root";  
    private static final String DB_PASS = "";
    private PetCRUD petCRUD;
    private JComboBox<String> comboSexo;
    private JTextField txtNome, txtEspecie, txtRaca, txtNascimento, txtCor;
    private int petId;

    public PetEditDialog(int petId, PetCRUD petCRUD) {
        this.petId = petId;
        this.petCRUD = petCRUD;
        setTitle("Editar Pet");
        setSize(350, 350);  
        setLayout(null);     
        setLocationRelativeTo(null);
        setModal(true); // Torna o diálogo modal
        
        // Campos do formulário
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(20, 20, 100, 25);
        add(lblNome);
        txtNome = new JTextField();
        txtNome.setBounds(120, 20, 200, 25);
        add(txtNome);

        JLabel lblEspecie = new JLabel("Espécie:");
        lblEspecie.setBounds(20, 60, 100, 25);
        add(lblEspecie);
        txtEspecie = new JTextField();
        txtEspecie.setBounds(120, 60, 200, 25);
        add(txtEspecie);

        JLabel lblRaca = new JLabel("Raça:");
        lblRaca.setBounds(20, 100, 100, 25);
        add(lblRaca);
        txtRaca = new JTextField();
        txtRaca.setBounds(120, 100, 200, 25);
        add(txtRaca);

        JLabel lblNascimento = new JLabel("Nascimento:");
        lblNascimento.setBounds(20, 140, 100, 25);
        add(lblNascimento);
        txtNascimento = new JTextField();
        txtNascimento.setBounds(120, 140, 200, 25);
        add(txtNascimento);

        JLabel lblSexo = new JLabel("Sexo:");
        lblSexo.setBounds(20, 180, 100, 25);
        add(lblSexo);
        comboSexo = new JComboBox<>(new String[]{"Masculino", "Feminino"});
        comboSexo.setBounds(120, 180, 200, 25);
        add(comboSexo);

        JLabel lblCor = new JLabel("Cor:");
        lblCor.setBounds(20, 220, 100, 25);
        add(lblCor);
        txtCor = new JTextField();
        txtCor.setBounds(120, 220, 200, 25);
        add(txtCor);

        // Botão de salvar
        JButton btnSave = new JButton("Salvar");
        btnSave.setBounds(120, 260, 100, 30);
        btnSave.addActionListener(e -> saveChanges());
        add(btnSave);

        // Carregar os dados do pet no formulário
        loadPetData();
    }

    // Carregar dados da tabela pet
    private void loadPetData() {
        String sql = "SELECT pet.nome, especie.especie, raca.raca, pet.nascimento, sexo.sexo, cor.cor "
                   + "FROM pet "
                   + "JOIN especie ON pet.especie = especie.id_especie "
                   + "JOIN raca ON pet.raca = raca.id_raca "
                   + "JOIN sexo ON pet.sexo = sexo.id_sexo "
                   + "JOIN cor ON pet.cor = cor.id_cor "
                   + "WHERE id_pet = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, petId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtNome.setText(rs.getString("nome"));
                txtEspecie.setText(rs.getString("especie"));
                txtRaca.setText(rs.getString("raca"));
                txtNascimento.setText(rs.getString("nascimento"));
                comboSexo.setSelectedItem(rs.getString("sexo"));
                txtCor.setText(rs.getString("cor"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados do pet: " + e.getMessage());
        }
    }

    // Salvar dados
    private void saveChanges() {
        String nome = txtNome.getText();
        String especie = txtEspecie.getText();
        String raca = txtRaca.getText();
        String nascimento = txtNascimento.getText();
        String sexo = (String) comboSexo.getSelectedItem();
        String cor = txtCor.getText();

        // Verificar e obter o ID para cada campo
        int especieId = getOrInsert("especie", especie);
        int racaId = getOrInsert("raca", raca);
        int sexoId = getOrInsert("sexo", sexo);
        int corId = getOrInsert("cor", cor);

        String sql = "UPDATE pet SET nome = ?, especie = ?, raca = ?, nascimento = ?, sexo = ?, cor = ? WHERE id_pet = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setInt(2, especieId);
            ps.setInt(3, racaId);
            try {
                ps.setDate(4, Date.valueOf(nascimento)); // Verificar se a data está no formato correto
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Data inválida! O formato correto é AAAA-MM-DD.");
                return;
            }
            ps.setInt(5, sexoId);
            ps.setInt(6, corId);
            ps.setInt(7, petId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!");
            petCRUD.updateTable();
            this.dispose();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar alterações: " + e.getMessage());
        }
    }

    // Método para verificar se o valor já existe na tabela e obter o ID ou inserir o valor novo
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PetCRUD petCRUD = new PetCRUD();  // Criação da instância de PetCRUD
            new PetEditDialog(1, petCRUD).setVisible(true); // Passando o petId e a instância de PetCRUD
        });
    }
}
