# -*- encoding: utf-8 -*-
from algorithms.ant import Ant
from algorithms.gen import Gen


class Ga(object):
    def __init__(self, p, q, r, ant_num, ant_iter, aCrossRate, aMutationRate, aLifeCount, gen_iter):
        self.ant = Ant(p, q, r, antNum=ant_num, iterNum=ant_iter)
        self.gen = Gen(aCrossRate=aCrossRate, aMutationRate=aMutationRate, aLifeCount=aLifeCount, iterNum=gen_iter)

    def run(self, tasks, nodes):
        ant_res = self.ant.ga_run(tasks=tasks, nodes=nodes)
        res = self.gen.ant_run(tasks=tasks, nodes=nodes, gene=ant_res)
        print "混合算法最优解耗时", res['time'], "解决方案", res['solution']
