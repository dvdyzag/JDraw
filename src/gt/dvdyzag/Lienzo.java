/**
 * @(#)lienzo_200819312.java
 *
 *
 * @author David Yzaguirre Gonzalez
 * @version 2.89 2009/9/7
 * Seccion B
 */
package gt.dvdyzag;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

public class Lienzo extends Canvas {

    //Es el Font para dibujar texto
    private Font JDrawFont;
    //Se guarda el texto a dibujar
    private String Stexto;
    //Es el color del texto
    private Color text_color;
    //Aqui se guardan las coordenadas respecto al lienzo
    private static int first_x;
    private static int first_y;
    private static int last_x;
    private static int last_y;
    //Estos son los colores de relleno y contorno
    private Color fill_color;
    private Color line_color;
    //Decide si dibuja la figura relleno o no
    private boolean fill;
    //Decide si dibuja la figura relleno con contorno o no
    private boolean fillWithLine;
    //Autoriza el dibujo del boceto actual
    private boolean draw_flag;
    //Autoriza el dibujo del boceto de manera de ver como encaja en el lienzo
    private boolean draw_preview;
    //Guarda el tamano del grosor
    private BasicStroke grosor;
    //Vectores de las coordenadas (x,y) para el triangulo
    private int[] x;
    private int[] y;
    //Donde se almacena la imagen de fondo
    private Image Imagen;
    //Donde se almacena todos los bocetos
    private BufferedImage BIDibujo;
    //Es la figura actual que debe dibujar sobre el lienzo, es null si es Texto
    private Shape figura;
    //Destingue el modo en que se dibuja (lapiz, ovalo, texto, etc.)
    protected int Shapetype;
    //Almacena las coordenadas mas cercanas al lienzo,mediante Math.min()
    private Point Pstart;
    //Almacena las coordenadas mas lejanas al lienzo, mediante Math.max()
    private Point Pfinish;
    //Almacena la altura y ancho si se tratase de una figura
    private Point PwidthHeight;
    //Almacena las dimensiones del lienzo
    private Dimension DLienzo;
    //Almacena la ruta a dibujar dado por el lapiz
    private GeneralPath GPlinea;
    //Decide si dibujar el angulo recto del triangulo en su lado inferior o superior
    private boolean btriangulo;
    //Son las constantes con las que se compraran con btriangulo
    private final boolean REGULAR = true;//Dibuja el lado recto en la parte inferior
    //private final boolean INVERTIDO = false;//Dibuja el lado recto en la parte superior
    //Estas constantes son las que se compararan con ShapeType
    protected final int LAPIZ = 1;
    protected final int BORRADOR = 2;
    protected final int LINEA = 3;
    protected final int RECTANGULO = 4;
    protected final int OVALO = 5;
    protected final int TRIANGULO = 6;
    protected final int TEXTO = 7;

    /**
     * Constructor del lienzo
     */
    Lienzo() {
        DLienzo = new Dimension(650, 250);
        setSize(DLienzo);
        /**
         *el BUfferImage BIDibujo se inicializa aqui, porque es donde
         *se va estar almacenado los bocetos y lo necesito en memoria
         *siempre durante la ejecucion del programa
         *El BufferImage BIFondo no se inicializo, ya que sobre ese se dibuja
         *primero el fondo, luego se le agrega los bocetos de BIDibujo.
         *
         *BIDibujo tiene le di su propiedad de BufferedImage.TRANSLUCENT para no interferir
         *con el fondo dado por BIFondo. La otra forma es un Transparency.TRANSLUCENT
         */
        BIDibujo = new BufferedImage(DLienzo.width, DLienzo.height, BufferedImage.TRANSLUCENT);
        fill_color = Color.WHITE;//Posteriormente se cambia a Negro
        line_color = Color.BLACK;
        text_color = Color.BLACK;
        grosor = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        Pstart = new Point(0, 0);
        Pfinish = new Point(DLienzo.width, DLienzo.height);
        PwidthHeight = new Point(DLienzo.width, DLienzo.height);
        GPlinea = new GeneralPath();
        GPlinea.moveTo(first_x, first_y);//Inicializar, es importante en este punto
        draw_flag = false;
        draw_preview = false;
        btriangulo = REGULAR;
    }

