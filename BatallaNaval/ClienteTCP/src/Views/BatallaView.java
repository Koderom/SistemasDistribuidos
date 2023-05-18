/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Views;

import BatallaEvents.BarcoEliminadoEvent;
import BatallaEvents.BatallaListener;
import BatallaEvents.GanadorEvent;
import BatallaEvents.JugadorPerdioEvent;
import BatallaEvents.ResultadoDisparoEvent;
import BatallaEvents.SiguienteTurnoEvent;
import ClienteSocket.ClienteTCP;
import Models.Barco;
import Models.Jugador;
import Models.Tablero;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author MIRKO
 */
public class BatallaView extends javax.swing.JDialog implements BatallaListener{
    ClienteTCP cliente;
    ArrayList<JLayeredPane> tableros;
    Tablero miTablero;
    ArrayList<Jugador> contrincantes = new ArrayList<>();
    String turno;
    boolean tuTurno;
    /**
     * Creates new form BatallaView
     */
    JLabel fondo_tablero;
    JLabel banner_mensaje;
    JLabel bomba;
    Jugador objetivo;
    JLayeredPane tablero_objetivo;
    public BatallaView(java.awt.Frame parent, boolean modal, ClienteTCP cliente, String turno, Tablero miTablero, ArrayList<Jugador> jugadores) {
        super(parent, modal);
        initComponents();
        
        this.cliente = cliente;
        this.cliente.addBatallaListener(this);
        this.miTablero = miTablero;
        this.tableros = new ArrayList<>();
        
        int i = 0;
        for(Jugador jugador : jugadores)
            if(jugador.nick.equals(this.cliente.nick)) i = jugadores.indexOf(jugador);
        jugadores.remove(i);
        this.contrincantes = jugadores;
        this.objetivo = contrincantes.get(0);
        
        this.turno = turno;
        this.tuTurno = false;
        iniciarMiTablero();
        iniciarTableroContrincantes();
        iniciarPartida();
        
        this.setTitle(this.cliente.nick);
    }
    private JLabel getLabelImage(int width, int height, String path){
        JLabel label = new JLabel();
        label.setBounds(0, 0, width, height);
        ImageIcon imageIcon = new ImageIcon(path);
        Icon icon = new ImageIcon(imageIcon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_DEFAULT));
        label.setIcon(icon);
        return label;
    }
    private void iniciarPartida(){
        if(this.turno.equals(cliente.nick)){
            this.tuTurno = true;
            boton_disparar.setEnabled(true);
        }
        String text = bitacora_batalla.getText();
        text += String.format("%s inicia la partida", turno);
        bitacora_batalla.setText(text);
    }
    private void mostrarMensaje(String texto){
        Font font = new Font("Segoe UI Black", Font.BOLD, 25);
        banner_mensaje = new JLabel(texto);
        
         
        int y = (mi_tablero_container.getHeight()/2);
        
        banner_mensaje.setFont(font);
        banner_mensaje.setBounds(0 ,y,400, banner_mensaje.getPreferredSize().height);
        banner_mensaje.setForeground(Color.WHITE);
        banner_mensaje.setBackground( new Color(0xa9dad4));
        banner_mensaje.setOpaque(true);
        banner_mensaje.setVerticalAlignment(SwingConstants.CENTER);
        banner_mensaje.setHorizontalAlignment(SwingConstants.CENTER);
        mi_tablero_container.add(banner_mensaje, Integer.valueOf(2));
        
        mi_tablero_container.revalidate();
        this.repaint();
    }
    private void iniciarTableroContrincantes(){
        for(Jugador jugador : contrincantes){
            GridPanel tablero = new GridPanel(10, 10);
            tablero.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            tablero.setBounds(0, 0, 400, 400);
            tablero.setBackground( new Color(0x2F495A));
            tablero.setBorder(null);
            
            JLayeredPane layered_tablero = new JLayeredPane();
            layered_tablero.setBounds(0, 0, 400, 400);
            
            layered_tablero.add(tablero, Integer.valueOf(0));
            tableros_container.add(layered_tablero);
            this.tableros.add(layered_tablero);
            
            if(jugador.nick.equals(this.objetivo.nick)) layered_tablero.setVisible(true);
            else layered_tablero.setVisible(false);
        }
        text_objetivo.setText(objetivo.nick);
    }
    private void iniciarMiTablero(){
//        this.fondo_tablero = new JLabel();
//        fondo_tablero.setBounds(0, 0, 400, 400);
//        ImageIcon image = new ImageIcon("src/assets/agua.jpg");
//        Icon icon = new ImageIcon(image.getImage().getScaledInstance(400, 400, Image.SCALE_DEFAULT));
//        fondo_tablero.setIcon( icon);
        
        GridPanel grid_panel = new GridPanel(10, 10);
        grid_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        grid_panel.setBounds(0, 0, 400, 400);
        grid_panel.setBackground( new Color(0x2F495A));
        
        //container_principal.add(fondo_tablero, Integer.valueOf(0));
        mi_tablero_container.add(grid_panel, Integer.valueOf(1));
        
        pintarMisBarcos();
        
        mi_tablero_container.revalidate();
        this.repaint();
    }
    private void pintarMisBarcos(){
        HashMap<Character, Barco> misBarcos = this.miTablero.getBarcos();
        
        for(Barco barco : misBarcos.values()){
            JLabel mibarco = new JLabel();
            
            switch (barco.size) {
                case 3:
                if(barco.getOrientacion() == 'V') mibarco = getLabelImage(40, 120, "src/assets/buque_de_guerra_2V.png");
                else mibarco = getLabelImage(120, 40, "src/assets/buque_de_guerra_2H.png");
                break;
            case 4:
                if(barco.getOrientacion() == 'V') mibarco = getLabelImage(40, 160, "src/assets/buque_de_guerra_3V.png");
                else mibarco = getLabelImage(160, 40, "src/assets/buque_de_guerra_3H.png");
                break;
            case 5:
                if(barco.getOrientacion() == 'V') mibarco = getLabelImage(40, 200, "src/assets/buque_de_guerra_4V.png");
                else mibarco = getLabelImage(200, 40, "src/assets/buque_de_guerra_4H.png");
                break;
            default:
            }
            
            mibarco.setLocation(barco.getY()*40, barco.getX()*40);
            mi_tablero_container.add(mibarco, Integer.valueOf(2));
            repaint();
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

        mi_tablero_container = new javax.swing.JLayeredPane();
        boton_disparar = new javax.swing.JButton();
        tableros_container = new javax.swing.JPanel();
        text_objetivo = new javax.swing.JLabel();
        boton_anterior = new javax.swing.JButton();
        boton_siguiente = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        bitacora_batalla = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        mi_tablero_container.setPreferredSize(new java.awt.Dimension(430, 430));
        mi_tablero_container.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                mi_tablero_containerMouseDragged(evt);
            }
        });
        mi_tablero_container.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mi_tablero_containerMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout mi_tablero_containerLayout = new javax.swing.GroupLayout(mi_tablero_container);
        mi_tablero_container.setLayout(mi_tablero_containerLayout);
        mi_tablero_containerLayout.setHorizontalGroup(
            mi_tablero_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        mi_tablero_containerLayout.setVerticalGroup(
            mi_tablero_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );

        boton_disparar.setText("Disparar");
        boton_disparar.setEnabled(false);
        boton_disparar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_dispararActionPerformed(evt);
            }
        });

        tableros_container.setPreferredSize(new java.awt.Dimension(400, 400));
        tableros_container.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tableros_containerMouseDragged(evt);
            }
        });
        tableros_container.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tableros_containerMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout tableros_containerLayout = new javax.swing.GroupLayout(tableros_container);
        tableros_container.setLayout(tableros_containerLayout);
        tableros_containerLayout.setHorizontalGroup(
            tableros_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        tableros_containerLayout.setVerticalGroup(
            tableros_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );

        text_objetivo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        text_objetivo.setForeground(new java.awt.Color(153, 153, 153));
        text_objetivo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        text_objetivo.setText("JUGADOR");
        text_objetivo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        boton_anterior.setText("Anterior");
        boton_anterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_anteriorActionPerformed(evt);
            }
        });

        boton_siguiente.setText("Siguiente");
        boton_siguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_siguienteActionPerformed(evt);
            }
        });

        bitacora_batalla.setColumns(20);
        bitacora_batalla.setRows(5);
        jScrollPane1.setViewportView(bitacora_batalla);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(mi_tablero_container, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tableros_container, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(12, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(boton_anterior)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(boton_disparar, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                            .addComponent(text_objetivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boton_siguiente)
                        .addGap(83, 83, 83))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mi_tablero_container, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tableros_container, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(boton_anterior)
                            .addComponent(boton_siguiente)
                            .addComponent(text_objetivo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boton_disparar)
                        .addGap(19, 19, 19))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mi_tablero_containerMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mi_tablero_containerMouseDragged
        if(tuTurno && bomba != null){
            int x = evt.getX() - bomba.getWidth() / 2;
            int y = evt.getY() - bomba.getHeight() / 2;
            bomba.setLocation(x, y);
        }
    }//GEN-LAST:event_mi_tablero_containerMouseDragged

    private void mi_tablero_containerMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mi_tablero_containerMouseReleased
        if(tuTurno && bomba != null){
            int x = (evt.getX()/40)*40;
            int y = (evt.getY()/40)*40;
            bomba.setLocation(x, y);
            bomba = null;
            tuTurno = false;   
        }
    }//GEN-LAST:event_mi_tablero_containerMouseReleased

    private void boton_dispararActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_dispararActionPerformed
        int i = 0;
        for(Jugador jugador : contrincantes)
            if(jugador.nick.equals(this.objetivo.nick)) i = contrincantes.indexOf(jugador);
        this.tablero_objetivo = this.tableros.get(i);
        
        this.bomba = getLabelImage(40, 40, "src/assets/bomba_40x40.png");
        tablero_objetivo.add(bomba, Integer.valueOf(1));
        tablero_objetivo.revalidate();
        tablero_objetivo.repaint();
        boton_disparar.setEnabled(false);
    }//GEN-LAST:event_boton_dispararActionPerformed

    private void tableros_containerMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableros_containerMouseDragged
        if(bomba != null){
            int x = evt.getX() - bomba.getWidth() / 2;
            int y = evt.getY() - bomba.getHeight() / 2;
            bomba.setLocation(x, y);
            repaint();
        }
    }//GEN-LAST:event_tableros_containerMouseDragged

    private void tableros_containerMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableros_containerMouseReleased
        if(bomba == null)return;
        int x = (evt.getX() / 40);
        int y = (evt.getY() / 40);
        System.out.println(String.format("objetivo: %s, fil: %s, col: %s", this.objetivo.nick, y, x));
        this.cliente.dispararA(objetivo.nick, y, x);
        this.bomba.setVisible(false);
        this.bomba = null;
    }//GEN-LAST:event_tableros_containerMouseReleased

    private void boton_siguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_siguienteActionPerformed
        int i = contrincantes.indexOf(this.objetivo);
        tableros.get(i).setVisible(false);
        i++;
        if(!(i < contrincantes.size())) i = 0;
        
        objetivo = contrincantes.get(i);
        text_objetivo.setText(objetivo.nick);
        tableros.get(i).setVisible(true);
        
        if(!objetivo.eliminado && tuTurno) boton_disparar.setEnabled(true);
        else boton_disparar.setEnabled(false);
        
        
        repaint();
    }//GEN-LAST:event_boton_siguienteActionPerformed

    private void boton_anteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_anteriorActionPerformed
        int i = contrincantes.indexOf(this.objetivo);
        tableros.get(i).setVisible(false);
        i--;
        if(!(i >= 0)) i = contrincantes.size()-1;
        
        objetivo = contrincantes.get(i);
        text_objetivo.setText(objetivo.nick);
        tableros.get(i).setVisible(true);
        
        if(!objetivo.eliminado && tuTurno) boton_disparar.setEnabled(true);
        else boton_disparar.setEnabled(false);
        
        repaint();
    }//GEN-LAST:event_boton_anteriorActionPerformed
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea bitacora_batalla;
    private javax.swing.JButton boton_anterior;
    private javax.swing.JButton boton_disparar;
    private javax.swing.JButton boton_siguiente;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLayeredPane mi_tablero_container;
    private javax.swing.JPanel tableros_container;
    private javax.swing.JLabel text_objetivo;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onResultadoDisparo(ResultadoDisparoEvent event) {
        String nick_objetivo = event.getNick_objetivo();
        if(nick_objetivo.equals(this.cliente.nick)){
            JLabel impacto_image = new JLabel();
            if(event.getResultado() == 'I')impacto_image = getLabelImage(40, 40, "src/assets/fuego.gif");
            else impacto_image = getLabelImage(40, 40, "src/assets/fallo_image.png");
            impacto_image.setLocation(event.getColumna()*40, event.getFila()*40);
            this.mi_tablero_container.add(impacto_image, Integer.valueOf(3));
            this.mi_tablero_container.revalidate();
            repaint();
        }else{
            int i = 0;
            for(Jugador j : contrincantes)
                if(j.nick.equals(event.getNick_objetivo())) i = contrincantes.indexOf(j);
            JLayeredPane tablero_objetivo = tableros.get(i);
            
            JLabel impacto_image = new JLabel();
            if(event.getResultado() == 'I')impacto_image = getLabelImage(40, 40, "src/assets/fuego.gif");
            else impacto_image = getLabelImage(40, 40, "src/assets/fallo_image.png");
            impacto_image.setLocation(event.getColumna()*40, event.getFila()*40);
            tablero_objetivo.add(impacto_image, Integer.valueOf(3));
            tablero_objetivo.revalidate();
            repaint();
        }
        
        String text = bitacora_batalla.getText();
        text +="\n"+ String.format("%s ha realizador un disparo a %s, en las coordenadas(%s, %s) ", event.getNick_autor(), event.getNick_objetivo(), event.getFila(), event.getColumna());
        if(event.getResultado() == 'F') text += ", fallo el disparo";
        else text += ", disparo exitoso";
        bitacora_batalla.setText(text);
    }
    
    private JLabel getBomba(){
        ImageIcon image = new ImageIcon("src/assets/bomba_40x40.png");
        JLabel label = new JLabel(image);
        label.setSize(40,40);
        return label;
    }

    @Override
    public void onSiguienteTurno(SiguienteTurnoEvent event) {
        if(event.getTurno().equals(this.cliente.nick)){
            for(Jugador j : contrincantes){
                if(j.nick.equals(this.objetivo.nick) && !j.eliminado)boton_disparar.setEnabled(true);
            }
            this.tuTurno = true;
        }else{ 
            boton_disparar.setEnabled(false);
            this.tuTurno = false;
        }
        this.turno = event.getTurno();
        
        String text = bitacora_batalla.getText();
        text +="\n"+ String.format("turno de %s ", event.getTurno());
        bitacora_batalla.setText(text);
    }

    @Override
    public void onBarcoEliminado(BarcoEliminadoEvent event) {
        String nick_objetivo = event.getNick_objetivo();
        if(!nick_objetivo.equals(this.cliente.nick)){
            int i = 0;
            for(Jugador j : contrincantes)
                if(j.nick.equals(event.getNick_objetivo())) i = contrincantes.indexOf(j);
            JLayeredPane tablero_objetivo = tableros.get(i);
            
            JLabel barco_destruido = new JLabel();
            
            switch (event.getSize()) {
                case 3:
                if(event.getOrientacion() == 'V') barco_destruido = getLabelImage(40, 120, "src/assets/buque_de_guerra_2V.png");
                else barco_destruido = getLabelImage(120, 40, "src/assets/buque_de_guerra_2H.png");
                break;
            case 4:
                if(event.getOrientacion() == 'V') barco_destruido = getLabelImage(40, 160, "src/assets/buque_de_guerra_3V.png");
                else barco_destruido = getLabelImage(160, 40, "src/assets/buque_de_guerra_3H.png");
                break;
            case 5:
                if(event.getOrientacion() == 'V') barco_destruido = getLabelImage(40, 200, "src/assets/buque_de_guerra_4V.png");
                else barco_destruido = getLabelImage(200, 40, "src/assets/buque_de_guerra_4H.png");
                break;
            default:
            }
            
            barco_destruido.setLocation(event.getColumna()*40, event.getFila()*40);
            tablero_objetivo.add(barco_destruido, Integer.valueOf(2));
            tablero_objetivo.revalidate();
            repaint();
        }
        
        String text = bitacora_batalla.getText();
        text +="\n"+ String.format("%s ha eliminado el barco de %s", event.getNick_autor(), event.getNick_objetivo());
        bitacora_batalla.setText(text);
    }

    @Override
    public void onJugadorPerdio(JugadorPerdioEvent event) {
        if(event.getNick().equals(this.cliente.nick)){
            JOptionPane.showMessageDialog(null, "Has sido eliminado");
            boton_disparar.setEnabled(false);
            tuTurno = false;
        } 
        else{
            for(Jugador j :  contrincantes)
                if(j.nick.equals(event.getNick())) j.eliminado = true;
        }
        
        String text = bitacora_batalla.getText();
        text +="\n"+ String.format("%s ha eliminado ", event.getNick());
        bitacora_batalla.setText(text);
    }

    @Override
    public void onGanador(GanadorEvent event) {
        if(event.getNick().equals(this.cliente.nick)) JOptionPane.showMessageDialog(null, "Ganaste");
        else JOptionPane.showMessageDialog(null, "El ganador de la partida es: " + event.getNick());
        
        
        String text = bitacora_batalla.getText();
        text +="\n"+ String.format("%s es el ganador de la partida", event.getNick());
        bitacora_batalla.setText(text);
    }
    
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
}
