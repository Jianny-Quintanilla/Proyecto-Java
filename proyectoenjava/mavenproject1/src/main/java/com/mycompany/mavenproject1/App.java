package com.mycompany.mavenproject1;

import java.util.Scanner;
import java.util.Random;

public class App {
    static Scanner sc = new Scanner(System.in);
    static Random rand = new Random();

    static String[] nombres = new String[51], paralelos = new String[51], carreras = new String[51], facultades = new String[51];
    static int[] puntos = new int[51], rachas = new int[51], misionesHechas = new int[51], limpiezasUsuario = new int[51], primeraLimpiezaDia = new int[51];
    static String[] nombresZonas = {"", "Bosque", "Entrada", "Bloque 5", "Gimnasio", "Comedores"};
    static int[] residuosZonas = new int[6], limpiezaZonas = new int[6], limpiezasZonas = new int[6];
    static String[] estadosZonas = new String[6], prioridadesZonas = new String[6];
    static int totalUsuarios = 0, diaActual = 1, totalLimpiezasGlobal = 0;
    static boolean[][] historialLimpieza = new boolean[6][6];
    static boolean[] logroPrimeraLimpieza = new boolean[51];
    static boolean[] logroEcoGuardian = new boolean[51];
    static boolean[] logroVeterano = new boolean[51];
    static boolean[] logroLeyenda = new boolean[51];
    static String[] bancoMisiones = {
    "Recoge 10 residuos",
    "Recoge 20 residuos",
    "Limpia una zona verde",
    "Limpia una zona critica",
    "Interviene el Bosque",
    "Interviene Comedores",
    "Realiza una limpieza rapida",
    "Ayuda en una zona prioritaria",
    "Completa una actividad ambiental"
    };

    public static void main(String[] args) {
        inicializarZonas();
        String registrar;
        do {
            totalUsuarios++;
            System.out.println("Ingrese nombre:"); nombres[totalUsuarios] = sc.next();
            System.out.println("Ingrese paralelo:"); paralelos[totalUsuarios] = sc.next();
            System.out.println("Ingrese carrera:"); carreras[totalUsuarios] = sc.next();
            System.out.println("Ingrese facultad:"); facultades[totalUsuarios] = sc.next();
            puntos[totalUsuarios] = 0;
            rachas[totalUsuarios] = 0;
            misionesHechas[totalUsuarios] = 0;
            limpiezasUsuario[totalUsuarios] = 0;
            primeraLimpiezaDia[totalUsuarios] = 1;
            System.out.println("Desea registrar otro usuario? (SI/NO)"); registrar = sc.next();
        } while (!registrar.equalsIgnoreCase("NO"));

        int u = 1, op = 0;
        while (op != 8) {
            System.out.println("\n===== MENU GREENZONE - DIA " + diaActual + " =====\n1.Perfil\n2.Misiones\n3.RankUp\n4.Limpieza\n5.Estadisticas\n6.Cambiar Usuario\n7.Avanzar Dia\n8.Salir");
            op = sc.nextInt();
            switch (op) {
                case 1: mostrarPerfil(u); break;
                case 2: realizarMisiones(u); break;
                case 3: rankUp(); break;
                case 4: realizarLimpieza(u); break;
                case 5: mostrarEstadisticas(); break;
                case 6:
                    System.out.println("Usuario actual: " + u);
                    int nuevo = sc.nextInt();
                    if(nuevo >= 1 && nuevo <= totalUsuarios){
                        u = nuevo;}else{System.out.println("Usuario no valido.");
                    }
                    break;
                case 7: avanzarDia(); 
                break;
            }
        }
    }

