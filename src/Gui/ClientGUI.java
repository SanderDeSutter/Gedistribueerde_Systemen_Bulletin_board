//package Gui;
//
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.io.IOException;
//import java.rmi.NotBoundException;
//import java.rmi.RemoteException;
//import java.rmi.registry.LocateRegistry;
//import java.rmi.registry.Registry;
//import java.util.Set;
//
//public class ClientGUI {
//
//    String name;
//    JFrame frame = new JFrame("Chatter");
//    JTextField textField = new JTextField(50);
//    JTextArea messageArea = new JTextArea(16, 50);
//    JTextArea onlineClients = new JTextArea(40, 16);
//
//    public ClientGUI() {
//
//        textField.setEditable(false);
//        onlineClients.setEditable(false);
//        messageArea.setEditable(false);
//        frame.getContentPane().add(textField, BorderLayout.SOUTH);
//        frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
//        frame.getContentPane().add(new JScrollPane(onlineClients), BorderLayout.WEST);
//        frame.pack();
//
//        // Send on enter then clear to prepare for next message
//        textField.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    if (textField.getText().startsWith("/w")) {
//                        String receiver = textField.getText().split(" ")[1];
//                        impl.sendMessagePrivate(name, textField.getText().substring(4 + receiver.length()), receiver);
//                    } else {
//                        impl.sendMessage(name, textField.getText());
//                    }
//                } catch (RemoteException e1) {
//                    e1.printStackTrace();
//                }
//                textField.setText("");
//            }
//        });
//
//        frame.addWindowListener(new WindowAdapter() {
//
//            @Override
//            public void windowClosing(WindowEvent e) {
//                boolean cont = false;
//                while (!cont) {
//                    try {
//                        if (impl.unRegister(name)) {
//                            cont = true;
//                        }
//                    } catch (RemoteException e1) {
//                        e1.printStackTrace();
//                    }
//                }
//            }
//        });
//    }
//
//    public static void main(String[] args) throws Exception {
//
//        ClientGUI client = new ClientGUI();
//        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        client.frame.setVisible(true);
//        client.run();
//    }
//
//    private String getName() {
//        return JOptionPane.showInputDialog(frame, "Choose a screen name:", "Screen name selection",
//                JOptionPane.PLAIN_MESSAGE);
//    }
//
//    private void run() throws IOException, NotBoundException {
//        try {
//
//            Registry myRegistry = LocateRegistry.getRegistry("127.0.0.1", 1099);
//            impl = (Chat) myRegistry.lookup("ChatService");
//
//            boolean hasName = false;
//            while (!hasName) {
//                name = getName();
//                if (name==null){
//                    return;
//                }
//                hasName = impl.register(name);
//                frame.setTitle(name);
//            }
//
//            textField.setEditable(true);
//            Set<String> names = impl.getUsers();
//            onlineClients.setText("");
//            for (String name : names) {
//                onlineClients.append(name + "\n");
//            }
//
//            while (true) {
//                String message = impl.getMessage(name);
//                messageArea.append(message + "\n");
//                names = impl.getUsers();
//                onlineClients.setText("");
//                for (String name : names) {
//                    onlineClients.append(name + "\n");
//                }
//            }
//
//        } finally {
//            frame.setVisible(false);
//            frame.dispose();
//        }
//    }
//}