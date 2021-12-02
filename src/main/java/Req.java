public class Req {
    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public String getHttpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String[] arr = content.split("/", 3);
        String[] arrName = arr[2].split(" ", 2);
        String httpType = arr[0].trim();
        String poohMode = arr[1];
        String name;
        String param;
        String[] paramArr = arrName[1].split("\\s");
        if (httpType.equals("GET") && poohMode.equals("topic")) {
            String nameAndParam = arrName[0];
            String[] nameAndParamArr = nameAndParam.split("/");
            name = nameAndParamArr[0];
            param = nameAndParamArr[1];
        } else {
            name = arrName[0];
            param = paramArr[paramArr.length - 1];
            if (param.equals("*/*")) {
                param = "";
            }
        }
        return new Req(httpType, poohMode, name, param);
    }

}
