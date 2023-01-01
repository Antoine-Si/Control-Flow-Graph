package main;

import java.util.ArrayList;

public class Node {

    private ArrayList<Integer> childList = new ArrayList<>();
    private int nodeNumber;
    private ArrayList<Statement> statements = new ArrayList<>();
    private Node parentNode;
    public boolean isLoop = false;
    public boolean isIf = false;
    public boolean isElse = false;
    public boolean isElseIf = false;
    public boolean parentOfIf = false;
    public boolean isDoWhileLoop = false;

    public void setParentNode(Node parent) {
        parentNode = parent;
    }

    public Node getParentNode() {
        return parentNode;
    }


    public Node(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }


    public void addChild(int childNumber) {
        childList.add(childNumber);
    }

    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    public void printStatement() {
        System.out.println(statements.size());
        for(int i = 0; i < statements.size(); i++)
            System.out.println(statements.get(i).getStatement());
    }
/*
public String getStatement(){
        int statementSize = statements.size();
        String statement1= "";
        for(int i = 0; i < statements.size(); i++){
            String statement2 = statements.get(i).getStatement()+ "\n";
            statement1.concat(statement2);
        }
          return statement1;
    }
*/
    public void printChild() {
        System.out.print("Node number:  " + nodeNumber + "   >>>   ");
        for(int i = 0; i < childList.size(); i++) {
            System.out.print(childList.get(i)+ "   ");
        }
        System.out.println();
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public boolean isChild(int childNumber) {

        for(int i = 0;i < childList.size(); i++)
            if(childList.get(i) == childNumber)
                return true;


        return false;
    }

    public Node getParent() {
        return parentNode;
    }

    public ArrayList<Integer> getChildList() {
        return childList;

    }

    public int getTotalChild() {
        return childList.size();
    }





}
