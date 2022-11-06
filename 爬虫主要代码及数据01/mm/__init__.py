import json
import random
import threading
import time

import __init__

threadlock = threading.Lock()
threads = []

error_list = [str(x) for x in range(10)] + ['[', ']', '：', '？', '?', '"', '!', '@', '#', '$', '%', '^', '&', '*', '（',
                                            '）', '‘', '’', '(', ')']


def save(word_detail, percent, f_json, s_time):
    ok = 0
    try:
        # ----------------------------------写入json 文件
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
    if ok == 1:
        print("\r%s   "%(percent),end='')


def handle(word, percent, f_json, s_time):
    ok = 0
    for i in word['spell']:
        ok = 0
        if i in error_list:
            ok = 1
            break
    if ok == 1 or not word['spell']:
        print(word['spell'])
        return
    theword = __init__.Vocabulary(word)
    # ---------------------------------------------对象转json
    word_detail = json.dumps(theword.__dict__)  # 将单词对象 转化为json格式
    # -----------------------------------------------储存数据
    threadlock.acquire()  # 加个同步锁就好了
    save(word_detail, percent, f_json, s_time)
    threadlock.release()


def kk(path1, path2, s_time):
    global threads
    threads = []
    word_list = list()  # 初始化单词列表
    try:
        with open(path1, 'r', encoding="utf-8") as f:  # 读取 json文件
            data = json.load(f)
    except:
        print("文件不存在")
        return  # 没有文件则返回

    # ----------------------------- 打开所有文件
    f_json = open(path2, 'w+')
    f_error = open("./datas/daily/errors.txt", 'r+')

    # ------------------------------- 清空json文件
    f_json.truncate()  # 清空
    f_json.writelines('[]')  # 初始化 并保持
    f_json.flush()
    # -------------------------------排除错误字符
    random.shuffle(data)  # ------------------- 打乱list
    if len(data) > 0:
        for index, word in enumerate(data):
            percent = (index / len(data)) * 100  # 进度百分比
            handle(word, percent, f_json, s_time)
            # --------------------------------------多线程
        #     t = threading.Thread(target=handle, args=(
        #         word, percent, f_json,  s_time))
        #     threads.append(t)
        # num = 500  # 设置的线程数
        # t = None
        # for i, t in enumerate(threads):
        #     try:
        #         t.start()
        #     except Exception as e:
        #         pass
        #     while len(threading.enumerate()) > num:
        #         time.sleep(0.3)
        # for t in threads:
        #     t.join()
        # t.join()

    f_json.close()
    f_error.close()
    print("")
    
    


def mysort():
    f_json = open("./datas/newjson/all.json",'w+')
    f_json.truncate()  # 清空
    f_json.writelines('[]')  # 初始化 并保持
    f_json.flush()

    word_list=list()
    for i in range(ord('a'), ord('z') + 1):
        path2 = './datas/newjson/' + chr(i) + '.json'
        with open(path2,'r',encoding='utf-8') as f:
            data = json.load(f)
        word_list.extend(data)
    random.shuffle(word_list)
    for index,i in enumerate(word_list):
        percent = (index / len(word_list)) * 100  # 进度百分比
        word_detail = json.dumps(i)
        threadlock.acquire()  # 加个同步锁就好了
        save(word_detail,percent=percent,f_json=f_json,s_time=1)
        threadlock.release()
    f_json.close()
    print("-----------------------mysort完成")

