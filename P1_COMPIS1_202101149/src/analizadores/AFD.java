/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadores;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
/**
 *
 * @author Mariano Rack
 */
public class AFD {

    private final Nodo_Binario arbol_expresion;
    private final String nombre;
    private String grafica_arbol_expresion = "";
    private String grafica_afnd = "";
    private String grafica_tabla_siguientes = "";
    private String grafica_tabla_transiciones = "";
    private String grafica_afd = "";
    private final ArrayList<String> terminales = new ArrayList<>();
    private final ArrayList<ArrayList> transiciones = new ArrayList<>();
    private final ArrayList<Tabla_siguientes> siguientes = new ArrayList<>();
    private final ArrayList<Integer> estados_aceptacion = new ArrayList<>();
    private int num_nodo = 0;
    private int num_hoja = 0;

    public Nodo_Binario getArbol_expresion() {
        return arbol_expresion;
    }

    public AFD(Nodo_Binario arbol_expresion, String nombre) {
        Nodo_Binario raiz = new Nodo_Binario(".");
        Nodo_Binario aceptacion = new Nodo_Binario("#");
        aceptacion.setHoja(true);
        raiz.setDer(aceptacion);
        raiz.setIzq(arbol_expresion);
        this.arbol_expresion = raiz;
        this.nombre = nombre;
    }

    public void proceso() throws IOException, InterruptedException {
        numerar_nodos(arbol_expresion);
        calcular_arbol_expresion(arbol_expresion);
        grafica_arbol_expresion += "digraph{label = \"Arbol de expresión\"\n" + graficar_arbol(arbol_expresion, 0) + "}";
        crear_archivo("src/ARBOLES_202101149/", "Arbol " + nombre, grafica_arbol_expresion);
        num_nodo = 1;
        grafica_afnd += "digraph {label = \"AFND " + nombre
                + "\";\nrankdir=\"LR\";\nnode [shape=\"circle\"];"
                + "\nN_0[fontcolor=\"white\"];\n"
                + "\nN_1[shape = doublecircle, fontcolor=\"white\"];\n" + graficar_thompson(0, 1, arbol_expresion.getIzq()) + "}";
        crear_archivo("src/AFND_202101149/", "AFND " + nombre, grafica_afnd);
        calcular_transiciones();
        grafica_tabla_transiciones += "graph{label = \"Tabla transiciones\"\n" + graficar_transiciones() + "}";
        crear_archivo("src/TRANSICIONES_202101149/", "Transiciones " + nombre, grafica_tabla_transiciones);
        grafica_tabla_siguientes += "digraph {label = \"Siguientes " + nombre + "\"\n" + graficar_siguientes() + "}";
        crear_archivo("src/SIGUIENTES_202101149/", "Siguientes " + nombre, grafica_tabla_siguientes);
        grafica_afd += "digraph {label = \"AFD " + nombre + "\"\n" + graficar_AFD() + "}";
        crear_archivo("src/AFD_202101149/", "AFD " + nombre, grafica_afd);
    }

    public void numerar_nodos(Nodo_Binario actual) {
        if (actual == null) {
            return;
        }
        numerar_nodos(actual.getIzq());
        numerar_nodos(actual.getDer());
        if (actual.isHoja()) {
            actual.setNumero(num_hoja);
            num_hoja++;
            siguientes.add(new Tabla_siguientes(actual.getDato(), actual.getNumero()));
            if (!terminales.contains(actual.getDato()) && !actual.getDato().equals("#")) {
                terminales.add(actual.getDato());
            }
        }
    }

