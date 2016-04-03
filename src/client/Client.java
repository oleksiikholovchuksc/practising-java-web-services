package client;

import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.xml.sax.SAXException;
import phonewebservice.PhoneWebService;


public class Client {
    public static void main(String[] args) throws MalformedURLException {
        // get sax web service object
        URL url = new URL("http://localhost:1986/phonewebservice?wsdl");
        QName qname = new QName("http://phonewebservice/", "PhoneWebServiceImplService");
        Service service = Service.create(url, qname);
        PhoneWebService phoneWebService = service.getPort(PhoneWebService.class);

        // create and run GUI
        ClientForm clientGUI = new ClientForm(phoneWebService);
        clientGUI.setSize(500, 150);
        clientGUI.setVisible(true);
        clientGUI.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }
}

class ClientForm extends JFrame {

    private PhoneWebService phoneService;

    private JTextField nameTextField;
    private JButton saxButton;
    private JButton domButton;
    private JLabel resultsLabel;

    ClientForm(PhoneWebService phoneService) {
        super("Phone-by-name getter");

        this.phoneService = phoneService;

        arrangeUI();
        setButtonsListeners();
    }

    private void arrangeUI() {
        setLayout(new GridLayout(4, 1));

        // add name text field and a corresponding label
        JPanel namePanel = new JPanel(new FlowLayout());

        JLabel nameLabel = new JLabel("Enter a name: ");
        namePanel.add(nameLabel);

        nameTextField = new JTextField(30);
        namePanel.add(nameTextField);

        add(namePanel);

        // add buttons to search using two approaches
        JPanel buttonsPanel = new JPanel(new FlowLayout());

        saxButton = new JButton("Find with SAX");
        buttonsPanel.add(saxButton);

        domButton = new JButton("Find with DOM");
        buttonsPanel.add(domButton);

        add(buttonsPanel);

        // add a horizontal separator
        add(new JSeparator(SwingConstants.HORIZONTAL));

        // add results label
        resultsLabel = new JLabel("", SwingConstants.CENTER);
        resultsLabel.setVisible(false);
        add(resultsLabel);
    }

    private void setButtonsListeners() {
        // set listener for SAX
        saxButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String nameToSearch = nameTextField.getText();

                if(checkInput(nameToSearch)) {
                    try {
                        setResultToLabel(phoneService.getPhoneByNameUsingSAX(nameToSearch));
                    } catch(SAXException ex) {

                    } catch(IOException ex) {

                    }
                }

            }
        });

        // set listener for DOM
        domButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String nameToSearch = nameTextField.getText();

                if(checkInput(nameToSearch)) {
                    setResultToLabel(phoneService.getPhoneByNameUsingDOM(nameToSearch));
                }

            }
        });
    }

    private boolean checkInput(String inputToCheck) {
        String error = "";

        if(inputToCheck.isEmpty()) {
            error = "Please enter a name to search.";
        }

        if(error.isEmpty()) {
            return true;
        } else {
            JOptionPane.showMessageDialog(this, error);
            return false;
        }
    }

    private void setResultToLabel(String result) {
        String textToSet;
        if(result.isEmpty()) {
            textToSet = "<html><font color='red'>No such name found.</font></html>";
        } else {
            textToSet = "<html>Found phone: <font color='green'>" +
                    result +
                    "</font>.</html>";
        }

        resultsLabel.setText(textToSet);
        resultsLabel.setVisible(true);
    }

}