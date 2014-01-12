/**
 * @(#)textoDialog_200819312.java
 *
 *
 * @author David Yzaguirre Gonzalez
 * @version 1.85 2009/9/7
 * Seccion B
 */
package gt.dvdyzag;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class TextoDialog implements ActionListener {

    //Denota si se presiono Aceptar(true) o Cancelar(false)
    private boolean opcion;
    //Estilo de letra
    private int EstLetra;
    //Es el Font que devuelve el metodo getFont()
    private Font Fgeneral;

    /**
     * Crea los objetos necesario para una interface
     * para ingresar texto
     * @param padre Es el JFrame que recibe como padre
     */
    TextoDialog(JFrame padre) {
        /* Inicializa JDtexto teniendo com padre el
         * JFrame de la clase interfaz_200819312
         **/

        JDingresoTexto = new JDialog(padre, "Ingrese Texto", true);
        JDingresoTexto.setLayout(null);
        JDingresoTexto.setBounds(310, 150, 300, 150);
        JDingresoTexto.setResizable(false);
        //JDingresoTexto.setUndecorated(true);

        /*Aqui preparamos el combobox de todos las fuentes
         *disponibles en el sistema*/
        GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String envfonts[] = gEnv.getAvailableFontFamilyNames();
        DCBMletra = new DefaultComboBoxModel();//Esto es lo que se incluye al ComboBox
        for (int i = 1; i < envfonts.length; i++) {
            DCBMletra.addElement(envfonts[i]);
        }

        //Inicia los botones y objetos
        JTFtextoIngresado = new JTextField();

        JTBbold = new JToggleButton(new ImageIcon(getClass().getClassLoader().getResource("iconos/bold.png")));
        JTBitalic = new JToggleButton(new ImageIcon(getClass().getClassLoader().getResource("iconos/italic.png")));
        JCBletra = new JComboBox(DCBMletra);
        SMNrango = new SpinnerNumberModel(12, 1, 100, 1);
        JStamLetra = new JSpinner(SMNrango);
        JBcolorTexto = new JButton();
        JBaceptar = new JButton("Aceptar");
        JBcancelar = new JButton("Cancelar");

        //Otras opciones
        JBcolorTexto.setBackground(Color.BLACK);

        //Ubicacion de los objetos
        JTFtextoIngresado.setBounds(15, 10, 205, 30);
        JStamLetra.setBounds(230, 10, 45, 30);
        JCBletra.setBounds(15, 45, 150, 23);
        JTBbold.setBounds(170, 45, 22, 23);
        JTBitalic.setBounds(198, 45, 22, 23);
        JBcolorTexto.setBounds(225, 45, 45, 45);
        //JBcolorTexto.setBounds(225,45,22,23);
        JBaceptar.setBounds(15, 73, 90, 20);
        JBcancelar.setBounds(110, 73, 90, 20);

        //TextToolTip
        JStamLetra.setToolTipText("Cambia tama�o de letra");
        JCBletra.setToolTipText("Selecciona tipo de letra");
        JTBbold.setToolTipText("Negrita");
        JTBitalic.setToolTipText("Cursiva");
        JBcolorTexto.setToolTipText("Cambia color de texto");

        //Eventos
        JTBbold.addActionListener(this);
        JTBitalic.addActionListener(this);
        JBcolorTexto.addActionListener(this);
        JBaceptar.addActionListener(this);
        JBcancelar.addActionListener(this);

        //Agregaciones
        JDingresoTexto.add(JTFtextoIngresado);
        JDingresoTexto.add(JCBletra);
        JDingresoTexto.add(JTBbold);
        JDingresoTexto.add(JTBitalic);
        JDingresoTexto.add(JStamLetra);
        JDingresoTexto.add(JBcolorTexto);
        JDingresoTexto.add(JBaceptar);
        JDingresoTexto.add(JBcancelar);
    }

    /**
     * Pone visible la interface para ingresar texto
     */
    public void startDialog() {
        JTFtextoIngresado.setText("");
        /*
         *Esto se hace para posicionar el cursor
         *en el campo de texto
         *
         **/
        JBaceptar.setFocusable(false);
        JBaceptar.setFocusable(true);
        JDingresoTexto.setVisible(true);
    }

    /**
     * Controla los metodos de eventos de los objetos
     * @param evt Es el evento que envia el objeto
     */
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == JBaceptar) {
            System.out.println("Presiono Aceptar");
            opcion = true;//Esto es, si le di aceptar
            JDingresoTexto.setVisible(false);
        }

        if (evt.getSource() == JBcancelar) {
            System.out.println("Presiono Cancelar");
            opcion = false;
            JDingresoTexto.setVisible(false);
        }
        if (evt.getSource() == JTBbold) {
            switch (EstLetra) {
                case 3://Esto es, Font.BOLD+Font.ITALIC
                    EstLetra = Font.ITALIC;
                    break;
                case 2://Esto es, Font.ITALIC
                    EstLetra = EstLetra + Font.BOLD;
                    break;
                case 1://Esto es, Font.BOLD
                    EstLetra = Font.PLAIN;
                    break;
                default://Si no se ha inicializado
                    EstLetra = Font.BOLD;
            }
        }
        if (evt.getSource() == JTBitalic) {
            switch (EstLetra) {
                case 3://Esto es, Font.BOLD+Font.ITALIC
                    EstLetra = Font.BOLD;
                    break;
                case 2://Esto es, Font.ITALIC
                    EstLetra = Font.PLAIN;
                    break;
                case 1://Esto es, Font.BOLD
                    EstLetra = Font.ITALIC + Font.BOLD;
                    break;
                default: //No se ha inicializado
                    EstLetra = Font.ITALIC;
            }
        }
        if (evt.getSource() == JBcolorTexto) {
            Color colorText = JColorChooser.showDialog(JDingresoTexto,
                    "Elija un color para el texto",
                    getColor());
            if (colorText != null) {
                JBcolorTexto.setBackground(colorText);
            } else {
                colorText = JBcolorTexto.getBackground();
            }

        }
        setFont();//Siempre se va actualizar el Font.
    }

    /**
     * Establece la variable del tipo, estilo y tamano de letra
     */
    private void setFont() {
        Fgeneral = new Font(DCBMletra.getElementAt(JCBletra.getSelectedIndex()).toString(), EstLetra, Integer.parseInt(JStamLetra.getValue().toString()));
        /* Puede que EstLetra no se halla inicializado,
         * se toma su valor como 0
         **/
    }

    /**
     * Devuelve el texto ingresado
     * @return JTFtextoIngresado.getText() Consigue el texto ingresado
     */
    public String getString() {
        return JTFtextoIngresado.getText();
    }

    /**
     * Devuelve las circunstancias de Fuente en que
     * se selecciona
     * @return Fgeneral Es el tipo, estilo, tamano de letra seleccionado
     */
    public Font getFont() {
        return Fgeneral;
    }

    /**
     * Devuelve el color que usara el texto
     * @return JBcolorTexto.getBackground() Es el color de fondo del boton del color
     */
    public Color getColor() {
        return JBcolorTexto.getBackground();
    }

    /**
     * Devuelve si se presiono Aceptar o lo contrario
     * @return opcion Devuelve <code>true</code> si se presiono
     * 				  Aceptar
     *				  <code>false</code> si cancela el ingreso
     */
    public boolean getOpcion() {
        return opcion;
    }
    //Es el JDialog que es la interface para el texto
    private JDialog JDingresoTexto;
    //Se guarda todos los Fonts disponibles
    private DefaultComboBoxModel DCBMletra;
    //Es donde se muestra los Fonts
    private JComboBox JCBletra;
    //Es donde se ingresa el texto
    private JTextField JTFtextoIngresado;
    //Guarda el rango del tamano del texto
    private SpinnerNumberModel SMNrango;
    //Es para configurar el tamano del texto
    private JSpinner JStamLetra;
    //El boton para seleccionar negrita
    private JToggleButton JTBbold;
    //El boton para seleccionar cursivo
    private JToggleButton JTBitalic;
    //El boton para elegir color
    private JButton JBcolorTexto;
    //Para salir del JDialog y dibujar el texto
    private JButton JBaceptar;
    //Para salir del JDialog
    private JButton JBcancelar;
}
