/**
 * Pauvocoder - SAE 1&2
 * Mehdi LAFAY - Groupe S1.B2
 * Aylan HADDOUCHI -Groupe S1.B1
 * Compilation : $ javac PauvocoderInterface.java
 * Exécution : $ java PauvocoderInterface
 * Code Vérifié et Corrigé avec SonarQube
 */

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PauvocoderInterface {
    private static String inputPath;
    private static boolean fileSelected = false;
    private static JFileChooser fileChooser = new JFileChooser();
    private static double[] inputWav;
    private static double freqScale = 1.0;
    private static double[] newPitchWav;
    private static double[] outputWav;
    private static boolean playWav = false;
    private static int currentSample = 0;
    private static JLabel statusLabel;
    private static double echoGain = 0.5; // Gain par défaut pour l'écho
    public static final String ECHO_GAIN_LABEL = "Echo Gain: ";
    public static final String FREQUENCE_LABEL = "Fréquence: ";


    public static void main(String[] args) {
        setupGUI();
    }


    /**
     * Initialize buttons and GUI
     * @return Container + buttons
     */
    private static void setupGUI() {
        JFrame frame = createMainFrame();
        JPanel mainPanel = createMainPanel(frame);

        createStatusLabel(frame);
        createFilePanel(mainPanel);
        createFreqPanel(mainPanel);
        createResamplePanel(mainPanel);
        createOptionsPanel(mainPanel);
        createEchoGainPanel(mainPanel);
        createControlPanel(mainPanel);

        frame.setVisible(true);
        setupTimer();
    }

    private static JFrame createMainFrame() {
        JFrame frame = new JFrame("Pauvocoder");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(900, 500);
        frame.setLayout(new BorderLayout());
        return frame;
    }

    private static JPanel createMainPanel(JFrame frame) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        frame.add(mainPanel, BorderLayout.CENTER);
        return mainPanel;
    }

    private static void createStatusLabel(JFrame frame) {
        statusLabel = new JLabel("Commencez par sélectionner un fichier", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        frame.add(statusLabel, BorderLayout.SOUTH);
    }

    private static void createFilePanel(JPanel mainPanel) {
        JPanel filePanel = new JPanel();
        JButton selectFileButton = new JButton("Sélectionner un fichier");
        selectFileButton.addActionListener(e -> selectFile());
        filePanel.add(selectFileButton);
        mainPanel.add(filePanel);
    }

    private static void createFreqPanel(JPanel mainPanel) {
        JPanel freqPanel = new JPanel();
        JLabel freqLabel = new JLabel(FREQUENCE_LABEL + freqScale);

        JButton reduceFreqButton = new JButton("-");
        reduceFreqButton.addActionListener(e -> {
            reduceFreq();
            freqLabel.setText(FREQUENCE_LABEL + freqScale);
        });

        JButton addFreqButton = new JButton("+");
        addFreqButton.addActionListener(e -> {
            addFreq();
            freqLabel.setText(FREQUENCE_LABEL + freqScale);
        });

        freqPanel.add(reduceFreqButton);
        freqPanel.add(freqLabel);
        freqPanel.add(addFreqButton);
        mainPanel.add(freqPanel);
    }

    private static void createResamplePanel(JPanel mainPanel) {
        JPanel resamplePanel = new JPanel();
        JButton resampleButton = new JButton("Appliquer Resampling");
        resampleButton.addActionListener(e -> {
            if (fileSelected) {
                newPitchWav = Pauvocoder.resample(inputWav, freqScale);
                outputWav = newPitchWav;
                statusLabel.setText("Resampling appliqué à " + freqScale);
            }
        });
        resamplePanel.add(resampleButton);
        mainPanel.add(resamplePanel);
    }

    private static void createOptionsPanel(JPanel mainPanel) {
        JPanel optionsPanel = new JPanel();
        JButton simpleButton = new JButton("Simple");
        simpleButton.addActionListener(e -> {
            if (fileSelected) {
                outputWav = Pauvocoder.vocodeSimple(newPitchWav, 1.0 / freqScale);
                selectOption("Simple");
            }
        });

        JButton simpleOverButton = new JButton("Simple Over");
        simpleOverButton.addActionListener(e -> {
            if (fileSelected) {
                outputWav = Pauvocoder.vocodeSimpleOver(newPitchWav, 1.0 / freqScale);
                selectOption("Simple Over");
            }
        });

        JButton overCrossButton = new JButton("Over Cross");
        overCrossButton.addActionListener(e -> {
            if (fileSelected) {
                outputWav = Pauvocoder.vocodeSimpleOverCross(newPitchWav, 1.0 / freqScale);
                selectOption("Over Cross");
            }
        });

        JButton echoButton = new JButton("Echo");
        echoButton.addActionListener(e -> {
            if (fileSelected) {
                outputWav = Pauvocoder.echo(newPitchWav, 100, echoGain, 44100);
                selectOption("Echo");
            }
        });

        optionsPanel.add(simpleButton);
        optionsPanel.add(simpleOverButton);
        optionsPanel.add(overCrossButton);
        optionsPanel.add(echoButton);
        mainPanel.add(optionsPanel);
    }

    private static void createEchoGainPanel(JPanel mainPanel) {
        JPanel echoGainPanel = new JPanel();
        JLabel echoGainLabel = new JLabel(ECHO_GAIN_LABEL + echoGain);

        JButton reduceEchoGainButton = new JButton("-");
        reduceEchoGainButton.addActionListener(e -> {
            updateEchoGain(echoGain - 0.1);
            echoGainLabel.setText(ECHO_GAIN_LABEL + echoGain);
        });

        JButton addEchoGainButton = new JButton("+");
        addEchoGainButton.addActionListener(e -> {
            updateEchoGain(echoGain + 0.1);
            echoGainLabel.setText(ECHO_GAIN_LABEL + echoGain);
        });

        echoGainPanel.add(reduceEchoGainButton);
        echoGainPanel.add(echoGainLabel);
        echoGainPanel.add(addEchoGainButton);
        mainPanel.add(echoGainPanel);
    }

    private static void createControlPanel(JPanel mainPanel) {
        JPanel controlPanel = new JPanel();
        JButton playButton = new JButton("Play");
        playButton.addActionListener(e -> {
            if (outputWav != null && outputWav.length > 0) {
                Pauvocoder.displayWaveform(outputWav);
                new Thread(() -> StdAudio.play(outputWav)).start();
            } else {
                statusLabel.setText("Aucun fichier ou donnée audio à lire !");
            }
        });

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> reset());

        JButton saveButton = new JButton("Sauvegarder");
        saveButton.addActionListener(e -> {
            if (outputWav != null && outputWav.length > 0) {
                saveFile();
            } else {
                statusLabel.setText("Aucun fichier ou donnée audio à sauvegarder !");
            }
        });

        controlPanel.add(playButton);
        controlPanel.add(resetButton);
        controlPanel.add(saveButton);
        mainPanel.add(controlPanel);
    }

    private static void setupTimer() {
        new Timer(10, e -> {
            if (playWav) {
                if (currentSample < outputWav.length) {
                    StdAudio.play(outputWav[currentSample]);
                    currentSample++;
                } else {
                    currentSample = 0;
                    playWav = false;
                }
            }
        }).start();
    }

    private static void addFreq() {
        if (freqScale < 2.0) {
            freqScale = Math.round((freqScale + 0.1) * 10) / 10.0;
        }
    }

    private static void reduceFreq() {
        if (freqScale > 0.5) {
            freqScale = Math.round((freqScale - 0.1) * 10) / 10.0;
        }
    }

    private static void updateEchoGain(double newGain) {
        if (newGain >= 0.1 && newGain <= 1.0) {
            echoGain = Math.round(newGain * 10) / 10.0;
        }
    }

    /**
     * Select a file
     * @return statusLabel + selectedFile
     */
    private static void selectFile() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("WAV Files", "wav");
        fileChooser.setFileFilter(filter);
        int response = fileChooser.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            inputPath = fileChooser.getSelectedFile().getAbsolutePath();
            inputWav = StdAudio.read(inputPath);
            outputWav = inputWav;
            fileSelected = true;
            statusLabel.setText("Fichier sélectionné : " + fileChooser.getSelectedFile().getName());
            // Appliquez le resampling immédiatement après la sélection du fichier
            newPitchWav = Pauvocoder.resample(inputWav, freqScale);
        }
    }

    /**
     * Reset the options
     * @return Initial status of options
     */
    private static void reset() {
        inputPath = "";
        fileSelected = false;
        inputWav = null;
        freqScale = 1.0;
        newPitchWav = null;
        outputWav = null;
        playWav = false;
        currentSample = 0;
        echoGain = 0.5;
        statusLabel.setText("Commencez par sélectionner un fichier");
    }

    /**
     * Select an option to apply on the wav
     * @param option
     * @return statusLabel
     */
    private static void selectOption(String option) {
        statusLabel.setText("Méthode sélectionnée : " + option);
    }

    /**
     * Save the file after modifications
     * @return statusLabel + savedFile
     */
    private static void saveFile() {
        // Vérifiez si un fichier a été sélectionné avant de tenter de sauvegarder
        if (!fileSelected || inputPath == null || inputPath.isEmpty()) {
            statusLabel.setText("Aucun fichier sélectionné pour la sauvegarde !");
            return;
        }

        // Extraire le nom de base du fichier d'entrée (sans l'extension .wav)
        String outPutFile = inputPath.split("\\.")[0] + "_" + freqScale + "_";

        // Appliquer le resampling et sauvegarder le fichier
        double[] newPitchWav = Pauvocoder.resample(inputWav, freqScale);
        StdAudio.save(outPutFile + "Resampled.wav", newPitchWav);
        statusLabel.setText("Fichier Resampled sauvegardé sous : " + outPutFile + "Resampled.wav");

        // Appliquer la méthode vocodeSimple et sauvegarder le fichier
        double[] outputWav = Pauvocoder.vocodeSimple(newPitchWav, 1.0 / freqScale);
        StdAudio.save(outPutFile + "Simple.wav", outputWav);
        statusLabel.setText("Fichier Simple sauvegardé sous : " + outPutFile + "Simple.wav");

        // Appliquer la méthode vocodeSimpleOver et sauvegarder le fichier
        outputWav = Pauvocoder.vocodeSimpleOver(newPitchWav, 1.0 / freqScale);
        StdAudio.save(outPutFile + "SimpleOver.wav", outputWav);
        statusLabel.setText("Fichier SimpleOver sauvegardé sous : " + outPutFile + "SimpleOver.wav");

        // Appliquer l'écho et sauvegarder le fichier
        outputWav = Pauvocoder.echo(outputWav, 100, echoGain, 44100); // Ajustez le sampleRate si nécessaire
        StdAudio.save(outPutFile + "SimpleOverCrossEcho.wav", outputWav);
        statusLabel.setText("Fichier SimpleOverCrossEcho sauvegardé sous : " + outPutFile + "SimpleOverCrossEcho.wav");
    }


}
