package cn.micro.biz.commons.trace;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Trace Properties
 *
 * @author lry
 */
@Data
@ToString
@ConfigurationProperties(prefix = "micro.trace")
public class TraceProperties implements Serializable {

    /**
     * 调用链开关
     */
    private boolean enable;
    /**
     * 多少毫秒会打印耗时日志的时间阀值(单位：毫秒)
     */
    private long thresholdMillis = 300L;
    /**
     * 是否打印输入与输出参数
     */
    private boolean printArgs;
    /**
     * 打印日志的时间间隔，默认为60秒(单位：秒)
     */
    private long dumpPeriodSec = 60L;
    /**
     * 是否启用默认规则
     */
    private boolean defaultExpressions = true;
    /**
     * AOP拦截前缀
     */
    private String[] packagePrefixes;
    /**
     * 自定义AOP拦截规则
     */
    private List<String> expressions = new ArrayList<>();

    /**
     * 请求链路缓存开关
     */
    private boolean cacheEnable = true;
    /**
     * 最多缓存请求数量
     */
    private long cacheMaximumSize = 500;
    /**
     * 缓存写入后过期时间
     */
    private long cacheExpireAfterWriteSec = 600;

    /**
     * The Trace Event Type
     *
     * @author lry
     */
    public enum TraceEventType {

        /**
         * The Spring
         */
        SPG,

        /**
         * SQL
         */
        SQL;

    }

}
