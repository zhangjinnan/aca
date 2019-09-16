import numpy


class Data(object):
    def __init__(self, task_length=20, task_normal_size=100, node_length=10, node_calc_speed=20):
        self.task_length = task_length
        self.node_lengrh = node_length
        self.normal_task_size = task_normal_size
        self.normal_node_size = node_calc_speed
        self.data = None

    def get_data(self):
        if self.data:
            return self.data
        tasks = numpy.random.poisson(self.normal_task_size, self.task_length)
        nodes = numpy.random.poisson(self.normal_node_size, self.node_lengrh)
        self.data = dict(tasks=tasks, nodes=nodes)
        return self.data

    def update_data(self):
        self.data = None
        return self.get_data()
