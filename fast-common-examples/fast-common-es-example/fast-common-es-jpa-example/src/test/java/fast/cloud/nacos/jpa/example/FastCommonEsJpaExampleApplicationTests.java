package fast.cloud.nacos.jpa.example;

import fast.cloud.nacos.jpa.example.entity.HelloES;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
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
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
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
                withQuery(matchQuery("name","曾经")).
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
        });

        ideas.get().forEach(model -> {
            System.out.println(model.getName());
        });
    }


}
