package screens;

import game.UI.GameView;
import game.logic.GameEngine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.HashMap;
import java.util.Map;

public class OptionScreen {
    private boolean isGamePaused;
    private GameView gameView;
    private GameEngine gameEngine;


    public OptionScreen(GameView gameView, GameEngine gameEngine) {
        this.isGamePaused = false;
        this.gameView = gameView;
        this.gameEngine = gameEngine;
        setupPauseResumeKeyBinding();

    }

    private void setupPauseResumeKeyBinding() {
        gameView.getFrame().getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "pauseGame");
        gameView.getFrame().getRootPane().getActionMap().put("pauseGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isGamePaused) {
                    resumeGame();
                } else {
                    pauseGame();
                    displayOptionMenu();
                }
            }
        });
    }

    private void displayOptionMenu() {
        BufferedImage screenshot = new BufferedImage(gameView.getWidth(), gameView.getHeight() - 23, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = screenshot.createGraphics();
        gameView.paint(g2d);

        BufferedImage blurredScreenshot = blurImage(screenshot);
        ImageIcon backgroundImageIcon = new ImageIcon(blurredScreenshot);

        JLabel backgroundLabel = new JLabel(backgroundImageIcon);

        JDialog dialog = new JDialog(gameView.getFrame(), false);
        dialog.setUndecorated(false);
        dialog.setBackground(new Color(0, 0, 0, 0)); // Make the JDialog background transparent

        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 0));
        panel.setOpaque(false);
        JButton resumeButton = createButton("res/optionScreen/resumeButtonNormal.png", "res/optionScreen/resumeButtonHover.png", dialog, e -> resumeGame(), 450, 100);
        JButton option1Button = createButton("res/optionScreen/optionButtonNormal.png", "res/optionScreen/optionButtonHover.png", dialog, e -> handleOption1(),450, 100);
        JButton option2Button = createButton("res/optionScreen/quitGameButtonNormal.png", "res/optionScreen/quitGameButtonHover.png", dialog, e -> quitGame(),450, 100);
        panel.add(resumeButton);
        panel.add(option1Button);
        panel.add(option2Button);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(gameView.getWidth(), gameView.getHeight()));
        backgroundLabel.setBounds(0, 0, gameView.getWidth(), gameView.getHeight());
        int panelWidth = 450;
        int panelHeight = 300;
        int panelX = (gameView.getWidth() - panelWidth) / 2;
        int panelY = (gameView.getHeight() - panelHeight) / 2;
        panel.setBounds(panelX, panelY, panelWidth, panelHeight);
        layeredPane.add(backgroundLabel, Integer.valueOf(1));
        layeredPane.add(panel, Integer.valueOf(2));

        dialog.add(layeredPane);
        dialog.pack();
        dialog.setLocationRelativeTo(gameView.getFrame());
        dialog.setVisible(true);
    }

    private JButton createButton(String normalImagePath, String hoverImagePath, JDialog dialog, ActionListener actionListener, int scaledWidth, int scaledHeight) {

        ImageIcon normalIconOriginal = new ImageIcon(normalImagePath);
        ImageIcon hoverIconOriginal = new ImageIcon(hoverImagePath);

        ImageIcon normalIcon = new ImageIcon(normalIconOriginal.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_DEFAULT));
        ImageIcon hoverIcon = new ImageIcon(hoverIconOriginal.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_DEFAULT));

        JButton button = new JButton(normalIcon) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isRollover()) {
                    g.drawImage(hoverIcon.getImage(), 0, 0, scaledWidth, scaledHeight, this);
                } else {
                    g.drawImage(normalIcon.getImage(), 0, 0, scaledWidth, scaledHeight, this);
                }
            }
        };
        button.setRolloverIcon(hoverIcon);
        button.setHorizontalAlignment(JButton.CENTER);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(scaledWidth, scaledHeight));
        button.addActionListener(e -> {
            actionListener.actionPerformed(e);
            dialog.dispose();
        });

        return button;
    }

    private BufferedImage blurImage(BufferedImage image) {
        float ninth = 1.0f/9.0f;
        float[] blurKernel = {
                ninth, ninth, ninth,
                ninth, ninth, ninth,
                ninth, ninth, ninth
        };

        Map<RenderingHints.Key, Object> map = new HashMap<>();
        map.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        map.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        map.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        RenderingHints hints = new RenderingHints(map);
        BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, blurKernel), ConvolveOp.EDGE_NO_OP, hints);
        return op.filter(image, null);
    }

    private void handleOption1() {
        // Handle option 1
    }

    private void quitGame() {
        System.exit(0);
    }

    private void pauseGame() {
        isGamePaused = true;
        gameEngine.pause();
    }

    private void resumeGame() {
        isGamePaused = false;
        gameEngine.resume();
    }

    public boolean isGamePaused() {
        return isGamePaused;
    }
}
