package com.chj.easy.log.server.common.model;

import lombok.Getter;
import lombok.Setter;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/12 22:24
 */
@Setter
@Getter
public abstract class AbstractDoc implements Doc {

    private Map<String, String> highlight = new HashMap<>();

    @Override
    public void setHighlight(Map<String, HighlightField> highlightFields) {
        highlightFields.forEach((key, value) -> {
            highlight.put(key, Arrays.stream(value.getFragments()).findFirst().map(Text::string).orElse(""));
        });
    }
}