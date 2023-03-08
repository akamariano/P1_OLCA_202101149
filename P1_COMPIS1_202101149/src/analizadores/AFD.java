/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadores;

/**
 *
 * @author Mariano Rack
 */
public class AFD {
    private Nodo_Binario arbol_expresion;
    private int num_nodo = 0;

    public Nodo_Binario getArbol_expresion() {
        return arbol_expresion;
    }

    public AFD(Nodo_Binario arbol_expresion) {
        this.arbol_expresion = arbol_expresion;
    }
    
    public String graficar_arbol(Nodo_Binario nodo, int padre){
        String s = "";
        num_nodo += 1;
        
        int actual = num_nodo;
        if(nodo == null){
            num_nodo -= 1;
            return s;
        }

        s += "N_"+actual+"[label = \""+nodo.getDato().replaceAll("\"", "")+"\"];\n";
        
        if(padre != 0){
                s += "N_"+padre+" -> N_"+actual+";\n";
        }

        s += graficar_arbol(nodo.getIzq(),actual);

        s += graficar_arbol(nodo.getDer(),actual);

        return s;
    }
}