package Exception;

import User.User;

public class CastToUserHandleException extends ClassCastException{
    public CastToUserHandleException(String message){
        super(message);
    }
    public CastToUserHandleException(Object source) throws CastToUserHandleException{
        if(!(source instanceof User)){
            throw new CastToUserHandleException("Cannot Cast the object isn't type of User");
        }
    }
}