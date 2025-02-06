package agg;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        File directorioActual = new File(System.getProperty("user.dir"));

        int opcion;
        do {
            System.out.println("\n=== Menú de Procesos ===");
            System.out.println();
            System.out.println("Directorio actual: " + directorioActual.getAbsolutePath());
            System.out.println("1. Listar archivos del directorio (dir)");
            System.out.println("2. Cambiar de directorio (cd)");
            System.out.println("3. Leer archivo de texto (type)");
            System.out.println();
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    ejecutarComando(new String[]{"cmd", "/c", "dir"}, directorioActual);
                    break;
                case 2:
                    System.out.print("Ingrese el nuevo directorio: ");
                    String nuevoDirectorio = scanner.nextLine().trim();

                    File nuevoDir;
                    if (nuevoDirectorio.equals("..")) {
                        nuevoDir = directorioActual.getParentFile();
                    } else {
                        nuevoDir = new File(directorioActual, nuevoDirectorio);
                    }

                    if (nuevoDir != null && nuevoDir.exists() && nuevoDir.isDirectory()) {
                        System.out.println("DIRE NUEVO:");
                        System.out.println(nuevoDir);
                        System.out.println(nuevoDir.exists());
                        System.out.println();

                        directorioActual = nuevoDir.getAbsoluteFile();
                        System.out.println("Directorio cambiado a: " + directorioActual.getAbsolutePath());
                    } else {
                        System.out.println("Directorio no válido.");
                    }
                    break;

                case 3:
                    ejecutarComando(new String[]{"cmd","/c","dir *.txt"},directorioActual);
                    boolean txts = ejecutarComandoYComprobarSalida(new String[]{"cmd","/c","dir *.txt"},directorioActual);

                    if(txts){
                        System.out.print("Ingrese el archivo a leer: ");
                        String ficheroALeer = scanner.nextLine().trim();

                        ejecutarComando(new String[]{"cmd","/c","type",ficheroALeer},directorioActual);
                    }else{
                        System.out.println("No hay txts en el directorio actual");
                    }

                    break;
                case 0:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);

        scanner.close();
    }

    private static boolean ejecutarComandoYComprobarSalida(String[] comando, File directorio) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(comando);
            processBuilder.directory(directorio);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String linea;
            boolean encontrado = false;

            while ((linea = reader.readLine()) != null) {
                if (linea.contains(".txt")) {
                    encontrado = true;
                    break;
                }
            }

            process.waitFor();
            return encontrado;

        } catch (IOException | InterruptedException e) {
            System.err.println("Error al ejecutar el comando: " + e.getMessage());
            return false;
        }
    }

    private static void ejecutarComando(String[] comando, File directorio) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(comando);
            processBuilder.directory(directorio);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }

            int exitCode = process.waitFor();
            System.out.println("\nProceso terminado con código: " + exitCode);

        } catch (IOException | InterruptedException e) {
            System.err.println("Error al ejecutar el comando: " + e.getMessage());
        }
    }
}