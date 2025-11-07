package com.mitrais.cdc.cli.view;

public enum ScreenEnum {
    WELCOME_SCREEN(WelcomeScreen.class),
    TRANSACTION_SCREEN(TransactionScreen.class),
    FUND_TRANSFER_SCREEN(FundTransferScreen.class),
    FUND_TRANSFER_SUMMARY(FundTransferSummaryScreen.class),
    WITHDRAW_SCREEN(WithdrawScreen.class),
    WITHDRAW_SUMMARY_SCREEN(WithdrawSummaryScreen.class),
    OTHER_WITHDRAW_SCREEN(OtherWithdrawnScreen.class),
    HISTORY_TRANSACTION_SCREEN(HistoryTransactionScreen.class);

    private Class classObject;

    private ScreenEnum(Class classObject) {
        this.classObject = classObject;
    }

    public Class getClassObject() {
        return classObject;
    }
}
