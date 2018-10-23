package spring.services;

import javafx.util.Pair;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.*;

/**
 * TIM(Trace Information Model) record the relationships among the types of artifacts.
 * It provide information about which 2 types of artifacts may have trace links.
 */

public class TIMGraph {
    Set<String> TIMNode;
    Map<String, Set<String>> TIMLink; //Undirected graph

    /**
     * Construct a TIM Graph from a plain text file in CSV format
     *
     * @param TIMFileName The .tim file in the classpath
     */
    public TIMGraph(String TIMFileName) throws IOException {
        TIMNode = new HashSet<>();
        TIMLink = new HashMap<>();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(TIMFileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = null;
        //Read lines as pairs then construct it as a graph
        List<Pair<String, String>> links = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            String[] nodes = line.split(",");
            String sourceType = nodes[0];
            String targetType = nodes[1];
            links.add(new Pair<>(sourceType, targetType));
        }
        constructGraph(links);
    }

    private void constructGraph(List<Pair<String, String>> links) {
        for (Pair<String, String> link : links) {
            TIMNode.add(link.getKey());
            TIMNode.add(link.getValue());
            if (!TIMLink.containsKey(link.getKey())) {
                TIMLink.put(link.getKey(), new HashSet<>());
            }
            TIMLink.get(link.getKey()).add(link.getValue());
        }
    }
}
