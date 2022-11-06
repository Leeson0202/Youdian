# -*- coding: utf-8 -*-
import sys
import uuid
import requests
import hashlib
import time
from importlib import reload

import time

reload(sys)

YOUDAO_URL = 'https://openapi.youdao.com/v2/dict'
APP_KEY = '0a68e8bc16336424'
APP_SECRET = 'VjpWiKNZKY4Z2A6afKYsbqQFY0a3wZEI'


def encrypt(signStr):
    hash_algorithm = hashlib.sha256()
    hash_algorithm.update(signStr.encode('utf-8'))
    return hash_algorithm.hexdigest()


def truncate(q):
    if q is None:
        return None
    size = len(q)
    return q if size <= 20 else q[0:10] + str(size) + q[size - 10:size]


def do_request(data):
    headers = {'Content-Type': 'application/x-www-form-urlencoded'}
    return requests.post(YOUDAO_URL, data=data, headers=headers)


def connect():
    q = "match"

    data = {}
    data['q'] = "%s" % (q)
    data['langTpye'] = 'en'
    data['appKey'] = APP_KEY
    data["dicts"] = 'ce'
    salt = str(uuid.uuid1())
    data["salt"] = salt
    data['signType'] = 'v3'
    curtime = str(int(time.time()))
    data['curtime'] = curtime
    signStr = APP_KEY + truncate(q) + salt + curtime + APP_SECRET
    sign = encrypt(signStr)
    data['sign'] = sign

    response = do_request(data)
    contentType = response.headers['Content-Type']
    if contentType == "audio/mp3":
        millis = int(round(time.time() * 1000))
        filePath = "合成的音频存储路径" + str(millis) + ".mp3"
        fo = open(filePath, 'wb')
        fo.write(response.content)
        fo.close()
    else:
        print('1 ', end='')
        print(response.content.decode())
        with open("../datas/text.json", 'w', encoding='utf-8') as f:
            f.write(response.text)
    return


if __name__ == '__main__':
    connect()
