import threading

import MyClass
import json
from lxml import etree
import random
import time
from bs4 import BeautifulSoup

import Save
import get_response

url_head = "http://dict.cn/"
thread_list = list()


def two(data, f_json, percent):
    url = url_head + data.spell
    # url = url_head + "activity"
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
    soup = BeautifulSoup(response.text.replace("<br>", ""), "html.parser")
    del response

    # 1. 变形  word_deformation
    word_deformation = list()
    tag = ""
    tagList = list()
    result = soup.findAll(name='div', attrs={"class": "shape"})
    if result:
        for _ in result[0].contents:
            if _.name == "label":
                if tag != "":
                    word_deformation.append({"tag": tag, "tagList": tagList})
                    tagList = list()
                tag = _.text.strip()
            elif _.name == "a":
                tagList.append(_.text.strip())
            else:
                pass
        if len(tagList) > 0:
            word_deformation.append({"tag": tag, "tagList": tagList})
        pass

    data.word_deformation = word_deformation
    del result, word_deformation, tag, tagList

    # 2. 短语 phrase
    phrase = list()
    result = soup.findAll(name='div', attrs={"class": "layout phrase"})
    key = ""
    value = ""
    _ = ""
    if result:
        for _ in result[0].contents:
            if _.name == "dl":
                for __ in _.contents:
                    if __.name == "dt":
                        key = __.text.strip()
                    elif __.name == "dd":
                        value = __.text.strip().split("\n")[0]
                phrase.append({"key": key, "value": value})

    data.phrase = phrase
    del result, html, _, key, value, phrase,

    # 3. 近义词  similar
    similar = list()
    tag = ""
    tagList = []
    _ = ''
    __ = ''
    div = soup.findAll(name='div', attrs={
        "class": "layout nfo"})
    if div:
        for _ in div[0].contents:
            if _.name == "div" and "【" in _.text:
                if len(tagList) > 0:
                    similar.append({"tag": tag, "tagList": tagList})
                    tagList = []
                tag = _.text.strip()
            elif _.name == "ul":
                for __ in _.contents[:15]:
                    if __.name == "li" and len(tagList) < 5:
                        for ___ in __.contents:
                            if ___.name == "a":
                                tagList.append(___.text.strip())
                        del ___
                del __
            else:
                pass
        del _

        if len(tagList) > 0:
            similar.append({"tag": tag, "tagList": tagList})

    data.similar = similar

    del div, similar, tag, tagList

    # 例句 是否有 sentences
    sentences = list()
    if data.sentences.__eq__(dict()):
        key = ""
        value = ""
        div = soup.findAll(name='div', attrs={"class": "layout sort"})
        if div:
            for _ in div[0].contents:
                if _.name == "ol":
                    for __ in _.contents:
                        if __.name == "li" and len(sentences) < 5:
                            if len(__.contents) > 2:
                                key = __.contents[0].text.strip()
                                value = __.contents[2].text.strip()
                                sentences.append({"key": key, "value": value})
        data.sentences = sentences
        del key, value, div, sentences
    data.href = url
    del soup, url

    pass
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

    num = 100  # 设置的线程数
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
