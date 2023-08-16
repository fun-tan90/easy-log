package com.chj.easy.log.admin.rest;

import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/31 17:52
 */
@Slf4j
@RestController
@RequestMapping("cache")
public class CacheController {

    @Resource
    LoadingCache<String, String> loadingCache;

    @GetMapping("stats")
    public CacheStats cacheStats() {
        return loadingCache.stats();
    }

    @GetMapping
    public String getCache(String key) {
        return loadingCache.get(key);
    }

    @PutMapping
    public void putCache(String key, String value) {
        loadingCache.put(key, value);
    }

    @DeleteMapping
    public void delCache(String key) {
        loadingCache.invalidate(key);
    }
}