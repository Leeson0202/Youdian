import random

from scrapy.spider1 import *
from scrapy.spider2 import *
from scrapy.spider3 import *
from sql.in_sql import *
from watch.watch import *
import time
from get_IP import *

url_head_1 = "https://www.koolearn.com"
url_head_2 = "http://www.iciba.com/word?w="
url_head_3 = "https://www.koolearn.com/dict/search/index?keywords="
proxy_pool_url = 'http://localhost:5555/random'
ip_list = []
host = 'localhost'
port = 3306
user = 'root'
password = '123456'
database = 'youdian'


# 定义单词的类
class Vocabulary:
    # spell tag clearfix word_deformation sentences audio分别为: 拼写 标签 词性翻译 变形 例句列表 单词发音

    def __init__(self, spell=None, href=None, tag=None, clearfix=None, word_deformation=None, sentences=None,
                 audio=None):
        self.spell = spell  # 单词的拼写
        self.tag = tag  # 四六级、高中
        self.clearfix = clearfix  # 词性和翻译
        self.word_deformation = word_deformation  # 变形
        self.sentence = sentences  # 单词的例句
        self.audio = audio  # 单词的发音
        self.href = href  # 网址


def get_ok_ip():
    global ip_list
    with open("datas/ip/OkIp_Proxy.txt", "r") as f:
        ip_list = f.readlines()
    ip_list = [x.strip() for x in ip_list[:-1]]
    return


def end_save(f, list, str):
    try:
        f.seek(0, 2)
        for i in list:
            f.write(i + str)
        f.flush()
        return 1
    except:
        return 0


def init_vocabulay():
    spell = tag = audio = None
    clearfix = word_deformation = audio = list()
    sentences = dict()
    return spell, tag, clearfix, word_deformation, sentences, audio


def MyBeautifulSoup(soup=None, rex=None):
    '''
    提取关键词 并返回给相应的函数
    '''
    if rex == 1:

        box = soup.find_all(name='div', attrs={
            "class": "word-box"})[0]  # 找到 单词 box
        word_list = box.find_all('a')
        wordspell_list = [x.string for x in word_list]
        wordurl_list = [x.get('href') for x in word_list]
        p = list(zip(wordspell_list, wordurl_list))
        my_word_list = [':'.join(x) for x in p]
        return my_word_list

    elif rex == 3:
        # spell tag clearfix word_deformation sentences audio分别为: 拼写 标签 词性翻译 变形 例句列表 单词发音
        spell, tag, clearfix, word_deformation, sentences, audio = init_vocabulay()  # 初始化单词格式
        word_box = None


    else:
        # spell tag clearfix word_deformation sentences audio分别为: 拼写 标签 词性翻译 变形 例句列表 单词发音
        spell, tag, clearfix, word_deformation, sentences, audio = init_vocabulay()  # 初始化单词格式
        word_box = None
        try:
            # -------------------------------spell
            word_box = soup.find_all(
                name='div', attrs={"class": "FoldBox_fold__1GZ_2"})  # 找到 单词简介box
            word_box = word_box[0]
            spell = word_box.find_all(
                name='h1', attrs={"class": "Mean_word__3SsvB"})  # 找到单词
            if (not spell):
                spell = word_box.find_all(
                    name='h2', attrs={"class": "Mean_sentence__2NXAD"})[0].text  # 找到单词拼写
            else:
                spell = spell[0].text
            # --------------------------- clearfix
            Mean_part = word_box.find_all(
                name='ul', attrs={"class": "Mean_part__1RA2V"})  # 找到 词性 及 翻译
            if Mean_part:
                clearfix = [x.text for x in Mean_part[0].contents]
            else:
                Mean_part = word_box.find_all(
                    name='h3', attrs={"class": "Mean_title__2BwLF"})  # 找到 词性 及 翻译
                if Mean_part:
                    clearfix = Mean_part[0].nextSibling.contents[0]

            if len(clearfix) == 0 or spell is None:  # 判断拼写和翻译 使用否都存在
                init_vocabulay()
                return spell, tag, clearfix, word_deformation, sentences, audio
            else:
                # ----------------------------- tag
                tag = word_box.find_all(
                    name='p', attrs={"class": "Mean_tag__2vGcf"})  # 找到 标签 四六级
                if tag:  # 如果 有标签
                    tag = tag[0].text
                else:
                    tag = ''
                # ---------------------------------audio
                audio = word_box.find_all(name='ul', attrs={"class": "Mean_symbols__5dQX7"})
                if audio:
                    audio = audio[0].contents
                    audio = [x.text for x in audio]
                else:
                    audio = list()

                # -------------------------------- word_deformation
                word_deformation = soup.find_all(name='ul', attrs={"class": "Morphology_morphology__3g6fA"})
                if not word_deformation:
                    word_deformation = dict()
                else:
                    word_deformation = word_deformation[0].find_all('li')
                    word_deformation = [x.text for x in word_deformation]
                # -------------------------------- sentences_box
                sentences_box = soup.find_all(
                    name='div', attrs={"class": "SceneSentence_scene__1Dnz6"})  # 找到 例句box
                if sentences_box:  # 有例句则 提取出字典
                    sentences_e = sentences_box[0].find_all(
                        name='p', attrs={"class": "NormalSentence_en__3Ey8P"})  # 例句
                    sentences_c = sentences_box[0].find_all(
                        name='p', attrs={"class": "NormalSentence_cn__27VpO"})  # 例句翻译
                    if len(sentences_c) > 4:  # 提取并合成字典
                        sentences_e = [e.text for e in sentences_e[0:5]]
                        sentences_c = [c.text for c in sentences_c[0:5]]
                        sentences = dict(zip(sentences_e, sentences_c))
                    else:
                        sentences_e = [e.text for e in sentences_e]
                        sentences_c = [c.text for c in sentences_c]
                        sentences = dict(zip(sentences_e, sentences_c))
                else:
                    sentences = dict()

                return spell, tag, clearfix, word_deformation, sentences, audio
        except Exception as a:
            print('\n', 1, a)
            init_vocabulay()
            return spell, tag, clearfix, word_deformation, sentences, audio


