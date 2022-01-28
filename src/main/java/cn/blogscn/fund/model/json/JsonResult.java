package cn.blogscn.fund.model.json;

/**
 * 接口返回结果封装
 *
 * @author 20113368
 * @date 2021/1/18 14:52
 */
public class JsonResult<T> {
    private Integer code = 0;
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
     * @param code
     * @param message
     * @param data
     */
    private JsonResult(Integer code ,String message, T data) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private JsonResult(Integer code,String message) {
        super();
        this.code = code;
        this.message = message;
    }


    /**
     * 自定义成功信息
     */
    public static <T> JsonResult<T> success(String message, T data) {
        return new JsonResult<>(0,message, data);
    }

    /**
     * @描述： 默认success成功信息
     */
    public static <T> JsonResult<T> success(T data) {
        return new JsonResult<>(0,"success", data);
    }

    /**
     * 默认的成功信息，data为空
     */
    public static <T> JsonResult<T> success() {
        return new JsonResult<>(0, "success", null);
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
        return new JsonResult<> (500, message);
    }

    /**
     * 默认错误时使用
     */
    public static <T> JsonResult<T> error() {
        return error("");
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
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
