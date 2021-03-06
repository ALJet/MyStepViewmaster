package indi.aljet.mystepview_master.stepview.bean;

public class StepBean {

    public static final int STEP_UNDO = -1; //未完成  undo\

    public static final int STEP_CURRENT = 0; //正在进行

    public static final int STEP_COMPLETED = 1;//已完成


    private String name;

    private int state;


    public StepBean() {
    }

    public StepBean(String name, int state) {
        this.name = name;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
