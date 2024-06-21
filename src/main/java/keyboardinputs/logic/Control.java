package keyboardinputs.logic;

import com.google.gson.annotations.SerializedName;

public class Control {
    @SerializedName("name")
    private String name;
    private int keyCode;

    public Control(String name, int keyCode) {
        this.name = name;
        this.keyCode = keyCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }
}
