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
        Scanner tecladoStrings = new Scanner(System.in);

        try {
            System.out.println("Introduzca el nombre de la tabla que desea eliminar: ");
            String nombreTabla = tecladoStrings.nextLine();
            st = conection.createStatement();
            String sql = "DROP TABLE IF EXISTS " + nombreTabla + "";
            st.executeUpdate(sql);
            System.out.println("Tabla eliminada con exito.");
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

            System.out.println("\nIntroduce el numero de campos de la tabla: ");
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

        Scanner tecladoStrings = new Scanner(System.in);

        listaCampos = new ArrayList<>();
        tipoCampos = new ArrayList<>();
        getTabla();

        System.out.println("\nIntroduzca el nombre de la tabla: ");
        nombreTabla = tecladoStrings.nextLine();
        Statement st = null;
        Statement st2 = null;
        String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + nombreTabla + "'";
        String sql2 = "SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + nombreTabla + "'";

        try {

            st = conection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                listaCampos.add(rs.getString(1));
            }

            st2 = conection.createStatement();
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

    public void insertarDatos() throws SQLException {

        getCamposTabla();
        listaCampos.remove(0);
        tipoCampos.remove(0);

        Scanner tecladoStrings = new Scanner(System.in);

        String sql = "INSERT INTO " + nombreTabla + "(";

        for (int i = 0; i < listaCampos.size(); i++) {
            sql = sql + listaCampos.get(i) + ",";
        }

        int lastIndexOf = sql.lastIndexOf(",");
        sql = sql.substring(0, lastIndexOf);
        sql = sql + ") VALUES (";

        for (int i = 0; i < listaCampos.size(); i++) {
            if (tipoCampos.get(i).equals("int")) {
                System.out.println("Introduzca el dato del campo " + listaCampos.get(i) + " (numerico)");
                String dato = tecladoStrings.nextLine();
                sql = sql + "'" + dato + "',";
            } else {
                System.out.println("Introduzca el dato del campo " + listaCampos.get(i) + " (texto)");
                String dato = tecladoStrings.nextLine();
                sql = sql + "'" + dato + "',";
            }
        }

        int indice = sql.lastIndexOf(",");
        sql = sql.substring(0, indice);
        sql = sql + ")";

        Statement st = null;
        try {
            st = conection.createStatement();
            st.executeUpdate(sql);
            System.out.println("Datos añadidos.");
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public void editarDatos() throws SQLException {

        getCamposTabla();
        Statement st = null;
        String sql;

        Scanner tecladoStrings = new Scanner(System.in);
        Scanner tecladoInts = new Scanner(System.in);

        System.out.println("Listado de campos de la tabla: ");
        System.out.println(listaCampos);

        System.out.println("\nIntroduzca el ID del campo que quiere modificar: ");
        int id = tecladoInts.nextInt();

        System.out.println("Introduzca el nombre del campo a modificar: ");
        String campo = tecladoStrings.nextLine();

        System.out.println("Introduzca el tipo de dato del campo. Si el dato es texto escriba 1, si es numero escriba 2 : ");
        int tipoDato = tecladoInts.nextInt();

        if (tipoDato == 1) {
            System.out.println("Introduzca el nuevo valor: ");
            String nuevoValor = tecladoStrings.nextLine();
            sql = "UPDATE " + nombreTabla + " SET " + campo + " = '" + nuevoValor + "' WHERE id = " + id + "";
        } else {
            System.out.println("Introduzca el nuevo valor: ");
            int nuevoDato = tecladoInts.nextInt();
            sql = "UPDATE " + nombreTabla + " SET " + campo + " = '" + nuevoDato + "' WHERE id = " + id + "";
        }

        try {
            st = conection.createStatement();
            st.executeUpdate(sql);
            System.out.println("Registro actualizado con exito");

        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public void borrarRegistro() throws SQLException {

        getTabla();

        Scanner tecladoStrings = new Scanner(System.in);
        Scanner tecladoInts = new Scanner(System.in);

        System.out.println("\nIntroduzca el nombre de la tabla: ");
        nombreTabla = tecladoStrings.nextLine();
        System.out.println("Introduzca el ID del registro que desea eliminar: ");
        int id = tecladoInts.nextInt();

        String sql = "DELETE FROM " + nombreTabla + " WHERE id = " + id + "";

        Statement st = null;

        try {
            st = conection.createStatement();
            st.executeUpdate(sql);
            System.out.println("Registro eliminado con exito.");
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public void consultarCiclos() throws SQLException {

        getCamposTabla();
        String sql = "SELECT * FROM " + nombreTabla + "";

        Statement st = null;

        try {
            st = conection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int contador = 0;
            System.out.println(" ");

            while (rs.next()) {
                for (int i = 0; i < listaCampos.size(); i++) {
                    String dato = rs.getString(listaCampos.get(i));
                    System.out.println(listaCampos.get(i) + ": " + dato);
                }

                System.out.println(" ");

                contador++;
            }

            if (contador == 0) {
                System.out.println("No hay datos en esta tabla.");
            }

        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

}
