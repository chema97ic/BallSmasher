/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ballsmasher;

import com.sun.awt.AWTUtilities;
import java.awt.Color;
import java.awt.Event;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.timer.TimerNotification;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author chema
 */
public class Window extends javax.swing.JFrame {

    /**
     * Creates new form Window
     */
    public Window() {

        initComponents();
        initDatosGuardados();
        initJugar();
        initPausa();
        this.setLocationRelativeTo(null);
        btnReplay.setVisible(false);
        btnExit.setVisible(false);
        jpMenu.setVisible(true);
        jpGameOver.setVisible(false);
        jpJugar.setVisible(false);
        jpPausa.setVisible(false);
        java.awt.KeyboardFocusManager manager = java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new MyDispatcher());
    }

    //This needs to be substituded by a database.
    public void initDatosGuardados() {
        try {
            File fichero = new File("database.txt");
            if (fichero.exists() == false) {
                fichero.createNewFile();
                fw = new FileWriter("database.txt");
                fw.write("0\n");
                fw.close();
            } else {
                Scanner s = new Scanner(fichero);
                while (s.hasNextLine()) {
                    if (s.nextLine().equals("1")) {
                        JOptionPane.showMessageDialog(null, "prueba");
                    }

                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Make kee events to affect the whole window
    private class MyDispatcher implements java.awt.KeyEventDispatcher {

        @Override
        public boolean dispatchKeyEvent(java.awt.event.KeyEvent e) {
            if (lifeCount > 0) {
                switch (e.getID()) {
                    case java.awt.event.KeyEvent.KEY_PRESSED:
                        if (e.getKeyCode() == KeyEvent.VK_LEFT && iniciarTimers) {
                            timerLeft.start();
                        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && iniciarTimers) {
                            timerRight.start();
                        } else if (e.getKeyCode() == KeyEvent.VK_UP && tecla == false && iniciarTimers) {
                            timerUp.start();
                        }

                        if (e.getKeyCode() == KeyEvent.VK_Q && teclaQ == true) {
                            borrarPelotasRojas();
                            teclaQ = false;
                        }
                        
                        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && teclaEsc == true && lblCountdown.isVisible() == false) {
                            quitarPausa();
                            jpPausa.setVisible(false);
                        }else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && teclaEsc == false && jpJugar.isVisible() == true && lblCountdown.isVisible() == false) {
                            ponerPausa();
                            jpPausa.setVisible(true);
                        }
                        
                        
                        break;
                    case java.awt.event.KeyEvent.KEY_RELEASED:
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_LEFT:
                                timerLeft.stop();
                                tecla = false;
                                break;
                            case KeyEvent.VK_RIGHT:
                                timerRight.stop();
                                tecla = false;
                                break;
                            case KeyEvent.VK_UP:
                                timerUp.stop();
                                disparoRealizado = false;
                                break;
                        }
                        break;
                }
            }
            return false;
        }
    }

    public void stopTimers() {
        timer.stop();
        timerDisparo.stop();
        timerHit.stop();
        
    }

    public void startTimers() {
        timer.start();
        timerDisparo.start();
        timerHit.start();
    }
    
    public void ponerPausa(){
        iniciarTimers = false;
        stopTimers();
        teclaEsc = true;
        jpJugar.setVisible(false);
    }
    
    public void quitarPausa(){
        startTimers();
        teclaEsc = false;
        jpJugar.setVisible(true);
        iniciarTimers = true;
    }
    
    public void borrarPelotasRojas() {
        for (Pelota p : pelotas) {
            if (p.getN() == 0) {
                p.setVisible(false);
            }
        }
    }

    public void initJugar() {
        //jpJugar
        jpJugar.setBackground(new java.awt.Color(51, 255, 255));
        jpJugar.setPreferredSize(new java.awt.Dimension(50, 50));

        jpJugar.setLayout(null);
        this.add(jpJugar);
        //Nave
        lblNave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/nave.png"))); // NOI18N
        jpJugar.add(lblNave);
        lblNave.setBounds(450, 510, 40, 40);
        //Score
        lblScore.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        lblScore.setForeground(new java.awt.Color(255, 0, 0));
        lblScore.setText("0000");
        jpJugar.add(lblScore);
        lblScore.setBounds(0, 0, 110, 20);
        //Countdown
        lblCountdown.setFont(new java.awt.Font("Trebuchet MS", 1, 48)); // NOI18N
        lblCountdown.setForeground(new java.awt.Color(255, 0, 51));
        lblCountdown.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCountdown.setText("3");
        jpJugar.add(lblCountdown);
        lblCountdown.setBounds(360, 180, 230, 130);
        //lblLife3
        lblLife3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/life_icon.png"))); // NOI18N
        jpJugar.add(lblLife3);
        lblLife3.setBounds(920, 0, 20, 24);
        //lblLife2
        lblLife2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/life_icon.png"))); // NOI18N
        jpJugar.add(lblLife2);
        lblLife2.setBounds(940, 0, 20, 24);
        //lblLife1
        lblLife1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/life_icon.png"))); // NOI18N
        jpJugar.add(lblLife1);
        lblLife1.setBounds(960, 0, 20, 24);
        //adding the components to the panel
        jpJugar.add(lblNave);
        jpJugar.add(lblLife1);
        jpJugar.add(lblLife2);
        jpJugar.add(lblLife3);
        jpJugar.add(lblCountdown);
        jpJugar.add(lblScore);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jpJugar, javax.swing.GroupLayout.PREFERRED_SIZE, 986, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jpGameOver, javax.swing.GroupLayout.DEFAULT_SIZE, 986, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jpMenu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jpJugar, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jpGameOver, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jpMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

    }
    
    public void initPausa() {
        //jpPausa
        jpPausa.setBackground(new Color(0, 0, 0, 20));
        jpPausa.setPreferredSize(new java.awt.Dimension(50, 50));
        jpPausa.setLayout(null);
        this.add(jpPausa);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jpPausa, javax.swing.GroupLayout.PREFERRED_SIZE, 990, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jpGameOver, javax.swing.GroupLayout.DEFAULT_SIZE, 990, Short.MAX_VALUE)) //este es el valor a modificar para cambiar el tamaño
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jpMenu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jpPausa, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)//este es el valor a modificar para cambiar el tamaño
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jpGameOver, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jpMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

    }

    public void hit() {
        for (Disparo d : disparos) {
            for (Pelota p : pelotas) {
                if (d.getRectangle().intersects(p.getRectangle())) {
                    d.setVisible(false);
                    p.setVisible(false);
                    if (p.getN() == 1) {
                        score[0] = Integer.parseInt(lblScore.getText().charAt(0) + "");
                        score[1] = Integer.parseInt(lblScore.getText().charAt(1) + "");
                        score[2] = Integer.parseInt(lblScore.getText().charAt(2) + "");
                        score[3] = Integer.parseInt(lblScore.getText().charAt(3) + "");

                        score[3]++;
                        if (score[3] > 9) {
                            score[3] = 0;
                            score[2]++;
                            if (score[2] > 9) {
                                score[2] = 0;
                                score[1]++;
                                if (score[1] > 9) {
                                    score[1] = 0;
                                    score[0]++;
                                }
                            }
                        }

                        String scoreUpdated = score[0] + "" + score[1] + "" + score[2] + "" + score[3] + "";
                        lblScore.setText(scoreUpdated);
                    } else if (p.getN() == 0) {
                        lifeCount--;
                    }
                }
            }
        }

    }

    public void gameOver() {
        if (lifeCount == 0) {
            iniciarTimers = false;
            jpJugar.setVisible(false);
            jpGameOver.setVisible(true);
            btnReplay.setVisible(true);
            btnExit.setVisible(true);
            timer.stop();
            timerDisparo.stop();
            timerHit.stop();
            timerLeft.stop();
            timerRight.stop();
            timerUp.stop();
            for (Pelota p : pelotas) {
                p.setVisible(false);
                p = null;
            }
            for (Disparo d : disparos) {
                d.setVisible(false);
                d = null;
            }
        }
    }

    public void generar_pelotas() {
        x = (int) (Math.random() * 915);
        ball_color = (int) (Math.random() * 20);
        if (ball_color > 0 && ball_color <= 4) {
            n = 0;
        } else {
            n = 1;
        }
        Pelota pelota = new Pelota(n, x, y, 1);
        if (n == 1) {
            pelota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pelota_negra.png"))); // NOI18N

        } else if (n == 0) {
            pelota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/red_ball.png"))); // NOI18N

        }
        pelota.setContentAreaFilled(false);
        pelota.setPreferredSize(new java.awt.Dimension(70, 70));
        jpJugar.add(pelota);
        pelota.setBounds(pelota.getX(), pelota.getY(), 70, 70);
        pelotas.add(pelota);

    }

    public void generar_disparos() {
        int xDisparo = nave.getX();
        Disparo disparo = new Disparo(xDisparo, disparo1Y, disparo1Width, disparo1Height, 15);
        disparo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/disparo.gif"))); // NOI18N
        disparo.setBounds(410, 480, 40, 30);
        jpJugar.add(disparo);
        disparo.setBounds(disparo.getX(), disparo.getY(), disparo.getWidth(), disparo.getHeight());
        disparos.add(disparo);
    }

    public void jugar() {

        generar_pelotas();
        timer.start();
        timerDisparo.start();
        timerHit.start();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpMenu = new javax.swing.JPanel();
        btnCampaign = new javax.swing.JButton();
        btnStore = new javax.swing.JButton();
        btnOpciones = new javax.swing.JButton();
        btnEndless = new javax.swing.JButton();
        btnExitGame = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jpGameOver = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();
        btnReplay = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jpMenu.setBackground(new java.awt.Color(255, 102, 102));
        jpMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCampaign.setBackground(new java.awt.Color(51, 51, 255));
        btnCampaign.setText("CAMPAÑA");
        btnCampaign.setBorderPainted(false);
        btnCampaign.setFocusPainted(false);
        btnCampaign.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCampaignMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCampaignMouseExited(evt);
            }
        });
        btnCampaign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCampaignActionPerformed(evt);
            }
        });
        jpMenu.add(btnCampaign, new org.netbeans.lib.awtextra.AbsoluteConstraints(335, 136, 303, 37));

        btnStore.setBackground(new java.awt.Color(51, 51, 255));
        btnStore.setText("TIENDA");
        btnStore.setBorderPainted(false);
        btnStore.setFocusPainted(false);
        btnStore.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnStoreMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnStoreMouseExited(evt);
            }
        });
        jpMenu.add(btnStore, new org.netbeans.lib.awtextra.AbsoluteConstraints(335, 184, 303, 35));

        btnOpciones.setBackground(new java.awt.Color(51, 51, 255));
        btnOpciones.setText("OPCIONES");
        btnOpciones.setBorderPainted(false);
        btnOpciones.setFocusPainted(false);
        btnOpciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnOpcionesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnOpcionesMouseExited(evt);
            }
        });
        jpMenu.add(btnOpciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(335, 230, 303, 35));

        btnEndless.setBackground(new java.awt.Color(51, 51, 255));
        btnEndless.setText("ENDLESS");
        btnEndless.setBorderPainted(false);
        btnEndless.setFocusPainted(false);
        btnEndless.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEndlessMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEndlessMouseExited(evt);
            }
        });
        btnEndless.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEndlessActionPerformed(evt);
            }
        });
        jpMenu.add(btnEndless, new org.netbeans.lib.awtextra.AbsoluteConstraints(335, 276, 303, 35));

        btnExitGame.setBackground(new java.awt.Color(51, 51, 255));
        btnExitGame.setText("SALIR");
        btnExitGame.setBorderPainted(false);
        btnExitGame.setFocusPainted(false);
        btnExitGame.setRequestFocusEnabled(false);
        btnExitGame.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnExitGameMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnExitGameMouseExited(evt);
            }
        });
        btnExitGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitGameActionPerformed(evt);
            }
        });
        jpMenu.add(btnExitGame, new org.netbeans.lib.awtextra.AbsoluteConstraints(335, 322, 303, 33));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/menu_image.jpg"))); // NOI18N
        jpMenu.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 990, 550));

        jpGameOver.setBackground(new java.awt.Color(204, 102, 0));
        jpGameOver.setLayout(null);

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        btnExit.setContentAreaFilled(false);
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        jpGameOver.add(btnExit);
        btnExit.setBounds(520, 330, 80, 70);

        btnReplay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/replay.png"))); // NOI18N
        btnReplay.setContentAreaFilled(false);
        btnReplay.setFocusPainted(false);
        btnReplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReplayActionPerformed(evt);
            }
        });
        jpGameOver.add(btnReplay);
        btnReplay.setBounds(400, 330, 80, 70);

        jLabel1.setFont(new java.awt.Font("Tempus Sans ITC", 3, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("GAME OVER");
        jpGameOver.add(jLabel1);
        jLabel1.setBounds(330, 180, 340, 210);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 990, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpGameOver, javax.swing.GroupLayout.DEFAULT_SIZE, 986, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 550, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpGameOver, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jpMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnReplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReplayActionPerformed
        jpJugar.setVisible(true);
        jpGameOver.setVisible(false);
        timerStartGame.start();
        lifeCount = 3;
        lblScore.setText("0000");
        lblLife1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/life_icon.png")));
        lblLife2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/life_icon.png")));
        lblLife3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/life_icon.png")));
        lblNave.setBounds(spaceshipX, spaceshipY, spaceshipWidth, spaceshipHeight);
        nave.setX(450);
        lblCountdown.setVisible(true);
    }//GEN-LAST:event_btnReplayActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        jpGameOver.setVisible(false);
        jpMenu.setVisible(true);
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnEndlessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEndlessActionPerformed
        jpJugar.setVisible(true);
        jpGameOver.setVisible(false);
        timerStartGame.start();
        timerSkills.start();
        lifeCount = 3;
        lblScore.setText("0000");
        lblLife1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/life_icon.png")));
        lblLife2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/life_icon.png")));
        lblLife3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/life_icon.png")));
        lblNave.setBounds(spaceshipX, spaceshipY, spaceshipWidth, spaceshipHeight);
        nave.setX(450);
        lblCountdown.setVisible(true);
        jpJugar.setVisible(true);
        jpMenu.setVisible(false);
    }//GEN-LAST:event_btnEndlessActionPerformed

    private void btnExitGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitGameActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnExitGameActionPerformed

    private void btnExitGameMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitGameMouseEntered
        btnExitGame.setBackground(new java.awt.Color(0, 255, 0));
    }//GEN-LAST:event_btnExitGameMouseEntered

    private void btnExitGameMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitGameMouseExited
        btnExitGame.setBackground(new java.awt.Color(51, 51, 255));
    }//GEN-LAST:event_btnExitGameMouseExited

    private void btnEndlessMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEndlessMouseEntered
        btnEndless.setBackground(new java.awt.Color(0, 255, 0));
    }//GEN-LAST:event_btnEndlessMouseEntered

    private void btnEndlessMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEndlessMouseExited
        btnEndless.setBackground(new java.awt.Color(51, 51, 255));
    }//GEN-LAST:event_btnEndlessMouseExited

    private void btnOpcionesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpcionesMouseEntered
        btnOpciones.setBackground(new java.awt.Color(0, 255, 0));
    }//GEN-LAST:event_btnOpcionesMouseEntered

    private void btnOpcionesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpcionesMouseExited
        btnOpciones.setBackground(new java.awt.Color(51, 51, 255));
    }//GEN-LAST:event_btnOpcionesMouseExited

    private void btnStoreMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStoreMouseEntered
        btnStore.setBackground(new java.awt.Color(0, 255, 0));
    }//GEN-LAST:event_btnStoreMouseEntered

    private void btnStoreMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStoreMouseExited
        btnStore.setBackground(new java.awt.Color(51, 51, 255));
    }//GEN-LAST:event_btnStoreMouseExited

    private void btnCampaignMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCampaignMouseEntered
        btnCampaign.setBackground(new java.awt.Color(0, 255, 0));
    }//GEN-LAST:event_btnCampaignMouseEntered

    private void btnCampaignMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCampaignMouseExited
        btnCampaign.setBackground(new java.awt.Color(51, 51, 255));
    }//GEN-LAST:event_btnCampaignMouseExited

    private void btnCampaignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCampaignActionPerformed

    }//GEN-LAST:event_btnCampaignActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Window().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCampaign;
    private javax.swing.JButton btnEndless;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnExitGame;
    private javax.swing.JButton btnOpciones;
    private javax.swing.JButton btnReplay;
    private javax.swing.JButton btnStore;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jpGameOver;
    private javax.swing.JPanel jpMenu;
    // End of variables declaration//GEN-END:variables

    Timer timer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            gameOver();
            if (lifeCount == 2) {
                lblLife1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/life_lost.png")));
                lblLife2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/life_icon.png")));
                lblLife3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/life_icon.png")));
            } else if (lifeCount == 1) {
                lblLife2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/life_lost.png")));
                lblLife1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/life_lost.png")));
                lblLife3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/life_icon.png")));
            } else if (lifeCount == 0) {
                lblLife3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/life_lost.png")));
            } else if (lifeCount == 3) {
                lblLife1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/life_icon.png")));
                lblLife2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/life_icon.png")));
                lblLife3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/life_icon.png")));
            }
            contadorTiempo++;
            for (Pelota p : pelotas) {
                for (int i = 0; i < p.getSpeed(); i++) {
                    p.setY(p.getY() + 1);
                    p.setBounds(p.getX(), p.getY(), 70, 70);
                    if (p.getY() == 458) {
                        p.setVisible(false);

                    }
                    if (p.getY() == 458 && p.getN() != 0) {
                        lifeCount--;
                    }

                }
                if (p.isVisible() == false) {
                    p = null;
                }
            }
            for (int i = 0; i < pelotas.size(); i++) {
                if (pelotas.get(i).isVisible() == false) {
                    pelotas.remove(i);
                }
            }
            if (contadorTiempo == 50) { //ball spawn ratio
                generar_pelotas();
                contadorTiempo = 0;
            }
        }
    });

    Timer timerDisparo = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            for (Disparo d : disparos) {
                for (int i = 0; i < d.getSpeed(); i++) {
                    d.setY(d.getY() - 1);
                    d.setBounds(d.getX(), d.getY(), 40, 30);
                    if (d.getY() == 0) {
                        d.setVisible(false);

                    }

                }
                if (d.isVisible() == false) {
                    d = null;
                }
            }
            for (int i = 0; i < disparos.size(); i++) {
                if (disparos.get(i).isVisible() == false) {
                    disparos.remove(i);
                }
            }
        }
    });

    Timer timerLeft = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            tecla = true;
            for (int i = 0; i < nave.getSpeed(); i++) {
                if (nave.getX() != 0) {
                    nave = new Nave(nave.getX() - 1, nave.getY(), nave.getWidth(), nave.getHeight(), nave.getSpeed());

                    lblNave.setBounds(new Rectangle(nave.getX(), nave.getY(), nave.getWidth(), nave.getHeight()));
                }

            }
        }
    });

    Timer timerRight = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            tecla = true;
            for (int i = 0; i < nave.getSpeed(); i++) {
                if (nave.getX() != 946) {
                    nave = new Nave(nave.getX() + 1, nave.getY(), nave.getWidth(), nave.getHeight(), nave.getSpeed());
                    lblNave.setBounds(new Rectangle(nave.getX(), nave.getY(), nave.getWidth(), nave.getHeight()));
                }
            }
        }
    });

    Timer timerUp = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent e) {

            if (disparoRealizado == true) {
                timerUp.stop();
            } else {
                generar_disparos();
                disparoRealizado = true;
            }
        }
    });

    Timer timerHit = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            hit();
        }
    });

    Timer timerStartGame = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            lblCountdown.setVisible(true);
            lblCountdown.setText((Integer.parseInt(lblCountdown.getText()) - 1) + "");
            countdown++;
            if (countdown == 4) {
                countdown = 0;
                lblCountdown.setVisible(false);
                lblCountdown.setText("3");
                timerStartGame.stop();
                iniciarTimers = true;
                jugar();

            }
        }
    });
    Timer timerSkills = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (timerQ == 10 && teclaQ == false) {
                timerQ = 0;
                teclaQ = true;
            }
            if (teclaQ == false) {
                timerQ++;
            }
        }
    });
    JLabel lblNave = new JLabel();
    JLabel lblScore = new JLabel();
    JLabel lblCountdown = new JLabel();
    JLabel lblLife1 = new JLabel();
    JLabel lblLife2 = new JLabel();
    JLabel lblLife3 = new JLabel();
    final int spaceshipWidth = 40;
    final int spaceshipHeight = 40;
    final int spaceshipX = 450;
    final int spaceshipY = 510;
    final int disparo1Width = 40;
    final int disparo1Height = 30;
    final int disparo1Y = 480;
    final static int frameWidth = 990;
    final static int frameHeight = 550;
    int timerQ = 0;
    Stage jpJugar = new Stage(new ImageIcon(getClass().getResource("/img/background.jpg")).getImage());
    Stage jpPausa = new Stage(new ImageIcon(getClass().getResource("/img/pause_background.jpg")).getImage());
    Nave nave = new Nave(spaceshipX, spaceshipY, spaceshipWidth, spaceshipHeight, 7);
    int ball_color;
    int n;  //determines the type of ball
    int lifeCount = 3;
    int countdown = 0;
    boolean iniciarTimers = false;
    boolean disparoRealizado = false;
    ArrayList<Pelota> pelotas = new ArrayList<Pelota>();
    ArrayList<Disparo> disparos = new ArrayList<Disparo>();
    int[] score = new int[4];
    int x = 0;
    int y = 0;
    boolean tecla = false;
    int contadorTiempo = 0;
    FileWriter fw;
    boolean teclaQ = true;
    boolean teclaEsc = false;
}
