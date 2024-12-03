package remotedestopckltm;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Authentication extends JFrame {
    private Socket socket;
    private JTextField passwordField;
    private DataOutputStream out;
    private DataInputStream in;

    public Authentication(Socket socket) {
        this.socket = socket;
        setupUI();
    }

    private void setupUI() {
        setLayout(new GridLayout(2, 2));
        add(new JLabel("Nhập mật khẩu:"));
        passwordField = new JTextField();
        add(passwordField);
        JButton submitButton = new JButton("Xác nhận");
        add(submitButton);

        submitButton.addActionListener(e -> authenticate());
        setLocationRelativeTo(null);
        pack();
    }

    private void authenticate() {
        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            // Gửi mật khẩu người dùng nhập
            out.writeUTF(passwordField.getText());

            // Nhận phản hồi từ server
            String response = in.readUTF();
            if ("valid".equals(response)) {
                JOptionPane.showMessageDialog(this, "Kết nối thành công!");
                dispose(); // Đóng cửa sổ xác thực
                new CreateFrame(socket, in.readUTF(), in.readUTF()).start();
            } else {
                // Thông báo lỗi và cho phép nhập lại
                JOptionPane.showMessageDialog(this, "Mật khẩu sai! Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                passwordField.setText(""); // Xóa nội dung trường mật khẩu
                passwordField.requestFocus(); // Đặt con trỏ vào trường nhập mật khẩu
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối! Vui lòng thử lại sau.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            dispose(); // Đóng cửa sổ trong trường hợp lỗi kết nối
        }
    }
}
