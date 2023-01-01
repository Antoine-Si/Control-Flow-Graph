package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


//Analyzes an arraylist of the lines of the code.

public class Analyzer {

    private patternMatcher patternMatcher;
    Map<Integer, Boolean> isUsed;

    public Analyzer() {
        patternMatcher = new patternMatcher();
        isUsed = new HashMap<>();
    }
    //The main method for making the CFG. Makes the adjacency matrix, vertex list, and edge list.
    public void analyzeStatement(ArrayList<Statement> method)  {

        int i = 0;

        int nodeNumber = 1;

        ArrayList<Node> graph = new ArrayList<>();
        Stack<Node> nodeStack = new Stack<>();
        Stack<Node> parentOfIf = new Stack<>();

        Stack<Node> parentOfEndParenthasis = new Stack<>();

        Node currentNode = new Node(nodeNumber);
        nodeNumber++;
        graph.add(currentNode);
        nodeStack.push(currentNode);
        //While loop that goes through all the code to sort it out into an adjacency matrix.
        while (i < method.size()) {

            //else if statement
            if (isElseIfStatement(method.get(i).getStatement())) {

                Node elseIfNode = new Node(nodeNumber);
                nodeNumber++;
                graph.add(elseIfNode);

                Node parentOfifNode = parentOfIf.pop();
                parentOfifNode.addChild(elseIfNode.getNodeNumber());
                elseIfNode.setParentNode(parentOfifNode);
                elseIfNode.addStatement(method.get(i));

                if (parenthesisFound(method.get(i).getStatement())) {

                    elseIfNode.isElseIf = true;

                    parentOfEndParenthasis.push(elseIfNode);
                    nodeStack.push(elseIfNode);

                } else if (parenthesisFound(method.get(i + 1).getStatement())) {
                    elseIfNode.isElseIf = true;
                    i++;
                    elseIfNode.addStatement(method.get(i));
                    parentOfEndParenthasis.push(elseIfNode);
                    nodeStack.push(elseIfNode);

                } else {
                    i++;
                    elseIfNode.addStatement(method.get(i));

                    if (isElseStatement(method.get(i + 1).getStatement())
                            || isElseIfStatement(method.get(i + 1).getStatement())) {

                        parentOfIf.add(parentOfifNode);

                    } else {

                        Node nextNode = new Node(nodeNumber);
                        nodeNumber++;
                        graph.add(nextNode);

                        ArrayList<Integer> childList = parentOfifNode.getChildList();

                        for (int index1 = 0; index1 < childList.size(); index1++) {
                            for (int index2 = 0; index2 < graph.size(); index2++) {
                                if (graph.get(index2).getNodeNumber() == childList.get(index1)) {
                                    graph.get(index2).addChild(nextNode.getNodeNumber());
                                }
                            }
                        }

                        parentOfifNode.addChild(nextNode.getNodeNumber());
                        nodeStack.add(nextNode);
                    }

                }

            }
            //else statement
            else if (isElseStatement(method.get(i).getStatement())) {

                Node elseNode = new Node(nodeNumber);
                nodeNumber++;

                graph.add(elseNode);

                Node parentOfifNode = parentOfIf.pop();
                parentOfifNode.addChild(elseNode.getNodeNumber());
                elseNode.setParentNode(parentOfifNode);
                elseNode.addStatement(method.get(i));

                if (parenthesisFound(method.get(i).getStatement())) {
                    elseNode.isElse = true;

                    parentOfEndParenthasis.push(elseNode);
                    nodeStack.push(elseNode);


                } else if (parenthesisFound(method.get(i + 1).getStatement())) {

                    i++;
                    elseNode.addStatement(method.get(i));
                    elseNode.isElse = true;

                    parentOfEndParenthasis.push(elseNode);
                    nodeStack.push(elseNode);


                } else {

                    i++;
                    elseNode.addStatement(method.get(i));
                    i++;

                    Node nextNode = new Node(nodeNumber);
                    nodeNumber++;

                    ArrayList<Integer> childList = parentOfifNode.getChildList();

                    for (int index1 = 0; index1 < childList.size(); index1++) {
                        for (int index2 = 0; index2 < graph.size(); index2++) {
                            if (graph.get(index2).getNodeNumber() == childList.get(index1)) {
                                graph.get(index2).addChild(nextNode.getNodeNumber());
                            }
                        }
                    }

                    graph.add(nextNode);
                    nextNode.setParentNode(elseNode);

                    nodeStack.add(nextNode);
                    nextNode.addStatement(method.get(i));

                }

            }
            //if statement
            else if (isIfStatement(method.get(i).getStatement())) {

                Node previousNode = nodeStack.pop();

                currentNode = new Node(nodeNumber);
                currentNode.isIf = true;
                nodeNumber++;

                currentNode.setParentNode(previousNode);
                previousNode.addChild(currentNode.getNodeNumber());

                graph.add(currentNode);

                if (parenthesisFound(method.get(i).getStatement())) {

                    currentNode.addStatement(method.get(i));
                    parentOfIf.push(previousNode);
                    parentOfEndParenthasis.push(currentNode);
                    nodeStack.push(currentNode);



                } else if (parenthesisFound(method.get(i + 1).getStatement())) {

                    currentNode.addStatement(method.get(i));
                    i++;
                    currentNode.addStatement(method.get(i));

                    parentOfIf.push(previousNode);
                    parentOfEndParenthasis.push(currentNode);
                    nodeStack.push(currentNode);


                } else {

                    currentNode.addStatement(method.get(i));
                    i++;

                    if (isElseStatement(method.get(i + 1).getStatement())
                            || isElseIfStatement(method.get(i + 1).getStatement())) {

                        currentNode.addStatement(method.get(i));
                        parentOfIf.add(currentNode.getParent());

                    } else {

                        currentNode.addStatement(method.get(i));
                        i++;

                        Node newNode = new Node(nodeNumber);
                        nodeNumber++;
                        currentNode.addChild(newNode.getNodeNumber());
                        currentNode.getParent().addChild(newNode.getNodeNumber());
                        graph.add(newNode);
                        nodeStack.push(newNode);

                    }

                }

            }
            //For loop statement
            else if (isForLoopStarting(method.get(i).getStatement())
                    || isWhileLoopStarting(method.get(i).getStatement())) {
                Node previousNode = nodeStack.pop();
                previousNode.addChild(nodeNumber);
                currentNode = new Node(nodeNumber);
                nodeNumber++;

                currentNode.setParentNode(previousNode);
                currentNode.addStatement(method.get(i));

                graph.add(currentNode);

                if (parenthesisFound(method.get(i).getStatement())) {

                    Node newNode = new Node(nodeNumber);
                    nodeNumber++;
                    graph.add(newNode);
                    nodeStack.push(newNode);

                    newNode.setParentNode(currentNode);
                    currentNode.addChild(newNode.getNodeNumber());
                    currentNode.isLoop = true;
                    parentOfEndParenthasis.push(currentNode);

                } else if (parenthesisFound(method.get(i + 1).getStatement())) {

                    i++;

                    Node newNode = new Node(nodeNumber);
                    nodeNumber++;
                    graph.add(newNode);
                    nodeStack.push(newNode);

                    newNode.setParentNode(currentNode);
                    currentNode.addChild(newNode.getNodeNumber());
                    currentNode.isLoop = true;
                    parentOfEndParenthasis.push(currentNode);

                } else {

                    currentNode.addChild(nodeNumber);
                    Node nestedNode = new Node(nodeNumber);
                    graph.add(nestedNode);
                    nodeNumber++;
                    nestedNode.addStatement(method.get(i + 1));
                    i += 2;

                    nestedNode.setParentNode(currentNode);
                    nestedNode.addChild(currentNode.getNodeNumber());

                    Node newNode = new Node(nodeNumber);
                    nodeNumber++;
                    graph.add(newNode);

                    newNode.setParentNode(currentNode);
                    currentNode.addChild(newNode.getNodeNumber());
                    nestedNode.addChild(newNode.getNodeNumber());
                    nodeStack.push(newNode);
                    continue;
                }

            }
            //Do while loop statement
            else if (isDoWhileLoopStarting(method.get(i).getStatement())) {
                Node previousNode = nodeStack.pop();
                previousNode.addChild(nodeNumber);
                currentNode = new Node(nodeNumber);
                nodeNumber++;

                currentNode.setParentNode(previousNode);
                currentNode.addStatement(method.get(i));

                graph.add(currentNode);

                if (parenthesisFound(method.get(i).getStatement())) {

                    Node newNode = new Node(nodeNumber);
                    nodeNumber++;
                    graph.add(newNode);
                    nodeStack.push(newNode);

                    newNode.setParentNode(currentNode);
                    currentNode.addChild(newNode.getNodeNumber());
                    currentNode.isDoWhileLoop = true;
                    parentOfEndParenthasis.push(currentNode);

                } else if (parenthesisFound(method.get(i + 1).getStatement())) {

                    i++;

                    Node newNode = new Node(nodeNumber);
                    nodeNumber++;
                    graph.add(newNode);
                    nodeStack.push(newNode);

                    newNode.setParentNode(currentNode);
                    currentNode.addChild(newNode.getNodeNumber());
                    currentNode.isDoWhileLoop = true;
                    parentOfEndParenthasis.push(currentNode);

                } else {

                    currentNode.addChild(nodeNumber);
                    Node nestedNode = new Node(nodeNumber);
                    graph.add(nestedNode);
                    nodeNumber++;
                    nestedNode.addStatement(method.get(i + 1));
                    i += 2;

                    nestedNode.addStatement(method.get(i));
                    i++;

                    nestedNode.setParentNode(currentNode);
                    nestedNode.addChild(currentNode.getNodeNumber());

                    Node newNode = new Node(nodeNumber);
                    nodeNumber++;
                    graph.add(newNode);

                    newNode.setParentNode(nestedNode);
                    nestedNode.addChild(newNode.getNodeNumber());
                    nodeStack.push(newNode);
                    continue;
                }

            }
            //end of Do while parenthesis
            else if(endParenthesisOfDoWhileFound(method.get(i).getStatement()) && !parentOfEndParenthasis.isEmpty()) {

                Node startOfParanthesis = parentOfEndParenthasis.pop();
                Node previousNode = nodeStack.pop();

                Node newNode = new Node(nodeNumber);
                nodeNumber++;
                graph.add(newNode);
                previousNode.addChild(newNode.getNodeNumber());
                previousNode.addStatement(method.get(i));
                newNode.setParentNode(previousNode);
                nodeStack.push(newNode);
                previousNode.addChild(startOfParanthesis.getNodeNumber());

            }
            //end parenthesis of statements
            else if (endParenthesisFound(method.get(i).getStatement()) && !parentOfEndParenthasis.isEmpty()) {


                Node startOfParenthesis = parentOfEndParenthasis.pop();
                //start parenthesis of a loop
                if (startOfParenthesis.isLoop) {

                    Node previousNode = nodeStack.pop();

                    Node newNode = new Node(nodeNumber);
                    nodeNumber++;
                    graph.add(newNode);
                    previousNode.addChild(newNode.getNodeNumber());
                    startOfParenthesis.addChild(newNode.getNodeNumber());
                    newNode.setParentNode(previousNode);
                    nodeStack.push(newNode);
                    previousNode.addChild(startOfParenthesis.getNodeNumber());

                }


                //start of parenthesis in Else statement
                else if (startOfParenthesis.isElse) {

                    Node nextNode = new Node(nodeNumber);
                    nodeNumber++;

                    Node parentOfifNode = startOfParenthesis.getParent();

                    ArrayList<Integer> childList = parentOfifNode.getChildList();

                    for (int index1 = 0; index1 < childList.size(); index1++) {
                        for (int index2 = 0; index2 < graph.size(); index2++) {
                            if (graph.get(index2).getNodeNumber() == childList.get(index1)) {
                                graph.get(index2).addChild(nextNode.getNodeNumber());
                            }
                        }
                    }

                    graph.add(nextNode);
                    nextNode.setParentNode(startOfParenthesis);

                    startOfParenthesis.addStatement(method.get(i));
                    nodeStack.add(nextNode);

                }
                    //start of parentheiss for if statement
                else if (startOfParenthesis.isIf) {

                    Node parentOfifNode = startOfParenthesis.getParent();

                    if (isElseStatement(method.get(i + 1).getStatement())
                            || isElseIfStatement(method.get(i + 1).getStatement())) {

                        startOfParenthesis.addStatement(method.get(i));

                    } else {

                        Node nextNode = new Node(nodeNumber);
                        nodeNumber++;

                        parentOfifNode.addChild(nextNode.getNodeNumber());
                        startOfParenthesis.addChild(nextNode.getNodeNumber());
                        nextNode.setParentNode(startOfParenthesis);
                        startOfParenthesis.addStatement(method.get(i));

                        nodeStack.add(nextNode);
                        graph.add(nextNode);

                    }
                }
                    //Start of parenthesis of Else IF statement
                else if (startOfParenthesis.isElseIf) {

                    if (isElseStatement(method.get(i + 1).getStatement())
                            || isElseIfStatement(method.get(i + 1).getStatement())) {
                        startOfParenthesis.addStatement(method.get(i));
                        parentOfIf.push(startOfParenthesis.getParent());

                    } else {
                        startOfParenthesis.addStatement(method.get(i));

                        Node nextNode = new Node(nodeNumber);
                        nodeNumber++;

                        Node parentOfifNode = startOfParenthesis.getParent();

                        ArrayList<Integer> childList = parentOfifNode.getChildList();

                        for (int index1 = 0; index1 < childList.size(); index1++) {
                            for (int index2 = 0; index2 < graph.size(); index2++) {
                                if (graph.get(index2).getNodeNumber() == childList.get(index1)) {
                                    graph.get(index2).addChild(nextNode.getNodeNumber());
                                }
                            }
                        }

                        graph.add(nextNode);
                        nextNode.setParentNode(startOfParenthesis);

                        nodeStack.add(nextNode);
                        startOfParenthesis.getParent().addChild(nextNode.getNodeNumber());

                    }

                }

            }

            else {
                if (nodeStack.isEmpty())
                    break;

                Node tempNode = nodeStack.pop();
                tempNode.addStatement(method.get(i));
                nodeStack.push(tempNode);
            }

            i++;

        }
        //Prints out each node and statement
        for (int index = 0; index < graph.size(); index++) {
            graph.get(index).printChild();
            graph.get(index).printStatement();
        }
        //Prints out CFG Adjacency Matrix
        System.out.println();
        System.out.println("---------------------------------------------------------------");
        System.out.print("");

        System.out.print("\t\t\t\t\t\t\t");
        for (int index = 0; index < graph.size(); index++)
            System.out.print(index + 1 + "\t");
        System.out.println();

        System.out.print("\t\t\t\t\t\t\t");
        for (int index = 0; index < graph.size(); index++)
            System.out.print("-\t");
        System.out.println();

        for (int index1 = 0; index1 < graph.size(); index1++) {
            Node node = graph.get(index1);

            System.out.print("Node Number:  " + node.getNodeNumber() + " \t|\t\t");
            for (int index2 = 0; index2 < graph.size(); index2++) {
                if (node.isChild(index2 + 1)) {
                    System.out.print("1\t");
                } else {
                    System.out.print("0\t");
                }
            }

            System.out.println();

        }
        System.out.println();
        System.out.println();
        System.out.println("Vertex List"); //Vertex List
        for (int index = 0; index < graph.size(); index++){
            Node node = graph.get(index);
            System.out.print(node.getNodeNumber()+ ": ");
            graph.get(index).printStatement();
        }
        System.out.println();
        System.out.println();
        System.out.println("Edges List"); //Edges List
        for (int index = 0; index < graph.size(); index++){
            Node node = graph.get(index);
            for (int index2 = 0; index2 < graph.size(); index2++) {
                if (node.isChild(index2 + 1)) {
                    System.out.println("( "+ node.getNodeNumber() + ", "+ (index2 + 1) + ")");
                }
            }
        }

    }
    //Statement finding methods
    private boolean isDoWhileLoopStarting(String statement) {
        return patternMatcher.isMatch("^(\\s)*do", statement);
    }

    private boolean isWhileLoopStarting(String statement) {
        return patternMatcher.isMatch("^(\\s)*while", statement);
    }

    private boolean isElseIfStatement(String statement) {
        return patternMatcher.isMatch("^(\\s)*else if", statement);
    }

    private boolean isElseStatement(String statement) {
        return patternMatcher.isMatch("^(\\s)*else", statement);
    }

    private boolean isIfStatement(String statement) {
        return patternMatcher.isMatch("^(\\s)*if", statement);
    }

    private boolean endParenthesisFound(String statement) {

        return patternMatcher.isMatch("\\}(\\s)*$", statement);
    }

    private boolean endParenthesisOfDoWhileFound(String statement) {

        return patternMatcher.isMatch("^(\\s)*\\}(\\s)*while", statement);

    }

    private boolean parenthesisFound(String statement) {
        return patternMatcher.isMatch("\\{(\\s)*$", statement);
    }

    private boolean isForLoopStarting(String statement) {
        return patternMatcher.isMatch("^(\\s)*for", statement);
    }

}