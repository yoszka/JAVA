package jokii;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextPane;
import javax.swing.JComboBox;
import com.thoughtworks.xstream.XStream;
import java.awt.event.ActionListener;
import java.awt.Color;

import jokii.BackgroundConsoleWorker.BackgroundConsoleWorkerExecutor;

public class Main {

    private JFrame              mFrame;
    private JTextField          mEmailTextField;
    private JTextField          mOtaPinTextField;
    private JTextPane           mConsoleOutputJTextPane;
    private JComboBox<String>   mHistoryComboBox;
    private JTextField          mDescriptionTextField;
    private JButton             mBtnSave;
    private JTextField          mAdbPathTextField;
    private JButton             mCheckAdbBtn;

    private ProvisioningData    mStorageData;
    private XStream             mXstream                = new XStream();


    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main window = new Main();
                    window.mFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * Create the application.
     */
    public Main() {
        initialize();
    }


    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        mStorageData = StorageUtils.readStorageData();
        ConfigItem configData = StorageUtils.readConfigData();

        mFrame = new JFrame();
        mFrame.getContentPane().setBackground(Color.RED);
        mFrame.setBackground(Color.RED);
        mFrame.setForeground(Color.RED);
        mFrame.setTitle("Provisioning Tool");
        mFrame.setResizable(false);
        mFrame.setBounds(100, 100, 718, 430);
        mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mFrame.getContentPane().setLayout(null);
        setApplicationIcon();

        JButton btnProvision = new JButton("Provision");
        btnProvision.setBackground(Color.ORANGE);
        btnProvision.addActionListener(mProvisionActionListener);
        btnProvision.setBounds(10, 357, 89, 23);
        mFrame.getContentPane().add(btnProvision);

        JButton btnExit = new JButton("Exit");
        btnExit.setBackground(Color.ORANGE);
        btnExit.addActionListener(mExitActionListener);
        btnExit.setBounds(109, 357, 89, 23);
        mFrame.getContentPane().add(btnExit);

        mEmailTextField = new JTextField();
        mEmailTextField.setBackground(Color.WHITE);
        mEmailTextField.setBounds(10, 25, 234, 20);
        mFrame.getContentPane().add(mEmailTextField);
        mEmailTextField.setColumns(10);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setBounds(10, 11, 46, 14);
        mFrame.getContentPane().add(lblEmail);

        mOtaPinTextField = new JTextField();
        mOtaPinTextField.setBounds(256, 25, 250, 20);
        mFrame.getContentPane().add(mOtaPinTextField);
        mOtaPinTextField.setColumns(10);

        JLabel lblOta = new JLabel("OTA PIN");
        lblOta.setBounds(256, 11, 46, 14);
        mFrame.getContentPane().add(lblOta);

        mConsoleOutputJTextPane = new JTextPane();
        mConsoleOutputJTextPane.setForeground(Color.BLACK);
        mConsoleOutputJTextPane.setBackground(Color.WHITE);
        mConsoleOutputJTextPane.setEditable(false);
        mConsoleOutputJTextPane.setBounds(10, 141, 692, 173);
        mFrame.getContentPane().add(mConsoleOutputJTextPane);

        JLabel lblConsoleOutput = new JLabel("Console output");
        lblConsoleOutput.setBounds(10, 124, 89, 14);
        mFrame.getContentPane().add(lblConsoleOutput);

        mHistoryComboBox = new JComboBox<String>();
        mHistoryComboBox.setBackground(Color.ORANGE);
        mHistoryComboBox.setBounds(518, 54, 181, 23);

        fillComboBox();

        mHistoryComboBox.addItemListener(mComboItemListener);
        mFrame.getContentPane().add(mHistoryComboBox);

        mDescriptionTextField = new JTextField();
        mDescriptionTextField.setBounds(518, 25, 181, 20);
        mFrame.getContentPane().add(mDescriptionTextField);
        mDescriptionTextField.setColumns(10);

        JLabel lblDescription = new JLabel("Description");
        lblDescription.setBounds(518, 11, 74, 14);
        mFrame.getContentPane().add(lblDescription);

        mBtnSave = new JButton("Save");
        mBtnSave.setBackground(Color.ORANGE);
        mBtnSave.addActionListener(mSaveActionListener);
        mBtnSave.setBounds(256, 54, 83, 23);
        mFrame.getContentPane().add(mBtnSave);

