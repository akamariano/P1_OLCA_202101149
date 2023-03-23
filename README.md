# Manual Técnico Exregan
#
| Carnet            | Nombre      | Auxiliar | Sección|
|-------------------|-------------|------------|--------|
|202101149| Mariano Roberto Rac Noguera | Mynor Ruíz|A|
#
## Introducción

Este manual técnico es una guía para los desarrolladores y técnicos encargados del mantenimiento de la aplicación o bien interesados en el código y desarrollo de la app. Proporciona información detallada sobre la arquitectura, el diseño y las tecnologías utilizadas en el desarrollo de Exregan
## Requerimientos de Software
- Sistema operativo: A su elección
- Procesador: Procesador 64 Bits
- RAM: 4 GB
- Al menos 200 MB de espacio libre en disco duro
- IDE: Idealmente Apache Netbeans o o el de su elección para correr la aplicación o directamente ejecutar el .exe
- Sistema operativo compatible con Java (por ejemplo, Windows, Linux o Mac OS)
- Java Development Kit (JDK) instalado en la computadora. La versión de JDK debe ser compatible con la versión de Java utilizada para desarrollar el programa.

# Arquitectura
Se sigue una estructura de capas y utiliza patrones de diseño para separar las responsabilidades y mejorar la modularidad del código.
- Capa de análisis léxico: Esta capa utiliza Flex para realizar el análisis léxico del código fuente y generar una secuencia de tokens.
- Capa de análisis sintáctico: Esta capa utiliza Cup para realizar el análisis sintáctico del código fuente y generar un árbol de análisis sintáctico.
- Capa de generación de código: Esta capa se encarga de generar el código objeto o el código ejecutable a partir del árbol de análisis sintáctico y la información recopilada en las capas anteriores.

## Tecnologías utilizadas

Exregan utiliza las siguientes tecnologías:

* Java- para el desarrollo del programa
* Graphviz - para la  elaboración de reportes

## Introducción
# Estructura del Código

Para el desarrollo de la aplicación se implementó uso de las herramientas de JFLEX y CUP dentro del entorno de Java, utilizando como IDE ApacheNetbeans. El código está estructurado de la siguiente manera: 
- UI: La interfaz gráfica con los botones necesarios para manipular el archivo de entrada o bien redactar uno desde cero,
- Package Analizadores: Contiene los distintos paquetes que dictaminan el funcionamiento directo de la aplicación, siendo los principales por medio de Flex y Cup el analizador Léxico y sintáctico, así también las clases que generanel AFD, el análisis por Conjuntos, el árbol binario, el tester de cadenas y la tabla de siguientes
- Se generan también carpetas con las salidas
neas.

