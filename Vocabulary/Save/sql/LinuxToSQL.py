import pymysql,json,time
import random
import string


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


class MySQLConnection(object):
    def __init__(self, host, port, user, password, database=None):
        """
        host ip
        port 端口
        user root
        password 密码
        daatbases 数据库
        """
        self.host = host
        self.port = port
        self.user = user
        self.password = password
        self.database = database
        self.conn = None
        self.cursor = None
        self.connect_cursor()

    def connect_cursor(self):
        """
        链接数据库 返回connection  和 cursor对象
        """
        # 建立连接
        self.conn = pymysql.connect(host=self.host, port=self.port, user=self.user,
                                    password=self.password, database=self.database, charset='utf8')
        # 获取游标
        self.cursor = self.conn.cursor()

    def close(self):
        self.cursor.close()
        self.conn.close()

    def update_table(self, sql):
        """
        修改数据
        """
        try:
            pass
            self.cursor.execute(sql)
            self.conn.commit()
            print('---------------->修改数据成功')
        except Exception as error:
            print(error)
            self.conn.rollback()
        # finally:
        #     self.conn.close()

    def delete_content(self, sql):
        """
        删除一行
        """
        try:
            pass
            self.cursor.execute(sql)
            self.conn.commit()
            print('---------------->删除单行数据成功')
        except Exception as error:
            print(error)
            self.conn.rollback()

    def insert_table(self, sql):
        """
        插入数据
        """
        try:
            pass
            self.cursor.execute(sql)
            self.conn.commit()
            print('\r---------------->插入数据成功', end='')
        except Exception as error:
            print(error)
            self.conn.rollback()

    def search(self, sql):
        """
        查找一条数据
        """
        try:
            pass
            self.connect_cursor()
            self.cursor.execute(sql)
            return self.cursor.fetchone()
        except Exception as error:
            print(error)
            self.conn.rollback()


def youdian(datas, cursor):
    wordList = []
    audioList = []
    clearList = []
    defList = []
    phraseList = []
    sentencesList = []
    similarList = []
    for index, data in enumerate(datas):
        data = Vocabulary(data).__dict__
        # wid
        # print(data['spell'])
        w_id = ''.join(random.choice(string.ascii_uppercase + string.digits) for _ in range(39))[:39]
        # word 表
        wordList.append("""("{}","{}","{}","{}",0)""".format(w_id,
                                                             data['spell'], data['tag'] if data['tag'] else 0,
                                                             data['href']))

        # word_audio 表
        for _ in data['audio']:
            audioList.append("""("{}","{}","{}","{}")""".format(w_id, _['tag'].replace('"', r'\"'),
                                                                _['tagDetail'].replace('"', r'\"'),
                                                                _['url'].replace('"', r'\"') if _['url'] else 0))
        # word_clear 表
        for _ in data['clearfix']:
            clearList.append("""("{}","{}")""".format(w_id, _.replace('"', r'\"')))

        # word_def 表

        for _ in data['word_deformation']:
            tagList = ""
            if len(_['tagList']) > 1:
                tagList = "+".join(_['tagList'])
            if len(_['tagList']) == 1:
                tagList = _['tagList'][0]
            defList.append(
                """("{}","{}","{}")""".format(w_id, _['tag'].replace('"', r'\"'), tagList.replace('"', r'\"')))

        # word_phrase 表
        for _ in data['phrase']:
            phraseList.append("""("{}","{}","{}")""".format(w_id,
                                                            _['key'].replace('"', r'\"'),
                                                            _['value'].replace('"', r'\"') if len(
                                                                _['value']) else ""))
        # word_sentences 表
        for _ in data['sentences']:
            sentencesList.append("""("{}","{}","{}")""".format(w_id,
                                                               _['key'].replace('"', r'\"'),
                                                               _['value'].replace('"', r'\"') if len(
                                                                   _['value']) else ""))
        # word_similar 表
        for _ in data['similar']:
            tagList = ""
            if len(_['tagList']) > 1:
                tagList = "+".join(_['tagList'])
            if len(_['tagList']) == 0:
                tagList = _['tagList'][0]

            similarList.append(
                """("{}","{}","{}")""".format(w_id, _['tag'].replace('"', r'\"'), tagList.replace('"', r'\"')))
        del _

        if (index + 1) % 2000 == 0 or index == len(datas) - 1:
            # word
            if len(wordList) > 0:
                sql = """insert into word values {}""".format(",".join(wordList))
                cursor.insert_table(sql=sql)
                wordList = []

            # audio
            if len(audioList) > 0:
                sql = """insert into word_audio values {}""".format(",".join(audioList))
                cursor.insert_table(sql=sql)
                audioList = []

            # clearfix
            if len(clearList) > 0:
                sql = """insert into word_clear values {}""".format(",".join(clearList))
                cursor.insert_table(sql=sql)
                clearList = []

            # word_def
            if len(defList) > 0:
                sql = """insert into word_def values {}""".format(",".join(defList))
                cursor.insert_table(sql=sql)
                defList = []

            # word_phrase
            if len(phraseList) > 0:
                sql = """insert into word_phrase values {}""".format(",".join(phraseList))
                cursor.insert_table(sql=sql)
                phraseList = []

            # sentences
            if len(sentencesList) > 0:
                sql = """insert into word_sentences values {}""".format(",".join(sentencesList))
                cursor.insert_table(sql=sql)
                sentencesList = []

            # word_similar
            if len(similarList) > 0:
                sql = """insert into word_similar values {}""".format(",".join(similarList))
                cursor.insert_table(sql=sql)
                similarList = []
            print((index + 1) / 2000)

    print(f" {data['spell'][0]} ")

    pass

def readFile(path):
    temp = []
    try:
        with open(path, 'r+', encoding='utf-8') as f:
            temp = json.load(f)
    except:
        pass
    return temp





def to_sql():
    # 1. 连接数据库
    # 2. 读取文件
    # 3. 循环操作
    start = time.time()
    cursor = MySQLConnection('localhost', 3306, 'root', 'root', 'vocabulary')  # 已经连接上数据库
    if not cursor:
        return
    for i in range(ord('a'), ord('z') + 1):
        path = './datas/ruler/' + chr(i) + '.json'
        datas = readFile(path)
        if len(datas) < 1:  # 读取成功 -进入解析
            return
        youdian(datas, cursor)

to_sql()