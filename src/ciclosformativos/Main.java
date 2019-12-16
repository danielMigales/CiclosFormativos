package ciclosformativos;

import java.util.Scanner;

/**
 * @Daniel Migales
 */
public class Main {

    public static void main(String[] args) {

        Conexion bd = null;
        Scanner teclado = new Scanner(System.in);
        boolean salir = true;

        try {

            bd = new Conexion();
            do {
                System.out.println("\n----------------MENU PRINCIPAL-------------\n");
                System.out.println("1. Crear ciclo formativo nuevo.");
                System.out.println("2. Añadir modulo");
                System.out.println("3. Editar modulo.");
                System.out.println("4. Borrrar modulo.");
                System.out.println("5. Borrar ciclo formativo.");
                System.out.println("6. Listar los ciclos formativos que hay en mi instituto.");
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