    private static void realizarLimpieza(int u) {
        System.out.println("Seleccione zona (1-5):");
        for (int i = 1; i <= 5; i++) {
          System.out.println(i + ". " +nombresZonas[i] +" [Estado: " +estadosZonas[i] +" | Prioridad: " +prioridadesZonas[i] +" | Residuos: " +residuosZonas[i] +"%]");
}
        int z = sc.nextInt();
        if(z < 1 || z > 5){
            System.out.println("Zona invalida.");
            return;
        }
        if(residuosZonas[z] == 0){
            System.out.println("La zona ya esta limpia.");
            return;
        }
        String estadoAnterior = estadosZonas[z];
        // Simulación visual
        for(int f=1; f<=8; f++) { 
            for(int c=1; c<=8; c++) 
            System.out.print((rand.nextInt(100) < residuosZonas[z] ? "* " : "  ")); 
        System.out.println(); 
        }
        System.out.println("Ingrese la fecha de limpieza:"); 
        sc.nextLine(); 
        String fecha = sc.nextLine();
        System.out.println("Ingrese el tiempo empleado (minutos):"); 
        int tiempo = sc.nextInt(); 
        
        // Cálculo puntos
        
        int pEstado = estadosZonas[z].equals("Critica")? 40 : 0;
        int pPrioridad = prioridadesZonas[z].equals("Media")? 20 : 0;
        int pTiempo = tiempo < 15 ? 10 : 0;
        int pRacha = rachas[u] > 0? 15 : 0;
        int pPrimera = primeraLimpiezaDia[u] == 1 ? 10 : 0;
        int pts = pEstado +pPrioridad +pTiempo +pRacha +pPrimera;
        residuosZonas[z] = 0; 
        puntos[u] += pts; 
        rachas[u]++; 
        limpiezasUsuario[u]++;
        limpiezasZonas[z]++;
        totalLimpiezasGlobal++;
        verificarLogros(u);
        primeraLimpiezaDia[u] = 0;
        actualizarEstado(z); 
        System.out.println("Estado anterior: "+ estadoAnterior);
        System.out.println("Estado actual: "+ estadosZonas[z]);
        if(diaActual <= 5){
            historialLimpieza[diaActual][z] = true;
        }
        System.out.println("Puntaje ganado: +" + pts);
        System.out.println();
        System.out.println("===== REPORTE MATEMATICO =====");
        System.out.println("Area critica: +"+ pEstado);
        System.out.println("Prioridad media: +"+ pPrioridad);
        System.out.println("Bono rapidez: +"+ pTiempo);
        System.out.println("Bono racha: +"+ pRacha);
        System.out.println("Primera limpieza: +"+ pPrimera);
        System.out.println("----------------");
        System.out.println("TOTAL: +"+ pts);
    }
    private static void verificarLogros(int u){
        if(!logroPrimeraLimpieza[u]&& limpiezasUsuario[u] >= 1){
            logroPrimeraLimpieza[u] = true;
            System.out.println();
            System.out.println("=====🏅 LOGRO DESBLOQUEADO=====");
            System.out.println("Primera Limpieza");
        }
        if(!logroEcoGuardian[u]&& puntos[u] >= 100){
            logroEcoGuardian[u] = true;
            System.out.println();
            System.out.println("=====🏅 LOGRO DESBLOQUEADO=====");
            System.out.println("Eco Guardian");
        }
        if(!logroVeterano[u]&& limpiezasUsuario[u] >= 5){
            logroVeterano[u] = true;
            System.out.println();
            System.out.println("=====🏅 LOGRO DESBLOQUEADO=====");
            System.out.println("Veterano Verde");
        }
        if(!logroLeyenda[u]&& puntos[u] >= 500){
            logroLeyenda[u] = true;
            System.out.println();
            System.out.println("=====🏆 LOGRO ESPECIAL=====");
            System.out.println("Leyenda GreenZone");
        }
    }
    
     private static void mostrarEstadisticas() {
        System.out.println("===== ESTADISTICAS AVANZADAS =====");
        int maxL = -1; 
        String masActivo = "";
        for (int i = 1; i <= totalUsuarios; i++) {
            if (limpiezasUsuario[i] > maxL) { 
                maxL = limpiezasUsuario[i]; 
                masActivo = nombres[i]; 
            }
            System.out.println("Limpiezas de " + nombres[i] + ": " + limpiezasUsuario[i]);
        }
        System.out.println("Usuario mas activo: " + masActivo);
        
        double prom = 0;
        if(diaActual > 0){
            prom = (double) totalLimpiezasGlobal / diaActual;
        }
        double porcTotal = 0;
        int mayorResiduo = -1;
        String zonaMasContaminada = "";
        int menorResiduo = 101;
        String zonaMasLimpia = "";
        for (int i = 1; i <= 5; i++) {
            if(residuosZonas[i] > mayorResiduo){
                mayorResiduo = residuosZonas[i];
                zonaMasContaminada =nombresZonas[i];
            }
            if(residuosZonas[i] < menorResiduo){
                menorResiduo =residuosZonas[i];
                zonaMasLimpia =nombresZonas[i];
            }
            System.out.println("Limpiezas en " + nombresZonas[i] + ": " + limpiezasZonas[i] + " | Limpieza actual: " + (100 - residuosZonas[i]) + "%");
            porcTotal += (100 - residuosZonas[i]);
        }
        System.out.println("Zona con mayor necesidad de limpieza: "+ zonaMasContaminada);
        System.out.println("Zona mas limpia: "+ zonaMasLimpia);
        System.out.println("Promedio limpiezas diarias: " + prom + "\nLimpieza general: " + (porcTotal / 5) + "%");
        System.out.println("TABLA SEMANAL:");
        for(int f=1; f<=5; f++) { 
            for(int c=1; c<=5; c++) 
        System.out.print((historialLimpieza[f][c] ? "V " : "F ") + "| "); 
            System.out.println(); }
        System.out.println("\n===== PARTICIPACION =====");
        for(int i=1;i<=totalUsuarios;i++){
    if(totalLimpiezasGlobal > 0){
        double participacion =(double)limpiezasUsuario[i]* 100/ totalLimpiezasGlobal;
        System.out.println(nombres[i]+ ": "+ participacion+ "%");
    }
}
    } 