    public void calcular_arbol_expresion(Nodo_Binario actual) {
        if (actual == null) {
            return;
        }

        calcular_arbol_expresion(actual.getIzq());
        calcular_arbol_expresion(actual.getDer());

        if (actual.isHoja()) {
            actual.setAnulable(false);
            actual.getPrimeros().add(actual.getNumero());
            actual.getUltimos().add(actual.getNumero());
        } else {
            switch (actual.getDato()) {
                case "*" -> {
                    actual.setAnulable(true);
                    actual.getPrimeros().addAll(actual.getIzq().getPrimeros());
                    actual.getUltimos().addAll(actual.getIzq().getPrimeros());
                    for (int est : actual.getIzq().getUltimos()) {
                        siguientes.get(est).getSiguientes().addAll(actual.getIzq().getPrimeros());
                    }
                }
                case "?" -> {
                    actual.setAnulable(true);
                    actual.getPrimeros().addAll(actual.getIzq().getPrimeros());
                    actual.getUltimos().addAll(actual.getIzq().getPrimeros());
                }
                case "+" -> {
                    actual.setAnulable(actual.getIzq().isAnulable());
                    actual.getPrimeros().addAll(actual.getIzq().getPrimeros());
                    actual.getUltimos().addAll(actual.getIzq().getPrimeros());
                    for (int est : actual.getIzq().getUltimos()) {
                        siguientes.get(est).getSiguientes().addAll(actual.getIzq().getPrimeros());
                    }
                }
                case "|" -> {
                    actual.setAnulable(actual.getIzq().isAnulable() || actual.getDer().isAnulable());
                    actual.getPrimeros().addAll(actual.getIzq().getPrimeros());
                    actual.getPrimeros().addAll(actual.getDer().getPrimeros());
                    actual.getUltimos().addAll(actual.getIzq().getUltimos());
                    actual.getUltimos().addAll(actual.getDer().getUltimos());
                }
                case "." -> {
                    actual.setAnulable(actual.getIzq().isAnulable() && actual.getDer().isAnulable());
                    if (actual.getIzq().isAnulable()) {
                        actual.getPrimeros().addAll(actual.getIzq().getPrimeros());
                        actual.getPrimeros().addAll(actual.getDer().getPrimeros());
                    } else {
                        actual.getPrimeros().addAll(actual.getIzq().getPrimeros());
                    }
                    if (actual.getDer().isAnulable()) {
                        actual.getUltimos().addAll(actual.getIzq().getUltimos());
                        actual.getUltimos().addAll(actual.getDer().getUltimos());
                    } else {
                        actual.getUltimos().addAll(actual.getDer().getUltimos());
                    }
                    for (int est : actual.getIzq().getUltimos()) {
                        siguientes.get(est).getSiguientes().addAll(actual.getDer().getPrimeros());
                    }
                }
            }
        }
    }

    public void calcular_transiciones() {
        int indice = 0;
        transiciones.add(new ArrayList<>());
        ArrayList fila = transiciones.get(0);
        fila.add(arbol_expresion.getPrimeros());
        while (indice < transiciones.size()) {
            //llenar con espacios vacios las columnas de la fila
            fila = transiciones.get(indice);
            for (String s : terminales) {
                fila.add(new ArrayList<>());
            }
            //llenar transiciones
            for (int siguiente : (ArrayList<Integer>) fila.get(0)) {
                String simbolo = siguientes.get(siguiente).getSimbolo();
                if (simbolo.equals("#")) {
                    continue;
                }
                int columna = terminales.indexOf(simbolo) + 1;
                ArrayList<Integer> col_terminal = (ArrayList<Integer>) fila.get(columna);
                for (int i : siguientes.get(siguiente).getSiguientes()) {
                    if (!col_terminal.contains(i)) {
                        col_terminal.add(i);
                    }
                }
                Collections.sort(col_terminal);
            }
            //generar nuevos estados
            boolean encontrado;
            for (int i = 1; i < fila.size(); i++) {
                encontrado = false;
                ArrayList<Integer> estado = (ArrayList<Integer>) fila.get(i);
                //ver si el estado creado por el terminal existe
                for (ArrayList<ArrayList> filas : transiciones) {
                    if (filas.get(0).equals(estado)) {
                        encontrado = true;
                        break;
                    }
                }
                //si no existe hacer un nuevo estado con los siguientes 
                if (!encontrado && !estado.isEmpty()) {
                    ArrayList<ArrayList> nueva_fila = new ArrayList<>();
                    nueva_fila.add(estado);
                    transiciones.add(nueva_fila);
                }
            }
            //bajar una fila
            indice++;
        }
    }

