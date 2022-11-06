import threading

import MyClass
import json
from lxml import etree
import random
import time
from bs4 import BeautifulSoup

import Save
import get_response

url_head = "https://dict.eudic.net/dicts/en/"
thread_list = list()


def two(data, f_json, percent):
    url = url_head + data.spell
    # url = url_head + "attitude"
    # print(url)
    response = get_response.requests_get(url)
    if not response:  # 是否爬取成功
        return
    # response.encoding = response.apparent_encoding

    html = etree.HTML(response.text)
    soup = BeautifulSoup(response.text.replace("<br>", ""), "html.parser")
    # 1. 音标 及 发音 audio
    result = html.xpath('//*[@id="exp-head"]/div/span[1]/span/text()')

    result1 = html.xpath('//*[@id="exp-head"]/div/span[1]/a/@data-rel')
    result1 = ["https://api.frdic.com/api/v2/speech/speakweb?" + _ for _ in result1]
    if len(result) == 4:
        this_audio = [[result[0], result[1]], [result[2], result[3]]]
    elif len(result) == 2:
        this_audio = [[result[0], result[1]]]
    else:
        this_audio = list()

    try:
        if len(result1) == 2:
            this_audio[0].append(result1[0]), this_audio[1].append(result1[1])
        elif len(result1) == 1:
            this_audio[0].append(result1[0])
        else:
            pass
        # print(this_audio)
    except:
        pass

    data.audio = this_audio  # 加入到单词里面
    del result, result1, response, this_audio

    # 2. 变形  word_deformation
    # word_deformation = list()
    # tag = ""
    # taglist = list()
    # result = html.xpath('//*[@id="trans"]/span/text()')
    # if (result):
    #     for _ in result:
    #         if ':' in _:
    #             if tag != "":
    #                 word_deformation.append({"tag": tag, "tagList": taglist})
    #                 taglist = list()
    #             tag = _
    #         else:
    #             taglist.append(_)
    #     if taglist:
    #         word_deformation.append({"tag": tag, "tagList": taglist})
    #
    # data.word_deformation = word_deformation
    # del result,word_deformation
    #
    # # 3. 短语 phrase
    # phrase = list()
    # result = html.xpath('//*[@id="phrase"]/i/text()')
    # result1 = html.xpath('//*[@id="phrase"]/span/text()')
    # index = ""
    # _ = ""
    # if result:
    #     for index, _ in enumerate(result[:5]):
    #         phrase.append({"key": result[index], "value": result1[index]})
    #
    # data.phrase = phrase
    # del result, result1, html, index, _
    #
    # # 4. 近义词  similar
    # similar = list()
    # div = soup.findAll(name='div', attrs={
    #     "id": "ExpSYNChild"})
    # if div:
    #     tag = ""
    #     tagList = list()
    #     wClass = ""
    #     wClassList = list()
    #     for _ in div[0].contents:
    #         try:
    #             if _.text:
    #                 if _.text[0:2] == "解析":
    #                     break
    #                 if _.name == "h5":
    #                     if tag != "":
    #                         if wClass != "":
    #                             tagList.append({"wClass": wClass, "wClassList": wClassList})
    #                             wClassList = list()
    #
    #                         similar.append(
    #                             {"tag": tag, "tagList": tagList})  # [{"wClass": wClass, "wClassList": wClassList}]
    #                         wClass = ""
    #                         tagList = list()
    #                     tag = _.text
    #                 elif _.text[0] == "[":
    #                     if wClass != "":
    #                         tagList.append({"wClass": wClass, "wClassList": wClassList})
    #                         wClassList = list()
    #                     wClass = _.text
    #                 else:
    #                     if _.text[0] != ",":
    #                         wClassList.append(_.text)
    #         except:
    #             pass
    #     # 跳出循环 加一个
    #     if wClass != "":
    #         tagList.append({"wClass": wClass, "wClassList": wClassList})
    #
    #     similar.append(
    #         {"tag": tag, "tagList": tagList})  # [{"wClass": wClass, "wClassList": wClassList}]
    #     del tag, tagList, wClass, wClassList, _
    #
    # data.similar = similar
    # del div,similar
    #
    # # 例句 是否有 sentences
    # sentences = dict()
    # if data.sentences.__eq__(dict()):
    #     div = soup.findAll(name='div', attrs={"class": "lj_item"})
    #     if div:
    #         for _ in div[:5]:
    #             try:
    #                 if _.contents[0].contents:
    #                     sentences[_.contents[0].contents[0].text] = _.contents[0].contents[1].text
    #                     # temp_list = _.contents[0].contents[0:2]
    #             except:
    #                 pass
    #             pass
    #         del div, _
    # if sentences != {}:
    #     data.sentences = sentences
    # data.href = url
    # del soup,url,sentences


    pass
    # 保存数据
    Save.saveElement(data.__dict__, f_json, percent)
    return


def one(list, f_json):
    # 对每个单词进行处理 爬取 保存
    for index, data in enumerate(list):
        percent = (index / len(list)) * 100  # 进度百分比
        # a = random.choice(list)
        data = MyClass.Vocabulary(data)
        # two(data, f_json, percent)

        # 多线程
        t = threading.Thread(target=two, args=(data, f_json, percent))
        thread_list.append(t)


    num = 200  # 设置的线程数
    for i in range(len(thread_list)):
        try:
            thread_list.pop().start()
        except Exception as e:
            print(e)
        while len(threading.enumerate()) > num:
            time.sleep(0.2)
    while len(threading.enumerate()) > 4:
        time.sleep(5)
    return
