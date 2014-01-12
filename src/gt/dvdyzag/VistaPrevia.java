/**
 * @(#)vistaPrevia_200819312.java
 *
 *
 * @author David Yzaguirre Gonzalez
 * @version 2.15 2009/9/7
 * Seccion B
 */
package gt.dvdyzag;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.beans.*;

public class VistaPrevia extends JPanel implements PropertyChangeListener {
    //Es el JFileChooser que selecciona los archivos de imagen

    private JFileChooser jfc;
    //Es donde guarda la instancia Image del archivo y la dibuja sobre el panel
    private Image img;

    /**
     * Es el constructor de la clase
     * @param jfc Es el frame de donde recibe el File
     */
    VistaPrevia(JFileChooser jfc) {
        this.jfc = jfc;
        Dimension panelSize = new Dimension(200, 200);
        setPreferredSize(panelSize);
    }

    /**
     * Detecta el cambia entre archivos y llama al metodo actualizarImagen
     * @param evt Es el evento PropertyChangeEvent
     */
    public void propertyChange(PropertyChangeEvent evt) {
        try {
            File archivo = jfc.getSelectedFile();
            actualizarImagen(archivo);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Guarda la instancia Image obtenido de File y actualiza la vista previa
     * @param file es el File de una imagen recibido por el JFileChooser
     */
    public void actualizarImagen(File file) throws IOException {
        if (file == null) {
            return;
        }
        img = ImageIO.read(file);
        repaint();
    }

    /**
     * Es el que dibuja el panel que sirve como vista previa
     * @param g Es de instancia Graphics
     **/
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (img != null) {
            /********Empieza a escalar la imagen*/
            int w = img.getWidth(null);
            int h = img.getHeight(null);
            int lado = Math.max(w, h);
            double escala = 200.0 / (double) lado;
            w = (int) (escala * (double) w);
            h = (int) (escala * (double) h);
            /************************************/
            g.drawImage(img, 0, 0, w, h, null);
            String dim = w + "x" + h;
            g.setColor(Color.BLACK);
            g.drawString(dim, 31, 196);
            g.setColor(Color.WHITE);
            g.drawString(dim, 30, 195);
        } else {
            //Esto es si no se ha escogido una imagen, o el archivo no es una imagen
            g.setColor(Color.BLACK);
            g.drawString("Seleccione una imagen", 30, 100);
        }
    }
}
