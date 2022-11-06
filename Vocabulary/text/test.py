import requests

urllist = [

    "https://dict.eudic.net/dicts/en/like"
    ]

response = requests.get(urllist[0])
response.encoding = response.apparent_encoding
with open('text.html', 'w', encoding='utf-8') as f:
    f.writelines(response.text)

# url =   'https://new.qq.com/rain/a/20210503A05MNT00'
# url = 'https://new.qq.com/omn/20210503/20210503A05MNT00.html'
# url = 'https://new.qq.com/omn/20210503/20210503A05MMZ00.html'
# response = requests.get(url)
# with open('text.html', 'w', encoding='utf-8') as f:
#     f.writelines(response.text)

# strt = '【17173鲜游快报，专注于快速带来全球新游信息】RockstarGames、2KGames等著名厂'
# re = '】'
# m = strt if re not in strt else strt[strt.index(re)+1:]
# print(m)
