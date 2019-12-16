package ciclosformativos;

/**
 * @Daniel Migales
 */
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Conexion {

    private static Connection conection;
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String user = "root";
    private static final String password = "";
    private static final String url = "jdbc:mysql://localhost:3306/ciclosformativos";

    private static String nombreTabla;
    private static ArrayList<String> listaCampos;
    private static ArrayList<String> tipoCampos = new ArrayList<>();

    public Conexion() {
        conection = null;
        try {
            Class.forName(driver);
            conection = (Connection) DriverManager.getConnection(url, user, password);
            if (conection != null) {
                System.out.println("Conexion a la base de datos correcta.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al conectar a la base de datos." + e);
        }
    }

    public void eliminaTabla() throws SQLException {
        Statement st = null;
        Scanner teclado = new Scanner(System.in);
        try {
            System.out.println("Introduce el nombre de la tabla a borrar: ");
            String nombreTabla = teclado.nextLine();
            st = conection.createStatement();
            String sql = "DROP TABLE IF EXISTS " + nombreTabla + "";
            st.executeUpdate(sql);
            System.out.println("Tabla eliminada");
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public void crearCiclo() throws SQLException {

        Statement st = null;
        Statement update;
        String nombreCampo;
        int opcion, tipoCampo;
        String act;
        listaCampos = new ArrayList<>();
        tipoCampos = new ArrayList<>();
        Scanner teclado = new Scanner(System.in);
        Scanner tecladoStrings = new Scanner(System.in);

        System.out.println("Introduzca el nombre de la tabla a crear.");
        nombreTabla = teclado.nextLine();

        try {
            st = conection.createStatement();
            update = conection.createStatement();
            eliminaTabla();

            String sql = "CREATE TABLE " + nombreTabla
                    + "(id SERIAL PRIMARY KEY)";
            st.executeUpdate(sql);

            System.out.println("Introduce el numero de campos de la tabla: ");
            opcion = teclado.nextInt();

            for (int i = 0; i < opcion; i++) {
                System.out.println("Introduce el nombre del campo: ");
                nombreCampo = tecladoStrings.nextLine();
                listaCampos.add(nombreCampo);
                System.out.println("Introduzca (1) para tipo VARCHAR, Introduzca (2) para INTEGER: ");
                do {
                    tipoCampo = teclado.nextInt();
                } while (tipoCampo != 1 && tipoCampo != 2);

                if (tipoCampo == 1) {
                    tipoCampos.add(i, "VARCHAR (50)");
                    System.out.println("Se ha añadido un campo de texto.");
                } else {
                    tipoCampos.add(i, "INT");
                    System.out.println("Se ha añadido un campo numerico.");
                }

                act = "ALTER TABLE " + nombreTabla + " ADD COLUMN " + listaCampos.get(i) + " " + tipoCampos.get(i) + " ";
                update.executeUpdate(act);
            }
            System.out.println("La tabla ha sido creada exitosamente.");

        } finally {

            if (st != null) {
                st.close();
            }
        }
    }

    public void desconectar() {
        conection = null;
    }

    public void getTabla() throws SQLException {

        Statement st = null;
        String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'ciclosformativos'";

        try {
            st = conection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            System.out.println("Listado de tablas en la Base de Datos: ");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }

        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public void getCamposTabla() throws SQLException {

        sc.nextLine();
        listaCampos = new ArrayList<>();
        tipoCampos = new ArrayList<>();
        getTabla(); // Llamar al metodo para escoger una tabla
        System.out.println("Introduce el nombre de una tabla");
        nombreTabla = sc.nextLine();
        Statement st = null;
        Statement st2 = null;
        String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + nombreTabla + "'";
        String sql2 = "SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + nombreTabla + "'";

        try {
            // Consulta para recoger los nombres de los campos

            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                listaCampos.add(rs.getString(1));
            }

            // Consulta para recoger el tipo de datos 
            st2 = conn.createStatement();
            ResultSet rs2 = st.executeQuery(sql2);
            while (rs2.next()) {
                tipoCampos.add(rs2.getString(1));
            }
        } finally {
            if (st != null) {
                st.close();
                st2.close();
            }
        }
    }

}