        JButton btnReplace = new JButton("Replace");
        btnReplace.setBackground(Color.ORANGE);
        btnReplace.addActionListener(mReplaceActionListener);
        btnReplace.setBounds(341, 54, 83, 23);
        mFrame.getContentPane().add(btnReplace);

        JButton mRemoveBtn = new JButton("Remove");
        mRemoveBtn.setBackground(Color.ORANGE);
        mRemoveBtn.addActionListener(mRemoveActionListener);
        mRemoveBtn.setBounds(426, 54, 83, 23);
        mFrame.getContentPane().add(mRemoveBtn);

        JButton button = new JButton("?");
        button.setBackground(Color.ORANGE);
        button.addActionListener(mAboutActionListener);
        button.setBounds(696, 378, 16, 23);
        mFrame.getContentPane().add(button);

        JLabel lblAdbPath = new JLabel("ADB path");
        lblAdbPath.setBounds(10, 329, 77, 14);
        mFrame.getContentPane().add(lblAdbPath);

        mAdbPathTextField = new JTextField();
        mAdbPathTextField.setToolTipText("Can be empty if ADB on system PATH");
        mAdbPathTextField.setBounds(71, 325, 508, 20);
        mAdbPathTextField.setColumns(10);
        String configuredAdbPath = (configData != null) ? configData.getAdbPath() : "";
        mAdbPathTextField.setText(configuredAdbPath);
        mFrame.getContentPane().add(mAdbPathTextField);
        
        mCheckAdbBtn = new JButton("Update/Check");
        mCheckAdbBtn.setBounds(589, 325, 113, 21);
        mCheckAdbBtn.addActionListener(mCheckAdbActionListener);
        mFrame.getContentPane().add(mCheckAdbBtn);
        

