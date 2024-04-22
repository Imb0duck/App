import org.imgscalr.Scalr;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageProcessor {
    private static PCords findTopLeftNonEmptyPixel(BufferedImage image){
        int xVal = -1, yVal = -1;
        outerLoop:
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int color = image.getRGB(x, y);
                if (color != Color.WHITE.getRGB()) {
                    yVal = y;
                    break outerLoop;
                }
            }
        }
        outerLoop:
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int color = image.getRGB(x, y);
                if (color != Color.WHITE.getRGB()) {
                    xVal = x;
                    break outerLoop;
                }
            }
        }
        return new PCords(xVal, yVal);
    }
    private static PCords findBottomRightNonEmptyPixel(BufferedImage image){
        int xVal = -1, yVal = -1;
        outerLoop:
        for (int y = image.getHeight()-1; y >= 0; y--) {
            for (int x = image.getWidth()-1; x >= 0; x--) {
                int color = image.getRGB(x, y);
                if (color != Color.WHITE.getRGB()) {
                    yVal = y;
                    break outerLoop;
                }
            }
        }
        outerLoop:
        for (int x = image.getWidth()-1; x >= 0; x--) {
            for (int y = image.getHeight()-1; y >= 0; y--) {
                int color = image.getRGB(x, y);
                if (color != Color.WHITE.getRGB()) {
                    xVal = x;
                    break outerLoop;
                }
            }
        }
        return new PCords(xVal, yVal);
    }
    private static BufferedImage cropEmptySpace(BufferedImage image, PCords topLeft, PCords bottomRight){
        return image.getSubimage(topLeft.x,topLeft.y,bottomRight.x-topLeft.x,bottomRight.y-topLeft.y);
    }
    private static BufferedImage expandEmptySpace(BufferedImage image, PCords topLeft, PCords bottomRight) {
        int frameOffset = (int)(Math.max(image.getWidth(), image.getHeight())*0.37);
        BufferedImage copyOfImage = new BufferedImage(Math.max(image.getWidth(), image.getHeight())+frameOffset, Math.max(image.getWidth(), image.getHeight())+frameOffset, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = copyOfImage.createGraphics();
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, copyOfImage.getWidth(), copyOfImage.getHeight());
        if(bottomRight.y - topLeft.y > image.getWidth()){
            g.drawImage(image, (image.getHeight() - image.getWidth()) / 2 + frameOffset/2, frameOffset/2, null);
        }
        else{
            g.drawImage(image, frameOffset/2, (image.getWidth() - image.getHeight()) / 2 + frameOffset/2, null);
        }
        return copyOfImage;
    }
    private static BufferedImage rectangleImageToSquare(BufferedImage image) throws EmptyImageException, IOException {
        PCords topLeft = findTopLeftNonEmptyPixel(image);
        PCords bottomRight= findBottomRightNonEmptyPixel(image);
        if(!topLeft.isValid() || !bottomRight.isValid()){
            throw new EmptyImageException("Non-empty pixels not found");
        }
        return expandEmptySpace(cropEmptySpace(image, topLeft, bottomRight), topLeft, bottomRight);
    }

    private static BufferedImage compressImage(BufferedImage image){
        return Scalr.resize(image, 64,63);
    }

    private static byte[] getPixelsArray(BufferedImage image) throws IOException {
        File outputfile = new File("image.jpg");
        ImageIO.write(image, "jpg", outputfile);
        byte [] pixelsArray = new byte[64*63/2];
        for (int y = 0; y < 63; y++){
            for (int x = 0; x < 64; x+=2){
                byte white1 =(byte) (((image.getRGB(x, y) & 0xFF) / 128 * 15)  << 4);
                byte white2 = (byte) ((image.getRGB(x+1, y) & 0xFF) / 128 * 15);
                pixelsArray[y*32+x/2] = (byte) (white1 | white2);
            }
        }
        return pixelsArray;
    }

    public static byte[] processImage(BufferedImage image) throws EmptyImageException{
        try {
            return getPixelsArray(compressImage(rectangleImageToSquare(image)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException{
        File file = new File("C:/Users/btlll/IdeaProjects/App/src/main/java/saves/image.png");
        //System.out.println(processImage(ImageIO.read(file)));
        //System.out.println(processImage(ImageIO.read(file)));
    }
    private static class PCords{
        private int x;
        private int y;
        public PCords(int x, int y){
            this.x = x;
            this.y = y;
        }
        public int getY() {
            return y;
        }
        public int getX() {
            return x;
        }
        public void setX(int x) {
            this.x = x;
        }
        public void setY(int y) {
            this.y = y;
        }
        public boolean isValid(){
            return x >= 0 && y >= 0;
        }
    }
    static class EmptyImageException extends Exception {
        public EmptyImageException(String message){
            super(message);
        }
    }
}
