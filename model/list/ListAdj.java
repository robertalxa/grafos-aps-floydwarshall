package model.list;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import exception.VertexNotFoundException;
import model.Graph;
import model.Vertex;

public class ListAdj implements Graph {
    private HashMap<Vertex, LinkedList<Edge>> vertices;
    private HashMap<Vertex, Double> verticesAdjacentes;
    private double[][] matrizAdj;
    private boolean isDirected;
    private double[][] matrizCustos;

    public ListAdj(boolean isDirected) {
        vertices = new HashMap<>();
        verticesAdjacentes = new HashMap<>();
        this.isDirected = isDirected;
    }

    @Override
    public boolean adjacent(Vertex u, Vertex v) {
        return vertices.get(u).stream().filter(e -> e.getVertex().equals(v)).count() > 0;
    }

    @Override
    public void addVertex(Vertex v)  {
        if (vertices.get(v) != null) {
            throw new Error("Vértice " + v.getName() + " não encontrado.");
        }
        vertices.put(v, new LinkedList<>());
    }

    public void addVertexTemp(Vertex v) throws VertexNotFoundException {
        if (verticesAdjacentes.get(v) != null) {
            throw new VertexNotFoundException("Vértice " + v.getName() + " não encontrado.");
        }
        verticesAdjacentes.put(v, Double.MAX_VALUE);
    }

    @Override
    public boolean removeVertex(Vertex v) {
        // TODO implement
        return false;
    }

    @Override
    public boolean addEdge(Vertex u, Vertex v, double value) {
        if (vertices.get(u) == null || vertices.get(v) == null) {
            return false;
        }
        vertices.get(u).add(new Edge(v, value));
        if (notDirected()) {
            vertices.get(v).add(new Edge(u, value));
        }
        return true;
    }

    @Override
    public boolean removeEdge(Vertex u, Vertex v) {
        vertices.get(u).forEach(e -> {
            if (e.getVertex().getName().equals(v.getName())) {
                vertices.get(u).remove(e);
                return;
            }
        });
        if (notDirected()) {
            vertices.get(v).forEach(e -> {
                if (e.getVertex().getName().equals(u.getName())) {
                    vertices.get(v).remove(e);
                    return;
                }
            });
        }
        return true;
    }

    public void algoritmoDjksrta() throws VertexNotFoundException {
        List<Map.Entry<Vertex, LinkedList<Edge>>> verticesStream = vertices.entrySet().stream()
                .collect(Collectors.toList());

        LinkedList<Edge> partida = vertices.get(new Vertex("Aa",0));
        //verticesAdjacentes.put(new Vertex("Aa"), 0.0);
        for (Edge edge : partida) {
            verticesAdjacentes.put(edge.getVertex(), edge.getValue());
        }

        System.out.println("ESTE É O VERTICE DE PARTIDA " + partida.toString());
        for (Map.Entry<Vertex, LinkedList<Edge>> item : verticesStream) {
            double custo = 99999999999.0;
            System.out.println("ESTE É O KEY DE ITEM " + item.getKey());
            for (Edge edge : partida) {
                double custoAresta = edge.getValue();
                String nomeAdj = edge.getVertex().getName();

                // Talvez esse código abaixo não seja necessário, com a criação do atributo id posso manipular as matrizes tipo matriz[vertex.id, edge.id] = custo, tipo isso;
                addVertexTemp(edge.getVertex());
                double valorAdjAtual = verticesAdjacentes.get(edge.getVertex()).doubleValue();

                System.out.println("ADJACENTE DE " + item.getKey() + " É " + edge);
                System.out.println("valor adj " + valorAdjAtual);
            }
        }
    }

    @Override
    public boolean isDirected() {
        return isDirected;
    }

    public boolean notDirected() {
        return !isDirected;
    }

    @Override
    public String toString() {
        String out = "";
        List<Map.Entry<Vertex, LinkedList<Edge>>> verticesStream = vertices.entrySet().stream()
                .collect(Collectors.toList());

        for (Map.Entry<Vertex, LinkedList<Edge>> item : verticesStream) {
            out += "from " + item.getKey();
            for (Edge edge : item.getValue()) {
                out += edge;
            }
            out += "\n";
        }

        try {
            algoritmoDjksrta();
        } catch (VertexNotFoundException e) {
            throw new RuntimeException(e);
        }
        return out;
    }

}
