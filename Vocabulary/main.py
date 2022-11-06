import os
import time

# 1. 读取数据 按文件读取
# 2. 解析

import json
import pymysql

from Web import eudic, hai, youdict
from tools import statistics
from Save.sql import MySQLConnection, youdian
from tools import to_ruler


def readFile(path):
    temp = []
    try:
        with open(path, 'r+', encoding='utf-8') as f:
            temp = json.load(f)
    except:
        pass
    return temp


def youdianmain():
    for i in range(ord('p'), ord('p') + 1):
        path = 'datas/json/' + chr(i) + '.json'
        json_path = 'datas/new/' + chr(i) + '.json'
        if not os.path.exists(json_path):  # --------判断文件是否存在
            open(json_path, 'w')  # 自动创建一个
        # f_json = None
        f_json = open(json_path, 'r+')
        f_json.truncate()  # 清空
        f_json.writelines('[]')  # 初始化 并保持
        f_json.flush()

        datas = readFile(path)
        if datas:  # 读取成功 -进入解析
            # youdict.one(datas, f_json)
            eudic.one(datas, f_json)

        # f_json.close()


def to_sql():
    # 1. 连接数据库
    # 2. 读取文件
    # 3. 循环操作
    start = time.time()
    cursor = MySQLConnection('localhost', 3306, 'root', 'root', 'vocabulary')  # 已经连接上数据库
    if not cursor:
        return
    for i in range(ord('a'), ord('z') + 1):
        path = 'datas/ruler/' + chr(i) + '.json'
        datas = readFile(path)
        if len(datas) < 1:  # 读取成功 -进入解析
            return
        youdian(datas, cursor)


# youdianmain()
# statistics.handle()
# to_ruler.doMain()
to_sql()
