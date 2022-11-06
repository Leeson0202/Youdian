import time

s_time = time.time()


# spell tag  clearfix href  word_deformation   phrase      sentences          audio                similar
#  拼写  标签  词性翻译  链接      变形              短语         例句列表           单词发音                近义词
#  str   str   list   str    dict-list        dict->list   dict ->list         list                  list
#                         [{tag  tagList}]  [{key  value}] [{key  value}]  [{tag tagDetail url}] [{tag tagList}]

#  word_deformation  phrase  similar  sentences

# 定义单词的类
class Vocabulary:
    def __init__(self, word=None):

        if word['spell']:
            self.spell = word['spell']  # 单词的拼写
        if word['tag']:
            self.tag = word['tag']  # 四六级、高中
        else:
            self.tag = ''

        if len(word['clearfix']) > 0:
            self.clearfix = word['clearfix']  # 词性和翻译
        else:
            self.clearfix = []

        try:
            if len(word['word_deformation']) > 0:
                self.word_deformation = word['word_deformation']  # 变形
            else:
                self.word_deformation = []
        except:
            self.word_deformation = []

        try:
            if len(word['phrase']) > 0:
                self.phrase = word['phrase']  # 短语
            else:
                self.phrase = []
        except:
            self.phrase = []

        try:
            if len(word['similar']) > 0:
                self.similar = word['similar']  # 近义词
            else:
                self.similar = []
        except:
            self.similar = []

        try:
            if len(word['audio']) > 0:
                self.audio = word['audio']  # 单词的发音
            else:
                self.audio = []
        except:
            self.audio = []

        if len(word['sentences']) > 0:
            self.sentences = word['sentences']  # 单词的例句
        else:
            self.sentences = []

        try:
            if word['href']:
                self.href = word['href']  # 网址
        except:
            self.href = ''
