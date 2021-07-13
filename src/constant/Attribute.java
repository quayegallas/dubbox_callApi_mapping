package constant;

/**
 * @author Aaron
 * @since 2020/11/6 16:54
 * <p>描述：注解的属性</p>
 */
public interface Attribute {

    /**
     * '@RequestMapping' 注解的属性
     */
    interface RequestMapping {
        String NAME = "name";
        String VALUE = "value";
        String PATH = "path";
        String METHOD = "method";
    }

    /**
     * '@FeignClient' 注解的属性
     */
    interface FeignClient {
        String NAME = "name";
        String VALUE = "value";
        String PATH = "path";
        String FALLBACK = "fallback";
    }

}
