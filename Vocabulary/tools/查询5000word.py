import pymysql
import random
import string



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
    
    
    def searchAll(self, sql):
        """
        查找多条数据
        """
        try:
            pass
            self.connect_cursor()
            self.cursor.execute(sql)
            return self.cursor.fetchall()
        except Exception as error:
            print(error)
            self.conn.rollback()


def main(cursor):
    """
    docstring
    """
    sql = """select * from word limit 50000;
    """
    mytuple = cursor.searchAll(sql)
    mylist = [list(i) for i in mytuple]
    mywords = []
    for i in mylist:
        mywords.append(i[1])
    pass


if __name__ == '__main__':
    cursor = MySQLConnection(host='localhost', port=3306,
                             user='root', password='root', database='vocabulary')
    main(cursor)

