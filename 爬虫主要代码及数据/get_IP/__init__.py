import requests
from lxml import etree
import time
import threading

threadlock = threading.Lock()
class XiLaIp_Spider:

    def __init__(self):
        self.url = 'https://ip.ihuan.me/'
        self.headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36',
        }
        self.proxy = '125.46.0.62:53281'
        self.proxies = {
            "http": "http://%s" % (self.proxy),
            "https": "https://%s" % (self.proxy)
        }
        self.list1 = []

    def get_url(self):
        for index in range(10):
            # time.sleep(0.2)
            print(index)
            try:
                res = requests.get(url=self.url,
                                   headers=self.headers,
                                   timeout=8).text
            except Exception as e:
                res = None
                print(e)
            if res:
                data_str="""/html/body/meta"utf-8"/div[2]/div[2]/table/tbody/tr/td[1]/a"""
                dscore_data_str="""/html/body/meta"utf-8"/div[2]/div[2]/table/tbody/tr/td[2]"""
                data = etree.HTML(res).xpath(data_str)
                score_data = etree.HTML(res).xpath(dscore_data_str)
                # for i in data:
                #     temp = i.text
                #     print(temp)
                #     self.list1.append(temp)
                # score_data = etree.HTML(res).xpath("//*[@id='list']/table/tbody/tr/td[2]")
                for i, j in zip(data, score_data):
                    temp = i.text + ':' + j.text
                    print(temp)
                    self.list1.append(temp)
        return

    def text01(self, temp,ok_file):
        print(temp)
        proxies = {'http': 'http://' + temp,
                   'https': 'https://' + temp}
        try:
            res = requests.get(url='http://www.iciba.com/word?w=like', headers=self.headers, proxies=proxies,
                               timeout=30)
            if res.status_code == 200:
                print(temp + "       2")
                threadlock.acquire()
                ok_file.write(temp + '\n')
                ok_file.flush()
                threadlock.release()
        except Exception as e:
            # print(e)
            pass

    def test(self):
        # 判断是否有效
        thread=[]
        ok_file = open('datas/ip/OkIp_Proxy.txt', 'w')
        for kk in self.list1:
            # self.text01(kk,ok_file)
            th = threading.Thread(target=self.text01, args=(kk,ok_file))
            thread.append(th)
        for i in thread:
            i.start()
        for j in thread:
            j.join()
        ok_file.close()
        return

    def run(self):
        self.get_url()
        print("---------------------------")
        return self.test()


def get_IP():
    dl = XiLaIp_Spider()
    dl.run()
