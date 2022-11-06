import __init__
from bs4 import BeautifulSoup
import get_HTTP_response

get_alph_url = "https://www.koolearn.com/dict/zimu_"
url_head_1 = "https://www.koolearn.com"


def spider_1(alph):
    # ----------------------------- 打开所有文件
    f_txt = open("./datas/txt/"+alph+'.txt',"r+")
    f_json = open('./datas/json/' + alph + '.json', "r+", encoding='utf-8')
    f_error = open("./datas/daily/errors.txt", 'r+')
    f_record = open('./datas/daily/record.txt', 'w')
    url1 = "https://www.koolearn.com/dict/zimu_"+alph+"_1.html"

    while alph is not None:  # 爬取每一页单词   判断列表是否还有 url
        try:
            word_list = list()  # 单词列表清空
            response = get_HTTP_response.get(url=url)  # 得到响应
            url = None  # 删除url

            # -----------------------------------------------------------解析部分
            soup = BeautifulSoup(response.text, 'html.parser')  # 一锅汤
            word_list.extend(__init__.MyBeautifulSoup(soup, rex=1))  # 获取单词列表

            if not __init__.end_save(f_txt, word_list, '\n'):
                print('{}\t保存失败'.format(alph))

            next = soup.find_all(
                name='a', attrs={"class": "next"})  # 查找 下一页

            if next and next[0].get('href'):  # 判断是否有下一页 有就添加到 url列表
                url = url_head_1 + next[0].get('href')  # 提取url
            else:  # 没有下一页 返回
                __init__.word_sort(path=path)  # 排序
                print('{}\t保存成功'.format(alph))
                return

        except Exception as error:  # 错误提示
            print(error)