    private static void inicializarZonas() {
        for (int z = 1; z <= 5; z++) { 
            residuosZonas[z] = rand.nextInt(71) + 10; 
            actualizarEstado(z); }
    }

    private static void actualizarEstado(int z) {
        if (residuosZonas[z] >= 75) { estadosZonas[z] = "Critica"; prioridadesZonas[z] = "Alta"; }
        else if (residuosZonas[z] >= 30) { estadosZonas[z] = "Requiere limpieza"; prioridadesZonas[z] = "Media"; }
        else { estadosZonas[z] = "Limpia"; prioridadesZonas[z] = "Baja"; }
    }

    private static void mostrarPerfil(int u) {
        String nivel = (puntos[u] <= 50) ? "Eco Novato" : (puntos[u] <= 200) ? "Eco Guardian" : (puntos[u] <= 350) ? "Eco Heroe" : "Eco Legendario";
        System.out.println("=====TU PERFIL=====");
        System.out.println("Nombre: " + nombres[u]);
        System.out.println("Carrera: "+ carreras[u]);
        System.out.println("Paralelo "+ paralelos[u]);
        System.out.println("Facultad: "+ facultades[u]);
        System.out.println("Puntos: " + puntos[u]);
        System.out.println("Racha: " + rachas[u]);
        System.out.println("Limpiezas realizadas: " + limpiezasUsuario[u]);
        System.out.println("Nivel: " + nivel);
        System.out.println();
        System.out.println("===== LOGROS =====");
        if(logroPrimeraLimpieza[u]){
            System.out.println("🏅 Primera Limpieza");
        }
        if(logroEcoGuardian[u]){
            System.out.println("🏅 Eco Guardian");
        }
        if(logroVeterano[u]){
            System.out.println("🏅 Veterano Verde");
        }
        if(logroLeyenda[u]){
            System.out.println("🏆 Leyenda GreenZone");
        }
    }

    private static void realizarMisiones(int u) {
        if (misionesHechas[u] == 1) {
            System.out.println("Ya completaste las misiones diarias.");
        } else {
            misionesHechas[u] = 1;
            int puntosGanados = 0;
            
            // Descripción de las misiones
           int m1 = rand.nextInt(bancoMisiones.length);
           int m2 = rand.nextInt(bancoMisiones.length);
           int m3 = rand.nextInt(bancoMisiones.length);
           while(m2 == m1){
               m2 = rand.nextInt(bancoMisiones.length);
           }
           while(m3 == m1 || m3 == m2){
               m3 = rand.nextInt(bancoMisiones.length);
           }
           System.out.println("MISION 1: " + bancoMisiones[m1]);
           System.out.println("¿Completaste la mision? (SI/NO)");
           if (sc.next().equalsIgnoreCase("SI")) {
               puntosGanados += rand.nextInt(16) + 10;
           }
           System.out.println("MISION 2: " + bancoMisiones[m2]);
           System.out.println("¿Completaste la mision? (SI/NO)");
           if (sc.next().equalsIgnoreCase("SI")) {
               puntosGanados += rand.nextInt(16) + 10;
           }
           System.out.println("MISION 3: " + bancoMisiones[m3]);
           System.out.println("¿Completaste la mision? (SI/NO)");
           if (sc.next().equalsIgnoreCase("SI")) {
               puntosGanados += rand.nextInt(16) + 10;
           }
            
            if (puntosGanados > 0) {
                rachas[u]++;
                puntos[u] += puntosGanados + (rachas[u] * 2);
                System.out.println("Puntos obtenidos en misiones: " + puntosGanados);
            } else {
                rachas[u] = 0;
            }
        }
    }
    
    private static void rankUp() {
        System.out.println("====RANKING DE USUARIOS====");
        for(int i=1; i<totalUsuarios; i++) for(int j=i+1; j<=totalUsuarios; j++) if(puntos[j]>puntos[i]) { int aux = puntos[i]; puntos[i] = puntos[j]; puntos[j] = aux; String n = nombres[i]; nombres[i] = nombres[j]; nombres[j] = n; }
        for(int i=1; i<=totalUsuarios; i++) System.out.println(i + ". " + nombres[i] + " - " + puntos[i] + " puntos");
    }

    private static void avanzarDia() {
        diaActual++;
        for (int i = 1; i <= totalUsuarios; i++) {
            primeraLimpiezaDia[i] = 1;
            if(misionesHechas[i] == 0){
                rachas[i] = 0;
            }
            misionesHechas[i] = 0;
        }
        for (int z = 1; z <= 5; z++) { 
            residuosZonas[z] = Math.min(100, residuosZonas[z] + (rand.nextInt(10)+5)); actualizarEstado(z); }
    }
}