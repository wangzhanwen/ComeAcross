package com.lyy_wzw.comeacross.homecommon;

import android.view.View;

/**
 * Created by yidong9 on 17/5/12.
 */

public interface BaseView<T> {
    void setPresenter(T presenter);
    void initViews(View view);

}
