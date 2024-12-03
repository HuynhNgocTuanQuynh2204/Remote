package remotedestopckltm;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenCaptureUtil {
    public static boolean captureScreen() {
        try {
            // Chụp ảnh màn hình
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);

            // Tạo thư mục lưu ảnh (nếu chưa có)
            File directory = new File("screenshots");
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Định dạng tên tệp ảnh
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File file = new File(directory, "screenshot_" + timestamp + ".png");

            // Lưu ảnh vào tệp
            ImageIO.write(screenFullImage, "png", file);
            System.out.println("Screenshot saved: " + file.getAbsolutePath());
            return true;
        } catch (AWTException | IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
