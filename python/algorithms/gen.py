# -*- encoding: utf-8 -*-
import random
LIFE_INIT_SCORE = -1


class Life(object):
    def __init__(self, aGen=None):
        self.gen = aGen
        self.score = LIFE_INIT_SCORE


class Gen(object):
    # 初始化概率
    def __init__(self, aCrossRate, aMutationRate, aLifeCount, iterNum):
        self.crossRate = aCrossRate         # 交叉概率
        self.mutationRate = aMutationRate   # 突变概率
        self.lifeCount = aLifeCount         # 种群数量
        self.lives = []
        self.tasks = []
        self.nodes = []
        self.best_list = []
        self.bounds = 0.0  # 适配度之和
        self.probability_matrix = []  # 适配概率矩阵
        self.iterNum = iterNum

    def initLifes(self, gene_length, base_size):
        """初始化种群
           gen_length: 基因长度
           base_size: 单体基因种类
        """
        self.lives = []
        for i in range(self.lifeCount):
            gene = [random.randint(0, base_size-1) for i in range(0, gene_length)]
            random.shuffle(gene)
            life = Life(gene)
            self.lives.append(life)

    # 接受测试数据开始模拟
    def run(self, tasks, nodes):
        self.tasks = tasks
        self.nodes = nodes
        self.initLifes(len(tasks), len(nodes))
        n = self.iterNum
        while n > 0:
            self.next()
            n = n-1
        print "基因最优解耗时", 1.0/self.best_list[0].score,"解决方案",self.best_list[0].gen

    def ant_init(self, gene):
        life = Life(gene)
        self.lives[0] = life

    def ant_run(self, tasks, nodes, gene):
        self.tasks = tasks
        self.nodes = nodes
        self.initLifes(len(tasks), len(nodes))
        self.ant_init(gene=gene)
        n = self.iterNum
        while n > 0:
            self.next()
            n = n - 1
        return dict(time=1.0/self.best_list[0].score, solution=self.best_list[0].gen)

    def next(self):
        """
        创建下一代
        :return:
        """
        self.judge()
        new_lives = []
        new_lives += self.best_list
        while len(new_lives) < self.lifeCount:
            new_lives.append(self.newChild())
        self.lives = new_lives

    def newChild(self):
        """
        繁衍一个后代
        :return:
        """
        parent1 = self.getOne()
        rate = random.random()
        # 判断是否发生交叉变异
        if rate < self.crossRate:
            # 交叉
            parent2 = self.getOne()
            gene = self.cross(parent1, parent2)
        else:
            gene = parent1.gen[:]
        # 按概率突变
        rate = random.random()
        if rate < self.mutationRate:
            gene = self.mutation(gene)
        return Life(gene)

    def cross(self, parent1, parent2):
        """
        交叉
        :return:
        """
        index1 = random.randint(0, len(self.tasks) - 1)
        index2 = random.randint(index1, len(self.tasks) - 1)
        tempGene = parent2.gen[index1:index2]
        newGene = []
        p1len = 0
        for index, g in enumerate(parent1.gen):
            if index >= index1 and index < index2:
                newGene.append(tempGene[index - index1])
            else:
                newGene.append(g)
        return newGene

    def mutation(self, gene):
        """
        基因突变
        :return:
        """
        index1 = random.randint(0, len(self.tasks) - 1)
        index2 = random.randint(0, len(self.tasks) - 1)
        gene[index1], gene[index2] = gene[index2], gene[index1]
        return gene

    def getOne(self):
        """
        按照适配度不等概率选择一个个体
        :return:
        """
        rate = random.random()
        index = 0
        for i, p in enumerate(self.probability_matrix):
            if rate < p:
                index = i
                break
        return self.lives[index]

    def judge(self):
        """
        评估每一个个体的适配值
        :return:
        """
        self.bounds = 0.0
        best_one = self.lives[0]
        best_one.score = self.fitnessFunction(best_one)
        self.bounds += best_one.score
        best_list = [best_one]
        for life in self.lives[1:]:
            life.score = self.fitnessFunction(life)
            self.bounds += life.score
            if life.score > best_list[0].score:
                best_list = [life]
            elif life.score == best_list[0].score:
                best_list.append(life)
        self.best_list = best_list
        p_index_value = 0
        for life in self.lives:
            p = p_index_value + life.score/self.bounds
            self.probability_matrix.append(p)
            p_index_value = p

    def fitnessFunction(self, alife):
        """
        定义个体适度函数, 消耗时间越长表明适应度越低
        :return:
        """
        time_use_sum = [0 for i in range(0, len(self.nodes))]
        for i, g in enumerate(alife.gen):
            time_use_sum[g] += self.tasks[i] * 1.0 / self.nodes[g]
        return 1.0/max(time_use_sum)
        #return max(time_use_sum)

