/**
 * @(#)interfaz_200819312.java
 *
 *
 * @author David Yzaguirre Gonzalez
 * @version 2.95 2009/9/7
 * Seccion B
 */
package gt.dvdyzag;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;//Solo admite imagenes
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.IOException;
import acercade.AcercaDe;

public class Interfaz extends JFrame {

    //Es el color de linea
    private Color colorLinea;
    //Es el color de relleno
    private Color colorRelleno;

    private AcercaDe acercade;

    /**
     *
     * Constructor de la clase
     */
    Interfaz() {
        //El JFrame principal
        super("Nuevo dibujo - JDraw");
        setBounds(150, 50, 800, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("iconos/brush.png")).getImage());
        acercade = new AcercaDe(this);

        //Creando los objetos
        JMBmenuprincipal = new JMenuBar();
        JMutilidades = new JMenu("Utilidades");
        JMIlimpiarLienzo = new JMenuItem("Limpiar lienzo");
        JMIlimpiarLienzo.setEnabled(false);
        JMIremoverFondo = new JMenuItem("Remover imagen de fondo");
        JMIremoverFondo.setEnabled(false);
        JMIsalir = new JMenuItem("Salir");
        JMayuda = new JMenu("Ayuda");
        JMIacercade = new JMenuItem("Acerca de...");

        lienzo = new Lienzo();

        JPopciones = new JPanel();
        JSgrosor = new JSlider(1, 15, 1);
        JLgrosor = new JLabel("1");
        JBcambiarColorLinea = new JButton();
        JBcambiarColorRelleno = new JButton();
        JCBrelleno = new JComboBox(new DefaultComboBoxModel(new String[]{"Sin Relleno", "Relleno sin contorno", "Relleno con contorno"}));
        JCKBtriangleOrientation = new JCheckBox("Triangulo Invertido");

        JPherramientas = new JPanel();
        BGherramientas = new ButtonGroup();
        JTBlapiz = new JToggleButton(new ImageIcon(getClass().getClassLoader().getResource("iconos/pencil.png")));
        JTBborrador = new JToggleButton(new ImageIcon(getClass().getClassLoader().getResource("iconos/eraser.png")));
        JTBlinea = new JToggleButton(new ImageIcon(getClass().getClassLoader().getResource("iconos/line.png")), true);
        JTBrectangulo = new JToggleButton(new ImageIcon(getClass().getClassLoader().getResource("iconos/rectangle.png")));
        JTBovalos = new JToggleButton(new ImageIcon(getClass().getClassLoader().getResource("iconos/oval.png")));
        JTBtriangulos = new JToggleButton(new ImageIcon(getClass().getClassLoader().getResource("iconos/triangle.png")));
        JTBtexto = new JToggleButton(new ImageIcon(getClass().getClassLoader().getResource("iconos/text.png")));
        JBinsertarfondo = new JButton(new ImageIcon(getClass().getClassLoader().getResource("iconos/insert.png")));//tiene que ser un boton
        JFCurlImagen = new JFileChooser();

        textDialog = new TextoDialog(this);

        //Tooltips
        JTBlapiz.setToolTipText("Lapiz");
        JTBborrador.setToolTipText("Borrador");
        JTBlinea.setToolTipText("Linea");
        JTBrectangulo.setToolTipText("Rectangulo");
        JTBovalos.setToolTipText("Ovalo");
        JTBtriangulos.setToolTipText("Triangulo");
        JTBtexto.setToolTipText("Texto");
        JBinsertarfondo.setToolTipText("Insertar imagen");

        JSgrosor.setToolTipText("Cambia grosor de linea");
        JBcambiarColorLinea.setToolTipText("Cambia color de linea");
        JBcambiarColorRelleno.setToolTipText("Cambia color de relleno");
        JCKBtriangleOrientation.setToolTipText("Cambia orientaci�n de triangulo");

        //Layouts
        JPherramientas.setLayout(new GridLayout(4, 2, 2, 1));
        JPopciones.setLayout(null);

        //SetBounds
        lienzo.setLocation(115, 0);
        JPopciones.setBounds(0, this.getHeight() - 125, 600, 90);
        JPherramientas.setBounds(0, 0, 100, 200);
        JSgrosor.setBounds(0, 0, 100, 25);
        JLgrosor.setBounds(50, 25, 35, 35);
        JBcambiarColorLinea.setBounds(105, 0, 45, 45);
        JBcambiarColorRelleno.setBounds(155, 0, 45, 45);
        JCBrelleno.setBounds(205, 0, 150, 25);
        JCKBtriangleOrientation.setBounds(360, 0, 160, 25);

