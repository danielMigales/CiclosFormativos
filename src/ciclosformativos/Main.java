package ciclosformativos;

import java.util.Scanner;

/**
 * @Daniel Migales
 */
public class Main {

    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) {

        Conexion bd = null;
        Scanner teclado = new Scanner(System.in);
        boolean salir = true;

        try {

            bd = new Conexion();
            do {
                System.out.println("\n----------------MENU PRINCIPAL-------------\n");
                System.out.println("1. Crear ciclo formativo nuevo." + ANSI_BLUE + "(CREAR TABLA NUEVA)" + ANSI_RESET);
                System.out.println("2. Añadir modulo." + ANSI_BLUE + "(INSERTAR UN REGISTRO EN LA TABLA)" + ANSI_RESET);
                System.out.println("3. Editar modulo." + ANSI_BLUE + "(EDITAR UN REGISTRO DE LA TABLA)" + ANSI_RESET);
                System.out.println("4. Borrrar modulo. " + ANSI_BLUE + "(BORRAR UN REGISTRO DE LA TABLA)" + ANSI_RESET);
                System.out.println("5. Borrar ciclo formativo. " + ANSI_BLUE + "(BORRAR UNA TABLA)" + ANSI_RESET);
                System.out.println("6. Listar los ciclos formativos que hay en mi instituto. "
                        + ANSI_BLUE + "(REALIZAR CONSULTA DE UNA TABLA)" + ANSI_RESET);
                System.out.println("7. Salir del programa.");

                System.out.println("\n" + "Elija una opcion.");
                int opcion = teclado.nextInt();

                switch (opcion) {

                    case 1:
                        bd.crearCiclo();
                        break;
                    case 2:
                        bd.insertarDatos();
                        break;
                    case 3:
                        bd.editarDatos();
                        break;
                    case 4:
                        bd.borrarRegistro();
                        break;
                    case 5:
                        bd.eliminaTabla();
                        break;
                    case 6:
                        bd.consultarCiclos();
                        break;
                    case 7:
                        salir = false;
                        break;
                }
            } while (salir);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bd != null) {
                bd.desconectar();
            }
        }
    }
}
