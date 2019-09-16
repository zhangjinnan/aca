# -*- coding: utf8 -*-
from data.initData import Data
from algorithms.gen import Gen
from algorithms.ant import Ant
from algorithms.ga import Ga
# -------------数据初始化相关------------
# 任务个数
TASK_LENGTH = 100
# 单个任务一般长度
TASK_NORMAL_SIZE = 100
# 处理任务的节点数量
NODE_LENGTH = 10
# 任务节点一般的处理能力
NODE_CALC_SPPED = 50

# -------------基因算法配置参数-----------
# 交叉概率
CROSS_RATE = 0.9
# 等位基因突变概率
MUTATION_RATE = 0.3
# 种群数量
LIFE_COUNT = 20
# 迭代次数
GEN_ITERNUM = 100

# -------------蚂蚁算法配置参数-----------
# 信息启发因子[0.01,0.99],越大越容易得出局部最优
ANT_P = 0.8
# 衰减值
ANT_Q = 1
# 浓度
ANT_R = 1
# 蚁群数量[10,10000]
ANT_NUM = 100
# 迭代次数
ANT_ITERNUM = 100

# --------------蚂蚁基因混合算法------------
# 迭代次数控制
# 基因迭代次数
GA_GEN_ITERNUM = 70
# 蚂蚁迭代次数
GA_ANT_ITERNUM = 30

if __name__ == '__main__':
    data = Data(task_length=TASK_LENGTH, task_normal_size=TASK_NORMAL_SIZE, node_length=NODE_LENGTH,
                node_calc_speed=NODE_CALC_SPPED).get_data()
    tasks = data['tasks']
    nodes = data['nodes']
    g = Gen(aCrossRate=CROSS_RATE, aMutationRate=MUTATION_RATE, aLifeCount=LIFE_COUNT, iterNum=GEN_ITERNUM)
    g.run(tasks=tasks, nodes=nodes)

    a = Ant(p=ANT_P, q=ANT_Q, r=ANT_R, antNum=ANT_NUM, iterNum=ANT_ITERNUM)
    a.run(tasks=tasks, nodes=nodes)

    ga = Ga(p=ANT_P, q=ANT_Q, r=ANT_R, ant_num=ANT_NUM, ant_iter=GA_ANT_ITERNUM, aCrossRate=CROSS_RATE,
            aMutationRate=MUTATION_RATE, aLifeCount=LIFE_COUNT, gen_iter=GA_GEN_ITERNUM)
    ga.run(tasks=tasks, nodes=nodes)
