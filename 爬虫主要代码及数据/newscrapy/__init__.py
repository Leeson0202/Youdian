from requests import *
import time
from newscrapy.spider1 import spider1

header = {
    "user-agent": 'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Mobile Safari/537.36'}  # 创建一个字段 浏览器5.0
url_head_1 = "https://www.koolearn.com"
url_head_2 = "http://www.iciba.com/word?w="
host = 'localhost'
port = 3306
user = 'root'
password = '123456'
database = 'youdian'

# 定义单词的类
class Vocabulary:
    # spell tag clearfix word_deformation sentences audio分别为: 拼写 标签 词性翻译 变形 例句列表 单词发音

    def __init__(self, spell=None, tag=None, clearfix=None, word_deformation=None, sentences=None, audio=None):
        self.spell = spell  # 单词的拼写
        self.tag = tag  # 四六级、高中
        self.clearfix = clearfix  # 词性和翻译
        self.word_deformation = word_deformation  # 变形
        self.sentence = sentences  # 单词的例句
        self.audio = audio  # 单词的发音


def get_HTTP_response(url=None, params=None):
    """
    获取response并返回
    """
    if url:
        try:
            r = get(url, headers=header, params=params,
                    timeout=30)  # 伪装浏览器进行爬取
            r.raise_for_status()  # 自动检测爬虫状态=200
        except:
            return None
        r.encoding = 'utf-8'  # 转换格式
        return r  # 返回response
    else:
        return None


def to_progress(id=0):
    """
    1. 根据id 进入不同的函数
    """
    if id == 1:  # ----重新收集词汇详情
        # url = url_head_1 + 'dict/zimu_' + chr(ord('a')) + '_1.html'  # 单个字母测试
        for a in range(ord('a'), ord('z') + 1):
            url = url_head_1 + '/dict/zimu_' + \
                chr(a) + '_1.html'  # 生成一个字母的首url链接
            path = './datas/txt/' + chr(a) + '.txt'
            spider1(url)
        exit()


    elif (id == 3):  # ----重新搜索词汇详情
        for i in range(ord('a'), ord('a') + 1):
            path1 = './datas/txt/' + chr(i) + '.txt'
            path2 = './datas/nn/' + chr(i) + '.nn'
            start = time.time()
            spider_2(path1, path2, letter=chr(i), key_word='', start=start)
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
            exit()
        for i in range(ord(a), ord('z') + 1):
            path1 = './datas/txt/' + chr(i) + '.txt'
            path2 = './datas/nn/' + chr(i) + '.nn'
            start = time.time()
            spider_2(path1, path2, letter=chr(i),
                     key_word=key_word, start=start)
        exit()

    elif(id == 5):
        for i in range(ord('a'), ord('z') + 1):
            path2 = './datas/nn/' + chr(i) + '.nn'
            in_sql(path2, host, port, user, password, database)
            print("\r  {}   导入成功".format(chr(i)))
        print("------------ mysql 录入成功 -------------")
        exit()
    elif(id == 6):
        for i in range(ord('a'), ord('z') + 1):
            path1 = './datas/nn/' + chr(i) + '.nn'
            path2 = './watch/nn/new_' + chr(i) + '.nn'
            watch(path1,path2)
            print("\r  {}   提取成功".format(chr(i)))
        print("------------ 提取完成 -------------")
        exit()

    else:
        exit()



def main():
    """
        主函数
        """
    while True:
        id = eval(input("""
    1. 重新收集词汇详情        2. 继续搜索词汇详情
    3. 导入数据库              4.提取手表格式
    0. 退出
    请输入序号---------》"""))

        if id > 0:
            to_progress(id)  # 进入分进程函数
        else:
            print("输入错误 ， 正在退出---")
            return









if __name__ == '__main__':
    main()