    public String graficar_arbol(Nodo_Binario nodo, int padre) {
        String s = "";
        num_nodo += 1;

        int actual = num_nodo;
        if (nodo == null) {
            num_nodo -= 1;
            return s;
        }

        if (nodo.isHoja()) {
            s += "N_" + actual + "[shape = none label=<\n"
                    + " <TABLE border=\"1\" cellspacing=\"2\" cellpadding=\"10\"  >\n"
                    + "  <TR>\n"
                    + "  <TD colspan=\"3\">" + nodo.isAnulable() + "</TD>\n"
                    + "  </TR>\n"
                    + "  <TR>\n"
                    + "  <TD >" + nodo.getPrimeros() + "</TD>\n"
                    + "  <TD >" + nodo.getDato() + "</TD>\n"
                    + "  <TD >" + nodo.getUltimos() + "</TD>\n"
                    + "  </TR>\n"
                    + "  <TR>\n"
                    + "  <TD colspan=\"3\">" + nodo.getNumero() + "</TD>\n"
                    + "  </TR>\n"
                    + " </TABLE>>];";
        } else {
            s += "N_" + actual + "[shape = none label=<\n"
                    + " <TABLE border=\"1\" cellspacing=\"2\" cellpadding=\"10\"  >\n"
                    + "  <TR>\n"
                    + "  <TD colspan=\"3\">" + nodo.isAnulable() + "</TD>\n"
                    + "  </TR>\n"
                    + "  <TR>\n"
                    + "  <TD >" + nodo.getPrimeros() + "</TD>\n"
                    + "  <TD >" + nodo.getDato() + "</TD>\n"
                    + "  <TD >" + nodo.getUltimos() + "</TD>\n"
                    + "  </TR>\n"
                    + " </TABLE>>];";
        }

        if (padre != 0) {
            s += "N_" + padre + " -> N_" + actual + ";\n";
        }

        s += graficar_arbol(nodo.getIzq(), actual);

        s += graficar_arbol(nodo.getDer(), actual);

        return s;
    }

    public String graficar_siguientes() {
        String s = "label=<\n"
                + " <TABLE border=\"1\" cellspacing=\"2\" cellpadding=\"10\"  >\n"
                + "  <TR>\n"
                + "  <TD>Simbolo</TD>\n"
                + "  <TD>Hoja</TD>\n"
                + "  <TD>Siguientes</TD>\n"
                + "  </TR>\n";
        for (Tabla_siguientes t : siguientes) {
            s += " <TR>\n"
                    + "  <TD>" + t.getSimbolo() + "</TD>\n"
                    + "  <TD>" + t.getHoja() + "</TD>\n"
                    + "  <TD>" + t.getSiguientes() + "</TD>\n"
                    + "  </TR>\n";
        }
        s += " </TABLE>>";
        return s;
    }

    public String graficar_transiciones() {
        String s = "label=<\n"
                + " <TABLE border=\"1\" cellspacing=\"2\" cellpadding=\"10\"  >\n"
                + "  <TR>\n"
                + "  <TD rowspan=\"2\">Estado</TD>\n"
                + "  <TD colspan=\"" + terminales.size() + "\">Terminales</TD>\n"
                + "  </TR>\n"
                + "  <TR>\n";

        for (String terminal : terminales) {
            s += "  <TD>" + terminal + "</TD>\n";
        }
        s += "  </TR>\n"
                + "  \n";
        for (ArrayList<ArrayList> fila : transiciones) {
            s += "  <TR>\n"
                    + "  <TD>S" + transiciones.indexOf(fila) + " " + fila.get(0) + "</TD>\n";
            for (int i = 1; i < fila.size(); i++) {
                ArrayList<Integer> actual = fila.get(i);
                if (actual.isEmpty()) {
                    s += "  <TD>---</TD>\n";
                }
                for (ArrayList<ArrayList> estados : transiciones) {
                    if (estados.get(0).equals(actual)) {
                        s += "  <TD>S" + transiciones.indexOf(estados) + "</TD>\n";
                        break;
                    }
                }
            }
            s += "  </TR>\n";
        }
        s += " </TABLE>>";
        return s;
    }

