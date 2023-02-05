package org.queenlang.examples;

public final class LabeledGraph implements Graph {

    int edges[][];

    public LabeledGraph(final int[][] edges) {
        this.edges = edges;
    }

    @Override
    public final Graph loseEdges(final int i, final int j) {
        int n = edges.length;
        int[][] unAnnNewEdges = new int[n][];
        int @NotNull [] @Ala [] newedges = new int[n][];
        for (int k = 0; k < n; ++k) {
            edgelist: {
                int z;
                search: {
                    if (k == i) {
                        for (z = 0; z < edges[k].length; ++z) {
                            if (edges[k][z] == j) {
                                break search;
                            }
                        }
                    } else {
                        if (k == j) {
                            for (z = 0; z < edges[k].length; ++z) {
                                if (edges[k][z] == i) {
                                    break search;
                                }
                            }
                        }
                    }
                    newedges[k] = edges[k];
                    other[k][y][z] = "test";
                    break edgelist;
                    continue edgeList;
                }
                int m = edges[k].length - 1;
                int[] ne = new int[m];
                System.arraycopy(edges[k], 0, ne, 0, z);
                System.arraycopy(edges[k], z + 1, ne, z, m - z);
                newedges[k] = ne;
            }
        }
        return new Graph(newedges);
    }
}
