package com.example.socialtpygui.utils;


import com.example.socialtpygui.domain.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph<T> {
    // A user define class to represent a graph.
    // A graph is an array of adjacency lists.
    // Size of array will be V (number of vertices in graph)
    int V;
    ArrayList<ArrayList<Integer>> adjListArray;
    Map<T,Integer> elmMap;
    List<T> elm;
    // constructor

    /**
     *
     * @param elem the list of entities to work with
     */
    public Graph(List<T> elem)
    {
        elm=elem;

        this.V = elem.size();
        // define the size of array as
        // number of vertices

        //lets initializate the map of elm
        elmMap=new HashMap<>();

        adjListArray = new ArrayList<>();

        // Create a new list for each vertex
        // such that adjacent nodes can be stored

        for (int i = 0; i < V; i++) {
            adjListArray.add(i, new ArrayList<>());
            elmMap.put(elem.get(i),i);
        }
    }

    /**
     *  add a edge to the graph
     * @param src
     * @param dest
     */
    public void addEdge(T src, T dest)
    {
        // Add an edge from src to dest.
        adjListArray.get(elmMap.get(src)).add(elmMap.get(dest));

        // Since graph is undirected, add an edge from dest
        // to src also
        adjListArray.get(elmMap.get(dest)).add(elmMap.get(src));
    }

    void DFSUtil(int v, boolean[] visited, List<T> cmp)
    {
        // Mark the current node as visited
        visited[v] = true;
        cmp.add(elm.get(v));
        // Recur for all the vertices
        // adjacent to this vertex
        for (int x : adjListArray.get(v)) {
            if (!visited[x])
                DFSUtil(x, visited,cmp);
        }
    }

    /**
     *
     * @return a Tuple of the nr oc connected components and the biggest one
     */
    public Tuple<Integer,List<T>> nrConnectedComponentsNBiggest()
    {
        // number of conex components
        int nr=0;
        List<T> fin=new ArrayList<>();
        // Mark all the vertices as not visited
        boolean[] visited = new boolean[V];
        for (int v = 0; v < V; ++v) {
            if (!visited[v]) {
                List<T> crr= new ArrayList<>();

                DFSUtil(v, visited,crr);

                if(crr.size()>fin.size())
                    fin=crr;
                nr++;
            }
        }
        return new Tuple<>(nr,fin);
    }
}
