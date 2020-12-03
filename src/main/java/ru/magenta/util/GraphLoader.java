package ru.magenta.util;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import ru.magenta.entity.DistanceEntity;
import ru.magenta.exception.EmptyDistanceListPassed;
import ru.magenta.service.DistanceDataAccess;

import java.util.List;

public class GraphLoader {
    private static GraphLoader graphLoader;
    private Graph<Integer, DefaultWeightedEdge> graph;

    private GraphLoader() {
    }

    private void setGraph(Graph<Integer, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public Graph<Integer, DefaultWeightedEdge> getGraph() {
        return graph;
    }

    public static synchronized GraphLoader getInstance(boolean reload) {
        if (graphLoader == null) {
            graphLoader = new GraphLoader();
        }

        //loads data from DB
        if (reload)
            graphLoader.setGraph(
                    graphLoader.createGraph(DistanceDataAccess.getInstance().getAll()));

        return graphLoader;
    }

    private Graph<Integer, DefaultWeightedEdge> createGraph(List<DistanceEntity> distances) {
        if (distances == null) {
            throw new EmptyDistanceListPassed("Can't create graph because list is null");
        }

        Graph<Integer, DefaultWeightedEdge> distanceGraph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        for (DistanceEntity distance : distances) {
            int from = distance.getCityFrom();
            int to = distance.getCityTo();

            distanceGraph.addVertex(from);
            distanceGraph.addVertex(to);
            distanceGraph.setEdgeWeight(
                    distanceGraph.addEdge(from, to),
                    distance.getDistance()
            );
        }

        return distanceGraph;
    }
}
