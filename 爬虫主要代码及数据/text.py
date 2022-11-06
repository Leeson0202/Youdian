import threading
import time

lock = threading.Lock()
def pr():
    for i in range(10):
        print(i)
        time.sleep(1)

def pp(mm):
    for i in range(2):
        time.sleep(0.5)
        print('第',mm,'函数')
    lock.acquire()
    print(mm,'已经加锁')
    time.sleep(5)
    print(mm,'已经解锁')
    lock.release()
    # print()


# t1=threading.Thread(target=pr,args=())
# t2=threading.Thread(target=pp,args=(1))
# t3=threading.Thread(target=pp,args=(1))
# t1.start()
# t2.start()
# t1.join()
# t2.join()


t2=threading.Thread(target=pp,args=(1,))
t3=threading.Thread(target=pp,args=(2,))
t3.start()
t2.start()
t3.join()
t2.join()