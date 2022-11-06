import threading

import MyClass
import json
from lxml import etree
import random
import time
from bs4 import BeautifulSoup

import Save
import get_response

url_head = "https://www.baihuawen.com/dict/word/"
thread_list = list()


def two(data, f_json, percent):
    url = url_head + data.spell
    # url = url_head + "hobby"
    # print(url)
    response = get_response.requests_get(url)
    if not response:  # 是否爬取成功
        data.word_deformation = list()
        data.phrase = list()
        data.similar = list()
        Save.saveElement(data.__dict__, f_json, percent)
        return
    # response.encoding = response.apparent_encoding

    html = etree.HTML(response.text)
    soup = BeautifulSoup(response.text, "html.parser")
    del response

    # 1. 翻译 clearfix
    clearfix = list()
    div = soup.findAll(name='p', attrs={"class": "chmean mt10"});
    if len(div) > 0:
        li = div[0]
        for _ in li:
            if _.text:
                clearfix.append(_.text)
    # 2. tag
    tag = ""
    div = soup.findAll(name='p', attrs={"class": "tag",});
    if len(div) > 0:
        tag = div[0].text.strip()
    # 判断是否爬到
    if len(clearfix) > 0:
        data.clearfix = clearfix
    if tag:
        data.tag = tag
    del div,url,soup,html,clearfix,tag
    # 保存数据
    Save.saveElement(data.__dict__, f_json, percent)
    return


def one(list, f_json):
    # 对每个单词进行处理 爬取 保存
    for index, data in enumerate(list):
        percent = (index / len(list)) * 100  # 进度百分比
        # data = MyClass.Vocabulary.sql(random.choice(list))
        data = MyClass.Vocabulary(data)
        # two(data, f_json, percent)

        # 多线程
        t = threading.Thread(target=two, args=(data, f_json, percent))
        thread_list.append(t)

    num = 20  # 设置的线程数
    for i in range(len(thread_list)):
        try:
            thread_list.pop().start()
        except Exception as e:
            print(e)
        while len(threading.enumerate()) > num:
            time.sleep(0.2)
    while len(threading.enumerate()) > 4:
        time.sleep(10)

    return
