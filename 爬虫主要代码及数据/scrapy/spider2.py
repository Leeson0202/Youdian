import re
from queue import Queue
import __init__
from json import *
import threading
from bs4 import BeautifulSoup
import time
from random import choice

import get_HTTP_response

url_head_2 = "http://www.iciba.com/word?w="

threadlock = threading.Lock()
threads = []


def save(word_detail, letter, word, percent, f_json, f_record, s_time):
    ok = 0
    try:
        f_json.seek(f_json.seek(0, 2) - 2, 0)  # 文件指针移动到最后一个
        if f_json.read() == '[]':  # 判断 是否为空
            f_json.seek(f_json.seek(0, 2) - 1, 0)
            f_json.write(word_detail + ']')
            ok = 1
        else:  # 不为空
            f_json.seek(f_json.seek(0, 2) - 1, 0)
            f_json.write(',\n' + word_detail + ']')
            ok = 1
    except Exception as e:
        print('\n', 2, word_detail, e)

    finally:
        f_json.flush()
        if ok == 1:  # 记录对应的日志 用于下一次继续爬取
            f_record.seek(0)
            f_record.truncate()  # 清空
            try:
                f_record.writelines(letter + '\n' + ":".join(word) + '\n')
            except:
                pass
            finally:
                f_record.flush()
    end = time.time()
    print('\r{:>3.0f}%  {:>.1f}s  {}\t\t\t\t\t'.format(
        percent, end - s_time, word[0]), end='')  # 显示进度


def handle(word, letter, percent, f_json, f_record, f_error, s_time, index,ip_list):
    """
    :param word: 单词
    :param letter:  字母
    :return:  None
    """

    # Clerk Typist & Secretary; / dict / wd_29797.html;2
    # chemist's;/dict/wd_27595.html;2
    # word[0] = "chemist's"
    url = url_head_2 + word[0]  # 对应单词的url
    response = None
    for i in range(5):
        ip = choice(ip_list)
        response = get_HTTP_response.get(url=url,ip=ip)
        if response:
            break
        else:
            if i ==4:
                try:
                    # del ip_list[ip_list.index(ip)]
                    f_error.seek(0, 2)
                    f_error.write(word[0] + ';' + word[1] + ';1\n')
                    f_error.flush()
                except:
                    pass
                return
    # --------------------------处理数据
    if not response:  # 否则下一个单词
        return
    else:
        soup = BeautifulSoup(response.text, 'html.parser')  # 一锅汤
        box = soup.find_all(name='div', attrs={
            "class": "Content_center__3EE2R"})  # 划小区域
        try:
            box = box[0]
        except:
            f_error.seek(0, 2)
            f_error.write(word[0] + ';' + word[1] + ';1\n')
            f_error.flush()
            return
        if not box.ul:  # 如果这个盒子不存在 下一个单词 ；
            threadlock.acquire()  # 加个同步锁就好了#  否则提取具体信息
            f_error.seek(0, 2)
            f_error.write(word[0] + ';' + word[1] + ';2\n')
            f_error.flush()
            threadlock.release()
            return
        else:
            # ---------------------------------------提取有用信息
            spell, tag, clearfix, word_deformation, sentences, audio = __init__.MyBeautifulSoup(
                soup=box.ul, rex=2)  # 进入spider 提取详细单词信息
            word_spell = word[0]
            boolen = spell.lower() != word_spell.lower()

            if not spell or boolen:  # 如果提取失败 下一个单词
                try:
                    threadlock.acquire()  # 加个同步锁就好了#  否则提取具体信息
                    f_error.seek(0, 2)
                    f_error.write(word[0] + ';' + word[1] + ';2\n')
                except:
                    pass
                finally:
                    f_error.flush()
                    threadlock.release()

            else:
                # --------------------------------------audio_url
                audio_url = list()
                pattern1 = re.compile(r'"ph_en_mp3":"(http://res.iciba.com/resource/amp3/.{20,100}.mp3)",')
                audio_url1 = pattern1.findall(response.text)
                if audio_url1:
                    audio_url.extend(audio_url1)
                pattern2 = re.compile(r'"ph_am_mp3":"(http://res.iciba.com/resource/amp3/.{20,100}.mp3)","ph_tts_mp3"')
                audio_url2 = pattern2.findall(response.text)
                if audio_url2:
                    audio_url.extend(audio_url2)
                audio = dict(zip(audio, audio_url))

                # ----------------------------------转化为实例对象
                theword = __init__.Vocabulary(
                    spell=spell, href=word[1], tag=tag, clearfix=clearfix, word_deformation=word_deformation,
                    audio=audio, sentences=sentences)  # 创建单词对象
                word_detail = dumps(theword.__dict__)  # 将单词对象 转化为json格式

                # -----------------------------------------------储存数据
                # if index > 0:
                #     ok = True
                #     while ok:
                #         try:
                #             threads[index-1].join()
                #             ok = False
                #         except:
                #             print((index))
                #             pass

                threadlock.acquire()  # 加个同步锁就好了
                save(word_detail, letter, word,
                     percent, f_json, f_record, s_time)
                threadlock.release()


def spider_2(path1=None, path2=None, letter=None, key_word=None, s_time=None,ip_list=None):
    """
    1. 读取文件 获得每个单词并对单词进行strip处理
    2. 判断 是否从上次位置继续爬取
    2. 获取response 并判断是否存在
    3. 一锅汤 处理提取信息
    4. 处理信息后 储存到本地的json文件
    5. 如果单词保存成功就记录日志

    """
    global threads
    threads = []
    word_list = list()  # 初始化单词列表
    try:
        with open(path1, 'r', encoding="utf-8") as f:  # 读取 字母.txt 文件
            word_list = f.readlines()
        word_list = [i.split(":") for i in word_list]
        word_list = [[a[0].strip(), a[1].strip()] for a in word_list]
    except:
        print("文件不存在")
        return  # 没有文件则返回

    # ----------------------------- 打开所有文件
    f_json = open(path2, 'r+')
    f_error = open("./datas/daily/errors.txt", 'r+')
    f_record = open('./datas/daily/record.txt', 'w')

    # ---------------------------- 确定起始爬取位置和爬取单词范围
    index = 0  # 本来是从0开始
    if key_word:  # 判断 是否从上次位置继续爬取
        i = 0
        for i, myword in enumerate(word_list):
            if myword[0].strip() == key_word.strip():
                index = i + 1  # 如果找到下一个单词 记录下标
                break
        if i == len(word_list):
            return
    else:  # 否则重新爬取单词 初始化json文件 清楚erroe记录
        f_json.truncate()  # 清空
        f_json.writelines('[]')  # 初始化 并保持
        f_json.flush()

    word_list = word_list[index:]

    # ----------------------------------------------------------------------开始爬取详情部分:
    if len(word_list) > 0:
        for index, word in enumerate(word_list):
            percent = (index / len(word_list)) * 100  # 进度百分比
            if not word:
                continue

            # handle(word, letter, percent, f_json, f_record, f_error, s_time,index,ip_list)
            # --------------------------------------多线程
            t = threading.Thread(target=handle, args=(
                word, letter, percent, f_json, f_record, f_error, s_time, index,ip_list))
            threads.append(t)
        num = 200  # 设置的线程数
        t = None
        for i, t in enumerate(threads):
            try:
                t.start()
            except Exception as e:
                pass
            while len(threading.enumerate()) > num:
                time.sleep(0.3)
        for t in threads:
            t.join()
        t.join()
        f_json.close()
        f_error.close()
        f_record.close()
        print("")
        return
    else:
        return
