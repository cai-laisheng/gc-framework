package com.allen.es.service.impl;

import com.allen.es.po.DocBean;
import com.allen.es.repository.ElasticRepository;
import com.allen.es.service.IElasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.erhlc.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * Created by xuguocai on 2021/4/7 10:44
 */
@Service
public class ElasticServiceImpl implements IElasticService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;
    @Autowired
    private ElasticRepository elasticRepository;

    private Pageable pageable = PageRequest.of(0,10);

    @Override
    public void createIndex() {
        DocBean docBean = new DocBean(1L, "XX0193hhh", "XX8064hhh", "xxxxxxgghhhh", 1);

        IndexQuery query = new IndexQuery();
        query.setId("1");
        query.setObject(docBean);
        query.setVersion(1L);
        elasticsearchTemplate.doIndex(query, IndexCoordinates.of(docBean.getClass().getSimpleName().toLowerCase()));
    }

    @Override
    public void deleteIndex(String index) {
        elasticsearchTemplate.delete(index);
    }

    @Override
    public void save(DocBean docBean) {
        elasticRepository.save(docBean);
    }

    @Override
    public void saveAll(List<DocBean> list) {
        elasticRepository.saveAll(list);
    }

    @Override
    public Iterator<DocBean> findAll() {
        return elasticRepository.findAll().iterator();
    }


}
