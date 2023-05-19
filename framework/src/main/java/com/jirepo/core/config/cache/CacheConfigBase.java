package com.jirepo.core.config.cache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.sf.ehcache.config.DiskStoreConfiguration;

@Configuration
@EnableCaching /* cache 활성화 */
public class CacheConfigBase {
    // @EnableCaching어노테이션을 추가한다고 해서 캐시가 자동으로 적용되지 않는다.
    // 단순히 spring에서 관리하는 cache management를 사용할 수 있게 활성화만 했을 뿐이다.


        
    /**
     * CacheManger를 생성한다. 
     * @return CacheManager
     */
    private net.sf.ehcache.CacheManager createCacheManager() {
        net.sf.ehcache.config.Configuration configuration = new net.sf.ehcache.config.Configuration();
        //configuration.diskStore(new DiskStoreConfiguration().path("java.io.tmpdir"));
        return net.sf.ehcache.CacheManager.create(configuration);
    }
    
    /** EhCacheCacheManager을 등록한다. */
    @Bean
    public EhCacheCacheManager ehCacheCacheManager() {
        net.sf.ehcache.CacheManager manager = this.createCacheManager();
        // import net.sf.ehcache.Cache;
        // import net.sf.ehcache.config.CacheConfiguration;
        // import net.sf.ehcache.config.CacheConfiguration.TransactionalMode;
        // import net.sf.ehcache.config.PersistenceConfiguration;

        // Cache getMenuCache = new Cache(new CacheConfiguration().maxEntriesLocalHeap(1000).maxEntriesLocalDisk(10000)
        //         .eternal(false).timeToIdleSeconds(1800).timeToLiveSeconds(1800).memoryStoreEvictionPolicy("LFU")
        //         .transactionalMode(TransactionalMode.OFF)
        //         .persistence(new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP))
        //         .name("getMenu"));
        // manager.addCache(getMenuCache);
        return new EhCacheCacheManager(manager);
    }

}///~
