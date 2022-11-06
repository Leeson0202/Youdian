from scrapy.spider1 import *
from scrapy.spider2 import *
from scrapy.spider3 import *
from sql.in_sql import *
from watch.watch import *
import time
from get_IP import *
from mm import *

url_head_1 = "https://www.koolearn.com"
url_head_2 = "http://www.iciba.com/word?w="
url_head_3 = "https://www.koolearn.com/dict/search/index?keywords="
proxy_pool_url = 'http://localhost:5555/random'
ip_list = []
host = '172.20.10.8'
port = 3306
user = 'root'
password = 'Zhw626938.'
database = 'youdianword'
table = 'word_word'

# host = 'localhost'
# port = 3306
# user = 'root'
# password = '123456'
# database = 'youdian'
# table = 'word'



# 定义单词的类
class Vocabulary:
    # spell tag clearfix word_deformation sentences audio href 分别为: 拼写 标签 词性翻译 变形 例句列表 单词发音
    # str   str  list      dict           dict    dict   str
    def __init__(self, word=None):
        if word['spell']:
            self.spell = word['spell']  # 单词的拼写
        if word['tag']:
            self.tag = word['tag']  # 四六级、高中
        else:
            self.tag = ''
        if word['clearfix']:
            self.clearfix = word['clearfix']  # 词性和翻译
        else:
            self.clearfix = list()
        try:
            if word['word_deformation']:
                self.word_deformation = word['word_deformation']  # 变形
        except:
            self.word_deformation = dict()
        if word['sentence']:
            self.sentences = word['sentence']  # 单词的例句
        else:
            self.sentences = dict()
        try:
            if word['audio']:
                self.audio = word['audio']  # 单词的发音
        except:
            self.audio = dict()
        try:
            if word['href']:
                self.href = word['href']  # 网址
        except:
            self.href = ''


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
    spell = ''
    word_deformation = audio = dict()
    return spell, word_deformation, audio


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

    else:
        # spell tag clearfix word_deformation sentences audio分别为: 拼写 标签 词性翻译 变形 例句列表 单词发音
        spell, word_deformation, audio = init_vocabulay()  # 初始化单词格式
        spell_box = None
        try:
            # -------------------------------spell
            spell_box = soup.find_all(
                name='h1', attrs={"class": "word-spell"})  # 找到 单词简介box
            if not spell_box:
                spell = None
            else:
                spell = spell_box[0].text

            # --------------------------word_deformation
            word_deformation = soup.find_all(name='h2', attrs={"class": "details-content-title"})
            if not word_deformation[0].next_sibling.next_sibling.p:
                word_deformation = list()
            else:
                try:
                    word_deformation = word_deformation[0].next_sibling.next_sibling.p.find_all('span')
                    word_deformation = [x.text for x in word_deformation]
                except:
                    pass

            # ---------------------------------audio
            audio = soup.find_all(name='div', attrs={"class": "word-spell-box"})
            audio_dict = dict()
            if not audio:
                audio = dict()
            else:
                try:
                    audio_dict[audio[0].contents[1].text] = audio[0].contents[2].attrs['data-url']
                    audio_dict[audio[1].contents[1].text] = audio[1].contents[2].attrs['data-url']
                except:
                    pass
            return spell, word_deformation, audio_dict

        except Exception as a:
            print('\n 1', spell, a)
            return spell, word_deformation, audio_dict


def word_sort(path):
    """
    词汇爬取后 进行去重 排序
    """
    lines = list()  # 初始化
    try:
        with open(path, 'r', encoding='utf-8') as ff:
            lines = ff.readlines()
            lines = list(set(x.strip() for x in lines))
            pass
    except:
        print('文件加载失败')
        return
    lines.sort()
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
            t = threading.Thread(
                target=spider_1, args=(chr(a)))  # 进入spider_1
            thread.append(t)
        for i in thread:
            i.start()
        for j in thread:
            j.join()
        exit()

    elif (id == 2):  # ----搜索词汇
        # spider3.spider3(word)
        pass

    elif (id == 3):  # ----重新搜索词汇详情
        with open("./datas/daily/errors.txt", 'r+') as f_error:
            f_error.truncate()  # 清空
            f_error.flush()
        for i in range(ord('a'), ord('z') + 1):
            path1 = './datas/txt/' + chr(i) + '.json'
            path2 = './datas/json/' + chr(i) + '.json'
            s_time = time.time()
            frist = True
            spider_2(path1, path2, letter=chr(i), key_word=None, s_time=s_time, ip_list=ip_list, frist=frist)
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

        for i in range(ord(a), ord('z') + 1):
            path1 = './datas/txt/' + chr(i) + '.txt'
            path2 = './datas/json/' + chr(i) + '.json'
            s_time = time.time()
            spider_2(path1, path2, letter=chr(i),
                     key_word=key_word, s_time=s_time, ip_list=ip_list, frist=False)
        print("------------单词爬取保存成功-------------")
        exit()
    elif (id == 5):
        # for i in range(ord('a'), ord('z') + 1):
        #     path2 = './datas/newjson/' + chr(i) + '.json'
        #     in_sql(path2, host, port, user, password, database)
        #     print("\r  {}   导入成功".format(chr(i)))
        path2 = './datas/newjson/all.json'
        in_sql(path2, host, port, user, password, database,table)

        print("------------ mysql 录入成功 -------------")
        exit()
    elif (id == 6):
        for i in range(ord('a'), ord('z') + 1):
            path1 = './datas/json/' + chr(i) + '.json'
            path2 = './watch/json/new_' + chr(i) + '.json'
            watch(path1, path2)
            print("\r  {}   提取成功".format(chr(i)))
        print("------------ 提取完成 -------------")
        exit()
    elif (id == 7):
        # for i in range(ord('a'), ord('z') + 1):
        #     path1 = './datas/json/' + chr(i) + '.json'
        #     path2 = './datas/newjson/' + chr(i) + '.json'
        #     s_time = time.time()
        #     kk(path1,path2,s_time)
        mysort()


    else:
        exit()


def main():
    """
    主函数
    """
    print("爬取IP中。。。。。")
    # get_IP()
    print("获取完成。。。")
    # get_ok_ip()
    to_progress(5)  # 进入分进程函数


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
