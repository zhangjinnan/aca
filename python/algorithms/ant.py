# -*- coding: utf-8 -*-
import random


class Ant(object):
    """
    问题抽象，任务分配抽象为蚂蚁觅食，时间为蚂蚁走过的路程.
    任务抽象为食物，分配策略即为迟到每个食物选择的路径
    """
    def __init__(self, p, q, r, antNum, iterNum):
        self.pheromoneMatrix = []
        self.p = p    # 随时间推移信息素衰减比例
        self.q = q      # 完成一次迭代，信息素增加比例
        self.r = r
        self.antNum = antNum  # 10只蚂蚁
        self.tasks = []
        self.nodes = []
        self.best_road = []   # 迟到改序号食物的最佳路径
        self.best_ant_index = 0
        self.all_road = []
        self.time_used = []
        self.solution = []
        self.iterNum = iterNum

    def init(self, task_length, node_length):
        """初始化信息素矩阵"""
        self.best_road = []
        self.pheromoneMatrix = []
        for task in range(task_length):
            self.best_road.append(random.randint(0, node_length - 1))
            self.pheromoneMatrix.append([1.0 for node in range(node_length)])
        self.solution = self.best_road

    def run(self, tasks, nodes):
        self.tasks = tasks
        self.nodes = nodes
        self.init(len(tasks), len(nodes))
        n = self.iterNum
        while n > 0:
            self.simulate()
            n = n-1
        print "蚂蚁最优解耗时", self.time_used[self.best_ant_index], "解决方案", self.all_road[self.best_ant_index]
        return self.all_road[self.best_ant_index]

    def ga_run(self, tasks, nodes):
        self.tasks = tasks
        self.nodes = nodes
        self.init(len(tasks), len(nodes))
        n = self.iterNum
        while n > 0:
            self.simulate()
            n = n-1
        print "混合中间值最优解耗时", self.time_used[self.best_ant_index], "中间解决方案", self.all_road[self.best_ant_index]
        return self.all_road[self.best_ant_index]

    def simulate(self):
        all_ant_road = []
        for ant in range(self.antNum):
            ant_road = []
            for index, task in enumerate(self.tasks):
                nodeIndex = self.assignOneTask(ant, index)
                ant_road.append(nodeIndex)
            all_ant_road.append(ant_road)
        self.all_road = all_ant_road
        self.judge()
        self.updatePheromoneMatrix()

    def judge(self):
        """
        评估蚂蚁走过的路径并输出到信息素矩阵中
        :return:
        """
        all_ant = self.all_road
        all_ant_time_used = []
        best_ant_time_used = float('inf')
        best_ant_index = 0
        for ant in range(self.antNum):
            time_use_sum = [0 for i in range(0, len(self.nodes))]
            for index, node in enumerate(all_ant[ant]):
                time_use_sum[node] += self.tasks[index] * 1.0 / self.nodes[node]
            time_used = max(time_use_sum)
            if time_used < best_ant_time_used:
                best_ant_index = ant
                best_ant_time_used = time_used
            all_ant_time_used.append(time_used)
        self.solution = all_ant[best_ant_index]
        self.time_used = all_ant_time_used
        self.best_ant_index = best_ant_index

    def updatePheromoneMatrix(self):
        for task in range(len(self.tasks)):
            for node in range(len(self.nodes)):
                self.pheromoneMatrix[task][node] -= self.q
                if self.pheromoneMatrix[task][node] < 0.1:
                    self.pheromoneMatrix[task][node] = 0.1

        for task, road in enumerate(self.all_road[self.best_ant_index]):
            self.pheromoneMatrix[task][road] += self.r
        for task in range(len(self.tasks)):
            self.best_road[task] = 0
            max_val = self.pheromoneMatrix[task][0]
            for index, node_val in enumerate(self.pheromoneMatrix[task][1:]):
                if node_val > max_val:
                    self.best_road[task] = index+1
                    max_val = node_val

    def assignOneTask(self, antIndex, index):
        criticalPoint = 0.2*max(self.pheromoneMatrix[index])/sum(self.pheromoneMatrix[index])*self.antNum
        if antIndex < criticalPoint:
            return self.solution[index]
        rate = random.random()
        if rate < self.p:
            return self.best_road[index]
        return random.randint(0, len(self.nodes) - 1)


