package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.math.BigInteger;
import java.rmi.server.ExportException;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.*;

public class EncodingForm extends JFrame {

    private JTextField fileField;
    private JTextField keyFileField;
    private JTextField keyOutputDirectoryField;
    private JTextArea resultArea;


    public EncodingForm() {
        Font font = new Font("Noto", Font.PLAIN, 22);
        setTitle("Cipher Form");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel keyPanel = new JPanel();
        keyPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        keyOutputDirectoryField = new JTextField(20);
        keyOutputDirectoryField.setFont(font);
        keyOutputDirectoryField.setEditable(false);
        keyPanel.add(keyOutputDirectoryField);

        JButton chooseKeyFileButton = new JButton("Choose key file");
        chooseKeyFileButton.setFont(font);
        keyPanel.add(chooseKeyFileButton);
        chooseKeyFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showDialog(EncodingForm.this, "Open");
                if (result == JFileChooser.APPROVE_OPTION) {
                    keyOutputDirectoryField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });



        JPanel generateKeyField = new JPanel();
        generateKeyField.setLayout(new FlowLayout(FlowLayout.LEFT));

        keyFileField = new JTextField(20);
        keyFileField.setFont(font);
        keyFileField.setEditable(false);
        generateKeyField.add(keyFileField);

        JButton chooseOutputDirectoryButton = new JButton("Choose output directory");
        chooseOutputDirectoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showDialog(EncodingForm.this, "Open");
                if (result == JFileChooser.APPROVE_OPTION) {
                    keyFileField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        chooseOutputDirectoryButton.setFont(font);
        generateKeyField.add(chooseOutputDirectoryButton);
        chooseOutputDirectoryButton.addActionListener((event) -> {
        });

        JButton generateKeyButton = new JButton("Generate public and private keys");
        generateKeyButton.setFont(font);
        generateKeyField.add(generateKeyButton);
        generateKeyButton.addActionListener((event) -> {
            RabinNumbersGenerator rabinNumbersGenerator = new RabinNumbersGenerator();
            BigInteger[] key = rabinNumbersGenerator.generateKey(1024);
            BigInteger n = key[0].multiply(key[1]);
            BigInteger p = key[0];
            BigInteger q = key[1];
            BigInteger b = key[2];
            if (keyFileField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Dir is empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            File privateKeyFile = new File(keyFileField.getText() + "/key.private");
            File publicKeyFile = new File(keyFileField.getText() + "/key.public");
            try {
                privateKeyFile.createNewFile();
                publicKeyFile.createNewFile();
                PrintWriter printWriter = new PrintWriter(new FileOutputStream(privateKeyFile));
                printWriter.println(p);
                printWriter.println(q);
                printWriter.println(b);
                printWriter.close();
                printWriter = new PrintWriter(new FileOutputStream(publicKeyFile));
                printWriter.println(n);
                printWriter.println(b);
                printWriter.close();
            } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cannot create file here", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        });

        mainPanel.add(generateKeyField);
        mainPanel.add(keyPanel);


        JPanel filePanel = new JPanel();
        filePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel filePanelLabel = new JLabel("File to encode/decode: ");
        filePanelLabel.setFont(font);
        filePanel.add(filePanelLabel);
        filePanel.setFont(font);
        fileField = new JTextField(20);
        fileField.setFont(font);
        fileField.setEditable(false);
        filePanel.add(fileField);
        JButton fileButton = new JButton("Choose File");
        fileButton.setFont(font);
        fileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(EncodingForm.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    fileField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        filePanel.add(fileButton);
        mainPanel.add(filePanel);

        JPanel resultPanel = new JPanel();
        JLabel resultLabel = new JLabel("result:   ");
        resultLabel.setFont(font);
        resultPanel.setLayout(new BorderLayout());
        resultPanel.add(resultLabel, BorderLayout.WEST);
        resultArea = new JTextArea();
        resultArea.setFont(font);
        resultArea.setEditable(false);
        resultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        mainPanel.add(resultPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton encryptButton = new JButton("Encode");
        encryptButton.setFont(font);

        ActionListener encodeActionListener = (event) -> {
            File file = new File(fileField.getText());
            File resultFile = new File(file.getParentFile() + "/" + file.getName() + "_result");
            try (FileInputStream fileInputStream = new FileInputStream(file);
                 FileInputStream keyFileInputStream = new FileInputStream(keyOutputDirectoryField.getText())) {

                resultFile.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(resultFile);

                Scanner keyIn = new Scanner(keyFileInputStream);
                BigInteger n = keyIn.nextBigInteger();
                BigInteger b = keyIn.nextBigInteger();

                Encoder encoder = new RabinCryptosystemEncoder(n, b);
                BigInteger[] cipher = encoder.encode(fileInputStream.readAllBytes());
                StringBuilder resultSb = new StringBuilder();
                for (int i = 0; i < cipher.length; i++) {
                    if (i != cipher.length - 1)
                        resultSb.append(cipher[i] + " ");
                    else
                        resultSb.append(cipher[i]);
                }

                String result = resultSb.toString();
                PrintWriter printWriter = new PrintWriter(fileOutputStream);
                printWriter.print(result);
                printWriter.close();

                setTextWithLineBreaks(resultArea, result, 50);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, "File not found", "ERROR", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Key file parsing error", "ERROR", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        };

        encryptButton.addActionListener(encodeActionListener);

        ActionListener decodeActionListener = (event) -> {
            File file = new File(fileField.getText());
                try (FileInputStream fileInputStream = new FileInputStream(file);
                     FileInputStream keyFileInputStream = new FileInputStream(keyOutputDirectoryField.getText())) {
                File resultFile = new File(file.getParentFile() + "/" + file.getName() + "_result");
                resultFile.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(resultFile);

                Scanner keyIn = new Scanner(keyFileInputStream);
                BigInteger p = keyIn.nextBigInteger();
                BigInteger q = keyIn.nextBigInteger();
                BigInteger b = keyIn.nextBigInteger();

                Scanner fileIn = new Scanner(fileInputStream);
                Decoder decoder = new RabinCryptosystemDecoder(p, q, b);
                ArrayList<BigInteger> cipher = new ArrayList<>();
                while (fileIn.hasNextBigInteger()) {
                    cipher.add(fileIn.nextBigInteger());
                }

                byte[] result = decoder.decode(cipher.toArray(new BigInteger[]{}));
                fileOutputStream.write(result);
                String strResult = new String(result);

                setTextWithLineBreaks(resultArea, strResult, 80);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, "File not found", "ERROR", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Key file parsing error", "ERROR", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        };

        JButton decryptButton = new JButton("Decode");
        decryptButton.setFont(font);

        decryptButton.addActionListener(decodeActionListener);

        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        mainPanel.add(buttonPanel);

        add(mainPanel);
    }

    public void setTextWithLineBreaks(JTextArea textArea, String text, Integer offset) {
        textArea.setText("");
        for (int i = 0; i < text.length() / offset; i++)
            textArea.append(text.substring(i * offset, offset * (1 + i)) + "\n");

        int lastAppended = text.length() - text.length() % offset;
        textArea.append(text.substring(lastAppended, text.length()));
    }

    public static void main(String[] args) {
        EncodingForm form = new EncodingForm();
        form.setVisible(true);
    }
}
