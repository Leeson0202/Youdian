import json
import threading
import time

import MyClass

threadlock = threading.Lock()


def saveToFile(new_detail, f_json):
    try:
        # ----------------------------------写入json 文件
        f_json.seek(f_json.seek(0, 2) - 1, 0)
        f_json.write('\n,' + new_detail + "]")
        # f_json.seek(0, 0)  # 文件指针移动到头部
        # if f_json.read() == '[]':  # 判断 是否为空
        #     f_json.seek(f_json.seek(0, 2) - 1, 0)
        #     f_json.write(new_detail + ']')
        # else:  # 不为空
        #     f_json.seek(f_json.seek(0, 2) - 1, 0)
        #     f_json.write(',\n' + new_detail + ']')
    except:
        pass
    finally:
        f_json.flush()


def saveElement(data, fjson, percent):
    stime = MyClass.s_time
    threadlock.acquire()  # 加个同步锁就好了
    saveToFile(json.dumps(data), fjson)  # 将单词对象 转化为json格式
    threadlock.release()
    print('\r{:>3.1f}%   {:>.1f}s   {}  \t\t\t\t\t\t\t'.format(100 - percent, time.time() - stime if stime else 0,
                                                               data['spell']), end='')  # 显示进度
    return
