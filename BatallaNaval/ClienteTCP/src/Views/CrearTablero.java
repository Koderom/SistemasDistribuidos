/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Views;

import ClienteSocket.ClienteTCP;
import CrearTableroEvents.CrearTableroListener;
import CrearTableroEvents.EntrarSalaEsperaEvent;
import Models.Tablero;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author MIRKO
 */
public class CrearTablero extends javax.swing.JDialog implements CrearTableroListener{
    MainForm parent;
    int dimencion;
    Tablero tablero;
    ClienteTCP cliente;
    
    JLabel current_ship;
    int size;
    char orientacion;
    int puntos;
    /**
     * Creates new form CrearTablero
     */
    public CrearTablero(java.awt.Frame parent, boolean modal, ClienteTCP cliente, int dimencion) {
        super(parent, modal);
        initComponents();
        
        this.parent = (MainForm)parent;
        this.dimencion = dimencion;
        this.tablero = new Tablero();
        this.cliente = cliente;
        this.cliente.addCrearTableroListener(this);
        
        size = 0;
        orientacion = 'N';
        puntos = 10;
        iniciarTablero();
    }
    
    private JLabel getLabelImage(int width, int height, String path){
        JLabel label = new JLabel();
        label.setBounds(0, 0, width, height);
        ImageIcon imageIcon = new ImageIcon(path);
        Icon icon = new ImageIcon(imageIcon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_DEFAULT));
        label.setIcon(icon);
        return label;
    }
    private void setImageButton(JButton button, String path){
        ImageIcon imageIcon = new ImageIcon(path);
        Icon icon = new ImageIcon(imageIcon.getImage().getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_DEFAULT));
        button.setIcon(icon);
        repaint();
    }
    private void actualizarPuntos(){
        puntos = puntos - size;
        text_puntos.setText("Pts : " + puntos);
        if(puntos < 3){
            small_ship.setEnabled(false);
            boton_rotar.setEnabled(false);
            boton_listo.setEnabled(true);
        }
        if(puntos < 4) medium_ship.setEnabled(false);
        if(puntos < 5) big_ship.setEnabled(false);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tablero_principal = new javax.swing.JLayeredPane();
        jPanel1 = new javax.swing.JPanel();
        small_ship = new javax.swing.JButton();
        medium_ship = new javax.swing.JButton();
        big_ship = new javax.swing.JButton();
        text_puntos = new javax.swing.JLabel();
        boton_listo = new javax.swing.JButton();
        boton_rotar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setFocusableWindowState(false);
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        tablero_principal.setPreferredSize(new java.awt.Dimension(400, 400));
        tablero_principal.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tablero_principalMouseDragged(evt);
            }
        });
        tablero_principal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablero_principalMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tablero_principalMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout tablero_principalLayout = new javax.swing.GroupLayout(tablero_principal);
        tablero_principal.setLayout(tablero_principalLayout);
        tablero_principalLayout.setHorizontalGroup(
            tablero_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        tablero_principalLayout.setVerticalGroup(
            tablero_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );

        jPanel1.setBackground(new java.awt.Color(47, 73, 90));

        small_ship.setBorder(null);
        small_ship.setBorderPainted(false);
        small_ship.setContentAreaFilled(false);
        small_ship.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                small_shipActionPerformed(evt);
            }
        });

        medium_ship.setBorder(null);
        medium_ship.setBorderPainted(false);
        medium_ship.setContentAreaFilled(false);
        medium_ship.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                medium_shipActionPerformed(evt);
            }
        });

        big_ship.setBorder(null);
        big_ship.setBorderPainted(false);
        big_ship.setContentAreaFilled(false);
        big_ship.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                big_shipActionPerformed(evt);
            }
        });

        text_puntos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        text_puntos.setForeground(new java.awt.Color(255, 255, 255));
        text_puntos.setText("Pts: 20");

        boton_listo.setText("Listo");
        boton_listo.setEnabled(false);
        boton_listo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_listoActionPerformed(evt);
            }
        });

        boton_rotar.setText("Rotar");
        boton_rotar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_rotarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(small_ship, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(medium_ship, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(big_ship, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boton_rotar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(boton_listo, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(text_puntos, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(medium_ship, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(small_ship, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(text_puntos, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(big_ship, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(boton_rotar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(boton_listo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tablero_principal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tablero_principal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        
    }//GEN-LAST:event_formWindowClosed

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved

    }//GEN-LAST:event_formMouseMoved

    private void boton_listoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_listoActionPerformed
        //this.parent.setVisible(true);
        //this.parent.entrarSalaEspera();
        this.cliente.entrarSalaEspera();
        //this.setVisible(false);
    }//GEN-LAST:event_boton_listoActionPerformed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        
    }//GEN-LAST:event_formMouseDragged

    private void small_shipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_small_shipActionPerformed
        if(current_ship != null) tablero_principal.remove(current_ship);
        this.size = 3;
        this.orientacion = 'H';
        current_ship = getLabelImage(120, 40, "src/assets/buque_de_guerra_2H.png");
        tablero_principal.add(current_ship, Integer.valueOf(2));
        small_ship.setEnabled(false);
        repaint();
    }//GEN-LAST:event_small_shipActionPerformed

    private void tablero_principalMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablero_principalMouseDragged
        if(current_ship != null){
            int x = evt.getX() - current_ship.getWidth()/ 2;
            int y = evt.getY() - current_ship.getHeight()/ 2;
            current_ship.setLocation(x, y);
            this.repaint();
        }
    }//GEN-LAST:event_tablero_principalMouseDragged

    private void tablero_principalMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablero_principalMouseReleased
        if(current_ship == null) return;
        int x = current_ship.getX();
        int y = current_ship.getY();
        x = x / 40;
        y = y / 40;
        
        if(tablero.colocarBarco(y, x, size, orientacion)){
            current_ship.setLocation(x*40, y*40);
            System.out.println(tablero.toString());
            parent.colocarBarco(y, x, size, orientacion);
            
            small_ship.setEnabled(true);
            medium_ship.setEnabled(true);
            big_ship.setEnabled(true);
            
            actualizarPuntos();
        }else{
            JOptionPane.showMessageDialog(this, "posicion invalida");
            current_ship.setVisible(false);
        }
        
        current_ship = null;
    }//GEN-LAST:event_tablero_principalMouseReleased

    private void tablero_principalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablero_principalMouseClicked
        
    }//GEN-LAST:event_tablero_principalMouseClicked

    private void medium_shipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_medium_shipActionPerformed
        if(current_ship != null) tablero_principal.remove(current_ship);
        this.size = 4;
        this.orientacion = 'H';
        current_ship = getLabelImage(160, 40, "src/assets/buque_de_guerra_3H.png");
        tablero_principal.add(current_ship, Integer.valueOf(2));
        medium_ship.setEnabled(false);
        repaint();
    }//GEN-LAST:event_medium_shipActionPerformed

    private void big_shipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_big_shipActionPerformed
        if(current_ship != null) tablero_principal.remove(current_ship);
        this.size = 5;
        this.orientacion = 'H';
        current_ship = getLabelImage(200, 40, "src/assets/buque_de_guerra_4H.png");
        tablero_principal.add(current_ship, Integer.valueOf(2));
        big_ship.setEnabled(false);
        repaint();
    }//GEN-LAST:event_big_shipActionPerformed

    private void boton_rotarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_rotarActionPerformed
        if(current_ship == null) return;
        tablero_principal.remove(current_ship);
        switch (size) {
            case 3:
                if(orientacion == 'H') current_ship = getLabelImage(40, 120, "src/assets/buque_de_guerra_2V.png");
                else current_ship = getLabelImage(120, 40, "src/assets/buque_de_guerra_2H.png");
                break;
            case 4:
                if(orientacion == 'H') current_ship = getLabelImage(40, 160, "src/assets/buque_de_guerra_3V.png");
                else current_ship = getLabelImage(160, 40, "src/assets/buque_de_guerra_3H.png");
                break;
            case 5:
                if(orientacion == 'H') current_ship = getLabelImage(40, 200, "src/assets/buque_de_guerra_4V.png");
                else current_ship = getLabelImage(200, 40, "src/assets/buque_de_guerra_4H.png");
                break;
            default:
        }
        if(orientacion == 'H') orientacion = 'V';
        else orientacion = 'H';
        tablero_principal.add(current_ship, Integer.valueOf(2));
        repaint();
    }//GEN-LAST:event_boton_rotarActionPerformed
 
    private void iniciarTablero(){
        // iniciar botones
        setImageButton(small_ship, "src/assets/buque_de_guerra_2A.png");
        setImageButton(medium_ship, "src/assets/buque_de_guerra_3A.png");
        setImageButton(big_ship, "src/assets/buque_de_guerra_4A.png");
        
        text_puntos.setText("Pts: "+puntos);
        
        GridPanel tablero_container = new GridPanel(10, 10);
        tablero_container.setBounds(0, 0, 400, 400);
        tablero_container.setBackground( new Color(0x2F495A));

        tablero_principal.add(tablero_container, Integer.valueOf(1));
        tablero_principal.revalidate();
        this.repaint();
    }
 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton big_ship;
    private javax.swing.JButton boton_listo;
    private javax.swing.JButton boton_rotar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton medium_ship;
    private javax.swing.JButton small_ship;
    private javax.swing.JLayeredPane tablero_principal;
    private javax.swing.JLabel text_puntos;
    // End of variables declaration//GEN-END:variables
    
    public class GridPanel extends JPanel{
        int filas;
        int columnas;
        public GridPanel(int filas, int columnas){
            super();
            this.filas = filas;
            this.columnas = columnas;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            int cellwidth = getWidth() / this.columnas;
            int cellHeight = getHeight() / this.filas;
            
            for(int fil = 0; fil < this.filas; fil++){
                for(int col = 0; col < this.columnas; col++){
                    int x = col * cellwidth;
                    int y = fil * cellHeight;
                    
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawRect(x, y, cellwidth, cellHeight);
                }
            }
        }
        
    }
    
    @Override
    public void onEntrarSalaEspera(EntrarSalaEsperaEvent event){
        int session_id = event.getSession_id();
        String nick = event.getNick();
        String rol = event.getRol();
        
        SalaEsperaView salaEspera = new SalaEsperaView(session_id, nick, rol, cliente, parent, false, tablero);
        salaEspera.setVisible(true);
        this.setVisible(false);
    }
}
