import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int frameWidth = 360;
    int frameHeight = 640;

    // image attribute
    Image backgroundImage;
    Image birdImage;
    Image lowerPipeImage;
    Image upperPipeImage;

    // player
    int playerStartPosX = frameWidth / 8;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;
    Player player;

    // pipe attributes
    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    ArrayList<Pipe> pipes;

    Timer gameLoop;
    Timer pipesCooldown;
    int gravity = 1;

    // game state
    boolean gameOver = false;

    // score
    double score = 0;
    JLabel scoreLabel;

    // constructor
    public FlappyBird(){
        setPreferredSize(new Dimension(360, 640));
        setBackground(Color.blue);
        setFocusable(true); // jangan lupa ini biar keyListener jalan
        addKeyListener(this);

        // load images
        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();

        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);
        pipes = new ArrayList<Pipe>();

        pipesCooldown = new Timer(4500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        pipesCooldown.start();

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();

        // score label
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(Color.white);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(scoreLabel);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.drawImage(backgroundImage, 0, 0, frameWidth, frameHeight, null);
        g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);

        for(Pipe pipe : pipes){
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
        }

        if(gameOver){
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over!", frameWidth / 4 - 20, frameHeight / 2);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press R to Restart", frameWidth / 4, frameHeight / 2 + 40);
        }
    }

    public void move(){
        if (!gameOver){
            player.setVelocityY(player.getVelocityY() + gravity);
            player.setPosY(player.getPosY() + player.getVelocityY());
            player.setPosY(Math.max(player.getPosY(), 0));

            for(Pipe pipe : pipes){
                pipe.setPosX(pipe.getPosX() + pipe.getVelocityX());

                // cek nabrak
                if (player.getPosX() < pipe.getPosX() + pipe.getWidth() &&
                        player.getPosX() + player.getWidth() > pipe.getPosX() &&
                        player.getPosY() < pipe.getPosY() + pipe.getHeight() &&
                        player.getPosY() + player.getHeight() > pipe.getPosY()) {
                    gameOver = true;
                }

                // cek kalau player berhasil lewat pipa (nambah score)
                if (!pipe.isPassed() && pipe.getPosX() + pipe.getWidth() < player.getPosX()) {
                    pipe.setPassed(true);
                    score += 0.5; // tambah 0.5 ke skor
                    scoreLabel.setText("Score: " + (int)Math.round(score)); // tampilkan skor dengan angka desimal
                }
            }

            // cek jatuh
            if(player.getPosY() > frameHeight){
                gameOver = true;
            }
        }
    }

    public void placePipes(){
        if (!gameOver) {
            int randomPosY = (int) (pipeStartPosY - pipeHeight/4 - Math.random() * (pipeHeight/2));
            int openingSpace = frameHeight / 4;

            Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
            pipes.add(upperPipe);

            Pipe lowerPipe = new Pipe(pipeStartPosX, (randomPosY + pipeHeight + openingSpace), pipeWidth, pipeHeight, lowerPipeImage);
            pipes.add(lowerPipe);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e){

    }

    @Override
    public void keyPressed(KeyEvent e){
        if(!gameOver){
            if(e.getKeyCode() == KeyEvent.VK_SPACE){
                player.setVelocityY(-10);
            }
        } else {
            if(e.getKeyCode() == KeyEvent.VK_R){
                restartGame();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e){

    }

    public void restartGame(){
        player.setPosX(playerStartPosX);
        player.setPosY(playerStartPosY);
        player.setVelocityY(0);

        pipes.clear();
        score = 0;
        scoreLabel.setText("Score: 0");
        gameOver = false;
    }
}
