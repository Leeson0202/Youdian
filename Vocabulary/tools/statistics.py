import json


def readFile(path):
    temp = []
    try:
        with open(path, 'r+', encoding='utf-8') as f:
            temp = json.load(f)
    except Exception as e:
        print(e)
        pass
    return temp


def do(datas):
    data = dict
    phrase = 0
    word_deformation = 0
    similar = 0
    min = 100
    max = 0
    for data in datas:
        try:
            if len(data['phrase']) > 0:
                phrase = phrase + 1
            if len(data['word_deformation']) > 0:
                word_deformation = word_deformation + 1
            if len(data['similar']) > 0:
                similar = similar + 1
            if len(data['spell']) < min:
                min = len(data['spell'])
            if len(data['spell']) > max:
                max = len(data['spell'])
        except:
            pass
    # print("{} :phrase:{}  word_deformation:{}  similar:{}".format(data['spell'][0], phrase, word_deformation, similar))
    print("{}  spell_max:{}  spell_min:{}".format(data['spell'][0],max, min))
    pass





def handle():
    for i in range(ord('a'), ord('z') + 1):
        path = 'E:/Code/Vocabulary.sql/datas/new/' + chr(i) + '.old'
        # path = 'old_data/ruler/' + chr(i) + '.old'

        datas = readFile(path)
        if datas:  # 读取成功 -进入解析
            do(datas)
