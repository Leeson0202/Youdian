package top.as.youdian.service.impl.word;

import top.as.youdian.entity.word.WordClear;
import top.as.youdian.dao.word.WordClearDao;
import top.as.youdian.entity.word.WordWIdClear;
import top.as.youdian.service.word.WordClearService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * (WordClear)表服务实现类
 *
 * @author makejava
 * @since 2021-10-24 13:40:06
 */
@Service("wordClearService")
public class WordClearServiceImpl implements WordClearService {
    @Resource
    private WordClearDao wordClearDao;

    /**
     * 通过ID查询单条数据
     *
     * @param wId 主键
     * @return 实例对象
     */
    @Override
    public List<WordClear> queryById(String wId) {
        return this.wordClearDao.queryById(wId);
    }

    @Override
    public List<WordWIdClear> queryAll(WordWIdClear wordClear) {
        List<WordWIdClear> wordClears = this.wordClearDao.queryAll(wordClear);
        List<WordWIdClear> wordWIdClears = new ArrayList<>();
        for (int i = 0; i < wordClears.size(); i++) {  // 循环每一个
            String[] clearsplit = wordClears.get(i).getClearfix().split("\\.");  //将汉字提取出来
            if (clearsplit.length == 1) { // 比较汉字的开头
                if (clearsplit[0].startsWith(wordClear.getClearfix())) {
                    wordWIdClears.add(wordClears.get(i));
                }

            } else {
                if (clearsplit[1].startsWith(wordClear.getClearfix())) {
                    wordWIdClears.add(wordClears.get(i));
                }
            }
        }

        Collections.sort(wordWIdClears, new Comparator<WordWIdClear>() {
            @Override
            public int compare(WordWIdClear o1, WordWIdClear o2) {
                if (o1.getClearfix().length() < o2.getClearfix().length())
                    return -1;
                else
                    return 1;
            }
        });


        return wordWIdClears;
    }


}