    public String graficar_thompson(int primero, int ultimo, Nodo_Binario actual) {
        String resultado = "";

        if (actual.isHoja()) {
            resultado += "N_" + primero + " -> N_" + ultimo + "[label=\"" + actual.getDato().replaceAll("\"", "") + "\"];\n";
            return resultado;
        }

        switch (actual.getDato()) {
            case "." -> {
                num_nodo += 1;
                int mitad = num_nodo;
                resultado += "N_" + num_nodo + "[label = \"\"];\n";
                resultado += graficar_thompson(primero, mitad, actual.getIzq());
                resultado += graficar_thompson(mitad, ultimo, actual.getDer());
            }
            case "|" -> {
                num_nodo += 1;
                int nodo_izq = num_nodo;
                resultado += "N_" + nodo_izq + "[label = \"\"];\n";
                resultado += "N_" + primero + " -> N_" + nodo_izq + "[label=\"ε\"];\n";
                num_nodo += 1;
                int nodo_der = num_nodo;
                resultado += "N_" + nodo_der + "[label = \"\"];\n";
                resultado += "N_" + nodo_der + " -> N_" + ultimo + "[label=\"ε\"];\n";
                resultado += graficar_thompson(nodo_izq, nodo_der, actual.getIzq());
                num_nodo += 1;
                nodo_izq = num_nodo;
                resultado += "N_" + nodo_izq + "[label = \"\"];\n";
                resultado += "N_" + primero + " -> N_" + nodo_izq + "[label=\"ε\"];\n";
                num_nodo += 1;
                nodo_der = num_nodo;
                resultado += "N_" + nodo_der + "[label = \"\"];\n";
                resultado += "N_" + nodo_der + " -> N_" + ultimo + "[label=\"ε\"];\n";
                resultado += graficar_thompson(nodo_izq, nodo_der, actual.getDer());
            }
            case "+" -> {
                num_nodo += 1;
                int nodo_izq = num_nodo;
                resultado += "N_" + nodo_izq + "[label = \"\"];\n";
                resultado += "N_" + primero + " -> N_" + nodo_izq + "[label=\"ε\"];\n";
                num_nodo += 1;
                int nodo_der = num_nodo;
                resultado += "N_" + nodo_der + "[label = \"\"];\n";
                resultado += "N_" + nodo_der + " -> N_" + ultimo + "[label=\"ε\"];\n";
                resultado += "N_" + nodo_der + " -> N_" + nodo_izq + "[label=\"ε\"];\n";
                resultado += graficar_thompson(nodo_izq, nodo_der, actual.getIzq());
            }
            case "*" -> {
                num_nodo += 1;
                int nodo_izq = num_nodo;
                resultado += "N_" + nodo_izq + "[label = \"\"];\n";
                resultado += "N_" + primero + " -> N_" + nodo_izq + "[label=\"ε\"];\n";
                resultado += "N_" + primero + " -> N_" + ultimo + "[label=\"ε\"];\n";
                num_nodo += 1;
                int nodo_der = num_nodo;
                resultado += "N_" + nodo_der + "[label = \"\"];\n";
                resultado += "N_" + nodo_der + " -> N_" + ultimo + "[label=\"ε\"];\n";
                resultado += "N_" + nodo_der + " -> N_" + nodo_izq + "[label=\"ε\"];\n";
                resultado += graficar_thompson(nodo_izq, nodo_der, actual.getIzq());
            }
            case "?" -> {
                num_nodo += 1;
                int nodo_izq = num_nodo;
                resultado += "N_" + nodo_izq + "[label = \"\"];\n";
                resultado += "N_" + primero + " -> N_" + nodo_izq + "[label=\"ε\"];\n";
                resultado += "N_" + primero + " -> N_" + ultimo + "[label=\"ε\"];\n";
                num_nodo += 1;
                int nodo_der = num_nodo;
                resultado += "N_" + nodo_der + "[label = \"\"];\n";
                resultado += "N_" + nodo_der + " -> N_" + ultimo + "[label=\"ε\"];\n";
                resultado += graficar_thompson(nodo_izq, nodo_der, actual.getIzq());
            }
        }
        return resultado;
    }