        //Colores
        JPopciones.setBackground(Color.GRAY);
        JBcambiarColorLinea.setBackground(Color.BLACK);
        JBcambiarColorRelleno.setBackground(Color.BLACK);
        JCKBtriangleOrientation.setBackground(Color.CYAN);
        JCKBtriangleOrientation.setForeground(Color.BLACK);
        JSgrosor.setBackground(Color.GRAY);
        JLgrosor.setForeground(Color.WHITE);

        JPherramientas.setBackground(Color.GREEN);

        //otras opciones
        JMIlimpiarLienzo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
        JMIremoverFondo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
        JMIsalir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
        JMayuda.setMnemonic(java.awt.event.KeyEvent.VK_U);
        colorLinea = Color.WHITE;
        colorRelleno = Color.WHITE;

        JCBrelleno.setSelectedIndex(0);
        JCBrelleno.setVisible(false);
        JBcambiarColorRelleno.setVisible(false);
        JCKBtriangleOrientation.setVisible(false);

        /* A continuacion se aplica los filtros para
         * el JFileChooser para que solo pueda abrir
         * archivos de imagenes. Despues se le aplica
         * la clase vistaPrevia_200819312 como lo indica
         * su nombre, implementar la vista previa de los
         * imagenes
         **/
        filtroAbrirTodo = new FileNameExtensionFilter("Imagen (*.jpg, *.bmp, *.gif, *.png)", "jpg", "bmp", "gif", "png");
        filtroAbrirJPEG = new FileNameExtensionFilter("JPEG (*.jpg)", "jpg");
        filtroAbrirBMP = new FileNameExtensionFilter("BMP (*.bmp)", "bmp");
        filtroAbrirPNG = new FileNameExtensionFilter("PNG (*.png)", "png");
        filtroAbrirGIF = new FileNameExtensionFilter("GIF (*.gif)", "gif");
        JFCurlImagen.setFileFilter(filtroAbrirGIF);
        JFCurlImagen.setFileFilter(filtroAbrirPNG);
        JFCurlImagen.setFileFilter(filtroAbrirBMP);
        JFCurlImagen.setFileFilter(filtroAbrirJPEG);
        JFCurlImagen.setFileFilter(filtroAbrirTodo);

        /* Ahora se le agrega la vista previa al selector de imagenes
         **/
        VistaPrevia vistaPrevia = new VistaPrevia(JFCurlImagen);
        JFCurlImagen.addPropertyChangeListener(vistaPrevia);
        JFCurlImagen.setAccessory(vistaPrevia);

        //Agregaciones
        JPopciones.add(JSgrosor);
        JPopciones.add(JLgrosor);
        JPopciones.add(JBcambiarColorLinea);
        JPopciones.add(JBcambiarColorRelleno);
        JPopciones.add(JCBrelleno);
        JPopciones.add(JCKBtriangleOrientation);

        JPherramientas.add(JTBlapiz);
        JPherramientas.add(JTBborrador);
        JPherramientas.add(JTBlinea);
        JPherramientas.add(JTBrectangulo);
        JPherramientas.add(JTBovalos);
        JPherramientas.add(JTBtriangulos);
        JPherramientas.add(JTBtexto);
        JPherramientas.add(JBinsertarfondo);

        BGherramientas.add(JTBlapiz);
        BGherramientas.add(JTBborrador);
        BGherramientas.add(JTBlinea);
        BGherramientas.add(JTBrectangulo);
        BGherramientas.add(JTBovalos);
        BGherramientas.add(JTBtriangulos);
        BGherramientas.add(JTBtexto);

        JMutilidades.add(JMIlimpiarLienzo);
        JMutilidades.add(JMIremoverFondo);
        JMutilidades.add(new JSeparator());
        JMutilidades.add(JMIsalir);
        JMayuda.add(JMIacercade);
        JMBmenuprincipal.add(JMutilidades);
        JMBmenuprincipal.add(JMayuda);
        setJMenuBar(JMBmenuprincipal);
        add(JPopciones);
        add(JPherramientas);
        add(lienzo);