    /**
     * Limpiar los bocetos del lienzo
     */
    public void cleanCanvas() {
        this.BIDibujo = new BufferedImage(DLienzo.width, DLienzo.height, BufferedImage.TRANSLUCENT);
        repaint();
    }

    /**
     * Este es el metodo que "autoriza" el trazo
     * de una manera temporal
     * @param d Booleano que se almacena en draw_preview
     */
    public void setDraw_preview(boolean d) {
        this.draw_preview = d;
        repaint();
    }

    /**
     * Dibuja el boceto actual
     */
    public void draw() {
        draw_flag = true;
        repaint();
    }

    /**
     * Cambia el grosor que se dibuja el lapiz,
     * borrador, linea y contornos
     * Solo tiene una setencia, para probar diferentes estilos
     * de lineas, solo remueve los diagonales de uno de las
     * demas setencias.
     * @param g Entero que es el valor
     * 			del grosor de linea o contorno
     */
    public void setGrosor(int g) {
        this.grosor = new BasicStroke(g, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        //this.grosor = new BasicStroke(g,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
        //this.grosor = new BasicStroke(g,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_MITER);
        //this.grosor = new BasicStroke(g,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_BEVEL);
    }

    /**
     * Cambia la orientacion en que se dibuja los
     * triangulos
     * @param b Booleano que se almacena en btriangulo
     */
    public void setTriangleOrientation(boolean b) {
        this.btriangulo = b;
    }

    /**
     * Inicializa el dibujo de lapiz o borrador
     */
    public void startGPlinea() {
        GPlinea = new GeneralPath();
        GPlinea.moveTo(first_x, first_y);
    }

    /**
     * Este compone el Font, Color y Texto
     * que se va a imprimir
     * @param f Font del texto que se va imprimir
     * @param c Color del texto que se va imprimir
     * @param s Cadena de caracteres a imprimir
     */
    public void setFontColorString(Font f, Color c, String s) {
        JDrawFont = f;
        text_color = c;
        Stexto = s;
    }

    /**
     * Cambia el valor del color de las lineas
     * y contornos
     * @param c Color de la linea o contorno
     */
    public void setLineColor(Color c) {
        this.line_color = c;
    }

    /**
     * Cambia el valor del color del relleno
     * @param c Color del relleno
     */
    public void setFillColor(Color c) {
        this.fill_color = c;
    }

    /**
     * Se guarda en memoria las primeras coordenadas
     * de donde se inicia un dibujo
     * @param width Entero de la coordenada "x"
     * @param height Entero de la coordenada "y"
     */
    public void setFirstCoordinates(int width, int height) {
        first_x = width;
        first_y = height;
    }

    /**
     * Guarda en memoria las ultimas coordenadas y encuentra
     * las coordenadas (x,y) del menor y mayor distancia respecto
     * al lienzo. Tambien consigue la altura y longitud
     * @param width Entero que guarda la ultima coordenada "x"
     * @param height Entero que guarda la ultima coordenada "y"
     */
    public void setLastCoordinates(int width, int height) {
        last_x = width;
        last_y = height;
        Pstart.setLocation(Math.min(first_x, last_x), Math.min(first_y, last_y));
        Pfinish.setLocation(Math.max(first_x, last_x), Math.max(first_y, last_y));
        PwidthHeight.setLocation(Math.abs(last_x - first_x), Math.abs(last_y - first_y));
    }

    /**
     * Recolecta el File y consigue guardarlo como
     * una instancia de Image
     * @param file File de la imagen de fondo
     * @exception IOException Si no es imagen lo que esta guardando
     */
    public void setImageBackground(File file) throws IOException {
        if (file == null) {
            return;
        }
        Imagen = ImageIO.read(file);
        repaint();
    }

    /**
     * Elimina la variable Imagen, para que ya no
     * lo dibuje como fondo en el lienzo
     */
    public void removeImageBackground() {
        this.Imagen = null;
        repaint();
    }

    /**
     * Guarda el tipo de figura que se dibuja actualmente
     * @param s Entero que luego se compara con las constantes
     *			de la clase Lienzo
     */
    public void setShape(int s) {
        this.Shapetype = s;
    }

    /**
     * Si el boceto es una figura, se dibuja con relleno
     * @param f Booleano que decide dibujar
     *			con o sin relleno las figuras
     */
    public void setFill(boolean f) {
        this.fill = f;
    }

    /**
     * Si el boceto es una figura, se establece si se dibuja su contorno
     * @param fl Booleano que decide si se dibuja
     *			con o sin contorno las figuras
     */
    public void setFillWithLine(boolean fl) {
        this.fillWithLine = fl;
    }

    /**
     * Prepara las coordenas al momento de dibuja un triangulo
     * Aqui utilizo la referencia de los grados de la manera formal, es decir,
     * a partir de la horizontal hacia la izquierda en contra de las manecillas
     * del reloj
     */
    public void setTriangleCoordinates() {
        if ((Pstart.x == first_x && Pstart.y == first_y) || (Pstart.x == last_x && Pstart.y == last_y)) {
            /*Esto es, si dibujamos una linea inclinada con -theta grados*/
            if (btriangulo == REGULAR) {
                x = new int[]{Pstart.x, Pfinish.x, Pstart.x};
                y = new int[]{Pstart.y, Pfinish.y, Pfinish.y};
            } else {//Si es INVERTIDO
                x = new int[]{Pstart.x, Pfinish.x, Pfinish.x};
                y = new int[]{Pstart.y, Pfinish.y, Pstart.y};
            }
        } else {
            /*Si dibujamos una linea inclinada con +theta grados*/
            if (btriangulo == REGULAR) {
                x = new int[]{Pstart.x, Pfinish.x, Pfinish.x};
                y = new int[]{Pfinish.y, Pstart.y, Pfinish.y};
            } else {//Si es INVERTIDO
                x = new int[]{Pstart.x, Pfinish.x, Pstart.x};
                y = new int[]{Pfinish.y, Pstart.y, Pstart.y};
            }
        }
    }

    /**
     * Devuelve la figura lista para su aplicacion
     * en la tecnica del DobleBuffer
     * @return figura Instancia de {@link Shape}
     */
    private Shape getShape() {
        switch (Shapetype) {
            case LAPIZ:
            case BORRADOR:
                GPlinea.lineTo(last_x, last_y);
                figura = GPlinea;
                break;
            case LINEA:
                figura = new Line2D.Double(first_x, first_y, last_x, last_y);
                break;
            case RECTANGULO:
                figura = new Rectangle2D.Double(Pstart.x, Pstart.y, PwidthHeight.x, PwidthHeight.y);
                break;
            case OVALO:
                figura = new Ellipse2D.Double(Pstart.x, Pstart.y, PwidthHeight.x, PwidthHeight.y);
                break;
            case TRIANGULO:
                setTriangleCoordinates();//Compone las coordenadas de x[] y y[]
                figura = new Polygon(x, y, 3);
                break;
            case TEXTO:
                System.out.println("Devuelve 'null' porque estas dibujando en modo de TEXTO");
                figura = null;
                break;
            default://Esto corre la primera vez desde el constructor
                Shapetype = LINEA; //Despues de limpiar Canvas, pasa a linea
                setFill(false);//Inicializa el booleano de relleno
                setFillColor(Color.BLACK);
        }
        return figura;
    }

    /**
     * Dibuja los bocetos con la tecnica de Doble Buffer
     * @param g2 Instancia de Graphics2D
     */
    private void pintarFueraPantalla(Graphics2D g2) {
        /*Aqui aplica el Doble Buffer para los bocetos
         *para evitar el parpadeo
         **/
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//antialiasing
        g2.setStroke(grosor);
        if (fill) {
            g2.setColor(fill_color);
            g2.fill(getShape());//Dibuja la figura rellena sin contorno
            if (fillWithLine) {
                g2.setColor(line_color);//Dibuja la figura rellena con contorno
                g2.draw(getShape());
            }
        } else {
            /*
             *En esta parte califica si estas en modo de TEXTO,
             *LINEA, LAPIZ O BORRADOR
             **/
            if (Shapetype == TEXTO) {
                g2.setFont(JDrawFont);
                g2.setColor(text_color);
                g2.drawString(Stexto, first_x, first_y);
            } else {
                //Si no fue TEXTO, se elige entre LINEA, LAPIZ O BORRADOR
                if (Shapetype == BORRADOR) {
                    g2.setColor(Color.WHITE);//Color blanco para borrar
                } else {
                    g2.setColor(line_color);//Dibuja la figura sin relleno
                }
                g2.draw(getShape());
            }
        }
    }

    /**
     * Aplica la primera tecnica del Doble Buffer, para
     * el BufferedImage temporal, BIFondo
     * @param g2 Instancia de Graphics2D
     */
    private void fondoFueraPantalla(Graphics2D g2) {
        /*La ideologia es la siguiente, se dibuja el fondo blanco
         *luego la imagen de fondo, despues los bocetos*/
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//antialiasing
        g2.setColor(Color.WHITE);
        g2.fill(new Rectangle2D.Double(0, 0, DLienzo.getWidth(), DLienzo.getHeight()));
        /*
         *Esto es el lienzo blanco
         */
        if (Imagen != null) {
            g2.drawImage(Imagen, 0, 0, this);
        }

        if ((getShape() != null || Shapetype == TEXTO) && draw_flag) {
            /*La primera condicion es importante para evitar errores de inicializacion
             *Lo segundo evita el repintar innecesario.
             *Para TEXTO, getShape() retorno un null, por eso debo incluir
             *el "or"
             **/
            Graphics2D g2dDibujo = BIDibujo.createGraphics();//g2dDibujo manipula los bocetos
            g2dDibujo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//antialiasing
            pintarFueraPantalla(g2dDibujo);//Este metodo es el Doble Buffering//***********
            draw_flag = false;
        }

        g2.drawImage(BIDibujo, 0, 0, this);//Agrega los bocetos al fondo

        /**************Este es la vista previa de los bocetos****/
        if (getShape() != null && draw_preview) {
            pintarFueraPantalla(g2);
        }
        /********************************************************/
    }

    /**
     * Es el que sobre escribe el metodo original de update()
     * @param g Es de tipo Graphics
     */
    @Override//Sobreescribe el metodo update(Graphics g)
    public void update(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        BufferedImage BIFondo = new BufferedImage(DLienzo.width, DLienzo.height, BufferedImage.TYPE_INT_RGB);
        /* BIFondo es temporal porque realmente no lo
         * necesito que guarde en memoria lo anterior,
         * si igual lo va sobreescribir con un
         * rectangulo relleno de blanco
         **/
        Graphics2D g2dFondo = BIFondo.createGraphics();

        g2dFondo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//antialiasing

        fondoFueraPantalla(g2dFondo);//Doble Buffer del fondo

        g2.drawImage(BIFondo, 0, 0, this);//Dibuja el producto final
    }

    /**
     * Sobre escribe el metodo paint()
     * @param g Es de tipo Graphics
     */
    @Override
    public void paint(Graphics g) {
        update(g);
    }
}