        if(mStorageData != null && !mStorageData.getList().isEmpty()) {
            ProvisioningItem firstElement = mStorageData.getList().get(0);
            fillDisplayFields(firstElement.email, firstElement.otaPin, firstElement.description);
        }
    }


    private ItemListener mComboItemListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent arg0) {
            int selectedItemIndex = mHistoryComboBox.getSelectedIndex();
            if(selectedItemIndex != -1) {
                ProvisioningItem chosenConfiguration = mStorageData.getList().get(selectedItemIndex);

                mConsoleOutputJTextPane.setText("Selected:\n" + chosenConfiguration.description);

                fillDisplayFields(chosenConfiguration.email, chosenConfiguration.otaPin, chosenConfiguration.description);
            }
        }
        
        
    };


    private ActionListener mProvisionActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            prepareInputTexts();
            try {
                String email  = mEmailTextField.getText();
                String otaPin = mOtaPinTextField.getText();
    
                if(validateProvisioningData(email, otaPin)) {
                    executeProvision(email, otaPin);
                }
    
            } catch (Exception error) {
                error.printStackTrace();
            }
        }
    };


    private ActionListener mExitActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    };
    private ActionListener mAboutActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
            showAboutDialog();
        }
    };


    private ActionListener mSaveActionListener = new ActionListener() {
    
        public void actionPerformed(ActionEvent e) {
            prepareInputTexts();
    
            if(trySaveCurrent()) {
                showMessageDialog("Saved");
            }
        }
    };
    private ActionListener mCheckAdbActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            updateAdbPath();
            checkAdbPath();
        }

        private void updateAdbPath() {
            ConfigItem configItem = new ConfigItem();
            configItem.setAdbPath(getAdbCommand());
            StorageUtils.updateConfigData(configItem);
        }
    };
    private ActionListener mRemoveActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
            int currentItem = mHistoryComboBox.getSelectedIndex();
    
            if(currentItem != -1) {
                String itemDescription = mStorageData.getList().get(currentItem).description;
                boolean result = showConfirmDialog("Are you sure you want to remove\n\n\"" + itemDescription + "\"\n\n");
    
                if(result) {
                    mHistoryComboBox.removeItemAt(currentItem);
    
                    mStorageData.getList().remove(currentItem);
    
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(ProjectConst.DATA_FILE_NAME);
                        mXstream.toXML(mStorageData, fos);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                        showErrorDialog("Storage access denny");
                    } finally {
                        try {
                            if(fos != null) {
                                fos.close();
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
    
                    fillInitialDisplayFields();
                }
            }
        }
    };
    private ActionListener mReplaceActionListener = new ActionListener() {
    
        public void actionPerformed(ActionEvent arg0) {
            int currentItem = mHistoryComboBox.getSelectedIndex();
            prepareInputTexts();
    
            if(currentItem != -1) {
    
                if(validateFields()) {
    
                    if(isCurrentItemAlreadyExist()) {
                        showErrorDialog("Entry already exist");
                    } else {
                        String itemDescription = mStorageData.getList().get(currentItem).description;
                        boolean result = showConfirmDialog("Are you sure you want to replace with\n\n\"" + itemDescription + "\"\n\n");
    
                        if(result && trySaveCurrent()) {
                            mHistoryComboBox.removeItemAt(currentItem);
    
                            mStorageData.getList().remove(currentItem);
    
                            FileOutputStream fos = null;
                            try {
                                fos = new FileOutputStream(ProjectConst.DATA_FILE_NAME);
                                mXstream.toXML(mStorageData, fos);
                            } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                                showErrorDialog("Storage access denny");
                            } finally {
                                try {
                                    if(fos != null) {
                                        fos.close();
                                    }
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                            setLastItemSelected();
                        }
                    }
    
                }
            }
        }
    };

    private void fillDisplayFields(String email, String otaPin, String description) {
        mEmailTextField.setText(email);
        mOtaPinTextField.setText(otaPin);
        mDescriptionTextField.setText(description);
    }


    private void fillInitialDisplayFields() {
        if(mStorageData != null && !mStorageData.getList().isEmpty()) {
            mHistoryComboBox.setSelectedIndex(0);
            ProvisioningItem firstElement = mStorageData.getList().get(0);
            fillDisplayFields(firstElement.email, firstElement.otaPin, firstElement.description);
        }
    }


    private void fillComboBox() {
        if(mStorageData != null) {
            if(mHistoryComboBox.getItemCount() > 0) {
                mHistoryComboBox.removeAllItems();
            }

            for(ProvisioningItem item : mStorageData.getList()) {
                mHistoryComboBox.addItem(item.description);
            }
        }
    }


    private void showInfoDialog(String title, String body) {
        JOptionPane.showMessageDialog(mFrame,
                body,
                title,
                JOptionPane.INFORMATION_MESSAGE);
    }


    private void showAboutDialog(){
        final int OK_OPTION              = 0;
        final int XSTREAM_LICENSE_OPTION = 1;

        Object[] options = {"OK",
                            "XStream license"};
        int selectedOption = JOptionPane.showOptionDialog(mFrame,
                        "Author: Tomasz Jokiel\n" +
                        "version: " + ProjectConst.APP_VERSION,
        "Application info",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.INFORMATION_MESSAGE,
        null,                   //do not use a custom Icon
        options,                //the titles of buttons
        options[OK_OPTION]);    //default button title
        
        if(selectedOption == XSTREAM_LICENSE_OPTION) {
            showInfoDialog("XStream license", FileUtils.readFileContentFromResource(ProjectConst.XSTREAM_LICENSE_FILE_NAME));
        }
    }


    private void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(mFrame, message);
    }


    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(mFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }


    private boolean showConfirmDialog(String message) {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(null, message , "Confirm", dialogButton);
        return (dialogResult == 0);
    }


    private void executeProvision(final String email, final String otaPin) throws IOException {

        final String adbPath = getAdbCommand();

        new BackgroundConsoleWorker(new BackgroundConsoleWorkerExecutorLinePrinter() {
            @Override
            public void doInBackground(BackgroundConsoleWorker backgroundConsoleWorker) {
                try {
                    backgroundConsoleWorker.runConsoleCommand(adbPath + " shell input text " + email);
                    backgroundConsoleWorker.runConsoleCommand(adbPath + " shell input keyevent 66");

                    String[] otaPinArray = otaPin.split("-");
                    backgroundConsoleWorker.runConsoleCommand(adbPath + " shell input text " + otaPinArray[0]);
                    Thread.sleep(1000);
                    backgroundConsoleWorker.runConsoleCommand(adbPath + " shell input text " + otaPinArray[1]);
                    Thread.sleep(1000);
                    backgroundConsoleWorker.runConsoleCommand(adbPath + " shell input text " + otaPinArray[2]);
                    Thread.sleep(500);
                    backgroundConsoleWorker.runConsoleCommand(adbPath + " shell input keyevent 66");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).execute();
    }

    private void checkAdbPath() {

        final String adbPath = getAdbCommand();

        new BackgroundConsoleWorker(new BackgroundConsoleWorkerExecutorLinePrinter() {
            @Override
            public void doInBackground(BackgroundConsoleWorker backgroundConsoleWorker) {
                backgroundConsoleWorker.runConsoleCommand(adbPath + " devices");
            }
        }).execute();

    }

    private String getAdbCommand() {
        String customAdbPath = mAdbPathTextField.getText().trim();
        final String adbPath = customAdbPath.isEmpty() ? "adb" : customAdbPath;
        return adbPath;
    }

    private boolean isCurrentItemAlreadyExist() {
        boolean isItemAlreadyExist = false;

        if(mStorageData != null) {

            ProvisioningItem data = new ProvisioningItem();
            data.email       = mEmailTextField.getText();
            data.otaPin      = mOtaPinTextField.getText();
            data.description = mDescriptionTextField.getText();

            isItemAlreadyExist = mStorageData.getList().contains(data);
        }

        return isItemAlreadyExist;
    }

    private boolean updateStorage() {
        if (mStorageData == null) {
            mStorageData = new ProvisioningData();
        }

        ProvisioningItem data = new ProvisioningItem();
        data.email       = mEmailTextField.getText();
        data.otaPin      = mOtaPinTextField.getText();
        data.description = mDescriptionTextField.getText();

        if (!mStorageData.getList().contains(data)) {
            mStorageData.add(data);

            boolean isFileAccessGranted = StorageUtils.updateStorageData(mStorageData);
            if (!isFileAccessGranted) {
                showErrorDialog("Storage access denny");
            }
        } else {
            showErrorDialog("Entry already exist");
            return false;
        }

        return true;
    }

    private void setLastItemSelected() {
        int itemCount = mHistoryComboBox.getItemCount();
        if(itemCount > 0) {
            mHistoryComboBox.setSelectedIndex(itemCount - 1);
        }
    }


    private boolean validateFields() {
        String emailText  = mEmailTextField.getText();
        String otaPinText = mOtaPinTextField.getText();
        boolean isSuccess = false;
        
        if(!emailText.isEmpty() 
                && !otaPinText.isEmpty()
                && !mDescriptionTextField.getText().isEmpty()) {
            
            if(validateProvisioningData(emailText, otaPinText)) {
                isSuccess = true;
            }
        } else {
            showMessageDialog("Please fill all fields");
        }

        return isSuccess;
    }

    private boolean trySaveCurrent() {
        boolean isSuccess = false;
        
        if(validateFields() && updateStorage()) {
            
            fillComboBox();
            setLastItemSelected();
            isSuccess = true;
        }
        return isSuccess;
    }

    private void prepareInputTexts() {
        String email  = mEmailTextField.getText();
        String otaPin = mOtaPinTextField.getText();
        mEmailTextField.setText(email.trim());
        mOtaPinTextField.setText(otaPin.trim());
    }

    private boolean validateProvisioningData(String emailText, String otaPinText) {
        boolean isValid = false;

        if(!validateEmail(emailText)) {
            showErrorDialog("Provide proper email");
        } else if(!validateOtaPin(otaPinText)) {
            showErrorDialog("Provide proper PIN\nformat: xxx-xxx-xxx");
        } else {
            isValid = true;
        }

        return isValid;
    }

    private boolean validateEmail(String emailText) {
        if(emailText == null) {
            return false;
        }
        final String EMAIL_PATTERN = 
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emailText);
        return matcher.matches();
    }


    private boolean validateOtaPin(String otaPinText) {
        if(otaPinText == null) {
            return false;
        }
        final String OTA_PIN_PATTERN = "^[\\w]+\\-[\\w]+\\-[\\w]+$";
        
        Pattern pattern = Pattern.compile(OTA_PIN_PATTERN);
        Matcher matcher = pattern.matcher(otaPinText);
        return matcher.matches();
    }
    
    private void setApplicationIcon() {
        try {
            ClassLoader cl = this.getClass().getClassLoader();
            ImageIcon programIcon = new ImageIcon(cl.getResource("red.png"));
            mFrame.setIconImage(programIcon.getImage());
         } catch (Exception whoJackedMyIcon) {
            System.out.println("Could not load program icon.");
         }
    }

    private abstract class BackgroundConsoleWorkerExecutorLinePrinter implements BackgroundConsoleWorkerExecutor {
        StringBuilder sb = new StringBuilder();
    
        @Override
        public void printOutputLine(String printline) {
            System.out.println(printline);
            sb.append(printline).append("\n");
            mConsoleOutputJTextPane.setText(sb.toString());
        }
    
        @Override
        public abstract void doInBackground(BackgroundConsoleWorker backgroundConsoleWorker);
        
    }

}
