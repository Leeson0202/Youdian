
from email.headerregistry import ContentTypeHeader
from random import randint
import requests
import json
from datetime import date
import datetime
import time

tel = "11111111118"
pwd = "11111111118"
# urlhead = "http://localhost:8000"
urlhead = "http://youdian.asedrfa.top"

mydict = {"time_record": [{"day": "1643126400000", "sumTime": 4}], "word_record": [
    {"cDate": "1643185203862", "difficult": 0, "id": "0480b459-7def-11ec-ac70-00ff816348b7", "tag": 4, "timesReview": 0}]}


# 处理日期
def produce2():
    time_record = []
    for i in range(20):
        record = dict()
        now = datetime.datetime(2022, 1, 26-i)
        now_time = int(time.mktime(now.timetuple()))*1000  # datetime 转时间戳
        record['day'] = now_time
        record['sumTime'] = randint(20, 100)
        time_record.append(record)
    return time_record


# 处理单词
def produce(data):
    word_record = []
    for i in data:
        tempOne = dict()
        tempOne['id'] = i['id']
        tempOne['tag'] = randint(1, 4)
        tempOne['timesReview'] = randint(0, 3)
        tempOne['difficult'] = randint(1, 4)
        now = datetime.datetime(
            2022, 1, 25-randint(1, 15), randint(0, 23), randint(0, 59), randint(0, 59))
        now_time = int(time.mktime(now.timetuple()))*1000  # datetime 转时间戳
        tempOne['cDate'] = now_time
        word_record.append(tempOne)
    return word_record


# 登录
def load():
    response = requests.post(url=urlhead+"/user/load",
                             data={"tel": tel, "pwd": pwd, "devId": "12345"})
    if response.status_code == 200:
        return response.text
    else:
        return None


# 获取陌生单词
def getData(token):
    response = requests.post(
        url=urlhead+"/user/getlist", headers={"token": token})
    return response.text


# 提交背诵数据
def postData(token, word_record=list(), time_record=list()):
    response = requests.post(url=urlhead+"/user/postdata",
                             headers={"token": token
                                      },
                             json={"word_record": word_record,
                                            "time_record": time_record}
                                   )
    print(response.text)


# 1. 登录
response = json.loads(load())
if(response['state'] != 200):
    print("!200")
print("登录成功!")
token = response['data']['token']
# 2. 获取陌生单词
response = json.loads(getData(token))
if(response['state'] != 200):
    print("获取陌生单词失败")
data = response['data']
# 处理数据
word_record = produce(data)  # 处理单词
time_record = produce2()  # 处理日期
# time_record = []  # 处理日期


# 3. 提交背诵数据
postData(token=token, time_record=time_record, word_record=word_record)
