package top.as.youdian.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import top.as.youdian.entity.word.Word;
import top.as.youdian.entity.word.WordWIdClear;
import top.as.youdian.service.word.WordClearService;
import top.as.youdian.service.word.WordService;
import org.springframework.web.bind.annotation.*;
import top.as.youdian.tools.AssertUtil;

import javax.annotation.Resource;
import java.util.*;

/**
 * (Word)表控制层
 *
 * @author makejava
 * @since 2021-10-20 21:31:59
 */
@RestController
@RequestMapping("/word")
public class WordController {

    /**
     * 服务对象
     */
    @Resource
    private WordService wordService;
    @Resource
    private WordClearService wordClearService;

    @ApiOperation(value = "通过单词本tag类型 查询单词本中单词的个数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tag", value = "tag", paramType = "query", required = true, dataTypeClass = String.class),
    })
    @GetMapping("queryNumByTag")
    public Map<String, Object> queryNumByTag(String tag) {
        Integer i = this.wordService.queryNumByTag(tag);
        HashMap<String, Object> returnMap = new HashMap<>();
        returnMap.put("tag", tag);
        returnMap.put("num", i);
        return AssertUtil.returnMap(200, "请求成功", returnMap);
    }


    /**
     * 通过实体作为筛选条件查询
     *
     * @param word 实例对象
     * @return 对象列表
     */
    @ApiOperation(value = "获取/查询单词")
    @GetMapping("queryAll")
    public List<Word> queryAll(Word word, String q, Integer num) {
        List<Word> words = new ArrayList<Word>();
        if (num == null) {
            num = -1;
        }
        if (q != null) {  // 如果输入框查询
            if (64 < (byte) q.charAt(0) && (byte) q.charAt(0) < 123) {  // 英文字母
                word.setSpell(q);
                words = this.wordService.queryAll(word, num);
            } else {  // 汉语 通过翻译查询
                WordWIdClear wordWIdClear = new WordWIdClear();
                wordWIdClear.setClearfix(q);
                // 获取 clearfix列表
                List<WordWIdClear> wordWIdClears = wordClearService.queryAll(wordWIdClear);
                // 获取 单词列表
                for (WordWIdClear wIdClear : wordWIdClears) {
                    Word word1 = new Word();
                    word1.setWId(wIdClear.getWId());
                    List<Word> words1 = wordService.queryAll(word1, -1);
                    Word word2 = words1.get(0);
                    words.add(word2);
                }
                // 英文单词排序

                Collections.sort(words, new Comparator<Word>() {
                    @Override
                    public int compare(Word o1, Word o2) {
                        if (o1.getSpell().length() < o2.getSpell().length())
                            return -1;
                        else
                            return 1;
                    }
                });
            }
        } else {
            words = this.wordService.queryAll(word, num);
        }
        return words;
    }
}