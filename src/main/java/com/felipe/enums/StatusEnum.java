package com.felipe.enums;

public enum StatusEnum {
    TO_DO("to_do"),
    DOING("doing"),
    DONE("done");

    private final String value;

    StatusEnum(String value){this.value = value;}

    public String getValue(){
        return this.value;
    }

    public static StatusEnum fromString(String value){
        for(StatusEnum status : StatusEnum.values()){
            if(status.value.equalsIgnoreCase(value)){
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status: " + value);
    }

    @Override
    public String toString(){return value;}

}
