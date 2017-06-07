/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits.ReturnClasses;

import com.crumbits.Info.NotificationInfo;
import java.util.ArrayList;

/**
 *
 * @author mik_f
 */
public class PaginationRet {
    private Object list;
    private int lastPage;

    public Object getList() {
        return list;
    }

    public void setList(Object list) {
        this.list = list;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }
}
