package ServiceLayer.Inventory;

public class Response {
    private Object returnValue;
    private String errorMessage;

    public Response() {}

    public Response(Object returnValue, String errorMessage) {
        this.returnValue = returnValue;
        this.errorMessage = errorMessage;
    }

    public static Response success(Object returnValue) {
        return new Response(returnValue,null );
    }

    public static Response failure(String errorMessage) {
        return new Response(null,errorMessage);
    }

    public Object getMessage() {
        return returnValue;
    }

    public void setMessage(String returnValue) {
        this.returnValue = returnValue;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public boolean isError(){
        return errorMessage!=null;
    }

    @Override
    public String toString() {
        if (errorMessage != null) {
            return "Error: " + errorMessage;
        }
        return "Return Value: " + returnValue;
    }
}

