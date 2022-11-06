package top.as.youdian.service.impl.word;

import top.as.youdian.dao.word.*;
import top.as.youdian.entity.word.Word;
import top.as.youdian.service.word.WordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * (Word)表服务实现类
 *
 * @author makejava
 * @since 2021-10-20 21:31:59
 */
@Service("wordService")
public class WordServiceImpl implements WordService {
    @Resource
    private WordDao wordDao;
    @Resource
    private WordAudioDao wordAudioDao;
    @Resource
    private WordClearDao wordClearDao;
    @Resource
    private WordDefDao wordDefDao;
    @Resource
    private WordPhraseDao wordPhraseDao;
    @Resource
    private WordSentencesDao wordSentencesDao;
    @Resource
    private WordSimilarDao wordSimilarDao;

    /**
     * 通过实体作为筛选条件查询
     *
     * @param word 实例对象
     * @return 对象列表
     */
    @Override
    public List<Word> queryAll(Word word, int num) {
        List<Word> words = this.wordDao.queryAll(word, num);
        words = this.getElements(words);
        return words;
    }

    @Override
    public Integer queryNumByTag(String tag) {
        return this.wordDao.queryNumByTag(tag);
    }

    /**
     * 通过ID查询单条数据
     *
     * @param wId 主键
     * @return 实例对象
     */
    @Override
    public Word queryById(String wId) {
        return this.wordDao.queryById(wId);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<Word> queryAllByLimit(int offset, int limit) {
        return this.wordDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param word 实例对象
     * @return 实例对象
     */
    @Override
    public Word insert(Word word) {
        word.setWId(UUID.randomUUID().toString());
        this.wordDao.insert(word);
        return word;
    }

    /**
     * 修改数据
     *
     * @param word 实例对象
     * @return 实例对象
     */
    @Override
    public Word update(Word word) {
        this.wordDao.update(word);
        return this.queryById(word.getWId());
    }

    /**
     * 通过主键删除数据
     *
     * @param wId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String wId) {
        return this.wordDao.deleteById(wId) > 0;
    }

    @Override
    public List<Word> getElements(List<Word> words) {
        for (Word word : words) {
            String wId = word.getWId();
            // audio
            word.setAudio(wordAudioDao.queryById(wId));
            // clearfix
            word.setClearfix(wordClearDao.queryById(wId));
            // def
            word.setDeformation(wordDefDao.queryById(wId));
            // phrase
            word.setPhrase(wordPhraseDao.queryById(wId));
            // sentences
            word.setSentences(wordSentencesDao.queryById(wId));
            // similar
            word.setSimilars(wordSimilarDao.queryById(wId));

        }
        return words;
    }

    @Override
    public Word getElements(String wId) {
        Word word = this.queryById(wId);
        // audio
        word.setAudio(wordAudioDao.queryById(wId));
        // clearfix
        word.setClearfix(wordClearDao.queryById(wId));
        // def
        word.setDeformation(wordDefDao.queryById(wId));
        // phrase
        word.setPhrase(wordPhraseDao.queryById(wId));
        // sentences
        word.setSentences(wordSentencesDao.queryById(wId));
        // similar
        word.setSimilars(wordSimilarDao.queryById(wId));
        return word;

    }
}