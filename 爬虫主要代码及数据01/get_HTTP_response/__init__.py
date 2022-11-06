import requests
from fake_useragent import UserAgent
from random import choices



def get_response(url=None, headers=None, proxies=None):
    try:

        # url = ''
        r = requests.get(url, headers=headers,
                         timeout=30)  # 伪装浏览器进行爬取
        r.raise_for_status()  # 自动检测爬虫状态=200
    except Exception as e:
        print(e)
        return None
    r.encoding = 'utf-8'  # 转换格式
    return r  # 返回response


def get(url=None, ip=None):
    """
    获取response并返回
    """
    if not url:
        return None
    headers = {
        'User-Agent': UserAgent(verify_ssl=False).random,
    }
    ip = '61.164.109.14:2'
    proxies = {'http': 'http://{}'.format(ip),
               'https': 'hppts://{}'.format(ip)}
    return get_response(url, headers, proxies)
