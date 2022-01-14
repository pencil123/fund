package cn.blogscn.fund.model.json;

/**
 * 接口返回结果封装
 *
 * @author 20113368
 * @date 2021/1/18 14:52
 */
public class JsonResult<T> {
    private static Boolean success = true;
    private String message = "";
    private T data;

    /**
     * 只为序列化，不能使用
     */
    private JsonResult() {
        super();
    }

    /**
     * 内部构建
     * @param success
     * @param message
     * @param data
     */
    private JsonResult(Boolean success ,String message, T data) {
        super();
        this.success = success;
        this.message = message;
        this.data = data;
    }

    private JsonResult(Boolean success,String message) {
        super();
        this.success = success;
        this.message = message;
    }


    /**
     * 自定义成功信息
     */
    public static <T> JsonResult<T> success(String message, T data) {
        return new JsonResult<>(true,message, data);
    }

    /**
     * @描述： 默认success成功信息
     */
    public static <T> JsonResult<T> success(T data) {
        return new JsonResult<>(true,"success", data);
    }

    /**
     * 默认的成功信息，data为空
     */
    public static <T> JsonResult<T> success() {
        return new JsonResult<>(true, "success", null);
    }
    private static final String EXCEPTION_MESSAGE_500 = "Server Internal Error";
    /**
     * 400 Default message
     */
    private static final String EXCEPTION_MESSAGE_400 = "Bad request";

    /**
     * 仅指定message时使用
     */
    public static  <T>  JsonResult <T>  error( String message) {
        return new JsonResult<> (false, message);
    }

    /**
     * 默认错误时使用
     */
    public static <T> JsonResult<T> error() {
        return error("");
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
