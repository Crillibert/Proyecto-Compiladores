import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class InterfazLecturaCodigo extends JFrame {
    private JTextArea areaCodigo;
    private JButton botonCargar;

    public InterfazLecturaCodigo() {
        // Configuración de la ventana
        setTitle("Analizador Léxico - Cargar Código Fuente");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear componentes
        areaCodigo = new JTextArea();
        botonCargar = new JButton("Cargar Archivo");

        // Configurar el área de texto
        areaCodigo.setEditable(true);
        areaCodigo.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Configurar el botón de cargar archivo
        botonCargar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarArchivo();
            }
        });

        // Agregar componentes a la ventana
        JScrollPane scrollPane = new JScrollPane(areaCodigo);
        add(scrollPane, BorderLayout.CENTER);
        add(botonCargar, BorderLayout.SOUTH);
    }

    private void cargarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                areaCodigo.setText("");
                String linea;
                while ((linea = br.readLine()) != null) {
                    areaCodigo.append(linea + "\n");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InterfazLecturaCodigo().setVisible(true);
            }
        });
    }
}