package ga;

import com.node;
import com.task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class AcaCore {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //信息素矩阵
    double[][] pheromoneMatrix;
    //时间矩阵
    Double[][] timeMatrix;
    //任务数
    int taskNum;
    //节点数
    int nodeNum;
    //随时间推移信息素衰减比例
    float p=0.5f;
    //int p=5;
    //完成一次迭代，信息素增加比例
    int q=2;
    //int q=20;
    //蚂蚁数量
   // int antNum;
    //任务集合(tasks[i]表示第i个任务的长度)
    List<Integer> tl=new ArrayList<Integer>();
    //处理节点集合(nodes[i]表示第i个处理节点的处理速度)
    List<Integer> nl=new ArrayList<Integer>();
    //最大信息素矩阵
      ArrayList<Integer> maxPheromoneMatrix=new ArrayList();
    //蚂蚁临界编号
      ArrayList<Integer> criticalPointMatrix=new ArrayList();
    public HashMap<Integer, List> exe(Integer iteratorNum,Integer antNum){
        try {
            task tasks = new task();
            node nodes = new node();
            taskNum = tasks.getTaskNum();
            nodeNum = nodes.getNodeNum();
            // 初始化任务集合
            tl = initRandomArray(taskNum);
            logger.info(Arrays.toString(tl.toArray()));
            //debug
    //        int debug_t[] = {70, 93, 11, 36, 53, 7, 16, 93, 21, 64, 81, 28, 40, 57, 64, 98, 73, 99, 80, 22, 12, 16, 65, 91, 24, 80, 97, 77, 7, 20, 93, 23, 96, 42, 19, 24, 88, 92, 29, 43, 99, 52, 3, 66, 75, 65, 79, 20, 88, 10, 2, 7, 14, 63, 8, 50, 76, 24, 9, 70, 98, 57, 66, 88, 81, 51, 75, 71, 91, 79, 27, 84, 37, 49, 66, 28, 87, 1, 24, 21, 71, 97, 77, 49, 86, 62, 13, 76, 33, 6, 39, 53, 23, 99, 59, 13, 64, 51, 71, 73};
     //       for(int i=0;i<debug_t.length;i++){ tl.add(debug_t[i]); }
            tasks.setTasks(tl);
            // 初始化节点集合
            nl = initRandomArray(nodeNum);
            logger.info(Arrays.toString(nl.toArray()));
            // debug
    //        int debug_n[] = {49, 29, 70, 98, 80, 9, 84, 57, 10, 100};
     //       for(int i=0;i<debug_n.length;i++) { nl.add(debug_n[i]); }
            nodes.setNodes(nl);
            //aca算法
            return aca(tasks, nodes,iteratorNum,antNum);
        }catch (Exception e) {
            logger.error("执行异常信息：",e);
            throw new  RuntimeException("执行异常");
        }
    }

    public HashMap<Integer, List> aca(task tasks,node nodes,Integer iteratorNum,Integer antNum) {
        // 初始化任务执行时间矩阵
        timeMatrix=initTimeMatrix(tasks, nodes);
        // 初始化信息素矩阵
        pheromoneMatrix=initPheromoneMatrix(taskNum, nodeNum);
        //初始化临界点矩阵
        initCriticalPointMatrix(pheromoneMatrix,antNum);
        // 迭代搜索
        return acaSearch(iteratorNum, antNum);
    }
    /**
     * 创建随机数组
     * @param length 数组长度
     */
    public List initRandomArray(int length) {
        ArrayList<Integer> randomArray=new ArrayList();
        Random random = new Random();
        for (int i=0; i<length; i++) {
            randomArray.add(random.nextInt(100) + 1);
        }
        return randomArray;
    }
    /**
     * 初始化任务处理时间矩阵
     *
     * @param tasks 任务(长度)列表
     * @param nodes 节点(处理速度)列表
     */
    public Double [ ][ ] initTimeMatrix(task tasks, node nodes) {
        Double [ ][ ] timeMatrix;
        timeMatrix = new Double [tasks.getTaskNum()][ ];    //动态创建第一维
        for (int i=0; i<tasks.getTaskNum(); i++) {
            timeMatrix [i] = new Double [nodes.getNodeNum()];    //动态创建第二维
            for (int j=0; j<nodes.getNodeNum(); j++) {
                timeMatrix[i][j]=Double.parseDouble(String.valueOf(tasks.getTasks().get(i)*1.0 / nodes.getNodes().get(j)));
            }
        }
        return timeMatrix;
    }

    /**
     * 初始化信息素矩阵(全为1)
     * @param taskNum 任务数量
     * @param nodeNum 节点数量
     */
    public double [ ][ ] initPheromoneMatrix(Integer taskNum, Integer nodeNum) {
        double [ ][ ] pheromoneMatrix;
        pheromoneMatrix = new double [taskNum][];    //动态创建第一维
        for (int i=0; i<taskNum; i++) {
            pheromoneMatrix [i] = new double [nodeNum];    //动态创建第二维
            for (int j=0; j<nodeNum; j++) {
                pheromoneMatrix[i][j]=1.0;
            }
        }
        return pheromoneMatrix;
    }
    /**
     * 迭代搜索
     * @param iteratorNum 迭代次数
     * @param antNum 蚂蚁数量
     */
    public HashMap acaSearch(Integer iteratorNum, Integer antNum) {
        HashMap resultData=new HashMap();
        for (int itCount=0; itCount<iteratorNum; itCount++) {
            // 本次迭代中，所有蚂蚁的路径
            HashMap pathMatrix_allAnt=new HashMap<Integer,List>();
            for (int antCount=0; antCount<antNum; antCount++) {
                // 第antCount只蚂蚁的分配策略(pathMatrix[i][j]表示第antCount只蚂蚁将i任务分配给j节点处理)
                int [ ][ ] pathMatrix_oneAnt = initMatrix(taskNum, nodeNum, 0);
                for (int taskCount=0; taskCount<taskNum; taskCount++) {
                    // 将第taskCount个任务分配给第nodeCount个节点处理
                  //  int nodeCount = assignOneTask(antCount, taskCount, nodes, pheromoneMatrix);
                    int nodeCount = assignOneTask(antCount, taskCount,0, pheromoneMatrix);
                    pathMatrix_oneAnt[taskCount][nodeCount] = 1;
                }
                // 将当前蚂蚁的路径加入pathMatrix_allAnt
                pathMatrix_allAnt.put(antCount,pathMatrix_oneAnt);
            }

            // 计算本次迭代中所有蚂蚁的任务处理时间
            List timeArray_oneIt = calTime_oneIt(pathMatrix_allAnt);
            // 将本地迭代中 所有蚂蚁的 任务处理时间加入总结果集
            resultData.put(itCount,timeArray_oneIt);

            // 更新信息素
            updatePheromoneMatrix(pathMatrix_allAnt, pheromoneMatrix, timeArray_oneIt,antNum);
        }
        return resultData;
    }


    public int [ ][ ] initMatrix(int taskNum, int nodeNum, int defaultNum){
        int [ ][ ] arr ;
        arr = new int [taskNum][ ];    //动态创建第一维
        for (int i = 0 ; i < taskNum ; i++ ) {
            arr [ i ] = new int [ nodeNum ];    //动态创建第二维
            for(int j=0 ; j < nodeNum ; j++) {
                arr [ i ][ j ] = defaultNum;
            }
        }
        return arr;
    }

    /**
     * 将第taskCount个任务分配给某一个节点处理
     * @param antCount 蚂蚁编号
     * @param taskCount 任务编号
     * @param nodes 节点集合
     * @param pheromoneMatrix 信息素集合
     */
    public int assignOneTask(int antCount, int taskCount,int nodes, double[][] pheromoneMatrix) {
        //当前的任务编号是taskCount，当前的信息素浓度矩阵是pheromoneMatrix，那么任务将会分配给pheromoneMatrix[taskCount]这一行中信息素浓度最高的节点。
         // 若当前蚂蚁编号在临界点之前，则采用最大信息素的分配方式
         if (antCount < criticalPointMatrix.get(taskCount)) {
             return maxPheromoneMatrix.get(taskCount);
         }
        // 若当前蚂蚁编号在临界点之后，则采用随机分配方式
        Random random = new Random();
        return random.nextInt((nodeNum-1) + 0);
    }

    /**
     * 计算一次迭代中，所有蚂蚁的任务处理时间
     * @param pathMatrix_allAnt 所有蚂蚁的路径
     */
    public List calTime_oneIt(HashMap pathMatrix_allAnt) {
        ArrayList<Integer> time_allAnt=new ArrayList();
        for (int antIndex=0; antIndex<pathMatrix_allAnt.size(); antIndex++) {
            // 获取第antIndex只蚂蚁的行走路径
            int[][] pathMatrix=(int[][])pathMatrix_allAnt.get(antIndex);
            // 获取处理时间最长的节点 对应的处理时间
            int maxTime = -1;
            for (int nodeIndex=0; nodeIndex<nodeNum; nodeIndex++) {
                // 计算节点taskIndex的任务处理时间
                int time = 0;
                for (int taskIndex=0; taskIndex<taskNum; taskIndex++) {
                    if(pathMatrix[taskIndex][nodeIndex]==1){
                        time +=timeMatrix[taskIndex][nodeIndex];
                    }
                }
                // 更新maxTime
                if (time > maxTime) {
                    maxTime = time;
                }
            }
            time_allAnt.add(maxTime);
        }
        return time_allAnt;
    }

    /**
     * 更新信息素
     * @param pathMatrix_allAnt 本次迭代中所有蚂蚁的行走路径
     * @param pheromoneMatrix 信息素矩阵
     * @param timeArray_oneIt 本次迭代的任务处理时间的结果集
     */
    public void updatePheromoneMatrix(HashMap pathMatrix_allAnt, double[][] pheromoneMatrix, List timeArray_oneIt,int antNum) {
        // 所有信息素均衰减p%
        for (int i=0; i<taskNum; i++) {
            for (int j=0; j<nodeNum; j++) {
                pheromoneMatrix[i][j] *= p;
            }
        }

        // 找出任务处理时间最短的蚂蚁编号
        //Long minTime = 0L;
        Long minTime=Long.MAX_VALUE;
        int minIndex = -1;
        for (int antIndex=0; antIndex<antNum; antIndex++) {
            if (Integer.parseInt(timeArray_oneIt.get(antIndex).toString()) < minTime) {
                minTime = Long.parseLong(timeArray_oneIt.get(antIndex).toString()) ;
                minIndex = antIndex;
            }
        }

        // 将本次迭代中最优路径的信息素增加q%
        int [][]pathMatrix_oneAnt = (int[][])pathMatrix_allAnt.get(minIndex);
        for (int taskIndex=0; taskIndex<taskNum; taskIndex++) {
            for (int nodeIndex=0; nodeIndex<nodeNum; nodeIndex++) {
                //double[][] pathMatrix_oneAnt=(double[][])pathMatrix_allAnt.get(minIndex);
                if (pathMatrix_oneAnt!=null) {
                    if (pathMatrix_oneAnt[taskIndex][nodeIndex] == 1) {
                        pheromoneMatrix[taskIndex][nodeIndex] *= q;
                    }
                }
            }
        }

        for (int taskIndex=0; taskIndex<taskNum; taskIndex++) {
            double maxPheromone = pheromoneMatrix[taskIndex][0];
            int maxIndex = 0;
            double sumPheromone = pheromoneMatrix[taskIndex][0];
            boolean isAllSame = true;

            for (int nodeIndex=1; nodeIndex<nodeNum; nodeIndex++) {
                if (pheromoneMatrix[taskIndex][nodeIndex] > maxPheromone) {
                    maxPheromone = pheromoneMatrix[taskIndex][nodeIndex];
                    maxIndex = nodeIndex;
                }

                if (pheromoneMatrix[taskIndex][nodeIndex] != pheromoneMatrix[taskIndex][nodeIndex-1]){
                    isAllSame = false;
                }

                sumPheromone += pheromoneMatrix[taskIndex][nodeIndex];
            }

            // 若本行信息素全都相等，则随机选择一个作为最大信息素
            if (isAllSame==true) {
                Random random = new Random();
                maxIndex =random.nextInt((nodeNum-1) + 0);
                maxPheromone = pheromoneMatrix[taskIndex][maxIndex];
            }

            // 将本行最大信息素的下标加入maxPheromoneMatrix
            maxPheromoneMatrix.add(maxIndex);

            // 将本次迭代的蚂蚁临界编号加入criticalPointMatrix(该临界点之前的蚂蚁的任务分配根据最大信息素原则，而该临界点之后的蚂蚁采用随机分配策略)
            if (sumPheromone!=0) {
                criticalPointMatrix.add((int) Math.round(antNum * (maxPheromone / sumPheromone)));
            }
        }
    }

    public void initCriticalPointMatrix(double[][] pheromoneMatrix,int antNum){
        for (int taskIndex=0; taskIndex<taskNum; taskIndex++) {
            double maxPheromone = pheromoneMatrix[taskIndex][0];
            int maxIndex = 0;
            double sumPheromone = pheromoneMatrix[taskIndex][0];
            boolean isAllSame = true;
            for (int nodeIndex = 1; nodeIndex < nodeNum; nodeIndex++) {
                if (pheromoneMatrix[taskIndex][nodeIndex] > maxPheromone) {
                    maxPheromone = pheromoneMatrix[taskIndex][nodeIndex];
                    maxIndex = nodeIndex;
                }
                if (pheromoneMatrix[taskIndex][nodeIndex] != pheromoneMatrix[taskIndex][nodeIndex - 1]) {
                    isAllSame = false;
                }
                sumPheromone += pheromoneMatrix[taskIndex][nodeIndex];
            }
            // 若本行信息素全都相等，则随机选择一个作为最大信息素
            if (isAllSame == true) {
                Random random = new Random();
                maxIndex = random.nextInt((nodeNum - 1) + 0);
                maxPheromone = pheromoneMatrix[taskIndex][maxIndex];
            }
            // 将本行最大信息素的下标加入maxPheromoneMatrix
            maxPheromoneMatrix.add(maxIndex);
            // 将本次迭代的蚂蚁临界编号加入criticalPointMatrix(该临界点之前的蚂蚁的任务分配根据最大信息素原则，而该临界点之后的蚂蚁采用随机分配策略)
            if (sumPheromone != 0) {
                //criticalPointMatrix.add((int) Math.round(antNum * (maxPheromone / sumPheromone)));
                criticalPointMatrix.add(0);
            }
        }
    }
}
