import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartMenu extends JFrame implements ActionListener {
    private JButton playButton;
    private Image backgroundImage;
    private Image playImage;

    public StartMenu() {
        // set ukuran dan pengaturan frame
        setTitle("Flappy Bird - Start Menu");
        setSize(360, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // load images
        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        playImage = new ImageIcon(getClass().getResource("assets/play.png")).getImage();

        // layout manual (biar bebas atur posisi)
        setLayout(null);

        // buat button pakai gambar play
        playButton = new JButton();
        playButton.setIcon(new ImageIcon(playImage));
        playButton.setBounds(110, 270, 140, 100); // posisikan tombol di tengah
        playButton.setBorderPainted(false);
        playButton.setContentAreaFilled(false);
        playButton.setFocusPainted(false);
        playButton.setOpaque(false);
        playButton.addActionListener(this);

        add(playButton);

        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // gambar background dulu
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            // buka FlappyBird
            JFrame gameFrame = new JFrame("Flappy Bird");
            gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameFrame.setSize(360, 640);
            gameFrame.setLocationRelativeTo(null);
            gameFrame.setResizable(false);

            FlappyBird flappyBird = new FlappyBird();
            gameFrame.add(flappyBird);
            gameFrame.pack();
            gameFrame.setVisible(true);

            // tutup StartMenu
            dispose();
        }
    }
}
