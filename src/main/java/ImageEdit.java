import  java.awt.*;
import  java.awt.event.*;
import  java.io.*;
import  javax.swing.*;
import  java.awt.image.*;
import  javax.imageio.*;
import  javax.swing.filechooser.FileFilter;
import  java.util.Stack;
import  java.net.URL;
 
public class ImageEdit
{
    int  xPad;
    int  xf;
    int  yf;
    int  yPad;
    boolean pressed=false;
    Color maincolor;
    MyFrame f;
    MyPanel japan;
    BufferedImage imag;
    Stack<BufferedImage> history = new Stack<>();

    public ImageEdit()
    {
        f=new MyFrame("Графический редактор");
        f.setSize(350,350);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        maincolor=Color.black;
         
        JMenuBar menuBar = new  JMenuBar();
        f.setJMenuBar(menuBar);
        menuBar.setBounds(0,0,350,30);
        JMenu fileMenu = new  JMenu("Режимы");
        menuBar.add(fileMenu);
         
        /*Action loadAction = new  AbstractAction("")
        {
           public void actionPerformed(ActionEvent event)
           {
              }
            };
        JMenuItem loadMenu = new  JMenuItem(loadAction);
        fileMenu.add(loadMenu);
         
        Action saveAction = new  AbstractAction("")
        {
           public void actionPerformed(ActionEvent event)
           {
               
           }
            };
        JMenuItem saveMenu = new  JMenuItem(saveAction);
        fileMenu.add(saveMenu);
         
        Action saveasAction = new  AbstractAction("")
        {
           public void actionPerformed(ActionEvent event)
           {
              
            };
        JMenuItem saveasMenu = new  JMenuItem(saveasAction);
        fileMenu.add(saveasMenu);*/
         
        japan = new  MyPanel();
        japan.setBounds(0,30,256,256); //кривые границы
        japan.setBackground(Color.white);
        japan.setOpaque(true);
        f.add(japan);
         
        JToolBar toolbar = new  JToolBar("Toolbar", JToolBar.HORIZONTAL);
           
          JButton backbutton = new  JButton();
          URL backIconUrl = getClass().getResource("/resources/back.png");
          if (backIconUrl != null) {
            ImageIcon backIcon = new ImageIcon(backIconUrl);
            backbutton.setIcon(backIcon);
          }

          backbutton.addActionListener(new  ActionListener()
            {
              public void actionPerformed(ActionEvent event)
              { 
                if (!history.isEmpty()) {
                  BufferedImage previousImage = history.pop();
                  imag = previousImage;
                  japan.repaint();
              }
              }
            });
          toolbar.add(backbutton);

          JButton pushresult = new  JButton();
          URL pushresultIconUrl = getClass().getResource("/resources/pushresult.png");
          if (pushresultIconUrl != null) {
            ImageIcon pushresultIcon = new ImageIcon(pushresultIconUrl);
            pushresult.setIcon(pushresultIcon);
          }

          pushresult.addActionListener(new  ActionListener()
            {
              public void actionPerformed(ActionEvent event)
              { 
                try
               {
               String currentDir = System.getProperty("user.dir");
               String saveDirPath = currentDir + File.separator + "src/saves";
               File saveDir = new File(saveDirPath);
               if (!saveDir.exists() || !saveDir.isDirectory()) {
                 JOptionPane.showMessageDialog(f, "Директория сохранения не найдена");
                 return;
                 }

                 saveImageAsPNG(new File(saveDir, "image.png"));
                 saveImageAsJPEG(new File(saveDir, "image.jpg"));
                 clearImage();

                 }
                catch(IOException ex)
                {
                 JOptionPane.showMessageDialog(f, "Ошибка ввода-вывода");
                }
              }

            //PNG
            private void saveImageAsPNG(File file) throws IOException {
              ImageIO.write(imag, "png", file);
            }
            
            //JPEG
            private void saveImageAsJPEG(File file) throws IOException {
              BufferedImage copy = new BufferedImage(imag.getWidth(), imag.getHeight(), BufferedImage.TYPE_INT_RGB);
              Graphics2D g2d = copy.createGraphics();
              g2d.drawImage(imag, 0, 0, null);
              g2d.dispose();
              ImageIO.write(copy, "jpeg", file);
            }

            private void clearImage() {
              Graphics2D g2 = imag.createGraphics();
              g2.setColor(Color.white);
              g2.fillRect(0, 0, imag.getWidth(), imag.getHeight());
              g2.dispose();
              japan.repaint();
            }
            
          });
          toolbar.add(pushresult);
           
          toolbar.setBounds(0, 0, 350, 30);
          f.add(toolbar);
           
          japan.addMouseMotionListener(new  MouseMotionAdapter()
                  {
                      public void mouseDragged(MouseEvent e) 
                      { 
                          if (pressed)
                          {
                          //Graphics g = imag.getGraphics();
                          Graphics2D g2 = imag.createGraphics();
                          g2.setColor(maincolor);
                          
                          g2.setStroke(new  BasicStroke(3.0f));
                          g2.drawLine(xPad, yPad, e.getX(), e.getY());
                      
                          xPad=e.getX();
                          yPad=e.getY();
                          g2.dispose();
                          japan.repaint();
                          }
                      }
                  });
          japan.addMouseListener(new  MouseAdapter()
                  {
                     public void mouseClicked(MouseEvent e) {
                           
                     //Graphics g = imag.getGraphics();
                     Graphics2D g2 = imag.createGraphics();
                     
                          g2.setColor(maincolor);
                          
                          g2.setStroke(new  BasicStroke(3.0f));
                          g2.drawLine(xPad, yPad, xPad+1, yPad+1);
                          
                          xPad=e.getX();
                          yPad=e.getY();
                           
                          pressed=true;
                          japan.repaint();

                          BufferedImage currentImageCopy = deepCopy(imag);
                          history.push(currentImageCopy);
                   }
                     public void mousePressed(MouseEvent e) {
                         xPad=e.getX();
                          yPad=e.getY();
                          xf=e.getX();
                          yf=e.getY();
                          pressed=true;
                        }
                    
                  });
                  
        f.addComponentListener(new  ComponentAdapter() {
                public void componentResized(java.awt.event.ComponentEvent evt) {
                  
                    japan.setSize(f.getWidth()-40, f.getHeight()-80);
                    BufferedImage tempImage = new  BufferedImage(japan.getWidth(), japan.getHeight(), BufferedImage.TYPE_INT_RGB);
                             Graphics2D d2 = (Graphics2D) tempImage.createGraphics();
                        d2.setColor(Color.white);
                        d2.fillRect(0, 0, japan.getWidth(), japan.getHeight());
                    tempImage.setData(imag.getRaster());
                    imag=tempImage;
                    japan.repaint();
                   
                }
                });

        f.setLayout(null);
        f.setVisible(true);
    }    
    public static void main(String[] args) {
         
        SwingUtilities.invokeLater(new  Runnable() {
      public void run() {
        new  ImageEdit();
      }
    });        
    }

    private BufferedImage deepCopy(BufferedImage image) {
      ColorModel colorModel = image.getColorModel();
      boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
      WritableRaster raster = image.copyData(null);
      return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }
  
     class MyFrame extends JFrame
     {
      public void paint(Graphics g) {
        super.paint(g);
    }

    public MyFrame(String title) {
        super(title);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        setLocation(0, 0);
    }
     }
 
     class MyPanel extends JPanel
     {
       public MyPanel(){}

       public void paintComponent (Graphics g)
          {
            super.paintComponent(g);
            if(imag==null)
             {
                 imag = new  BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
                 Graphics2D d2 = (Graphics2D) imag.createGraphics();
                 d2.setColor(Color.white);
                 d2.fillRect(0, 0, imag.getWidth(), imag.getHeight());
             }

             
             g.drawImage(imag, 0, 0, getWidth(), getHeight(), null);      
          }
     }
     
     class TextFileFilter extends FileFilter 
     {
         private String ext;
         public TextFileFilter(String ext)
         {
             this.ext=ext;
         }
         public boolean accept(java.io.File file) 
         {
              if (file.isDirectory()) return true;
              return (file.getName().endsWith(ext));
         }
         public String getDescription() 
         {
              return "*"+ext;
         }
     }
}