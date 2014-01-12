/**
 * @(#)JDraw_200819312.java
 *
 * JDraw_200819312 application
 *
 * @author David Yzaguirre Gonzalez 200819312
 * @version 2.00 2009/8/29
 * Seccion B
 */
package gt.dvdyzag;

import javax.swing.SwingUtilities;

public class JDraw {

    /**
     * Es el que hace inicializa la interface de la
     * herramienta de dibujo JDraw
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                new Interfaz().setVisible(true);
            }
        });
    }
}
