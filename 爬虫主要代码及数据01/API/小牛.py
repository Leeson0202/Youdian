import json
import urllib
import urllib3
import sys
import requests
import datetime
import time

def translate(sentence, src_lan, tgt_lan, apikey):
    t = time.time()
    # t = datetime.datetime.timetuple()
    timeStamp = int(round(t * 1000))
    url = 'https://dict.iciba.com/dictionary/word/query/web?'
   # 'client=6&key=1000006&timestamp=1615290576244&word=people&signature=040b347516200bd74fdc52cebdb0666f'
    data = {"client":6, "key":1000006,"timestamp":timeStamp,"word":"like"}
    # data_en = urllib.urlencode(data)

    # data_en = url.encode(data)
    # req = url + "&" + data_en
    # res = urllib.request.urlopen(req)
    res = requests.get(url,params=data)
    # res = res.read()
    res = res.content.decode()
    print(res)
    # res_dict = json.loads(res)
    #
    # result = ""
    # if (res_dict.has_key("tgt_text")):
    #     result = res_dict['tgt_text']
    # else:
    #     result = res
    # return result


if __name__ == "__main__":
    word = 'like'
    trans = translate(word, 'zh', 'en', '226023128e584da04dcab103293e45fb')
    # print(trans.decode())

    # insrc = open("zh.txt","r")
    # outsrc = open("zh.txt.big.test","w")
    # lines = insrc.readlines()
    # for line in lines:
    #     line = line.strip('\n')
    #     line = line.strip('\r')
    #     trans = translate(line,'zh','en','226023128e584da04dcab103293e45fb')
    #     try:
    #         trans = trans.encode('utf-8')
    #     except:
    #         trans = trans
    #     outsrc.write(trans)