def word_sort(path):
    """
    词汇爬取后 进行去重 排序
    """
    lines = list()  # 初始化
    try:
        with open(path, 'r', encoding='utf-8') as ff:
            lines = ff.readlines()
            lines = list(set(x.strip() for x in lines))
    except:
        print('文件加载失败')
        return
    random.shuffle(lines)
    try:
        with open(path, "w", encoding='utf-8') as f:
            for i in lines:
                f.write(i + '\n')
        return
    except:
        return


def to_progress(id=0):
    """
    1. 根据id 进入不同的函数

    """
    if id == 1:  # ----爬取字母列表
        # url = url_head_1 + 'dict/zimu_' + chr(ord('a')) + '_1.html'  # 单个字母测试
        thread = list()
        for a in range(ord('a'), ord('z') + 1):
            url = url_head_1 + '/dict/zimu_' + \
                  chr(a) + '_1.html'  # 生成一个字母的首url链接
            path = './datas/txt/' + chr(a) + '.txt'
            t = threading.Thread(
                target=spider_1, args=(chr(a), url, path))  # 进入spider_1
            thread.append(t)
        for i in thread:
            i.start()
        i.join()
        exit()

    elif (id == 2):  # ----搜索词汇
        # spider3.spider3(word)
        pass

    elif (id == 3):  # ----重新搜索词汇详情
        with open("./datas/daily/errors.txt", 'r+') as f_error:
            f_error.truncate()  # 清空
            f_error.flush()
        for i in range(ord('a'), ord('z') + 1):
            path1 = './datas/txt/' + chr(i) + '.txt'
            path2 = './datas/nn/' + chr(i) + '.nn'
            s_time = time.time()
            spider_2(path1, path2, letter=chr(i), key_word=None, s_time=s_time, ip_list=ip_list)
        print("------------单词爬取保存成功-------------")
        exit()

    elif (id == 4):  # ----继续搜索词汇详情
        a = 'a'
        key_word = 'A bed of roses'
        try:
            with open("./datas/daily/record.txt", 'r') as ff:  # 读取记录
                lines = ff.readlines()
            a, key_word = lines[0].strip(), lines[1].strip()  # 获取上次字母和单词位置
        except:
            to_progress(id=3)

        for i in range(ord('c'), ord('z') + 1):
            path1 = './datas/txt/' + chr(i) + '.txt'
            path2 = './datas/nn/' + chr(i) + '.nn'
            s_time = time.time()
            spider_2(path1, path2, letter=chr(i),
                     key_word=key_word, s_timet=s_time)
        print("------------单词爬取保存成功-------------")
        exit()
    elif (id == 5):
        for i in range(ord('a'), ord('z') + 1):
            path2 = './datas/nn/' + chr(i) + '.nn'
            in_sql(path2, host, port, user, password, database)
            print("\r  {}   导入成功".format(chr(i)))
        print("------------ mysql 录入成功 -------------")
        exit()
    elif (id == 6):
        for i in range(ord('a'), ord('z') + 1):
            path1 = './datas/nn/' + chr(i) + '.nn'
            path2 = './watch/nn/new_' + chr(i) + '.nn'
            watch(path1, path2)
            print("\r  {}   提取成功".format(chr(i)))
        print("------------ 提取完成 -------------")
        exit()

    else:
        exit()


def main():
    """
    主函数
    """
    print("爬取IP中。。。。。")
    get_IP()
    print("获取完成。。。")
    get_ok_ip()
    to_progress(3)  # 进入分进程函数


#     while True:
#         id = eval(input("""
# 1. 收集词汇表        2. 搜索词汇
# 3. 重新收集词汇详情  4.继续搜索词汇详情
# 5. 导入数据库        6.提取手表格式
# 0. 退出
# 请输入序号---------》"""))
#         if id > 0:
#             to_progress(id)  # 进入分进程函数
#         else:
#             return


if __name__ == '__main__':
    main()
