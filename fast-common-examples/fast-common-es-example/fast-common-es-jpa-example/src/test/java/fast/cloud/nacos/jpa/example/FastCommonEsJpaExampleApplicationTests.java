package fast.cloud.nacos.jpa.example;

import fast.cloud.nacos.jpa.example.entity.HelloES;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FastCommonEsJpaExampleApplicationTests {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;    //es工具

    @Test
    public void testAddDoc() {
        HelloES helloES = new HelloES();
        helloES.setName("曾经沧海难为水");
        helloES.setDescription("本课程主要从四个章节进行讲解: 1.微服务架构入门 2.spring cloud基础入门3.实战SpringBoot4.注册中心eureka");
        IndexQuery indexQuery = new IndexQuery();
        indexQuery.setObject(helloES);
        elasticsearchTemplate.index(indexQuery);

    }

    @Test
    public void testHeightLight() {
        String preTag = "<font color='#dd4b39'>";//google的色值
        String postTag = "</font>";

        SearchQuery searchQuery = new NativeSearchQueryBuilder().
                withQuery(matchQuery("name", "曾经")).
                withHighlightFields(new HighlightBuilder.Field("name").preTags(preTag).postTags(postTag),
                        new HighlightBuilder.Field("description").preTags(preTag).postTags(postTag)).build();
        List<HelloES> helloES = elasticsearchTemplate.queryForList(searchQuery, HelloES.class);
        System.out.println(helloES);

        // 高亮字段
        AggregatedPage<HelloES> ideas = elasticsearchTemplate.queryForPage(searchQuery, HelloES.class, new SearchResultMapper() {

            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                List<HelloES> chunk = new ArrayList<>();
                for (SearchHit searchHit : response.getHits()) {
                    if (response.getHits().getHits().length <= 0) {
                        return null;
                    }
                    HelloES idea = new HelloES();

                    String sourceAsString = searchHit.getSourceAsString();
                    System.out.println(sourceAsString);
                    //name or memoe
                    HighlightField ideaTitle = searchHit.getHighlightFields().get("name");
                    if (ideaTitle != null) {
                        idea.setName(ideaTitle.fragments()[0].toString());
                    }
                    HighlightField ideaContent = searchHit.getHighlightFields().get("description");
                    if (ideaContent != null) {
                        idea.setDescription(ideaContent.fragments()[0].toString());
                    }

                    chunk.add(idea);
                }
                if (chunk.size() > 0) {
                    return new AggregatedPageImpl<>((List<T>) chunk);
                }
                return null;
            }

//            @Override
//            public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
//                return null;
//            }
        });

        ideas.get().forEach(model -> {
            System.out.println(model.getName());
        });
    }

    @Test
    public void testSuggest() {
        String[] an = getAnalyzes("hello_es", "曾经沧海难");
        getSuggestion(HelloES.class, "曾经沧海难");
        Stream.of(an).forEach(model -> System.out.println(model));

    }

    /*
     * index 索引index
     * text 需要被分析的词语
     * 默认使用中文ik_smart分词
     * */
    public String[] getAnalyzes(String index, String text) {
        //调用ES客户端分词器进行分词
        AnalyzeRequestBuilder ikRequest = new AnalyzeRequestBuilder(elasticsearchTemplate.getClient(),
                AnalyzeAction.INSTANCE, index, text).setAnalyzer("ik_smart");
        List<AnalyzeResponse.AnalyzeToken> ikTokenList = ikRequest.execute().actionGet().getTokens();

        // 赋值
        List<String> searchTermList = new ArrayList<>();
        ikTokenList.forEach(ikToken -> {
            searchTermList.add(ikToken.getTerm());
        });

        return searchTermList.toArray(new String[searchTermList.size()]);
    }

    /*
     * Class clazz指定的索引index实体类类型
     * String text 搜索建议关键词
     * */
    public String[] getSuggestion(Class clazz, String text) {
        //构造搜索建议语句
        SuggestionBuilder completionSuggestionFuzzyBuilder = SuggestBuilders.completionSuggestion("suggest").prefix(text, Fuzziness.AUTO);

        //根据
        final SearchResponse suggestResponse = elasticsearchTemplate.suggest(new SuggestBuilder().addSuggestion("my-suggest", completionSuggestionFuzzyBuilder), clazz);
        CompletionSuggestion completionSuggestion = suggestResponse.getSuggest().getSuggestion("my-suggest");
        List<CompletionSuggestion.Entry.Option> options = completionSuggestion.getEntries().get(0).getOptions();
        System.err.println(options);
        System.out.println(options.size());
        System.out.println(options.get(0).getText().string());

        List<String> suggestList = new ArrayList<>();
        options.forEach(item -> suggestList.add(item.getText().toString()));

        return suggestList.toArray(new String[suggestList.size()]);
    }
}
