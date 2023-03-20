import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChatClientSwing extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private final JTextField textField;
    private final JButton sendButton;
    private final Socket socket;
    private final PrintWriter out;

    public ChatClientSwing(JTextField textField, JButton sendButton, Socket socket, PrintWriter out) {
        this.textField = textField;
        this.sendButton = sendButton;
        this.socket = socket;
        this.out = out;
    }

    public static void main(String[] args) throws IOException {
        new ChatClientSwing();
    }

    public ChatClientSwing() throws IOException {
        this.socket = new Socket("localhost", 4444);
        this.out = new PrintWriter(socket.getOutputStream(), true);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        textField = new JTextField();
        panel.add(textField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.addActionListener(this);
        panel.add(sendButton, BorderLayout.EAST);

        getContentPane().add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 100);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String message = textField.getText();
        textField.setText("");
        out.println(message);
    }
}
