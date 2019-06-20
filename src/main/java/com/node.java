package com;

import java.util.ArrayList;
import java.util.List;

public class node {
    //处理节点集合(nodes[i]表示第i个处理节点的处理速度)
    List<Integer> nodes=new ArrayList<Integer>();
    //处理节点数量
    int nodeNum = 10;
    /** 节点处理速度取值范围 */
    int[] nodeSpeendRange = {10,100};
    public int getNodeNum() {
        return nodeNum;
    }

    public void setNodeNum(int nodeNum) {
        this.nodeNum = nodeNum;
    }
    public List<Integer> getNodes() {
        return nodes;
    }

    public void setNodes(List<Integer> nodes) {
        this.nodes = nodes;
    }

    public int[] getNodeSpeendRange() {
        return nodeSpeendRange;
    }

    public void setNodeSpeendRange(int[] nodeSpeendRange) {
        this.nodeSpeendRange = nodeSpeendRange;
    }

}
