package remotedestopckltm;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

/**
 *
 * @author TUẤN QUỲNH CODER
 */
class CreateFrame extends Thread {
    String width = "", height = "";
    private JFrame frame = new JFrame();
    private JDesktopPane desktop = new JDesktopPane();
    private Socket cSocket = null;
    private JInternalFrame interFrame = new JInternalFrame("Server Screen", true, true, true);
    private JPanel cPanel = new JPanel();

    public CreateFrame(Socket cSocket, String width, String height) {
        this.width = width;
        this.height = height;
        this.cSocket = cSocket;
        start();
    }

    public void drawGUI() {
        frame.add(desktop, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        interFrame.setLayout(new BorderLayout());
        interFrame.getContentPane().add(cPanel, BorderLayout.CENTER);
        interFrame.setSize(100, 100);
        desktop.add(interFrame);

        try {
            interFrame.setMaximum(true);
        } catch (PropertyVetoException ex) {
            ex.printStackTrace();
        }

        cPanel.setFocusable(true);
        cPanel.requestFocusInWindow();
        interFrame.setVisible(true);

        // Thêm nút chụp ảnh màn hình
        JButton btnCapture = new JButton("Chụp ảnh màn hình");
        btnCapture.setBounds(10, 10, 200, 30); // Đặt kích thước và vị trí cho nút
        desktop.add(btnCapture);

        // Xử lý sự kiện khi bấm nút
        btnCapture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean success = ScreenCaptureUtil.captureScreen();
                if (success) {
                    JOptionPane.showMessageDialog(frame, 
                        "Ảnh màn hình đã được lưu!", 
                        "Thông báo", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, 
                        "Lỗi khi lưu ảnh!", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void run() {
        InputStream in = null;

        drawGUI();

        try {
            in = cSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new ReceivingScreen(in, cPanel);
        new SendEvents(cSocket, cPanel, width, height);
    }
}
