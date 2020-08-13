package com.baidu.iov.dueros.waimai.presenter;

import com.baidu.iov.dueros.waimai.interfacedef.Ui;

public class AddressSuggestionPresenter extends Presenter<AddressSuggestionPresenter.AddressSuggestionUi>  {




    public interface AddressSuggestionUi extends Ui {

        void selectListItem(int i);
    }
}
