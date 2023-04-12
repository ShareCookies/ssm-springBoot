@Service(value = "UrlInterceptor")
public class UrlInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(UrlInterceptor.class);

    private static final String GET_ALL = "getAll";
    private static final String GET_HEADER = "getHeader";

    /**
     * 进入Controller层之前拦截请求，默认是拦截所有请求
     * @param httpServletRequest request
     * @param httpServletResponse response
     * @param o object
     * @return  preHandle()方法只有返回true，Controller中接口方法才能执行，否则不能执行，直接在preHandle()返回后false结束流程。
     * @throws Exception 可能出现的异常
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.info("go into preHandle method ... ");
        String requestURI = httpServletRequest.getRequestURI();
        if (requestURI.contains(GET_ALL)) {
            return true;
        }
        if (requestURI.contains(GET_HEADER)) {
            httpServletResponse.sendRedirect("/user/redirect");
        }
        return true;
    }

    /**
     * 处理完请求后但还未渲染试图之前进行的操作
     * @param httpServletRequest request
     * @param httpServletResponse response
     * @param o object
     * @param modelAndView mv
     * @throws Exception E
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        logger.info("go into postHandle ... ");
    }

    /**
     * 视图渲染后但还未返回到客户端时的操作
     * @param httpServletRequest request
     * @param httpServletResponse response
     * @param o object
     * @param e exception
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        logger.info("go into afterCompletion ... ");
    }
}