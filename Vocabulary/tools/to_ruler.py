import json

import MyClass
import Save


def readFile(path):
    temp = []
    try:
        with open(path, 'r+', encoding='utf-8') as f:
            temp = json.load(f)
    except:
        pass
    return temp


def do(datas, f_json):
    for data in datas:

        # 转换 audio
        audio = list()
        _ = ""
        for _ in data['audio']:
            if len(_) == 3:
                audio.append({"tag": _[0], "tagDetail": _[1], "url": _[2]})
            elif len(_) == 2:
                audio.append({"tag": _[0], "tagDetail": _[1], "url": ""})
            else:
                audio.append({"tag": "", "tagDetail": "", "url": ""})
        data['audio'] = audio

        del audio, _

        # 转换  sentences
        # sentences = list()
        # key = ""
        # value = ""
        # for key, value in data['sentences'].items():
        #     sentences.append({"key": key, "value": value})
        #
        # data['sentences'] = sentences
        # del sentences, key, value
        # if len(data['word_deformation']) > 0 or len(data['phrase']) > 0:
        #     print(1)

        # 转换  word_deformation ok不需要转换
        Save.saveElement(data, f_json, 0)

        pass


def doMain():
    for i in range(ord('p'), ord('p') + 1):
        path = 'datas/new/' + chr(i) + '.json'
        json_path = 'datas/ruler/' + chr(i) + '.json'
        # if not os.path.exists(json_path):  # --------判断文件是否存在
        open(json_path, 'w')  # 自动创建一个
        f_json = open(json_path, 'r+')
        f_json.truncate()  # 清空
        f_json.writelines('[]')  # 初始化 并保持
        f_json.flush()

        datas = readFile(path)
        if datas:  # 读取成功 -进入解析
            do(datas, f_json)

        f_json.close()
