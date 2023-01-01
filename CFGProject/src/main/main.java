package main;

import java.util.ArrayList;
 public class main {
    public static void main(String[] args) {
        System.out.println("Control Flow Graph");
        System.out.println("++++++++++++++++++");
        System.out.println();
        System.out.println();

        CodeReader codeReader = new CodeReader();
        ArrayList<Statement> lines = codeReader.readCode("test/testcode3.txt");

        MethodFinder finder = new MethodFinder();
        ArrayList<ArrayList<Statement>> allMethods = finder.findMethods(lines);

        System.out.println(allMethods.size());
        for (int i = 0; i < allMethods.size(); i++) {
            System.out.println(".........................");
            Analyzer analyser = new Analyzer();
            analyser.analyzeStatement(allMethods.get(i));

            System.out.println();
            System.out.println();

            System.out.println("======================================================");
        }
    }
}

