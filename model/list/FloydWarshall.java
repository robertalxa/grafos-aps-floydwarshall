package model.list;

import model.Vertex;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FloydWarshall {
    private HashMap<Vertex, LinkedList<Edge>> vertices;
    private double[][] matrizCustos;
    private Vertex[][] matrizAntecessores;
    private Vertex[] listaAntecessores;
    private boolean isDirected;

    public FloydWarshall(boolean isDirected, int qtdVertices) {
        this.vertices = new HashMap<>();
        this.matrizCustos = new double[qtdVertices][qtdVertices];
        this.matrizAntecessores = new Vertex[qtdVertices][qtdVertices];
        this.listaAntecessores = new Vertex[qtdVertices];
        this.isDirected = isDirected;

        for (int i = 0; i < this.matrizCustos.length; i++) {
            for (int j = 0; j < this.matrizCustos.length; j++) {
                this.matrizCustos[i][j] = (i == j ? 0 : Integer.MAX_VALUE);
            }
        }
    }

    public boolean notDirected() {
        return !isDirected;
    }

    public void addVertex(Vertex v)  {
        if (vertices.get(v) != null) {
            throw new Error("Vértice " + v.getName() + " não encontrado.");
        }
        vertices.put(v, new LinkedList<>());
    }

    public boolean addEdge(Vertex u, Vertex v, double value) {
        if (vertices.get(u) == null || vertices.get(v) == null) {
            return false;
        }
        vertices.get(u).add(new Edge(v, value));
        matrizCustos[u.getId()][v.getId()] = value;
        matrizAntecessores[u.getId()][v.getId()] = v;
        if (notDirected()) {
            vertices.get(v).add(new Edge(u, value));
            matrizCustos[v.getId()][u.getId()] = value;
            matrizAntecessores[v.getId()][u.getId()] = u;
        }
        return true;
    }

    public Vertex getVertexExistente(String nome) {
        List<Map.Entry<Vertex, LinkedList<Edge>>> verticesStream = vertices.entrySet().stream()
                .collect(Collectors.toList());

        for(Map.Entry<Vertex, LinkedList<Edge>> item: verticesStream){
            if(item.getKey().getName().equals(nome)) return item.getKey();
        }

        return null;
    }

    public Vertex getVertexExistentePorId(int id) {
        List<Map.Entry<Vertex, LinkedList<Edge>>> verticesStream = vertices.entrySet().stream()
                .collect(Collectors.toList());

        for(Map.Entry<Vertex, LinkedList<Edge>> item: verticesStream){
            if(item.getKey().getId() == id) return item.getKey();
        }

        return null;
    }

    public void aplicaAlgoritmo () {
        List<Map.Entry<Vertex, LinkedList<Edge>>> listaVertices = vertices.entrySet().stream()
                .collect(Collectors.toList());

        for (Map.Entry<Vertex, LinkedList<Edge>> item: listaVertices) {
            Vertex vtPivo = item.getKey();

            for (int partida = 0; partida < matrizCustos.length; partida++) {
                for (int chegada = 0; chegada < matrizCustos[partida].length; chegada++) {

                    if (partida == chegada) {
                        matrizCustos[partida][chegada] = 0;
                        matrizAntecessores[partida][chegada] = getVertexExistentePorId(partida);
                        continue;
                    }

                    double custoDireto = matrizCustos[partida][chegada];
                    double custoIndireto = matrizCustos[partida][vtPivo.getId()] + matrizCustos[vtPivo.getId()][chegada];

                    if (custoIndireto < custoDireto) {
                        matrizCustos[partida][chegada] = custoIndireto;
                        matrizAntecessores[partida][chegada] = vtPivo;
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        String conteudo = "";

        for (int i = 0; i < matrizCustos.length; i++) {
            conteudo += ("\n\nResultados Vértice : \"" + getVertexExistentePorId(i).getName() + "\"");

            for (int j = 0; j < matrizCustos[i].length; j++) {
                Vertex vtCaminho = matrizAntecessores[i][j];
                conteudo += "\n(" + getVertexExistentePorId(i).getName() + "," + getVertexExistentePorId(j).getName() + ") = " + (matrizCustos[i][j] == Integer.MAX_VALUE ? "INF" : matrizCustos[i][j]) + " | " + (vtCaminho != null ? "Passando por → " + vtCaminho.getName() : "Sem caminho");
            }
        }

        return conteudo;
    }
}
