package com.easyctrl.manager;

import com.easyctrl.ldy.domain.ModulePortBean;
import java.util.ArrayList;

public class OperatorBeanManager {
    private static OperatorBeanManager instance;
    private ArrayList<ModulePortBean> operatorArray;

    private OperatorBeanManager() {
    }

    public static synchronized OperatorBeanManager getInstance() {
        OperatorBeanManager operatorBeanManager;
        synchronized (OperatorBeanManager.class) {
            if (instance == null) {
                instance = new OperatorBeanManager();
            }
            operatorBeanManager = instance;
        }
        return operatorBeanManager;
    }

    public void setOperatorArray(ArrayList<ModulePortBean> operatorArray) {
        this.operatorArray = operatorArray;
    }

    public ArrayList<ModulePortBean> getOperatorArray() {
        return this.operatorArray;
    }
}