    public String graficar_AFD() {
        String s = "rankdir=\"LR\";\n"
                + "node [shape=\"circle\"];\n"
                + "SI[shape = none, fontcolor=\"white\"];";
        for (int i = 0; i < transiciones.size(); i++) {
            if (((ArrayList<ArrayList>) transiciones.get(i)).get(0).contains(siguientes.size() - 1)) {
                s += "S" + i + "[shape=\"doublecircle\"];\n";
                estados_aceptacion.add(i);
            } else {
                s += "S" + i + ";\n";
            }
        }
        s += "SI->S0[label=\"Inicio\"];\n";
        for (ArrayList<ArrayList> f : transiciones) {
            for (int indice = 1; indice < f.size(); indice++) {
                ArrayList<Integer> actual = f.get(indice);
                for (ArrayList<ArrayList> estados : transiciones) {
                    if (estados.get(0).equals(actual)) {
                        s += "  S" + transiciones.indexOf(f) + "->S" + transiciones.indexOf(estados) + "[label=\"" + siguientes.get(indice - 1).getSimbolo().replaceAll("\"", "") + "\"]";
                        break;
                    }
                }
            }
        }
        return s;
    }

    public boolean analizar_cadena(ArrayList<Conjunto> conjuntos, String cadena) {
        int num_estado = 0;
        int caracter;
        int num_col;
        ArrayList<Integer> transicion;
        boolean encontrado = false;

        for (int indice = 1; indice < cadena.length() - 1; indice++) {
            ArrayList<ArrayList> estado = transiciones.get(num_estado);
            caracter = (int) cadena.charAt(indice);
            encontrado = false;
            for (String t : terminales) {
                if (t.startsWith("\"") && t.endsWith("\"")) {
                    if ((int) t.charAt(1) == caracter) {
                        encontrado = true;
                        num_col = terminales.indexOf(t) + 1;
                        transicion = estado.get(num_col);
                        for (ArrayList<ArrayList> fila : transiciones) {
                            if (fila.get(0).equals(transicion)) {
                                num_estado = transiciones.indexOf(fila);
                                break;
                            }
                        }
                        break;
                    }
                } else if (t.startsWith("\\")) {
                    if ((int) cadena.charAt(indice + 1) == (int) t.charAt(1)) {
                        encontrado = true;
                        num_col = terminales.indexOf(t) + 1;
                        transicion = estado.get(num_col);
                        indice++;
                        for (ArrayList<ArrayList> fila : transiciones) {
                            if (fila.get(0).equals(transicion)) {
                                num_estado = transiciones.indexOf(fila);
                                break;
                            }
                        }
                        break;
                    }
                } else {
                    ArrayList<Integer> evaluado = new ArrayList<>();
                    for (Conjunto c : conjuntos) {
                        if (c.getNombre().equals(t)) {
                            evaluado = c.getCaracteres();
                            break;
                        }
                    }
                    if (evaluado.contains(caracter)) {
                        encontrado = true;
                        num_col = terminales.indexOf(t) + 1;
                        transicion = estado.get(num_col);
                        for (ArrayList<ArrayList> fila : transiciones) {
                            if (fila.get(0).equals(transicion)) {
                                num_estado = transiciones.indexOf(fila);
                                break;
                            }
                        }
                        break;
                    }
                }

            }
            if (!encontrado) {
                return false;
            }
        }

        return estados_aceptacion.contains(num_estado);
    }

    public void crear_archivo(String dir, String nombre, String texto) throws IOException, InterruptedException {
        File file = new File(dir, nombre);

        if (!file.exists()) {
            file.createNewFile();
        }
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(file);
        } catch (FileNotFoundException ex) {

        }

        pw.write(texto);
        pw.flush();
        pw.close();

        String[] c = {"dot", "-Tpng", file.getAbsolutePath(), "-O"};
        Process p = Runtime.getRuntime().exec(c);
        int err = p.waitFor();
        file.delete();
    }

    public String getNombre() {
        return nombre;
    }

}