        //Eventos de botones
        JTBlapiz.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                lapiz_actionPerformed(evt);
            }
        });
        JTBborrador.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                borrador_actionPerformed(evt);
            }
        });

        JTBlinea.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                linea_actionPerformed(evt);
            }
        });

        JTBrectangulo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                rectangulo_actionPeformed(evt);
            }
        });

        JTBovalos.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                ovalos_actionPerformed(evt);
            }
        });

        JTBtriangulos.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                triangulos_actionPerformed(evt);
            }
        });

        JTBtexto.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                texto_actionPerformed(evt);
            }
        });

        JBinsertarfondo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                insertarfondo_actionPerformed(evt);
            }
        });

        //Eventos de los opciones

        JSgrosor.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent evt) {
                grosor_stateChanged(evt);
            }
        });

        JBcambiarColorLinea.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                cambiarColorLinea_actionPerformed(evt);
            }
        });
        JBcambiarColorRelleno.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                cambiarColorRelleno_actionPerformed(evt);
            }
        });

        JCBrelleno.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                relleno_actionPerformed(evt);
            }
        });

        JCKBtriangleOrientation.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                triangleOrientation_actionPerformed(evt);
            }
        });

        //Eventos de menu
        JMIlimpiarLienzo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                lienzo.cleanCanvas();
                JMIlimpiarLienzo.setEnabled(false);
            }
        });

        JMIremoverFondo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                lienzo.removeImageBackground();
                JMIremoverFondo.setEnabled(false);
            }
        });

        JMIsalir.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                System.exit(0);//Salida del programa
            }
        });

        JMIacercade.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                acercade.setVisible(true);
            }
        });

        //Eventos de lienzo
        lienzo.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent evt) {
                lienzo_mousePressed(evt);
            }

            @Override
            public void mouseClicked(MouseEvent evt) {
                lienzo_mouseClicked(evt);
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                lienzo_mouseReleased(evt);
            }

            @Override
            public void mouseEntered(MouseEvent evt) {
                if (lienzo.Shapetype == lienzo.TEXTO) {
                    lienzo.setCursor(new Cursor(Cursor.TEXT_CURSOR));
                } else {
                    lienzo.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                }
            }
        });
        lienzo.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent evt) {
                lienzo_mouseDragged(evt);
            }

            @Override
            public void mouseMoved(MouseEvent evt) {
                lienzo_mouseMoved(evt);
            }
        });
    }

    /**
     * Es el que envia las coordenadas
     * del primer clic con el puntero
     * ***Si esta en modo lapiz o borrador,
     * ***inicializa el GeneralPath que se encarga de
     * ***dibujarlo
     * @param m Es el objeto MouseEvent
     */
    private void lienzo_mousePressed(MouseEvent m) {
        JMIlimpiarLienzo.setEnabled(true);
        lienzo.setFirstCoordinates(m.getX(), m.getY());
        if (lienzo.Shapetype == lienzo.LAPIZ || lienzo.Shapetype == lienzo.BORRADOR) {
            lienzo.startGPlinea();
        } else if (lienzo.Shapetype == lienzo.TEXTO) {
            proporcionarTexto();//Inicia el dialogo para ingresar texto
        }
    }

    /**
     * Es el que manda las ultimas coordenas
     * Y activa el metodo de dibujar del lienzo
     * segun la figura que se trazo
     * @param m Es el objeto MouseEvent
     */
    private void lienzo_mouseClicked(MouseEvent m) {
        lienzo.setLastCoordinates(m.getX(), m.getY());
        lienzo.draw();
    }

    /**
     * Es sustituido por el lienzo_mouseClicked
     * Ya que al momento de soltar el puntero
     * puede que no participe este metodo, ya que
     * se trate de un simple clic.
     * @param m Es el objeto MouseEvent
     */
    private void lienzo_mouseReleased(MouseEvent m) {
        lienzo.setDraw_preview(false);
        lienzo_mouseClicked(m);
    }

    /**
     * Sirve para manipular el lapiz y el borrador
     * Tambien es el que permite "visualizar" la figura
     * cuando se estira y crece
     * @param m Es el objeto MouseEvent
     */
    private void lienzo_mouseDragged(MouseEvent m) {
        lienzo.setLastCoordinates(m.getX(), m.getY());
        lienzo.setDraw_preview(true);
    }

    /**
     * Este metodo es casi obsoleto, almenos que se requiere
     * conocer en todo momento la posicion del puntero sobre
     * el lienzo, quizas en futuras versiones
     * @param m Es el objeto MouseEvent
     */
    private void lienzo_mouseMoved(MouseEvent m) {
        System.out.println("Moving x=" + m.getX() + " and y=" + m.getY() + ".");
    }

    /**
     * Alista la herramienta de lapiz
     * @param button Es el objeto ActionEvent
     */
    private void lapiz_actionPerformed(ActionEvent button) {
        lienzo.setShape(lienzo.LAPIZ);
        JCBrelleno.setSelectedIndex(0);
    }

    /**
     * Alista la herramienta borrador
     * @param button Es el objeto ActionEvent
     */
    private void borrador_actionPerformed(ActionEvent button) {
        lienzo.setShape(lienzo.BORRADOR);
        JCBrelleno.setSelectedIndex(0);
    }

    /**
     * Alista la herramienta de linea
     * @param button Es el objeto ActionEvent
     */
    private void linea_actionPerformed(ActionEvent button) {
        lienzo.setShape(lienzo.LINEA);
        JCBrelleno.setSelectedIndex(0);
    }

    /**
     * Alista la herramienta de rectangulo
     * @param button Es el objeto ActionEvent
     */
    private void rectangulo_actionPeformed(ActionEvent button) {
        lienzo.setShape(lienzo.RECTANGULO);
        JCBrelleno.setSelectedIndex(JCBrelleno.getSelectedIndex());
    }

    /**
     * Alista la herramienta de ovalos
     * @param button Es el objeto ActionEvent
     */
    private void ovalos_actionPerformed(ActionEvent button) {
        lienzo.setShape(lienzo.OVALO);
        JCBrelleno.setSelectedIndex(JCBrelleno.getSelectedIndex());
    }

    /**
     * Alista la herramienta de triangulos
     * @param button Es el objeto ActionEvent
     */
    private void triangulos_actionPerformed(ActionEvent button) {
        lienzo.setShape(lienzo.TRIANGULO);
        JCBrelleno.setSelectedIndex(JCBrelleno.getSelectedIndex());
    }

    /**
     * Alista la herramienta para dibujar text
     * @param button Es el objeto ActionEvent
     */
    private void texto_actionPerformed(ActionEvent button) {
        lienzo.setShape(lienzo.TEXTO);
        JCBrelleno.setSelectedIndex(0);
    }

    /**
     * Visualiza el FileChooser para seleccionar
     * un archivo de imagen para el fondo
     * @param button Es el objeto ActionEvent
     */
    private void insertarfondo_actionPerformed(ActionEvent button) {
        try {
            int imagenElegido = JFCurlImagen.showOpenDialog(this);
            if (imagenElegido == JFileChooser.APPROVE_OPTION) {
                /*Solo va dibujar una nueva imagen si se presiona
                 * ACEPTAR, de lo contrario devolveria el ultimo
                 * archivo seleccionado*/

                lienzo.setImageBackground(JFCurlImagen.getSelectedFile());
                JMIremoverFondo.setEnabled(true);//Activa la barra de remover Fondo
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Cambia el valor del grosor
     * @param slide Es el objeto ChangeEvent
     */
    private void grosor_stateChanged(ChangeEvent slide) {
        lienzo.setGrosor(JSgrosor.getValue()); //Envia el valor del JSlider al lienzo
        JLgrosor.setText(String.valueOf(JSgrosor.getValue()));//Muestra en el label el valor del JSlider
    }

    /**
     * Cambia el color en que se dibuja las lineas
     * y contorno de las figuras
     * @param c Es el objeto ActionEvent
     */
    private void cambiarColorLinea_actionPerformed(ActionEvent c) {
        colorLinea = JColorChooser.showDialog(Interfaz.this,
                "Elija un color para la linea",
                JBcambiarColorLinea.getBackground());
        if (colorLinea != null) {
            lienzo.setLineColor(colorLinea);
            JBcambiarColorLinea.setBackground(colorLinea);
        } else {
            //Por si di Cancelar, restablece la variable colorLinea
            colorLinea = JBcambiarColorLinea.getBackground();
        }
    }

    /**
     * Cambia el color de relleno de las figuras
     * @param c Es el objeto ActionEvent
     */
    private void cambiarColorRelleno_actionPerformed(ActionEvent c) {
        colorRelleno = JColorChooser.showDialog(Interfaz.this,
                "Elija un color para el relleno",
                JBcambiarColorRelleno.getBackground());
        if (colorRelleno != null) {
            lienzo.setFillColor(colorRelleno);
            JBcambiarColorRelleno.setBackground(colorRelleno);
        } else {
            //Por si di Cancelar, restablece la variable colorRelleno
            colorRelleno = JBcambiarColorRelleno.getBackground();
        }

    }

    /**
     * Permite seleccionar de las tres opciones de dibujar
     * las figuras, sin relleno, con relleno y sin contorno o con
     * ambas cosas. Este metodo tambien gestiona la apariencia de la
     * barra de opciones.
     * @param r Es el objeto ActionEvent
     */
    private void relleno_actionPerformed(ActionEvent r) {
        switch (JCBrelleno.getSelectedIndex()) {
            case 0://Si lo quiere sin relleno
                lienzo.setFill(false);
                if (lienzo.Shapetype != lienzo.BORRADOR && lienzo.Shapetype != lienzo.TEXTO) {
                    JBcambiarColorLinea.setVisible(true);
                } else /*Si esta seleccionado el borrador, no tengo
                 *necesidad de seleccionar color
                 **/ {
                    JBcambiarColorLinea.setVisible(false);
                }
                JBcambiarColorRelleno.setVisible(false);
                if (lienzo.Shapetype == lienzo.TEXTO) {
                    JSgrosor.setVisible(false);
                    JLgrosor.setVisible(false);
                } else {
                    JSgrosor.setVisible(true);
                    JLgrosor.setVisible(true);
                }
                break;
            case 1://Si lo quiere con relleno y sin cotorno
                lienzo.setFill(true);
                lienzo.setFillWithLine(false);
                JSgrosor.setVisible(false);
                JLgrosor.setVisible(false);
                JBcambiarColorLinea.setVisible(false);
                JBcambiarColorRelleno.setVisible(true);
                break;
            default://Esto es por si es case 2
                lienzo.setFill(true);
                lienzo.setFillWithLine(true);
                JBcambiarColorLinea.setVisible(true);
                JBcambiarColorRelleno.setVisible(true);
                JSgrosor.setVisible(true);
                JLgrosor.setVisible(true);
        }
        if (lienzo.Shapetype != lienzo.TRIANGULO) {
            JCKBtriangleOrientation.setVisible(false);
        } else {
            JCKBtriangleOrientation.setVisible(true);
        }
        switch (lienzo.Shapetype) {
            case 1://Si es LAPIZ
            case 2://Si es BORRADOR
            case 3://Si es LINEA
            case 7://Si es TEXTO
                JCBrelleno.setVisible(false);
                break;
            default:
                JCBrelleno.setVisible(true);
        }
    }

    /**
     * Se encarga de la forma de dibuajar los triangulos.
     * @param evt Es el objeto ActionEvent
     **/
    private void triangleOrientation_actionPerformed(ActionEvent evt) {
        lienzo.setTriangleOrientation(!JCKBtriangleOrientation.isSelected());
    }

    /**
     * Es el que hace visible la interface
     * para ingresar texto
     **/
    private void proporcionarTexto() {
        textDialog.startDialog();
        if (textDialog.getOpcion()) {//Esto es, si se presiono Aceptar
            lienzo.setFontColorString(textDialog.getFont(), textDialog.getColor(), textDialog.getString());
            lienzo.draw();
        }
    }
    //La instancia del lienzo
    private Lienzo lienzo;
    //La instancia para insertar texto
    private TextoDialog textDialog;
    //Las instancias para formar la barra de utilidades
    private JMenuBar JMBmenuprincipal;
    private JMenu JMutilidades;
    private JMenu JMayuda;
    private JMenuItem JMIlimpiarLienzo;
    private JMenuItem JMIremoverFondo;
    private JMenuItem JMIsalir;
    private JMenuItem JMIacercade;
    //Las instancias para el panel de opciones
    private JPanel JPopciones;
    private JSlider JSgrosor;
    private JLabel JLgrosor;
    private JButton JBcambiarColorLinea;
    private JButton JBcambiarColorRelleno;
    private JComboBox JCBrelleno;
    private JCheckBox JCKBtriangleOrientation;
    //Las instancias para el panel de herramientas
    private JPanel JPherramientas;
    private ButtonGroup BGherramientas;
    private JToggleButton JTBlapiz;
    private JToggleButton JTBborrador;
    private JToggleButton JTBlinea;
    private JToggleButton JTBrectangulo;
    private JToggleButton JTBovalos;
    private JToggleButton JTBtriangulos;
    private JToggleButton JTBtexto;
    private JButton JBinsertarfondo;
    private JFileChooser JFCurlImagen;
    private FileNameExtensionFilter filtroAbrirTodo;
    private FileNameExtensionFilter filtroAbrirJPEG;
    private FileNameExtensionFilter filtroAbrirBMP;
    private FileNameExtensionFilter filtroAbrirPNG;
    private FileNameExtensionFilter filtroAbrirGIF;
}