## Diseño de la Interfaz de Usuario
![Interfaz Gráfica](https://github.com/akamariano/P1_OLCA_202101149/blob/main/UIEXREGAN.png)
Para la interfaz de usuario se utilizó:
- Primero se modifico el Look and Feel directamente en el código e importando la librería para tener la vista FlatlafDark, la interfaz fue realizada con Drag and Drop
- La interfaz se compone por un área de texto que permite la escritura de código o por medio del botón de abrir editar un archivo existente
- Se tiene ina visualización de consola de salida que informa al usuario cuando se ejecutan los botones, se envía la información del archivo al analizador, se ejecuta la lectura y posteriormente el objeto de la app, finalmente se lanzan las salidas y se toma en cuenta los errores

## Funcionamiento Interno de la Aplicación

El archivo de análisis Léxico se basa en la lectura y reconocimiento de los tokens del lenguaje establecido, y de no reconocer los tokens o existiese algun tipo de error detectarlo inmediatamente. Directamente, el archivo cuenta con los tokens establecidos y sus respectivos regex para dar lectura exitosa. Básicamente se reonocen de la siguiente manera: <YYINITIAL> {DISYUNCION} {System.out.println("DISYUNCION: "+yytext()); return new Symbol(sym.DISYUNCION, yyline, yycolumn, yytext());}
El archivo de análisis Sintáctico es el núcleo de la aplicación, verifica la existencia de los tokens en el orden establecido, y direcciona las ordenes o designa tareas con el texto reconocido. Teniendo un array para los automatas, uno de conjuntos, uno de pruebas y teniendo string de errores para poder demostrar si se encuentran errores. Tiene prácticamente dentro la gramática, y en las distintas partes de esta direcciona las acciones a realizar por supuesto siempre y cuando todo este bien, crea conjuntos y manda al Nodo de arbol binario sus respectivos valores respetando los operadores, por ejemplo la disyunción permite tener 2 nopdo hijos, entonces lo hace de esta manera: 
DISYUNCION:a expresion_regular:b expresion_regular:c {: 
                            Nodo_Binario n_nodo = new Nodo_Binario(a);
                            n_nodo.setIzq((Nodo_Binario)b);
                            n_nodo.setDer((Nodo_Binario)c);
                            RESULT =  n_nodo; :}|
Básicamente la creación de los nodos binarios permite el desarrollo de otros partes del programa como la generación de Automátas y la tabla de transiciones.   
El archivo AFD permite el desarrollo a mayor escala de la app, pues crea el árbol binario usando código de graphviz, ejemplo,  actual.setAnulable(true);
                    actual.getPrimeros().addAll(actual.getIzq().getPrimeros());
Se verifica con los datos que envía el Sintáctico que operaciones se están realizando, dependiendo cuál ver si puede tener 1 o 2 hijos y también respetando la jerarquía de operadores o agrupación. Iterando la expresión regular ingresada con los respectivos conjuntos que se tienen, el funcionamiento de las otras partes del programa es bastante similar.
Calculo de transiciones, por medio del árbol de expresión se llenan las transiciones, se generan nuevos estados y si es necesario volver a alguno anterior se hace, iterando constantemente .
for (ArrayList<ArrayList> filas : transiciones) {
                    if (filas.get(0).equals(estado)) {
                        encontrado = true;
                        break
Otro de los pilares en funcionamiento de la aplicación que es el Análisis se desarrolla comprobando la existencia de los conjuntos y respetando el orden jerárquico de operadores de la expresión regular indexada .
if (evaluado.contains(caracter)) {
                        encontrado = true;
                        num_col = terminales.indexOf(t) + 1;
                        transicion = estado.get(num_col);
                        for (ArrayList<ArrayList> fila : transiciones) {
                            if (fila.get(0).equals(transicion)) {
                                num_estado = transiciones.indexOf(fila);
                                break;

## Pruebas de la Aplicación

Para el testing de la app, se probaron diversos archivos de entrada con distintas complejidades y se probo la resistencia y recuperación de errores de la app
# Gramática
Respetando las reglas y empleando la Back Naus Form
TOKENS
CONJ = "CONJ"
DOS_PUNTOS=":"
FLECHA = "-" {ESPACIO}* ">"
COMA = ","
SEPARADOR="~"
LLAVE_A="{"
LLAVE_C="}"
CONCAT="."
DISYUNCION="|"
KLEENE="*"
POSTIVA="+"
CBOOL="?"
PORCENTAJE="%"
PCOMA=";"
ESPACIO= [ \t\r\n]+
COMMENT_MULTILINE="<!" ([^!] | ("!" [^\>]))+ "!>"
COMMENT_SIMPLE="//".*
MINUSC= [a-z]
MAYUSC= [A-Z]
NUM = [0-9]
ESP = [ -/:@\[-`{-}]
ESCAPADOS = "\\\""|"\\\'"|"\\n"
NO_ESCAPADOS = [^\'\"]
ID = [a-zA-Z_][a-zA-Z0-9_]+
CHAR = (\" {NO_ESCAPADOS} \")|{ESCAPADOS}
STRING = \" ([^\"]|"\\\"")+ \"

start with Codigo;

Codigo::= LLAVE_A conjuntos PORCENTAJE PORCENTAJE PORCENTAJE PORCENTAJE pruebas LLAVE_C;

conjuntos::= conjuntos conjunto|
            conjunto;

conjunto::= CONJ DOS_PUNTOS ID:a FLECHA notacion_conjuntos:b PCOMA |
            ID:b FLECHA expresion_regular:a PCOMA 

notacion_conjuntos::= MINUSC:a SEPARADOR MINUSC:b 
            |
                    MAYUSC:a SEPARADOR MAYUSC:b
            RESULT = nuevo_conjunto;
            |
                    NUM:a SEPARADOR NUM:b|
                    ESP:a SEPARADOR ESP:b|
                    conj_sep_comas:a ;

conj_sep_comas::= conj_sep_comas:a COMA conj_sep_coma:b  |
                  conj_sep_coma:a;

conj_sep_coma::= MINUSC:a|
                 MAYUSC:a|
                 NUM:a|
                 ESP:a};

expresion_regular ::= CONCAT:a expresion_regular:b expresion_regular:c |
                    DISYUNCION:a expresion_regular:b expresion_regular:c |
                    KLEENE:a expresion_regular:b |
                    POSITIVA:a expresion_regular:b |
                    CBOOL:a expresion_regular:b |
                    LLAVE_A ID:a LLAVE_C |
                    CHAR:a ;

pruebas ::= pruebas prueba|
            prueba;

prueba ::= ID:a DOS_PUNTOS STRING:b PCOMA 
           ID:a DOS_PUNTOS CHAR:b PCOMA 
